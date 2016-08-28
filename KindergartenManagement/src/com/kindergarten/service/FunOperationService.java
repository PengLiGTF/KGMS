package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kindergarten.data.FunOperation;
import com.kindergarten.util.DbUtils;

public class FunOperationService
{

	/**
	 * 根据角色ID和funId查找已经赋权的操作
	 * 
	 * */
	public List<FunOperation> queryGrantedFunOper(String roleId, String funId)
	{
		List<FunOperation> operList = new ArrayList<FunOperation>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select a.*,b.fun_id from fun_operation a, role_operation b ");
			sb.append(" where a.operation_id = b.operation_id ");
			sb.append(" and b.role_id = ? and b.fun_id = ? ");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, roleId);
			pstmt.setString(2, funId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				FunOperation oper = new FunOperation();
				oper.setOperationId(resultSet.getString("operation_id"));
				oper.setOperationName(resultSet.getString("operation_name"));
				oper.setFunId(resultSet.getString("fun_id"));
				operList.add(oper);
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
		return operList;
	}

	/**
	 * 查询 所有的操作
	 * */
	public List<FunOperation> queryAllFunOperation()
	{
		List<FunOperation> operList = new ArrayList<FunOperation>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select a.* from fun_operation a ");
			pstmt = connection.prepareStatement(sb.toString());
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				FunOperation oper = new FunOperation();
				oper.setOperationId(resultSet.getString("operation_id"));
				oper.setOperationName(resultSet.getString("operation_name"));
				operList.add(oper);
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
		return operList;
	}
}
