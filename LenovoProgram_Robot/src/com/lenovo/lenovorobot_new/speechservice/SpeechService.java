package com.lenovo.lenovorobot_new.speechservice;

import android.os.Handler;
import android.os.Message;

import com.lenovo.lenovorobot_new.BaseClass.BaseService;
import com.lenovo.lenovorobot_new.speechservice.SpeechCompoundUtils.CompoundListener;
import com.lenovo.lenovorobot_new.speechservice.SpeechWakeUtils.WakeListener;

/**
 * 语音服务 语音的相关部分全部要经过这个Service
 * 
 * @author Administrator
 * 
 */
public class SpeechService extends BaseService implements CompoundListener,
		WakeListener {

	private SpeechWakeUtils speechWakeUtils;
	private SpeechCompoundUtils compoundUtils;
	/**
	 * 想让该服务,合成一段话
	 */
	private final int SPEECHCOMPOUND = 1;
	/**
	 * 当开视频的时候,关闭麦克,停止唤醒
	 */
	private final int STOPWAKE = 2;
	/**
	 * 关闭视频的时候,开启唤醒
	 */
	private final int STARTWAKE = 3;
	/**
	 * 每一个服务中,都要存在怎么一个handler才行,这样的话才能保证和外界的交互,这样做也是松耦合的,只要用handler来进行数据的传递即可,
	 * 如果是外面的地方想使用,语音合成都要使用该Service来完成,为了保证程序的一致性
	 */
	public static Handler handler;

	@Override
	public void initService() {

		compoundUtils = new SpeechCompoundUtils(getApplicationContext());
		compoundUtils.setOnCompoundListener(this);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int key = msg.what;
				switch (key) {
				case SPEECHCOMPOUND:

					break;
				case STOPWAKE:
					if (speechWakeUtils != null) {
						speechWakeUtils.stopWake();
					}

					break;
				case STARTWAKE:
					if (speechWakeUtils != null) {
						speechWakeUtils.startWake();
					}
					break;
				}
			}
		};
	}

	@Override
	public void initServiceDate() {

	}

	@Override
	public void endCompound() {
		/**
		 * 开场语说完之后,在开始唤醒和合成,把所有的具体的实现,全部封装起来,留有接口向外面展示最终的结果,只要设置回调即可
		 */
		if (speechWakeUtils == null) {
			speechWakeUtils = new SpeechWakeUtils(getApplicationContext());
			speechWakeUtils.startWake();
			speechWakeUtils.setOnWakeListener(this);
			speechWakeUtils.setIsUserKeyWord(true);
		}
	}

	/**
	 * 合成初始化完成,才可以进行语音的播放,欢迎词的播放
	 */
	@Override
	public void initCompound() {
		compoundUtils.startCompound("欢迎使用智能机器人语音服务,我是机器人达尔文,很高兴为您服务");
	}

	// 最后的识别结果
	@Override
	public void result(String str) {
		speechWakeUtils.startWake();
		compoundUtils.startCompound(str);
		// 如果是关键词识别的话,只要是识别到了关键词都可以从这个地方给传递到,MIService中进行底层的调用

		// 如果只是普通的,聊天模式的话,只要是识别到的关键词,发送给出去即可
	}
}
