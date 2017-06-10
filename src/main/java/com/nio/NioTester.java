package com.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.junit.Test;

public class NioTester {

	@Test
	public void testReadFile() throws IOException {
		RandomAccessFile aFile = new RandomAccessFile("src/main/resource/nioTestFile.txt", "rw");
		FileChannel fileChannel = aFile.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(4);
		CharBuffer cuf = CharBuffer.allocate(2);
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		while(fileChannel.read(buf) != -1) {
			buf.flip(); // ready for read
			decoder.decode(buf, cuf, true);
			cuf.flip();
			while(cuf.hasRemaining()) {
				System.out.print((char)cuf.get());
			}
			System.out.println("");
			cuf.clear();
			buf.clear();
		}
	}
}
