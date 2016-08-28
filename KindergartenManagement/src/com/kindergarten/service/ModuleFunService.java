package com.kindergarten.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kindergarten.data.FunOperation;
import com.kindergarten.data.ModuleFun;
import com.kindergarten.util.DbUtils;

public class ModuleFunService
{
	private FunOperationService funOperationService =new FunOperationService();
	public List<ModuleFun> queryAllFunsByModuleIdAndRoleId(String moduleId, String roleId)
	{
		List<ModuleFun> moduleList = new ArrayList<ModuleFun>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select a.* from module_fun a, role_fun b where 1=1 ");
			sb.append(" and a.fun_id = b.fun_id ");
			sb.append(" and a.module_id = ?  ");
			sb.append(" and b.role_id = ?");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, moduleId);
			pstmt.setString(2, roleId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				ModuleFun moduleFun = new ModuleFun();
				moduleFun.setModuleId(resultSet.getString("module_id"));
				String funId = resultSet.getString("fun_id");
				moduleFun.setFunId(funId);
				moduleFun.setFunName(resultSet.getString("fun_name"));
				List<FunOperation> operList = funOperationService.queryGrantedFunOper(roleId, funId);
				moduleFun.setFunOperationList(operList);
				moduleList.add(moduleFun);
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

	public List<ModuleFun> queryAllFunsByModuleId(String moduleId)
	{
		List<ModuleFun> moduleList = new ArrayList<ModuleFun>();
		// 赋操作权限之前每个功能都有几个操作
		List<FunOperation> operList = new FunOperationService().queryAllFunOperation();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from module_fun where 1=1 and module_id=?");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, moduleId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				ModuleFun moduleFun = new ModuleFun();
				moduleFun.setModuleId(resultSet.getString("module_id"));
				String funId = resultSet.getString("fun_id");
				moduleFun.setFunId(funId);
				moduleFun.setFunName(resultSet.getString("fun_name"));
				moduleFun.setFunOperationList(deSerializeFunOperList(operList, funId));
				moduleList.add(moduleFun);
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

	public static void main(String[] args)
	{
		List<FunOperation> list = new ArrayList<FunOperation>();
		FunOperation op = new FunOperation();
		op.setOperationId("btnNew");
		op.setOperationName("新增");
		list.add(op);
		List<FunOperation> list2 = list;// deSerializeFunOperList(list);
		System.out.println("ddddddddddd");

	}

	@SuppressWarnings("unchecked")
	private List<FunOperation> deSerializeFunOperList(List<FunOperation> operList, String funId)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objOut;
		ObjectInputStream objInput;
		List<FunOperation> deSerializabled = null;
		try
		{
			objOut = new ObjectOutputStream(out);
			objOut.writeObject(operList);
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			objInput = new ObjectInputStream(in);
			deSerializabled = (List<FunOperation>) objInput.readObject();
			for (FunOperation funOperation : deSerializabled)
			{
				funOperation.setFunId(funId);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return deSerializabled;
	}
}
