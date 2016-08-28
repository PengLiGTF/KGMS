package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kindergarten.data.FunOperation;
import com.kindergarten.data.ModuleFun;
import com.kindergarten.data.Role;
import com.kindergarten.util.DbUtils;

public class RoleService
{

	public void batchGrantToRole(String roleId, List<ModuleFun> funList, List<FunOperation> funOperationList) throws SQLException
	{
		Connection connection = DbUtils.getConnection();
		try
		{
			connection.setAutoCommit(false);
			batchInsertRoleFun(roleId, funList, connection);
			batchInsertRoleOperation(roleId, funOperationList, connection);
			connection.commit();
		} catch (SQLException e)
		{
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
			DbUtils.closeQuietly(connection);
		}
	}

	/**
	 * 批量插入角色操作信息
	 * */
	public void batchInsertRoleOperation(String roleId, List<FunOperation> funOperationList, Connection connection)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into role_operation(role_id,fun_id,operation_id) values(?,?,?)");
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" delete from role_operation where role_id = ?");
		PreparedStatement pstmtDelete = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmtDelete = connection.prepareStatement(sb2.toString());
			pstmtDelete.setString(1, roleId);
			pstmtDelete.execute();

			pstmt = connection.prepareStatement(sb.toString());
			for (FunOperation funOperation : funOperationList)
			{
				pstmt.setString(1, roleId);
				pstmt.setString(2, funOperation.getFunId());
				pstmt.setString(3, funOperation.getOperationId());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(pstmtDelete);
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstmt);
		}
	}

	/**
	 * 批量插入角色《--》功能信息
	 * 
	 * */
	public void batchInsertRoleFun(String roleId, List<ModuleFun> funList, Connection connection)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into role_fun(role_id,fun_id) values(?,?)");

		StringBuilder sbDelete = new StringBuilder();
		sbDelete.append(" delete from role_fun where role_id=?");
		PreparedStatement pstmtDelete = null;

		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmtDelete = connection.prepareStatement(sbDelete.toString());
			pstmtDelete.setString(1, roleId);
			pstmtDelete.execute();

			pstmt = connection.prepareStatement(sb.toString());
			for (ModuleFun moduleFun : funList)
			{
				pstmt.setString(1, roleId);
				pstmt.setString(2, moduleFun.getFunId());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(pstmtDelete);
		}
	}

	public static void main(String[] args)
	{

		List<String> names = new ArrayList<>();
		for (int i = 0; i < 10; i++)
		{
			names.add("name" + i);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("insert into test(name) values(?)");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			for (String name : names)
			{
				pstmt.setString(1, name);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}

	}

	/**
	 * 插入用户角色信息
	 * */
	public void insertUserRole(String userId, String roleId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into user_role(user_id,role_id) values(?,?)");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, roleId);
			pstmt.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}

	}

	public List<Role> queryRolesByUserId(String userId)
	{
		List<Role> roleList = new ArrayList<Role>();
		StringBuilder sb = new StringBuilder();
		sb.append("select a.role_id, a.role_name from role a, user_role b where a.role_id = b.role_id");
		sb.append(" and b.user_id = ?");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Role role = new Role();
				role.setRoleId(resultSet.getString("role_id"));
				role.setRoleName(resultSet.getString("role_name"));
				roleList.add(role);
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
		return roleList;

	}

	public List<Role> queryAllRoles()
	{
		List<Role> roleList = new ArrayList<Role>();
		StringBuilder sb = new StringBuilder();
		sb.append("select role_id, role_name from role ");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Role role = new Role();
				role.setRoleId(resultSet.getString("role_id"));
				role.setRoleName(resultSet.getString("role_name"));
				roleList.add(role);
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
		return roleList;
	}

}
