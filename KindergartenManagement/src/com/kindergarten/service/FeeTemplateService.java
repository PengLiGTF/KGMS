package com.kindergarten.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.kindergarten.data.FeeTemplate;
import com.kindergarten.util.DbUtils;

public class FeeTemplateService
{

	public void deleteFeeTemplates(List<FeeTemplate> templateList)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append("delete from fee_template where id = ?");
			pstmt = connection.prepareStatement(sb.toString());
			for (FeeTemplate temp : templateList)
			{
				pstmt.setInt(1, temp.getId());
				pstmt.addBatch();
			}
			pstmt.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
	}

	public void updateFeeTemplate(int id, String name, Double amount)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" update  fee_template set fee_template_name=?,fee_amount=? where id = ? ");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, name);
			pstmt.setDouble(2, amount);
			pstmt.setInt(3, id);
			pstmt.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
	}

	public FeeTemplate getFeeTemplateById(int feeTemplateId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select * from fee_template where id = ? ");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setInt(1, feeTemplateId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				FeeTemplate template = new FeeTemplate();
				template.setId(resultSet.getInt("id"));
				template.setFeeTemplateName(resultSet.getString("fee_template_name"));
				template.setFeeAmount(resultSet.getDouble("fee_amount"));
				return template;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public List<FeeTemplate> queryByCondition(String name, BigDecimal amount)
	{
		List<FeeTemplate> list = new ArrayList<FeeTemplate>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from fee_template where 1=1 ");
		boolean nameIsNotNull = false;
		boolean amountIsNotNull = false;
		if (!StringUtils.isBlank(name))
		{
			nameIsNotNull = true;
			sb.append(" and fee_template_name like ?");
		}
		if (amount != null)
		{
			sb.append(" and fee_amount like ?");
			amountIsNotNull = true;
		}
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			if (nameIsNotNull)
			{
				pstmt.setString(1, "%" + name + "%");
			}
			if (amountIsNotNull && nameIsNotNull)
			{
				pstmt.setString(2, "%" + amount.doubleValue() + "%");
			}
			if (!nameIsNotNull && amountIsNotNull)
			{
				pstmt.setString(1, "%" + amount.intValue() + "%");
			}
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				FeeTemplate template = new FeeTemplate();
				template.setId(resultSet.getInt("id"));
				template.setFeeTemplateName(resultSet.getString("fee_template_name"));
				template.setFeeAmount(resultSet.getDouble("fee_amount"));
				list.add(template);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	public void addFeeTemplate(String templateName, double amount)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" insert into fee_template(fee_template_name, fee_amount) values(?,?) ");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, templateName);
			pstmt.setDouble(2, amount);
			pstmt.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
	}

	/**
	 * 
	 * */
	public List<FeeTemplate> queryAllFeeTemplateList()
	{
		List<FeeTemplate> templateList = new ArrayList<FeeTemplate>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from fee_template ");
			pstmt = connection.prepareStatement(sb.toString());
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				FeeTemplate template = new FeeTemplate();
				template.setId(resultSet.getInt("id"));
				template.setFeeTemplateName(resultSet.getString("fee_template_name"));
				template.setFeeAmount(resultSet.getInt("fee_amount"));
				templateList.add(template);
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
		return templateList;
	}
}
