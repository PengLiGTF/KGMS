package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.ibm.icu.util.Calendar;
import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.data.KinderLeaveInfo;
import com.kindergarten.util.DbUtils;

public class KinderLeaveService
{
	public void addLeave(KinderLeaveInfo leaveInfo) throws SQLException
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" insert into kinderLeave(kinder_id,kinder_name,leave_days,leave_description,operator_id,operator_time) ");
		sb.append("values(?,?,?,?,?,?)");
		List<KinderFeeInfo> feeInfoList = new KinderService().queryFeeHistory(leaveInfo.getKinderId());

		StringBuilder sb2 = new StringBuilder();
		sb2.append(" update kinder_fee_history set fee_expire_time = DATE_ADD(fee_expire_time,INTERVAL ? DAY) where kinder_id = ? ");
		try
		{
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, leaveInfo.getKinderId());
			pstmt.setString(2, leaveInfo.getKinderName());
			pstmt.setInt(3, leaveInfo.getLeaveDays());
			pstmt.setString(4, leaveInfo.getLeaveDesc());
			pstmt.setString(5, leaveInfo.getOperId());
			pstmt.setDate(6, new java.sql.Date(leaveInfo.getOperTime().getTime()));
			pstmt.execute();
			if (!feeInfoList.isEmpty())
			{
				KinderFeeInfo kinderInfo = feeInfoList.get(0);
				Date expireDate = kinderInfo.getFeeExpireTime();
				Calendar cl = Calendar.getInstance();
				cl.setTime(expireDate);
				cl.add(Calendar.DATE, leaveInfo.getLeaveDays());
				expireDate = cl.getTime();
				kinderInfo.setFeeExpireTime(expireDate);
				kinderInfo.setFeeEvent("请假延迟费用到期日期");
				new KinderService().recordKinderFee(kinderInfo);
			}
			connection.commit();
		} catch (SQLException e)
		{
			e.printStackTrace();
			try
			{
				connection.rollback();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			throw e;
		} finally
		{
			try
			{
				connection.setAutoCommit(true);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}

	}
}
