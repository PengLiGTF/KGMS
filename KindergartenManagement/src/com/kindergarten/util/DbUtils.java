package com.kindergarten.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtils
{
	private static String driver;
	private static String url;
	private static String userName;
	private static String passWord;

	static
	{
		System.out.println();
		File file = new File(System.getProperty("user.dir"), "dbProperties.property");
		// InputStream inStream =
		// DbUtils.class.getResourceAsStream("dbProperties.property");
		InputStream inStream = null;
		try
		{
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		Properties property = new Properties();
		try
		{
			property.load(inStream);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		driver = property.getProperty("Driver");
		try
		{
			Class.forName(driver);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		url = property.getProperty("Url");
		userName = property.getProperty("UserName");
		passWord = property.getProperty("PassWord");
	}

	public static Connection getConnection()
	{
		Connection connection = null;
		try
		{

			connection = DriverManager.getConnection(url, userName, passWord);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return connection;
	}

	public static void closeQuietly(ResultSet resultSet)
	{
		if (resultSet != null)
		{
			try
			{
				resultSet.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void closeQuietly(Connection connection)
	{
		if (connection != null)
		{
			try
			{
				connection.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void closeQuietly(PreparedStatement pstmt)
	{
		if (pstmt != null)
		{
			try
			{
				pstmt.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		System.out.println(DbUtils.getConnection());
	}

}
