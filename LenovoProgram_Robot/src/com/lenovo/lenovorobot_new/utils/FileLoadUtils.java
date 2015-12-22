package com.lenovo.lenovorobot_new.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 单例模式 , 用来 获取 资产目录中的一些内容,设置回调可以获取 字符串,在主线程可以直接使用
 * 
 * @author ZXC
 * 
 */
public class FileLoadUtils {
	private static final FileLoadUtils FILE_LOAD_UTILS = new FileLoadUtils();
	private FileLoadListener fileLoadListener;

	private Handler fileLoadHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int key = msg.what;
			switch (key) {
			case 0:
				if (fileLoadListener != null) {
					fileLoadListener.setFileResult((String) msg.obj);
				}
				break;
			case 1:
				if (fileLoadListener != null) {
					fileLoadListener.setError(key);
				}
				break;
			}
		};
	};

	private FileLoadUtils() {
	}

	public static FileLoadUtils getFileLoadUtilsInfo() {
		return FILE_LOAD_UTILS;
	}

	public void loadFile(Context context, String fileName) {
		new LoadFileThread(context, fileName).start();

	}

	class LoadFileThread extends Thread {
		private Context context;
		private String fileName;

		public LoadFileThread(Context context, String fileName) {
			this.context = context;
			this.fileName = fileName;
		}

		@Override
		public void run() {
			try {
				InputStream fileInputStream = context.getAssets()
						.open(fileName);
				String fileString = getTextFromStream(fileInputStream);
				Message msg = new Message();
				if (fileString != null) {
					msg.what = 0;
					msg.obj = fileString;
				} else {
					msg.what = 1;
				}
				fileLoadHandler.sendMessage(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getTextFromStream(InputStream is) {
		byte[] b = new byte[1024];
		int len;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((len = is.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			String text = new String(bos.toByteArray());
			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public interface FileLoadListener {
		public void setFileResult(String result);

		public void setError(int code);
	}

	public void setOnFileLoadListenerInfo(FileLoadListener fileLoadListener) {
		this.fileLoadListener = fileLoadListener;
	}
}
