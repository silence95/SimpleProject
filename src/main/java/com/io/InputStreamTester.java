package com.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

public class InputStreamTester {

	/**
	 * 如果InputStream内可读的数据不足以填满字节数组，那么数组剩余的部分将包含本次读取之前的数据。记得检查有多少数据实际被写入到了字节数组中。
	 * @throws IOException
	 */
	@Test
	public void testRead() throws IOException {
		InputStream inputstream = new FileInputStream("src/main/resource/ioTestFile.txt");
		byte[] data = new byte[10];
		int bytesRead = inputstream.read(data);
		while (bytesRead != -1) {
			System.out.println(new String(data, Charset.forName("utf-8")));
			bytesRead = inputstream.read(data);
		}
		inputstream.close();
	}
}
