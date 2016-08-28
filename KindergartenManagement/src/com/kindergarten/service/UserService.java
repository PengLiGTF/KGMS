package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.kindergarten.data.Module;
import com.kindergarten.data.ModuleFun;
import com.kindergarten.data.User;
import com.kindergarten.util.DbUtils;

public class UserService
{

	/**
	 * 更新用户
	 * 
	 * @throws SQLException
	 * */
	public void updateUser(String userId, String userName, char sex, String phone, String mail, String roleId) throws SQLException
	{
		Connection connection = DbUtils.getConnection();
		connection.setAutoCommit(false);
		StringBuilder sb = new StringBuilder();
		sb.append(" update user set user_name = ?,user_sex=?,user_phone=?,user_mail=? where user_id=?");

		StringBuilder sb2 = new StringBuilder();
		sb2.append(" update user_role set role_id = ? where user_id = ? ");
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try
		{
			pstmt1 = connection.prepareStatement(sb.toString());
			pstmt1.setString(1, userName);
			pstmt1.setString(2, String.valueOf(sex));
			pstmt1.setString(3, phone);
			pstmt1.setString(4, mail);
			pstmt1.setString(5, userId);
			pstmt1.executeUpdate();
			pstmt2 = connection.prepareStatement(sb2.toString());
			pstmt2.setString(1, roleId);
			pstmt2.setString(2, userId);
			pstmt2.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e)
		{
			e.printStackTrace();
			connection.rollback();
		} finally
		{
			DbUtils.closeQuietly(pstmt1);
			DbUtils.closeQuietly(pstmt2);
			DbUtils.closeQuietly(connection);
		}

	}

	/**
	 * 判断用户是否已经存在
	 * 
	 * */
	public boolean checkUserIsExisted(String userId)
	{

		boolean existed = false;
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from user where user_id = ?");
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
				int count = resultSet.getInt(1);
				if (count > 0)
				{
					existed = true;
				}
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
		return existed;
	}

	/**
	 * 删除用户
	 * */
	public void deleteUser(String userId) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" delete from user where user_id = ?");
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" delete from user_role where user_id = ?");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.execute();
			pstmt2 = connection.prepareStatement(sb2.toString());
			pstmt2.setString(1, userId);
			pstmt2.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(pstmt2);
			DbUtils.closeQuietly(connection);
		}
	}

	/**
	 * @throws SQLException
	 * 
	 * */
	public int updatePwd(String userId, String passWord) throws SQLException
	{
		int affectedRow = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(" update user set user_password = MD5(?), is_first='F' where user_id = ? ");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, passWord);
			pstmt.setString(2, userId);
			affectedRow = pstmt.executeUpdate();
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
		return affectedRow;

	}

	/**
	 * 新增用户
	 * 
	 * @throws SQLException
	 * */
	public void addUser(String userId, String userName, char sex, String phone, String mail, String roleId) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into user(user_id,user_name,user_sex,user_phone,user_mail,user_password) ");
		sb.append(" values(?,?,?,?,?,MD5('super'))");

		StringBuilder sb2 = new StringBuilder();
		sb2.append("insert into user_role(user_id,role_id) values(?,?)");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		try
		{
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, userName);
			pstmt.setString(3, String.valueOf(sex));
			pstmt.setString(4, phone);
			pstmt.setString(5, mail);
			pstmt.execute();

			pstmt2 = connection.prepareStatement(sb2.toString());
			pstmt2.setString(1, userId);
			pstmt2.setString(2, roleId);
			pstmt2.execute();

			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e)
		{
			e.printStackTrace();
			connection.rollback();
			throw e;
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(pstmt2);
			DbUtils.closeQuietly(connection);
		}
	}

	public User userIsValid(String userId, String password)
	{
		User user = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select user_id, user_name,is_first from user where user_id=? and user_password = MD5(?)");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, password);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				user = new User();
				user.setUserId(resultSet.getString("user_id"));
				user.setUserName(resultSet.getString("user_name"));
				user.setIsFirst(resultSet.getString("is_first").charAt(0));
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
		return user;
	}

	public List<Module> getUserOwnModules(String userId)
	{
		List<Module> moduleList = new ArrayList<Module>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct e.*,d.fun_id,d.fun_name ");
		sb.append(" from  user_role b, role_fun c,module_fun d,module e ");
		sb.append(" where b.role_id = c.role_id ");
		sb.append(" and c.fun_id = d.fun_id ");
		sb.append(" and d.module_id=e.module_id ");
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
				String moduleId = resultSet.getString("module_id");
				String moduleName = resultSet.getString("module_name");
				String funId = resultSet.getString("fun_id");
				String funName = resultSet.getString("fun_name");
				Module module = new Module();
				module.setModuleId(moduleId);
				module.setModuleName(moduleName);
				if (!moduleList.contains(module))
				{
					moduleList.add(module);
				}
				ModuleFun moduleFun = new ModuleFun();
				moduleFun.setModuleId(moduleId);
				moduleFun.setFunId(funId);
				moduleFun.setFunName(funName);
				// TODO moduleFun.setFunOperationList(funOperationList);
				List<ModuleFun> funList = moduleList.get(moduleList.indexOf(module)).getModuleFuns();
				funList.add(moduleFun);
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
		return moduleList;
	}

	public String queryUserRoleByUserId(String userId)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select role_id from user_role where user_id = ?");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				return resultSet.getString("role_id");
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
		return null;
	}

	/**
	 * 根据条件查询用户信息
	 * */
	public List<User> queryUserByCondition(String userId, String userName)
	{
		List<User> userList = new ArrayList<User>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from user where 1=1 ");
			boolean idIsNotNull = false;
			boolean nameIsNotNull = false;
			if (!StringUtils.isBlank(userId))
			{
				idIsNotNull = true;
				sb.append(" and user_id like ?");
			}
			if (!StringUtils.isBlank(userName))
			{
				sb.append(" and user_name like ?");
				nameIsNotNull = true;
			}
			pstmt = connection.prepareStatement(sb.toString());
			if (idIsNotNull)
			{
				pstmt.setString(1, "%" + userId + "%");
			}
			if (idIsNotNull && nameIsNotNull)
			{
				pstmt.setString(2, "%" + userName + "%");
			}
			if (!idIsNotNull && nameIsNotNull)
			{
				pstmt.setString(1, "%" + userName + "%");
			}
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				User user = new User();
				user.setUserId(resultSet.getString("user_id"));
				user.setUserName(resultSet.getString("user_name"));
				String sex = resultSet.getString("user_sex");
				user.setUserSex((sex == null || "".equals(sex)) ? 'O' : sex.charAt(0));
				user.setUserPhone(resultSet.getString("user_phone"));
				user.setUserMail(resultSet.getString("user_mail"));
				userList.add(user);
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
		return userList;
	}

	public static void main(String[] args)
	{
		List<Module> moduleList = new UserService().getUserOwnModules("201607002");
		System.out.println("kkkkkkkkkkkkk");
	}

}
