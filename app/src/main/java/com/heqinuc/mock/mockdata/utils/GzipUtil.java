package com.heqinuc.mock.mockdata.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Panoo on 2016/6/19.
 */
public class GzipUtil {

	public static byte[] compress(byte[] data) throws IOException {

		ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();

		try {

			GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteOutPutStream);

			try {
				gzipOutputStream.write(data);
				gzipOutputStream.flush();
			} finally {
				gzipOutputStream.close();
			}


		} catch (IOException e) {
			throw e;
		} finally {
			byteOutPutStream.close();
		}

		return byteOutPutStream.toByteArray();
	}

	public static byte[] deCompress(byte[] data) throws IOException {

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		try {

			GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);

			ByteArrayOutputStream byteRead = new ByteArrayOutputStream();

			try {
				byte[] readByte = new byte[1024];
				int readLen = -1;
				while ((readLen = gzipInputStream.read(readByte)) > -1) {
					byteRead.write(readByte, 0, readLen);
				}
				byteRead.flush();
			} finally {
				byteRead.close();
				gzipInputStream.close();
			}

			return byteRead.toByteArray();

		} catch (IOException e) {
			throw e;
		} finally {
			byteArrayInputStream.close();
		}
	}
}
