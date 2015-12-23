package com.lenovo.lenovorobot_new.speechservice;

import android.content.Context;

import android.util.Log;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.lenovo.lenovorobot_new.utils.Log_Toast;

/**
 * 上传关键词表
 * 
 * @author Administrator
 * 
 */
public class KeyWord {
	private static String TAG = "KeyWord";
	// 语音识别对象
	private SpeechRecognizer mAsr;
	// 本地语法文件
	private String mLocalGrammar = null;

	private static final String GRAMMAR_TYPE_BNF = "bnf";

	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 语记安装助手类
	private ApkInstaller mInstaller;
	private Context context;
	private Log_Toast log_Toast;

	// 语法、词典临时变量
	private String mContent;
	// 函数调用返回值
	private int ret = 0;

	public KeyWord(Context context) {
		this.context = context;
		log_Toast = new Log_Toast(context);
		speechKeyWordInit(context);
	}

	private void speechKeyWordInit(Context context) {
		// 初始化识别对象
		mAsr = SpeechRecognizer.createRecognizer(context, mInitListener);
		mLocalGrammar = FucUtil.readFile(context, "robotkeyword.bnf", "utf-8");

		mInstaller = new ApkInstaller(context);
		mEngineType = SpeechConstant.TYPE_LOCAL;

		/**
		 * 选择本地合成 判断是否安装语记,未安装则跳转到提示安装页面
		 */
		if (!SpeechUtility.getUtility().checkServiceInstalled()) {
			mInstaller.install();
		}
	}

	public void upLoadKeyWord() {
		mContent = new String(mLocalGrammar);
		mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
		// 指定引擎类型
		mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		ret = mAsr.buildGrammar(GRAMMAR_TYPE_BNF, mContent,
				mLocalGrammarListener);
		if (ret != ErrorCode.SUCCESS) {
			if (ret == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 未安装则跳转到提示安装页面
				mInstaller.install();
			} else {
				log_Toast.i(TAG, "语法构建失败,错误码：" + ret);
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
				log_Toast.i(TAG, "初始化失败,错误码：" + code);
			}
		}
	};
	/**
	 * 本地构建语法监听器。
	 */
	private GrammarListener mLocalGrammarListener = new GrammarListener() {
		@Override
		public void onBuildFinish(String grammarId, SpeechError error) {
			if (error == null) {
				log_Toast.i(TAG, "语法构建成功：" + grammarId);
			} else {
				log_Toast.i(TAG, "语法构建失败,错误码：" + error.getErrorCode());
			}
		}
	};
}
