package com.fmsysj.zbqmcs.record;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.fmsysj.zbqmcs.utils.IOUtils;
import com.fmsysj.zbqmcs.utils.ZipUtils;
import com.li.videoapplication.R;

/**
 * 录制核心
 * 
 * @author lin Create：2014-12
 */
public class ScreenCoreHandler {
	static final String TAG = "ScreenCoreHandler";
	static final String CORE = "ffmpeg_v2sh";
	static final Object installLock = new Object();
	static Context mcontext;

	public static String install(Context context) {
		synchronized (installLock) {
			mcontext = context;
			File coreFile = new File(context.getFilesDir(), CORE);
			if (!coreFile.exists()) {
				long st = System.currentTimeMillis();
				InputStream is = null;
				InputStream zipIs = null;
				try {
					String name;
					switch (Build.VERSION.SDK_INT) {
					case 15: {
						name = "ffmpeg1";
						break;
					}
					case 16: {
						name = "ffmpeg2";
						break;
					}
					case 17: {
						name = "ffmpeg3";
						break;
					}
					default: {
						name = "ffmpeg4";
					}
					}
					is = context.getResources()
							.openRawResource(R.raw.ffmpeg_v2);
					zipIs = new ByteArrayInputStream(des(context, is));
					ZipUtils.unzip(zipIs,
							context.getFilesDir().getAbsolutePath(),
							name,
							CORE);
					if (!coreFile.setExecutable(true, false)) {
						// noinspection ResultOfMethodCallIgnored
						coreFile.delete();
						Log.w(TAG, "set core file to executable error.");
						throw new RuntimeException("install error");
					}

				} catch (Exception e) {

					throw new RuntimeException("install error");
				} finally {
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(zipIs);
				}
			}
			return coreFile.getAbsolutePath();
		}
	}

	public static void uninstall(Context context) {
		synchronized (installLock) {
			File coreFile = new File(context.getFilesDir(), CORE);
			if (coreFile.exists()) {
				// noinspection ResultOfMethodCallIgnored
				coreFile.delete();
			}

		}
	}

	static byte[] des(Context context, InputStream is) {
		File coreFile = new File(context.getFilesDir(), CORE);
		Log.e(ScreenCoreHandler.class.getName(),
				String.valueOf(coreFile.hashCode()));
		try {
			byte[] data = IOUtils.toByteArray(is);
			int hx = 4, length = data.length, start = 0, bk = hx * hx;
			while (true) {
				if (length > (start + bk)) {
					byte[] bs = new byte[6];
					byte[] newBs = new byte[6];
					synchronized (CORE) {
						System.arraycopy(data, start, bs, 0, bs.length);
						for (int i = 0; i < bs.length; i++) {
							if (i % 2 == 0) {
								newBs[i] = bs[i + 1];
							} else {
								newBs[i] = bs[i - 1];
							}
						}
					}
					synchronized (CORE) {
						System.arraycopy(newBs, 0, data, start, newBs.length);
					}
					start += bk;
				} else {
					break;
				}
			}
			return data;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
