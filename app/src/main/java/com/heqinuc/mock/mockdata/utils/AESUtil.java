package com.heqinuc.mock.mockdata.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

	private static final String DEF_STR_ENCODING = "UTF-8";
	private static final String MD5_ALGORITHM = "MD5";
	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
	private static final String ALGORITHM = "AES";

	/**
	 * 加密
	 *
	 * @param content 需要加密的内容
	 * @param password 加密密码
	 * @return byte[] encrypted bytes
	 */
	public static byte[] encrypt(byte[] content, String password) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			byte[] raw = MD5Tool.toMD5(password.getBytes(DEF_STR_ENCODING));
			SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(content);
			encrypted = Base64.encode(encrypted, Base64.NO_WRAP);
			return encrypted;
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("NoSuchAlgorithmException, transformation=" + TRANSFORMATION, e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException("NoSuchPaddingException, transformation=" + TRANSFORMATION, e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 解密
	 *
	 * @param content 待解密内容
	 * @param password 解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {

		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			byte[] raw = MD5Tool.toMD5(password.getBytes(DEF_STR_ENCODING));
			SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted = Base64.decode(content,Base64.NO_WRAP);
			byte[] original = cipher.doFinal(encrypted);
			return original;
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("NoSuchAlgorithmException, transformation=" + TRANSFORMATION, e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException("NoSuchPaddingException, transformation=" + TRANSFORMATION, e);
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String encrypt(String sSrc, String password) {

		if(sSrc != null){
			try {
				byte[] encryptedByte = encrypt(sSrc.getBytes(DEF_STR_ENCODING),password);
				if(encryptedByte != null){
					return new String(encryptedByte,DEF_STR_ENCODING);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}


		return null;
	}


	public static String decrypt(String sSrc, String password) {

		if(sSrc != null){
			try {
				byte[] encryptedByte = decrypt(sSrc.getBytes(DEF_STR_ENCODING),password);
				if(encryptedByte != null){
					return new String(encryptedByte,DEF_STR_ENCODING);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
