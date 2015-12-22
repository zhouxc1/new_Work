package com.lenovo.lenovorobot_new.speechservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.lenovo.lenovorobot_new.utils.Log_Toast;

/**
 * 语音合成的帮助类
 * 
 * @author Administrator
 * 
 */
public class SpeechCompoundUtils {
	private static String TAG = "SpeechCompoundUtils";
	// 语音合成对象
	private SpeechSynthesizer mTts;

	// 引擎类型
	// private String mEngineType = SpeechConstant.TYPE_CLOUD;

	private String mEngineType = SpeechConstant.TYPE_LOCAL;
	// 语记安装助手类
	ApkInstaller mInstaller;

	private SharedPreferences mSharedPreferences;
	private Context context;
	private Log_Toast log_Toast;

	public SpeechCompoundUtils(Context context) {
		this.context = context;
		log_Toast = new Log_Toast(context);
		speechInit();
	}

	private void speechInit() {
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
		mSharedPreferences = context.getSharedPreferences(
				TtsSettings.PREFER_NAME, Context.MODE_PRIVATE);
		mInstaller = new ApkInstaller(context);
	}

	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				log_Toast.Toast("初始化失败,错误码：" + code, 0);
			} else {

			}
		}
	};

	public void startCompound(String text) {
		// 设置参数
		setParam();
		int code = mTts.startSpeaking(text, mTtsListener);
		// /**
		// * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
		// * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
		// */
		// String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
		// int code = mTts.synthesizeToUri(text, path, mTtsListener);

		if (code != ErrorCode.SUCCESS) {
			if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 未安装则跳转到提示安装页面
				mInstaller.install();
			} else {
				log_Toast.Toast("语音合成失败,错误码: " + code, 0);
			}
		}
	}

	private void showPresonSelectDialog() {
		if (!SpeechUtility.getUtility().checkServiceInstalled()) {
			mInstaller.install();
		} else {
			SpeechUtility.getUtility().openEngineSettings(
					SpeechConstant.ENG_TTS);
		}
	}

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakBegin() {
			log_Toast.Toast("开始播放", 0);
		}

		@Override
		public void onSpeakPaused() {
			log_Toast.Toast("暂停播放", 0);
		}

		@Override
		public void onSpeakResumed() {
			log_Toast.Toast("继续播放", 0);
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			// 合成进度
			// mPercentForBuffering = percent;
			// showTip(String.format(getString(R.string.tts_toast_format),
			// mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
			// mPercentForPlaying = percent;
			// showTip(String.format(getString(R.string.tts_toast_format),
			// mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				log_Toast.Toast("播放完成", 0);
			} else if (error != null) {
				log_Toast.Toast(error.getPlainDescription(true), 0);
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	private void setParam() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		// if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
		// mTts.setParameter(SpeechConstant.ENGINE_TYPE,
		// SpeechConstant.TYPE_CLOUD);
		// // 设置在线合成发音人
		// mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
		// if (!"neutral".equals(emot)) {
		// // 当前仅发音人“小艾”支持设置情感
		// // “小艾”发音人需要付费使用，具体请联系：msp_support@iflytek.com
		// mTts.setParameter(SpeechConstant.EMOT, emot);
		// }
		// // 设置合成语速
		// mTts.setParameter(SpeechConstant.SPEED,
		// mSharedPreferences.getString("speed_preference", "50"));
		// // 设置合成音调
		// mTts.setParameter(SpeechConstant.PITCH,
		// mSharedPreferences.getString("pitch_preference", "50"));
		// // 设置合成音量
		// mTts.setParameter(SpeechConstant.VOLUME,
		// mSharedPreferences.getString("volume_preference", "50"));
		// } else {
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		// 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
		mTts.setParameter(SpeechConstant.VOICE_NAME, "");
		/**
		 * TODO 本地合成不设置语速、音调、音量，默认使用语记设置 开发者如需自定义参数，请参考在线合成参数设置
		 */
		// }
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE,
				mSharedPreferences.getString("stream_preference", "3"));
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/tts.wav");
	}
}
