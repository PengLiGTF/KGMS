package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kindergarten.data.FeeStaticModel;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.DbUtils;

public class FeeStaticService
{
	/**
	 * 按统计类型统计费用
	 * */
	public List<FeeStaticModel> doFeeStatic(String groupByCode)
	{
		List<FeeStaticModel> list = new ArrayList<FeeStaticModel>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT t.* , t.sumActualMoney + t. sumDeductionPrefee + t.sumPreFee totalMoney FROM ");
		sb.append("( ");
		sb.append(" SELECT g.`grade_name`, g.grade_id,c.class_id,c.`class_name`,");
		sb.append("SUM(privilege_money) sumPrivilegeMoney,");
		sb.append("SUM(other_money) sumOtherMoney,");
		sb.append("SUM(pre_fee) sumPreFee,");
		sb.append("SUM(deduction_PreFee) sumDeductionPrefee,");
		sb.append("SUM(actual_money) sumActualMoney");
		sb.append(" FROM kinder_fee_history kf, kinder k,classes c, grade g ");
		sb.append(" WHERE k.kinder_id = kf.`kinder_id` AND k.`kinder_grade_id` = g.`grade_id` ");
		sb.append(" AND k.`kinder_class_id` = c.`class_id` AND feeVoucher_status != 103 ");
		if (CommonUtil.STATIC_BY_CLASS.equals(groupByCode))
		{
			sb.append(" GROUP BY kinder_class_id ");
		} else
		{
			sb.append(" GROUP BY kinder_grade_id ");
		}
		sb.append(") t");

		sb.append(" union ");

		sb.append("SELECT '合计' grade_name, '' grade_id,''class_id,'' class_name,SUM(t3.sumPrivilegeMoney) sumPrivilegeMoney,SUM(t3.sumOtherMoney) sumOtherMoney,SUM(t3.sumActualMoney) sumActualMoney,");
		sb.append("SUM(t3.sumPreFee) sumPreFee,SUM(t3.sumDeductionPrefee) sumDeductionPrefee,SUM(t3.totalMoney) totalMoney");
		sb.append(" from ");
		sb.append(" ( ");

		sb.append("SELECT t2.* , t2.sumActualMoney + t2. sumDeductionPrefee + t2.sumPreFee totalMoney FROM ");
		sb.append("( ");
		sb.append(" SELECT g.`grade_name`, g.grade_id,c.class_id,c.`class_name`,");
		sb.append("SUM(privilege_money) sumPrivilegeMoney,");
		sb.append("SUM(other_money) sumOtherMoney,");
		sb.append("SUM(pre_fee) sumPreFee,");
		sb.append("SUM(deduction_PreFee) sumDeductionPrefee,");
		sb.append("SUM(actual_money) sumActualMoney");
		sb.append(" FROM kinder_fee_history kf, kinder k,classes c, grade g ");
		sb.append(" WHERE k.kinder_id = kf.`kinder_id` AND k.`kinder_grade_id` = g.`grade_id` ");
		sb.append(" AND k.`kinder_class_id` = c.`class_id` AND feeVoucher_status != 103 ");
		if (CommonUtil.STATIC_BY_CLASS.equals(groupByCode))
		{
			sb.append(" GROUP BY kinder_class_id ");
		} else
		{
			sb.append(" GROUP BY kinder_grade_id ");
		}
		sb.append(") t2");
		sb.append(" )t3");

		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				FeeStaticModel temp = new FeeStaticModel();
				temp.setGradeName(resultSet.getString("grade_name"));
				temp.setClassName(resultSet.getString("class_name"));
				temp.setSumPrivilegeMoney(resultSet.getDouble("sumPrivilegeMoney"));
				temp.setSumOtherMoney(resultSet.getDouble("sumOtherMoney"));
				temp.setSumPreMoney(resultSet.getDouble("sumPreFee"));
				temp.setSumDeductionPreMoney(resultSet.getDouble("sumDeductionPrefee"));
				temp.setSumActualMoney(resultSet.getDouble("sumActualMoney"));
				temp.setTotalMoney(resultSet.getDouble("totalMoney"));
				list.add(temp);
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
		return list;
	}
}
