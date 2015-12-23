package com.lenovo.lenovorobot_new.speechservice;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.lenovo.lenovorobot_new.utils.Log_Toast;

/**
 * 语音识别的帮助类
 * 
 * @author Administrator
 * 
 */
public class SpeechRecognizeUtils {
	protected static final String TAG = "SpeechRecognizeUtils";
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private SharedPreferences mSharedPreferences;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_LOCAL;
	// 语记安装助手类
	ApkInstaller mInstaller;
	private Log_Toast log_Toast;
	int ret = 0; // 函数调用返回值
	private RecognizeListener recognizeListener;
	// 是否是关键词识别
	private boolean isKeyWord = true;

	public SpeechRecognizeUtils(Context context) {
		log_Toast = new Log_Toast(context);
		// 初始化识别无UI识别对象
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
		mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
		mIatDialog = new RecognizerDialog(context, mInitListener);

		mSharedPreferences = context.getSharedPreferences(
				IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
		// mResultText = ((EditText) findViewById(R.id.iat_text));
		mInstaller = new ApkInstaller(context);

		/**
		 * 选择本地听写 判断是否安装语记,未安装则跳转到提示安装页面
		 */
		if (!SpeechUtility.getUtility().checkServiceInstalled()) {
			mInstaller.install();
		} else {
			String result = FucUtil.checkLocalResource();
			if (!TextUtils.isEmpty(result)) {
				log_Toast.Toast(result, 0);
			}
		}
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				log_Toast.Toast("初始化失败，错误码：" + code, 0);
			}
		}
	};

	public void startRecognize() {
		// mResultText.setText(null);// 清空显示内容
		mIatResults.clear();
		// 设置参数
		setParam();
		boolean isShowDialog = mSharedPreferences.getBoolean("iat_show", true);
		if (isShowDialog) {
			// 显示听写对话框,加上这句话代码为了是能够,在service中把dialog给弹出来
			mIatDialog.getWindow().setType(
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

			mIatDialog.setListener(mRecognizerDialogListener);
			mIatDialog.show();
			log_Toast.Toast("开始说话", 0);
		} else {
			// 不显示听写对话框
			ret = mIat.startListening(mRecognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				log_Toast.Toast("听写失败,错误码：" + ret, 0);
			} else {
				log_Toast.Toast("请开始说话", 0);
			}
		}
	}

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			mIatDialog.dismiss();
			if (recognizeListener != null) {
				recognizeListener.setError(error.getErrorCode());
			}
		}

	};
	/**
	 * 听写监听器。
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
			log_Toast.Toast("开始说话", 0);
		}

		@Override
		public void onError(SpeechError error) {
			log_Toast.Toast(error.getPlainDescription(true), 0);
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
			log_Toast.Toast("结束说话", 0);
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			printResult(results);

			if (isLast) {
				// TODO 最后的结果
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		if (recognizeListener != null) {
			recognizeListener.setResult(resultBuffer.toString());
		}
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "4000"));

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "0"));

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/iat.wav");

		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA,
				mSharedPreferences.getString("iat_dwa_preference", "0"));

		if (isKeyWord) {
			// 设置本地识别使用语法id
			mIat.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call");
			// 设置本地识别的门限值
			mIat.setParameter(SpeechConstant.ASR_THRESHOLD, "30");
		}
	}

	public interface RecognizeListener {
		public void setResult(String result);

		public void setError(int code);
	}

	public void setOnRecognizeListener(RecognizeListener recognizeListener) {
		this.recognizeListener = recognizeListener;
	}
}
