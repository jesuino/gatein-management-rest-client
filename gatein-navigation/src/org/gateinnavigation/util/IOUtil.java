package org.gateinnavigation.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
	public static void writeFile(InputStream is, String file) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(
				is);
		FileOutputStream fos = new FileOutputStream(new File(file));
		byte[] bit = new byte[1024];
		while (bis.read(bit) != -1) {
			fos.write(bit);
		}
		bis.close();
		fos.close();
	}
	
	private IOUtil(){}
}
