package com.heqinuc.mock.mockdata.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tool {

	private static final String DEF_STR_ENCODING = "UTF-8";
	private static final String MD5_ALGORITHM = "MD5";

	/**
	 * convert md5
	 *
	 * @param content
	 * @return md5 string
	 */
	public static String toMD5(String content, boolean toUpperCase) {

		if (content == null) return null;

		try {

			byte[] digest = toMD5(content.getBytes(DEF_STR_ENCODING));
			return toHexStr(digest,toUpperCase);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * convert md5
	 *
	 * @param bytes the byte array that need md5
	 * @param toUpperCase need to upper case ?
	 * @return md5 string
	 */
	public static String toMD5(byte[] bytes, boolean toUpperCase) {
		return toHexStr(toMD5(bytes),toUpperCase) ;
	}

	/**
	 * convert byte to hex string
	 *
	 * @param md5Byte the byte that md5
	 * @return md5 string
	 */
	public static String toHexStr(byte[] md5Byte, boolean toUpperCase) {

		if (md5Byte == null) return "";

		StringBuffer sb = new StringBuffer();

		for (byte b : md5Byte) {
			sb.append(String.format("%02x", b & 0xff));
		}

		String md5Str = sb.toString();

		return toUpperCase ? md5Str.toUpperCase() : md5Str;
	}

	/**
	 * convert md5
	 *
	 * @param bytes
	 * @return md5 byte
	 */
	public static byte[] toMD5(byte[] bytes) {

		try {
			MessageDigest algorithm = MessageDigest.getInstance(MD5_ALGORITHM);
			algorithm.reset();
			return algorithm.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}
}
