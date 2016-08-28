package com.kindergarten.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HelpFileReader
{

	public static final int MAX_SIZE = 1024 * 1024;

	// read help document
	public static String readHelp()
	{
		try
		{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("src/com/wxy/library/util/help.txt")));
			byte[] b = new byte[HelpFileReader.MAX_SIZE];
			int length = bis.read(b);
			return new String(b, 0, length);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Tobe build......";
	}
}
