package com.kindergarten.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;

import com.ibm.icu.util.Calendar;
import com.kindergarten.data.KinderStatus;
import com.kindergarten.data.SexModel;

public class CommonUtil
{
	/** 审核 */
	public static final int CHECKED = 101;
	/** 反审核 */
	public static final int UN_CHECKED = 102;
	/** 作废 */
	public static final int INVALID = 103;

	/** 未入园 */
	public static final String STATUS_NOT_IN = "S_000";
	/** 已经入园 */
	public static final String STATUS_IN = "S_001";
	/** 已经毕业 */
	public static final String STATUS_GRADUATE = "S_002";
	/** 已经退学 */
	public static final String STATUS_QUIT = "S_003";

	public static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd";

	public static <T> T getSelectedItem(ComboViewer vomboViewer)
	{
		StructuredSelection selectedGrade = (StructuredSelection) vomboViewer.getSelection();
		if (!selectedGrade.isEmpty())
		{
			@SuppressWarnings("unchecked")
			T selectedType = (T) selectedGrade.getFirstElement();
			return selectedType;
		}
		return null;
	}

	public static String formatDateToString(Date date, String formatStr)
	{
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}

	public static Date formatStringToDate(String dateStr, String formatStr)
	{
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try
		{
			return format.parse(dateStr);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 学生入园缴费序号
	 * */
	public static String generateFeeCheckId()
	{
		return generateCheckId("kinderFeeIdSeq");
	}

	/**
	 * 学生入园缴费序号
	 * */
	public static String generatePreFeeCheckId()
	{
		return generateCheckId("kinderPreFeeIdSeq");
	}

	/**
	 * 学生入园缴费序号
	 * */
	public static String generateRenewFeeCheckId()
	{
		return generateCheckId("kinderRenewFeeIdSeq");
	}

	/**
	 * 获取递增序号 学生入园缴费（kinderFeeIdSeq）， 学生预交费单据（kinderPreFeeIdSeq），
	 * 学生续费单据（kinderRenewFeeIdSeq）
	 * 
	 * */
	public static String generateCheckId(String seqName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select `nextval`('").append(seqName).append("')");
		Connection connection = DbUtils.getConnection();
		CallableStatement pstmt = null;
		ResultSet resultSet = null;
		int sequence = 0;
		try
		{
			pstmt = connection.prepareCall(sb.toString());
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while (resultSet.next())
			{
				sequence = resultSet.getInt(1);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
		return String.valueOf(sequence);
	}

	/**
	 * 获取学号递增ID
	 * 
	 * */
	public static String generateKinderId()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select `nextval`('kinderIdSeq')");
		Connection connection = DbUtils.getConnection();
		CallableStatement pstmt = null;
		ResultSet resultSet = null;
		int sequence = 0;
		StringBuilder sbSequence = new StringBuilder();
		try
		{
			pstmt = connection.prepareCall(sb.toString());
			pstmt.execute();
			resultSet = pstmt.getResultSet();
			while (resultSet.next())
			{
				sequence = resultSet.getInt(1);
			}
			Calendar calendar = Calendar.getInstance();
			sbSequence.append(calendar.get(Calendar.YEAR));
			int month = calendar.get(Calendar.MONTH) + 1;
			if (month < 10)
			{
				sbSequence.append("0");
			}
			sbSequence.append(month);
			int day = calendar.get(Calendar.DATE);
			if (day < 10)
			{
				sbSequence.append("0");
			}
			sbSequence.append(day);
			if (sequence < 10)
			{
				sbSequence.append("00000");
			} else if (sequence < 100)
			{
				sbSequence.append("0000");
			} else if (sequence < 1000)
			{
				sbSequence.append("000");
			} else if (sequence < 10000)
			{
				sbSequence.append("00");
			} else if (sequence < 100000)
			{
				sbSequence.append("0");
			}
			sbSequence.append(sequence);
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
		return sbSequence.toString();
	}

	public static SexModel getSexModelByCode(char sexCode)
	{
		SexModel[] sexModels = getSexModel();
		for (SexModel temp : sexModels)
		{
			char code = temp.getCode();
			if (sexCode == code)
			{
				return temp;
			}
		}
		return null;
	}

	public static String getSexNameByCode(char sexCode)
	{
		SexModel[] sexModels = getSexModel();
		for (SexModel temp : sexModels)
		{
			char code = temp.getCode();
			if (sexCode == code)
			{
				return temp.getName();
			}
		}
		return "其他";
	}

	public static SexModel[] getSexModel()
	{
		SexModel[] sexs = new SexModel[]
		{ new SexModel("男", 'M'), new SexModel("女", 'W'), new SexModel("其他", 'O') };
		return sexs;
	}

	public static String getStatusNameByCode(String status)
	{
		KinderStatus[] statusList = getAllStatus();
		for (KinderStatus temp : statusList)
		{
			if (status.equals(temp.getStatusCode()))
			{
				return temp.getStatusName();
			}
		}
		return "未知状态";
	}

	public static KinderStatus getStatusByCode(String status)
	{
		KinderStatus[] statusList = getAllStatus();
		for (KinderStatus temp : statusList)
		{
			if (status.equals(temp.getStatusCode()))
			{
				return temp;
			}
		}
		return null;
	}

	public static KinderStatus[] getAllStatus()
	{
		KinderStatus[] status = new KinderStatus[]
		{ new KinderStatus(STATUS_NOT_IN, "未入园"), new KinderStatus(STATUS_QUIT, "已退园"), new KinderStatus(STATUS_IN, "已入园"),
				new KinderStatus(STATUS_GRADUATE, "已毕业"), };

		return status;
	}

	/**
	 * 正则判断输入是否为正常的数字形式
	 * 
	 * */
	private static boolean isOnlyDigital(String input)
	{
		String reg = "(^((-?)[1-9][0-9]*)|0)(\\.[0-9]+)*$";
		return Pattern.compile(reg).matcher(input).matches();
	}

	public static boolean isDigital(String input)
	{
		return isOnlyDigital(input);
	}

	public static void main(String[] args)
	{
		System.out.println(isOnlyDigital("0.0"));

		// System.out.println(formatDateToString(new Date(),
		// TIME_FORMAT_PATTERN));
		// System.out.println("dddddddd" + generateKinderId());
		//
		// System.out.println(formatStringToDate("2016-08-13",
		// TIME_FORMAT_PATTERN));
		// System.out.println(generateFeeCheckId());

	}

}
