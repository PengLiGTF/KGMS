package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kindergarten.data.Module;
import com.kindergarten.data.ModuleFun;
import com.kindergarten.util.DbUtils;

public class ModuleService
{

	/**
	 * 根据roleId查找属于该角色的所有模块和功能
	 * 
	 * 
	 * */
	ModuleFunService moduleFunService = new ModuleFunService();

	public List<Module> queryAllGrantedModuleFunsByRoleId(String roleId)
	{
		List<Module> moduleList = new ArrayList<Module>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from module where 1=1 ");
			pstmt = connection.prepareStatement(sb.toString());
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Module module = new Module();
				String moduleId = resultSet.getString("module_id");
				module.setModuleId(moduleId);
				module.setModuleName(resultSet.getString("module_name"));
				List<ModuleFun> funList = moduleFunService.queryAllFunsByModuleIdAndRoleId(moduleId, roleId);
				module.setModuleFuns(funList);
				if (!funList.isEmpty())
				{
					moduleList.add(module);
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
		return moduleList;
	}

	public List<Module> queryAllModules()
	{
		List<Module> moduleList = new ArrayList<Module>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from module where 1=1 ");
			pstmt = connection.prepareStatement(sb.toString());
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Module module = new Module();
				String moduleId = resultSet.getString("module_id");
				module.setModuleId(moduleId);
				module.setModuleName(resultSet.getString("module_name"));
				List<ModuleFun> funList = new ModuleFunService().queryAllFunsByModuleId(moduleId);
				module.setModuleFuns(funList);
				moduleList.add(module);
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
}
