package org.rondobell.racailum.base.io;

import java.io.*;

public class FileUtil {

	public static String readFileToString(String path) throws IOException {
		File file = new File(path);
		if(file.exists()&&file.isFile()){
			try {
				String content = fileToString(file);
				return content;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}else {
			return null;
		}
	}

	public static String fileToString(File file) throws IOException {
		FileInputStream fi = new FileInputStream(file);

		InputStreamReader ir = new InputStreamReader(fi);
		BufferedReader in = new BufferedReader(ir);
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		try {
			while ((line = in.readLine()) != null){
				buffer.append(line);
			}
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (ir!=null) {
				ir.close();
			}
			if (in!=null) {
				in.close();
			}
			if (fi!=null) {
				fi.close();
			}
		}
	}
}
