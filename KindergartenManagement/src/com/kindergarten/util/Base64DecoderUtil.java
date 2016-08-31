package com.kindergarten.util;

import javax.xml.bind.DatatypeConverter;

public class Base64DecoderUtil
{
	public static String encryptBase64(String normalStr)
	{
		return DatatypeConverter.printBase64Binary(normalStr.getBytes());
	}

	public static String deCryptBase64(String encryptedStr)
	{
		byte[] bs = DatatypeConverter.parseBase64Binary(encryptedStr);
		return new String(bs);
	}

	public static void main(String[] args)
	{
		String encry = encryptBase64("root123");
		System.out.println(encry);
		System.out.println(deCryptBase64(encry));
	}
}
