package com.lenovo.lenovorobot_new.speechservice;

import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;
import com.lenovo.lenovorobot_new.speechservice.SpeechRecognizeUtils.RecognizeListener;
import com.lenovo.lenovorobot_new.utils.Log_Toast;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * 语音唤醒
 * 
 * @author Administrator
 * 
 */
public class SpeechWakeUtils implements RecognizeListener {
	private Log_Toast log_Toast;
	private String TAG = "SpeechWakeUtils";
	// 语音唤醒对象
	private VoiceWakeuper mIvw;
	// 唤醒结果内容
	private String resultString;
	// 设置门限值 ： 门限值越低越容易被唤醒
	private final static int MAX = 60;
	private final static int MIN = -20;
	private int curThresh = MIN;
	private Context context;
	private SpeechRecognizeUtils recognizeUtils;

	public SpeechWakeUtils(Context context) {
		this.context = context;
		log_Toast = new Log_Toast(context);
		recognizeUtils = new SpeechRecognizeUtils(context);
		recognizeUtils.setOnRecognizeListener(this);

		compoundUtils = new SpeechCompoundUtils(context);

		initSpeech();
	}

	private void initSpeech() {
		// TODO Auto-generated method stub
		// 加载识唤醒地资源，resPath为本地识别资源路径
		StringBuffer param = new StringBuffer();
		String resPath = ResourceUtil.generateResourcePath(context,
				RESOURCE_TYPE.assets, "ivw/567904be.jet");

		param.append(SpeechConstant.IVW_RES_PATH + "=" + resPath);
		param.append("," + ResourceUtil.ENGINE_START + "="
				+ SpeechConstant.ENG_IVW);
		boolean ret = SpeechUtility.getUtility().setParameter(
				ResourceUtil.ENGINE_START, param.toString());
		if (!ret) {
			Log.d(TAG, "启动本地引擎失败！");
		}
		// 初始化唤醒对象
		mIvw = VoiceWakeuper.createWakeuper(context, null);
	}

	public void startWake() {
		// 非空判断，防止因空指针使程序崩溃
		mIvw = VoiceWakeuper.getWakeuper();
		if (mIvw != null) {
			resultString = "";
			// textView.setText(resultString);
			// 清空参数
			mIvw.setParameter(SpeechConstant.PARAMS, null);
			/**
			 * 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
			 * 示例demo默认设置第一个唤醒词，建议开发者根据定制资源中唤醒词个数进行设置
			 */
			mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
			// 设置唤醒模式
			mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
			// 设置持续进行唤醒
			mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1");
			mIvw.startListening(mWakeuperListener);
		} else {
			log_Toast.i(TAG, "唤醒未初始化");
		}
	}

	public void stopWake() {
		// Log.d(TAG, "onDestroy WakeDemo");
		// mIvw = VoiceWakeuper.getWakeuper();
		// if (mIvw != null) {
		// mIvw.destroy();
		// } else {
		// log_Toast.i(TAG, "唤醒未初始化");
		// }
		mIvw = VoiceWakeuper.getWakeuper();
		if (mIvw != null) {
			mIvw.stopListening();
		} else {
			log_Toast.i(TAG, "唤醒未初始化");
		}
	}

	private WakeuperListener mWakeuperListener = new WakeuperListener() {

		@Override
		public void onResult(WakeuperResult result) {
			try {
				String text = result.getResultString();
				JSONObject object;
				object = new JSONObject(text);
				StringBuffer buffer = new StringBuffer();
				buffer.append("【RAW】 " + text);
				buffer.append("\n");
				buffer.append("【操作类型】" + object.optString("sst"));
				buffer.append("\n");
				buffer.append("【唤醒词id】" + object.optString("id"));
				buffer.append("\n");
				buffer.append("【得分】" + object.optString("score"));
				buffer.append("\n");
				buffer.append("【前端点】" + object.optString("bos"));
				buffer.append("\n");
				buffer.append("【尾端点】" + object.optString("eos"));
				resultString = buffer.toString();
			} catch (JSONException e) {
				resultString = "结果解析出错";
				e.printStackTrace();
			}
			stopWake();
			recognizeUtils.startRecognize();
		}

		@Override
		public void onError(SpeechError error) {
		}

		@Override
		public void onBeginOfSpeech() {
			log_Toast.i(TAG, "开始说话");
		}

		@Override
		public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {

		}

		@Override
		public void onVolumeChanged(int volume) {

		}
	};
	private SpeechCompoundUtils compoundUtils;

	/**
	 * 识别结果回调
	 */
	@Override
	public void setResult(String result) {
		startWake();
		log_Toast.i(TAG, result);
		compoundUtils.startCompound(result);
	}
}
