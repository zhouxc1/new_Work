package com.lenovo.lenovorobot_new.utils;

import android.content.Context;

import com.lenovo.mi.plato.comm.MoPacket;

/**
 * 用来处理服务器,发送过来的数据包
 * 
 * @author Administrator
 * 
 */
public class AcceptOpenServerPackageUtils {
	private static final String TAG = "AcceptOpenServerPackageUtils";
	private Log_Toast log_Toast;

	public AcceptOpenServerPackageUtils(Context context) {
		log_Toast = new Log_Toast(context);
	}

	/**
	 * 成功拿到服务器传递过来的数据包,这个是拆包函数
	 * 
	 * @param pack
	 */
	public void openPackage(MoPacket pack) {
		int source = pack.getInt32();
		int target = pack.getInt32();
		int framesNumber = pack.getInt32();
		log_Toast.i(TAG, "source " + source + " " + target + "  "
				+ framesNumber);
		if ((source == 11 && target == 1) || (source == 21 && target == 2)
				|| (source == 0 && target == 1) || (source == 0 && target == 2)) {
			switch (framesNumber) {
			case 1:
				break;
			case 2:
				String string = pack.getString();
				if (string != null) {
					if (string.equals("11")) {
						log_Toast.i(TAG, "中文好友在线");
					} else if (string.equals("21")) {
						log_Toast.i(TAG, "英文文好友在线");
					}
				} else {
					log_Toast.i(TAG, "当前没有好友在线");
				}
				break;
			case 3:
				break;
			case 4:

				break;
			case 5:

				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;
			case 9:

				break;
			case 10:

				break;
			case 11:

				break;
			case 12:

				break;
			case 13:

				break;
			case 14:

				break;
			case 15:

				break;
			case 16:

				break;
			case 17:

				break;
			case 18:
				break;
			case 19:

				break;
			case 20:
				break;
			case 21:

				break;
			case 22:

				break;
			case 23:

				break;
			case 24:

				break;
			case 25:
				break;
			case 26:

				break;
			case 27:

				break;
			case 28:
				break;
			case 29:

				break;
			case 30:
				break;
			case 31:

				break;
			case 32:
				break;
			case 33:
				break;
			case 34:

				break;
			case 35:

				break;
			case 36:

				break;
			case 37:

				break;
			case 38:
				break;
			case 39:
				break;
			case 40:
				break;
			case 42:
				break;
			case 43:
				break;
			}
		}
	}
}
