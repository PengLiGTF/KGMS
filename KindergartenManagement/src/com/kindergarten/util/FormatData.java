package com.kindergarten.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatData
{

	public static String getCurrentTime()
	{
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static String dateToString(Date date)
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static void main(String[] args)
	{
		System.out.println(dateToString(new Date()));
	}
	
	
}
