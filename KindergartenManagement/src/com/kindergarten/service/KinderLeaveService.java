package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ibm.icu.util.Calendar;
import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.data.KinderLeaveInfo;
import com.kindergarten.util.DbUtils;

public class KinderLeaveService
{
	
	
	/**
	 * 根据条件查询学生请假历史
	 * 
	 * */
	public List<KinderLeaveInfo> queryLeaveInfoByCondition(Map<String, String> param)
	{
		List<KinderLeaveInfo> leaveInfoList = new ArrayList<KinderLeaveInfo>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select * from kinderLeave where 1=1 ");
		sb.append(" and kinder_id like ? ");
		sb.append(" and kinder_name like ? ");
		String kinderId = param.get("kinderId") == null ? "" : param.get("kinderId");
		String kinderName = param.get("kinderName") == null ? "" : param.get("kinderName");
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, "%" + kinderId + "%");
			pstmt.setString(2, "%" + kinderName + "%");
			resultSet = pstmt.executeQuery();
			while(resultSet.next())
			{
				KinderLeaveInfo temp = new KinderLeaveInfo();
				temp.setKinderId(resultSet.getString("kinder_id"));
				temp.setKinderName(resultSet.getString("kinder_name"));
				temp.setLeaveDays(resultSet.getInt("leave_days"));
				temp.setLeaveStartTime(resultSet.getDate("leave_start_time"));
				temp.setLeaveEndTime(resultSet.getDate("leave_end_time"));
				temp.setLeaveDesc(resultSet.getString("leave_description"));
				temp.setOperId(resultSet.getString("operator_id"));
				temp.setOperTime(resultSet.getDate("operator_time"));
				leaveInfoList.add(temp);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
		return leaveInfoList;
	}
	
	/**
	 * 学生请假条录入
	 * 
	 * */
	public void addLeave(KinderLeaveInfo leaveInfo) throws SQLException
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" insert into kinderLeave(kinder_id,kinder_name,leave_days,leave_description,operator_id,operator_time,leave_start_time,leave_end_time) ");
		sb.append("values(?,?,?,?,?,?,?,?)");
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
			pstmt.setDate(7, new java.sql.Date(leaveInfo.getLeaveStartTime().getTime()));
			pstmt.setDate(8, new java.sql.Date(leaveInfo.getLeaveEndTime().getTime()));
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
				//请假之后，作废之前的缴费记录（缴费记录中包含缴费到期时间），更新缴费到期时间
				new KinderService().updateKinderFee(kinderInfo, connection);
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
