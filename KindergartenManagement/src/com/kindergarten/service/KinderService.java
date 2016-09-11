package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindergarten.data.FeeExpireKinder;
import com.kindergarten.data.Kinder;
import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.data.KinderInfoModel;
import com.kindergarten.data.User;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.DbUtils;
import com.kindergarten.util.FeeTypeConstant;

public class KinderService
{

	/**
	 * 检测学号对于学生是否存在
	 * */
	public boolean isKinderExist(String kinderId)
	{
		boolean existed = false;
		StringBuilder sb = new StringBuilder();
		sb.append("select count(kinder_id) kinders from kinder where kinder_id = ? ");
		Connection conn = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, kinderId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				int num = resultSet.getInt("kinders");
				if (num > 0)
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
			DbUtils.closeQuietly(conn);
		}

		return existed;
	}

	/**
	 * 预交费学生入园处理，需要更新学生的预交费记录 ，将预交费记录设置为 作废状态，重新插入一条记录
	 * 
	 * @throws SQLException
	 * 
	 * */
	public void addKinderWithPreFee(Kinder kinder, KinderFeeInfo preFeeKinder) throws SQLException
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			connection.setAutoCommit(false);
			StringBuilder sb = new StringBuilder();
			sb.append(" update kinder_fee_history set feeVoucher_status = '103' where id = ?");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setInt(1, preFeeKinder.getKinderFeeInfoId());
			pstmt.executeUpdate();
			double deductionPreFee = kinder.getKinderFeeInfoList().get(0).getDeductionPreFee();
			double preFeeLeft = preFeeKinder.getPreFeeMoney() - deductionPreFee;
			preFeeKinder.setPreFeeMoney(preFeeLeft);
			addKinderWithTransaction(kinder, connection);
			// 重新插入一条预交费记录，更新预交费状态
			if(preFeeLeft > 0)
			{
				preFeeKinder.setFeeReason("预交费抵扣后剩余");
				//剩余预缴费对应的优惠额和其他费用都清空
				preFeeKinder.setPrivilegeMoney(0.00D);
				preFeeKinder.setOtherMoney(0.00D);
				addPrefeeRecord(preFeeKinder, connection);
			}
			connection.commit();
			connection.setAutoCommit(true);
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}

	}

	/**
	 * 改变学生缴费记录为反审核状态
	 * 
	 * @throws SQLException
	 * 
	 * */
	public void updateFeeStatusToUnCheck(int kinderFeePrimaryId) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" update kinder_fee_history set feeVoucher_status = ? where id = ?");
		Connection conn = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, CommonUtil.UN_CHECKED);
			pstmt.setInt(2, kinderFeePrimaryId);
			pstmt.executeUpdate();
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(conn);
		}
	}

	/**
	 * 改变学生缴费记录为作废，如果有抵扣预交费，则将预交费增加或新增一条预缴费记录
	 * 
	 * @throws SQLException
	 * 
	 * */
	public void updateFeeStatusToInValid(int kinderFeePrimaryId) throws SQLException
	{
		Connection conn = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		ResultSet resultSet1 = null;
		try
		{
			conn.setAutoCommit(false);
			StringBuilder sb1 = new StringBuilder();
			sb1.append("select kinder_id,deduction_prefee,operator_user_id from kinder_fee_history where id = ? ");
			pstmt1 = conn.prepareStatement(sb1.toString());
			pstmt1.setInt(1, kinderFeePrimaryId);
			resultSet1 = pstmt1.executeQuery();
			double deductionPrefee = 0.00d;
			String kinderId = "";
			String operUserId = "";
			while (resultSet1.next())
			{
				kinderId = resultSet1.getString(1);
				deductionPrefee = resultSet1.getDouble(2);
				operUserId = resultSet1.getString(3);
			}
			KinderFeeInfo kinderFeeInfo = new KinderFeeInfo();
			kinderFeeInfo.setKinderId(kinderId);
			kinderFeeInfo.setPreFeeMoney(deductionPrefee);
			kinderFeeInfo.setOperTime(new Date());
			kinderFeeInfo.setFeeTime(new Date());
			kinderFeeInfo.setFeeExpireTime(new Date());
			User user = new User();
			user.setUserId(operUserId);
			kinderFeeInfo.setOperator(user);
			kinderFeeInfo.setFeeReason("缴费作废抵扣预缴费充预缴费");
			addPrefeeRecord(kinderFeeInfo, conn);// TODO 是否存在未用完的预缴费，存在则更新之
			StringBuilder sb = new StringBuilder();
			sb.append(" update kinder_fee_history set feeVoucher_status = ? where id = ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, CommonUtil.INVALID);
			pstmt.setInt(2, kinderFeePrimaryId);
			pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		} finally
		{
			conn.setAutoCommit(true);
			DbUtils.closeQuietly(resultSet1);
			DbUtils.closeQuietly(pstmt1);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(conn);
		}
	}

	/**
	 * 更新幼儿缴费信息
	 * 
	 * @throws SQLException
	 * */
	public void updateKinder(Kinder kinder) throws SQLException
	{
		StringBuilder sbDelKinderFeeInfo = new StringBuilder();
		sbDelKinderFeeInfo.append(" delete from kinder_fee_history where id = ?");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(sbDelKinderFeeInfo.toString());
			pstmt.setInt(1, kinder.getKinderFeeInfoList().get(0).getKinderFeeInfoId());
			pstmt.execute();
			addKinderWithTransaction(kinder, connection);
			connection.commit();
			connection.setAutoCommit(true);
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
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
	}

	/**
	 * 根据条件查询预缴费学生信息
	 * 
	 * */
	public List<KinderFeeInfo> queryPreFeeKinderListByCondition(Map<String, String> param)
	{
		List<KinderFeeInfo> list = new ArrayList<KinderFeeInfo>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.*,b.`kinder_name`,b.`kinder_sex` FROM kinder_fee_history a ,kinder b WHERE a.`kinder_id` = b.`kinder_id` ");
		// condition
		String kinderName = param.get("kinderName") == null ? "" : param.get("kinderName");
		String kinderId = param.get("kinderId") == null ? "" : param.get("kinderId");
		String feeDate = param.get("feeDate") == null ? "" : param.get("feeDate");
		sb.append(" and a.fee_type = 'preFee' ");
		sb.append(" and (a.feeVoucher_status != '103' or a.feeVoucher_status is NULL)");
		sb.append(" and a.pre_fee > 0 ");
		sb.append(" and b.kinder_name like ? ");
		sb.append(" and a.kinder_id like ? ");
		sb.append(" and a.fee_time like ?");

		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, "%" + kinderName + "%");
			pstmt.setString(2, "%" + kinderId + "%");
			pstmt.setString(3, "%" + feeDate + "%");
			// pstmt.setString(1, kinderName);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				KinderFeeInfo temp = new KinderFeeInfo();
				temp.setKinderFeeInfoId(resultSet.getInt("id"));// 获取主键ID
				temp.setKinderId(resultSet.getString("kinder_id"));
				temp.setKinderName(resultSet.getString("kinder_name"));
				temp.setFeeEvent(resultSet.getString("fee_event"));
				temp.setFeeDays(resultSet.getInt("fee_days"));
				temp.setFeeTemplateId(resultSet.getInt("fee_template_id"));
				temp.setSex(resultSet.getString("kinder_sex") == null ? 'M' : resultSet.getString("kinder_sex").charAt(0));

				temp.setFeeTime(resultSet.getDate("fee_time"));
				temp.setFeeVoucherStatus(resultSet.getInt("feeVoucher_status"));
				temp.setPreFeeMoney(resultSet.getDouble("pre_fee"));
				User user = new User();
				user.setUserId(resultSet.getString("operator_user_id"));
				temp.setOperator(user);
				temp.setOperTime(resultSet.getDate("operator_time"));
				temp.setActualMoney(resultSet.getDouble("actual_money"));
				temp.setOtherMoney(resultSet.getDouble("other_money"));
				temp.setPrivilegeMoney(resultSet.getDouble("privilege_money"));
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

	/**
	 * 根据条件查询学生信息
	 * 
	 * */
	public List<KinderInfoModel> queryKinderInfoByCondition(Map<String, String> param)
	{
		List<KinderInfoModel> list = new ArrayList<KinderInfoModel>();
		String kinderId = param.get("kinderId") == null ? "" : (String) param.get("kinderId");
		String kinderName = param.get("kinderName") == null ? "" : (String) param.get("kinderName");
		String gradeId = param.get("gradeId") == null ? "" : (String) param.get("gradeId");
		String classId = param.get("classId") == null ? "" : (String) param.get("classId");
		String kinderStatus = param.get("kinderStatus") == null ? "" : (String) param.get("kinderStatus");

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT t.*, c.`grade_name`,d.`class_name`  FROM( ");
		sb.append(" SELECT a.`kinder_id`,a.`kinder_class_id`, a.`kinder_grade_id`,a.`kinder_name`,a.`kinder_sex`,a.`kinder_status_Code`,b.`fee_time`,b.`fee_days`,MAX(b.`fee_expire_time`) fee_expire_time ");
		sb.append("  FROM kinder a LEFT JOIN kinder_fee_history b  ON  a.`kinder_id`= b.`kinder_id`  ");
		sb.append(" GROUP BY a.`kinder_id`  ORDER BY b.`fee_expire_time` ASC )t ,grade c, classes d ");
		sb.append(" WHERE  t.`kinder_grade_id` = c.`grade_id`   AND t.`kinder_class_id` = d.`class_id` ");
		sb.append(" and t.kinder_id like ? ");
		sb.append(" and t.kinder_name like ? ");
		sb.append(" and c.grade_id like ? ");
		sb.append(" and d.class_id like ? ");
		sb.append(" and t.kinder_status_Code like ? ");

		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, "%" + kinderId + "%");
			pstmt.setString(2, "%" + kinderName + "%");
			pstmt.setString(3, gradeId.equals("") ? ("%" + gradeId + "%") : gradeId);
			pstmt.setString(4, classId.equals("") ? ("%" + classId + "%") : classId);
			pstmt.setString(5, "%" + kinderStatus + "%");
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				KinderInfoModel temp = new KinderInfoModel();
				temp.setKinderId(resultSet.getString("kinder_id"));
				temp.setKinderClassName(resultSet.getString("class_name"));
				temp.setKinderGradeName(resultSet.getString("grade_name"));
				temp.setKinderName(resultSet.getString("kinder_name"));
				temp.setKinderSex(resultSet.getString("kinder_sex"));
				temp.setKinderStatus(resultSet.getString("kinder_status_Code"));
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

	/**
	 * 查找下月到期费用到期学生
	 * 
	 * */
	public List<FeeExpireKinder> queryExpireAtNextMonth(Map<String, Object> param)
	{
		List<FeeExpireKinder> list = new ArrayList<FeeExpireKinder>();
		String kinderId = param.get("kinderId") == null ? "" : (String) param.get("kinderId");
		String kinderName = param.get("kinderName") == null ? "" : (String) param.get("kinderName");
		String gradeId = param.get("gradeId") == null ? "" : (String) param.get("gradeId");
		String classId = param.get("classId") == null ? "" : (String) param.get("classId");

		StringBuilder sb = new StringBuilder();
		sb.append("select * from (");
		sb.append(" SELECT a.`kinder_id`,a.`kinder_name`,MAX(b.`fee_time`) fee_time,MAX(b.`fee_days`) fee_days,MAX(b.`fee_expire_time`) fee_expire_time,");
		sb.append(" c.`grade_name`,d.`class_name` ");
		sb.append(" FROM kinder a, kinder_fee_history b ,grade c, classes d ");
		sb.append(" where ");
		sb.append(" a.`kinder_id` = b.`kinder_id` ");
		sb.append(" AND a.`kinder_grade_id` = c.`grade_id` ");
		sb.append(" AND a.`kinder_class_id` = d.`class_id` ");
		sb.append(" AND b.fee_type != 'preFee'");
		// conditioins
		sb.append(" and a.kinder_id like ? ");
		sb.append(" and a.kinder_name like ? ");
		sb.append(" and c.grade_id like ? ");
		sb.append(" and d.class_id like ? ");
		sb.append(" GROUP BY a.`kinder_id` ");
		sb.append(" ORDER BY b.`fee_expire_time` ASC ");
		sb.append(" )a1 where a1.fee_expire_time <= LAST_DAY(NOW())");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, "%" + kinderId + "%");
			pstmt.setString(2, "%" + kinderName + "%");
			pstmt.setString(3, "%" + gradeId + "%");
			pstmt.setString(4, "%" + classId + "%");
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				FeeExpireKinder temp = new FeeExpireKinder();
				temp.setKinderId(resultSet.getString("kinder_id"));
				// temp.setFeeEvent(resultSet.getString("fee_event"));
				temp.setFeeDays(resultSet.getInt("fee_days"));
				// temp.setFeeTemplateId(resultSet.getInt("fee_template_id"));
				temp.setFeeExpireTime(resultSet.getDate("fee_expire_time"));
				temp.setFeeTime(resultSet.getDate("fee_time"));
				temp.setKinderClassName(resultSet.getString("class_name"));
				temp.setKinderGradeName(resultSet.getString("grade_name"));
				temp.setKinderName(resultSet.getString("kinder_name"));
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

	/**
	 * 根据学号获取kinder信息
	 * */
	public Kinder getKinderByKinderId(String kinderId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select * from kinder where kinder_id = ?");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, kinderId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Kinder kinder = new Kinder();
				kinder.setKinderName(resultSet.getString("kinder_name"));
				kinder.setKinderSex(resultSet.getString("kinder_sex").charAt(0));
				return kinder;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 管理员获取学生最近的一次缴费情况进行修改，或反审核
	 * */
	public List<KinderFeeInfo> queryLastedFeeRecord(String kinderId, boolean isAdmin)
	{
		List<KinderFeeInfo> feeHisList = new ArrayList<KinderFeeInfo>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select t.*,max(t.fee_expire_time) maxExpireTime from (");
		sb.append(" SELECT a.*,b.`kinder_name`,b.kinder_status_Code,b.kinder_sex,b.kinder_grade_id, b.kinder_class_id FROM kinder_fee_history a ,kinder b ");
		sb.append(" WHERE a.`kinder_id` = b.`kinder_id` and a.fee_type != 'preFee' and a.kinder_id like ? ");
		if (!isAdmin)
		{
			sb.append(" and (a.feeVoucher_status = '102' or a.feeVoucher_status != '103' ) ");// 反审核
		}
		sb.append("  order by fee_expire_time desc");
		sb.append(" ) t GROUP BY t.`kinder_id`");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, "%" + kinderId + "%");
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				KinderFeeInfo temp = new KinderFeeInfo();
				temp.setKinderFeeInfoId(resultSet.getInt("id"));
				temp.setKinderId(resultSet.getString("kinder_id"));
				temp.setKinderName(resultSet.getString("kinder_name"));
				temp.setFeeEvent(resultSet.getString("fee_event"));
				temp.setFeeDays(resultSet.getInt("fee_days"));
				temp.setFeeTemplateId(resultSet.getInt("fee_template_id"));
				temp.setFeeExpireTime(resultSet.getDate("fee_expire_time"));
				temp.setFeeTime(resultSet.getDate("fee_time"));
				temp.setFeeVoucherStatus(resultSet.getInt("feeVoucher_status"));
				temp.setKinderClassId(resultSet.getInt("kinder_class_id"));
				temp.setKinderGradeId(resultSet.getInt("kinder_grade_id"));
				temp.setKinderStatus(resultSet.getString("kinder_status_Code"));
				temp.setSex(resultSet.getString("kinder_sex").charAt(0));
				User user = new User();
				user.setUserId(resultSet.getString("operator_user_id"));
				temp.setOperator(user);
				temp.setOperTime(resultSet.getDate("operator_time"));
				temp.setActualMoney(resultSet.getDouble("actual_money"));
				temp.setOtherMoney(resultSet.getDouble("other_money"));
				temp.setPrivilegeMoney(resultSet.getDouble("privilege_money"));
				temp.setPreFeeMoney(resultSet.getDouble("pre_fee"));
				temp.setDeductionPreFee(resultSet.getDouble("deduction_prefee"));
				feeHisList.add(temp);
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
		return feeHisList;

	}

	/**
	 * 查找学生历史缴费信息，按缴费到期日期降序排序，第一条就是最近的一条缴费信息
	 * */
	public List<KinderFeeInfo> queryFeeHistory(String kinderId)
	{
		List<KinderFeeInfo> feeHisList = new ArrayList<KinderFeeInfo>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.*,b.`kinder_name`,b.kinder_sex,b.kinder_grade_id, b.kinder_class_id FROM kinder_fee_history a ,kinder b WHERE a.`kinder_id` = b.`kinder_id`  and a.kinder_id = ? order by fee_expire_time desc");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, kinderId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				KinderFeeInfo temp = new KinderFeeInfo();
				temp.setKinderId(kinderId);
				temp.setKinderFeeInfoId(resultSet.getInt("id"));
				temp.setKinderName(resultSet.getString("kinder_name"));
				temp.setFeeEvent(resultSet.getString("fee_event"));
				temp.setFeeDays(resultSet.getInt("fee_days"));
				temp.setFeeTemplateId(resultSet.getInt("fee_template_id"));
				temp.setFeeExpireTime(resultSet.getDate("fee_expire_time"));
				temp.setFeeTime(resultSet.getDate("fee_time"));
				temp.setFeeVoucherStatus(resultSet.getInt("feeVoucher_status"));
				temp.setKinderClassId(resultSet.getInt("kinder_class_id"));
				temp.setKinderGradeId(resultSet.getInt("kinder_grade_id"));
				temp.setSex(resultSet.getString("kinder_sex").charAt(0));
				
				temp.setPreFeeMoney(resultSet.getDouble("pre_fee"));
				temp.setPrivilegeMoney(resultSet.getDouble("privilege_money"));
				temp.setOtherMoney(resultSet.getDouble("other_money"));
				temp.setActualMoney(resultSet.getDouble("actual_money"));
				temp.setDeductionPreFee(resultSet.getDouble("deduction_prefee"));
				
				User user = new User();
				user.setUserId(resultSet.getString("operator_user_id"));
				temp.setOperator(user);
				temp.setOperTime(resultSet.getDate("operator_time"));
				temp.setActualMoney(resultSet.getDouble("actual_money"));
				temp.setOtherMoney(resultSet.getDouble("other_money"));
				temp.setPrivilegeMoney(resultSet.getDouble("privilege_money"));
				temp.setPreFeeMoney(resultSet.getDouble("pre_fee"));
				temp.setDeductionPreFee(resultSet.getDouble("deduction_prefee"));
				feeHisList.add(temp);
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
		return feeHisList;
	}
	
	/**
	 * 作废原有缴费记录，插入更新时候的缴费记录
	 * @throws SQLException 
	 * 
	 * */
	public void updateKinderFee(KinderFeeInfo feeInfo , Connection connection) throws SQLException
	{
		StringBuilder sbFee = new StringBuilder();
		sbFee.append(" insert into kinder_fee_history (fee_template_id,kinder_id,fee_days,fee_time,fee_expire_time,operator_user_id,operator_time,privilege_money,other_money,actual_money,feeVoucher_status,fee_event,deduction_prefee,fee_type) ");
		sbFee.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		PreparedStatement pstmtFee = null;
		pstmtFee = connection.prepareStatement(sbFee.toString());
		pstmtFee.setInt(1, feeInfo.getFeeTemplateId());
		pstmtFee.setString(2, feeInfo.getKinderId());
		pstmtFee.setInt(3, feeInfo.getFeeDays());
		pstmtFee.setDate(4, new java.sql.Date(feeInfo.getFeeTime().getTime()));
		pstmtFee.setDate(5, new java.sql.Date(feeInfo.getFeeExpireTime().getTime()));
		pstmtFee.setString(6, feeInfo.getOperator().getUserId());
		pstmtFee.setDate(7, new java.sql.Date(feeInfo.getOperTime().getTime()));
		pstmtFee.setDouble(8, feeInfo.getPrivilegeMoney());
		pstmtFee.setDouble(9, feeInfo.getOtherMoney());
		pstmtFee.setDouble(10, feeInfo.getActualMoney());
		pstmtFee.setInt(11, feeInfo.getFeeVoucherStatus());
		pstmtFee.setString(12, feeInfo.getFeeEvent());
		pstmtFee.setDouble(13, feeInfo.getDeductionPreFee());
		pstmtFee.setString(14, feeInfo.getFeeType());
		pstmtFee.execute();
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" update kinder_fee_history set feeVoucher_status = ? where id = ? ");
		PreparedStatement pstmtInvalidFee = null;
		pstmtInvalidFee = connection.prepareStatement(sb2.toString());
		pstmtInvalidFee.setInt(1, CommonUtil.INVALID);
		pstmtInvalidFee.setInt(2, feeInfo.getKinderFeeInfoId());
		pstmtInvalidFee.executeUpdate();
	}
	
	
	public void recordKinderFee(KinderFeeInfo feeInfo)
	{
		StringBuilder sbFee = new StringBuilder();
		sbFee.append(" insert into kinder_fee_history (fee_template_id,kinder_id,fee_days,fee_time,fee_expire_time,operator_user_id,operator_time,privilege_money,other_money,actual_money,feeVoucher_status,fee_event) ");
		sbFee.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmtFee = null;
		try
		{
			pstmtFee = connection.prepareStatement(sbFee.toString());
			pstmtFee.setInt(1, feeInfo.getFeeTemplateId());
			pstmtFee.setString(2, feeInfo.getKinderId());
			pstmtFee.setInt(3, feeInfo.getFeeDays());
			pstmtFee.setDate(4, new java.sql.Date(feeInfo.getFeeTime().getTime()));
			pstmtFee.setDate(5, new java.sql.Date(feeInfo.getFeeExpireTime().getTime()));
			pstmtFee.setString(6, feeInfo.getOperator().getUserId());
			pstmtFee.setDate(7, new java.sql.Date(feeInfo.getOperTime().getTime()));
			pstmtFee.setDouble(8, feeInfo.getPrivilegeMoney());
			pstmtFee.setDouble(9, feeInfo.getOtherMoney());
			pstmtFee.setDouble(10, feeInfo.getActualMoney());
			pstmtFee.setInt(11, feeInfo.getFeeVoucherStatus());
			pstmtFee.setString(12, feeInfo.getFeeEvent());
			pstmtFee.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(pstmtFee);
			DbUtils.closeQuietly(connection);
		}
	}

	/**
	 * 给指定学生插入一条预交费记录
	 * 
	 * @throws SQLException
	 * 
	 * */
	public void addPrefeeRecord(KinderFeeInfo feeInfo, Connection connection) throws SQLException
	{
		StringBuilder sbFee = new StringBuilder();
		String reasonStr = feeInfo.getFeeReason();
		String reason = (reasonStr == null || "".equals(reasonStr)) ? "预缴费" : reasonStr;
		sbFee.append(" insert into kinder_fee_history (kinder_id,fee_time,operator_user_id,operator_time,privilege_money,other_money,fee_event,pre_fee,fee_type,fee_expire_time) ");
		sbFee.append(" values(?,?,?,?,?,?,'").append(reason).append("',?,?,?)");
		PreparedStatement pstmtFee = null;
		try
		{
			pstmtFee = connection.prepareStatement(sbFee.toString());
			pstmtFee.setString(1, feeInfo.getKinderId());
			pstmtFee.setDate(2, new java.sql.Date(feeInfo.getFeeTime().getTime()));
			pstmtFee.setString(3, feeInfo.getOperator().getUserId());
			pstmtFee.setDate(4, new java.sql.Date(feeInfo.getOperTime().getTime()));
			pstmtFee.setDouble(5, feeInfo.getPrivilegeMoney());
			pstmtFee.setDouble(6, feeInfo.getOtherMoney());
			pstmtFee.setDouble(7, feeInfo.getPreFeeMoney());
			pstmtFee.setString(8, FeeTypeConstant.PRE_FEE);
			pstmtFee.setDate(9, new java.sql.Date(feeInfo.getFeeExpireTime() == null ? new Date().getTime() : feeInfo.getFeeExpireTime().getTime()));
			pstmtFee.execute();
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
			DbUtils.closeQuietly(pstmtFee);
		}
	}

	/**
	 * 预交费学生录入
	 * */
	public void addPerFeeKinder(Kinder kinder) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into kinder(kinder_id,kinder_name,kinder_sex) ");
		sb.append(" values(?,?,?)");
		StringBuilder sbFee = new StringBuilder();
		sbFee.append(" insert into kinder_fee_history (kinder_id,fee_time,operator_user_id,operator_time,privilege_money,other_money,fee_event,pre_fee,fee_type,fee_expire_time) ");
		sbFee.append(" values(?,?,?,?,?,?,'预缴费',?,?,?)");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmtFee = null;
		try
		{
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, kinder.getKinderId());
			pstmt.setString(2, kinder.getKinderName());
			pstmt.setString(3, String.valueOf(kinder.getKinderSex()));
			pstmt.execute();
			pstmtFee = connection.prepareStatement(sbFee.toString());
			List<KinderFeeInfo> feeInfoList = kinder.getKinderFeeInfoList();
			if (!feeInfoList.isEmpty())
			{
				KinderFeeInfo feeInfo = feeInfoList.get(0);
				pstmtFee.setString(1, feeInfo.getKinderId());
				pstmtFee.setDate(2, new java.sql.Date(feeInfo.getFeeTime().getTime()));
				pstmtFee.setString(3, feeInfo.getOperator().getUserId());
				pstmtFee.setDate(4, new java.sql.Date(feeInfo.getOperTime().getTime()));
				pstmtFee.setDouble(5, feeInfo.getPrivilegeMoney());
				pstmtFee.setDouble(6, feeInfo.getOtherMoney());
				pstmtFee.setDouble(7, feeInfo.getPreFeeMoney());
				pstmtFee.setString(8, feeInfo.getFeeType());
				pstmtFee.setDate(9, new java.sql.Date(feeInfo.getFeeExpireTime().getTime()));
				pstmtFee.execute();
				connection.commit();
				connection.setAutoCommit(true);
			}
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
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(pstmtFee);
			DbUtils.closeQuietly(connection);
		}

	}

	/**
	 * 删除原有记录然后再插入学生记录
	 * */
	public void addKinderWithTransaction(Kinder kinder, Connection connection) throws SQLException
	{
		StringBuilder sb1 = new StringBuilder();
		sb1.append("delete from kinder where kinder_id = ?");
		StringBuilder sb = new StringBuilder();
		sb.append("insert into kinder(kinder_id,kinder_name,kinder_sex,kinder_grade_id,kinder_class_id,kinder_status_Code) ");
		sb.append(" values(?,?,?,?,?,?)");
		StringBuilder sbFee = new StringBuilder();
		sbFee.append(" insert into kinder_fee_history (fee_template_id,kinder_id,fee_days,fee_time,fee_expire_time,operator_user_id,operator_time,privilege_money,other_money,actual_money,feeVoucher_status,fee_event,fee_type,deduction_prefee) ");
		sbFee.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtFee = null;
		try
		{
			pstmt1 = connection.prepareStatement(sb1.toString());
			pstmt1.setString(1, kinder.getKinderId());
			pstmt1.execute();
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, kinder.getKinderId());
			pstmt.setString(2, kinder.getKinderName());
			pstmt.setString(3, String.valueOf(kinder.getKinderSex()));
			pstmt.setInt(4, kinder.getKinderGradeId());
			pstmt.setInt(5, kinder.getKinderClassId());
			pstmt.setString(6, kinder.getKinderStatusCode());
			pstmt.execute();
			pstmtFee = connection.prepareStatement(sbFee.toString());
			List<KinderFeeInfo> feeInfoList = kinder.getKinderFeeInfoList();
			if (!feeInfoList.isEmpty())
			{
				KinderFeeInfo feeInfo = feeInfoList.get(0);
				pstmtFee.setInt(1, feeInfo.getFeeTemplateId());
				pstmtFee.setString(2, feeInfo.getKinderId());
				pstmtFee.setInt(3, feeInfo.getFeeDays());
				pstmtFee.setDate(4, new java.sql.Date(feeInfo.getFeeTime().getTime()));
				pstmtFee.setDate(5, new java.sql.Date(feeInfo.getFeeExpireTime().getTime()));
				pstmtFee.setString(6, feeInfo.getOperator().getUserId());
				pstmtFee.setDate(7, new java.sql.Date(feeInfo.getOperTime().getTime()));
				pstmtFee.setDouble(8, feeInfo.getPrivilegeMoney());
				pstmtFee.setDouble(9, feeInfo.getOtherMoney());
				pstmtFee.setDouble(10, feeInfo.getActualMoney());
				pstmtFee.setInt(11, feeInfo.getFeeVoucherStatus());
				pstmtFee.setString(12, feeInfo.getFeeReason());
				pstmtFee.setString(13, feeInfo.getFeeType());
				pstmtFee.setDouble(14, feeInfo.getDeductionPreFee());
				pstmtFee.execute();
			}
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
			DbUtils.closeQuietly(pstmt1);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(pstmtFee);
		}
	}

	/**
	 * 增加幼儿信息
	 * 
	 * TODO 需要记录操作日志
	 * 
	 * @throws SQLException
	 * */
	public void addKinder(Kinder kinder) throws SQLException
	{
		StringBuilder sb1 = new StringBuilder();
		sb1.append("delete from kinder where kinder_id = ?");
		StringBuilder sb = new StringBuilder();
		sb.append("insert into kinder(kinder_id,kinder_name,kinder_sex,kinder_grade_id,kinder_class_id,kinder_status_Code) ");
		sb.append(" values(?,?,?,?,?,?)");
		StringBuilder sbFee = new StringBuilder();
		sbFee.append(" insert into kinder_fee_history (fee_template_id,kinder_id,fee_days,fee_time,fee_expire_time,operator_user_id,operator_time,privilege_money,other_money,actual_money,feeVoucher_status,fee_event,fee_type,deduction_prefee) ");
		sbFee.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtFee = null;
		try
		{
			connection.setAutoCommit(false);
			pstmt1 = connection.prepareStatement(sb1.toString());
			pstmt1.setString(1, kinder.getKinderId());
			pstmt1.execute();
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, kinder.getKinderId());
			pstmt.setString(2, kinder.getKinderName());
			pstmt.setString(3, String.valueOf(kinder.getKinderSex()));
			pstmt.setInt(4, kinder.getKinderGradeId());
			pstmt.setInt(5, kinder.getKinderClassId());
			pstmt.setString(6, kinder.getKinderStatusCode());
			pstmt.execute();
			pstmtFee = connection.prepareStatement(sbFee.toString());
			List<KinderFeeInfo> feeInfoList = kinder.getKinderFeeInfoList();
			if (!feeInfoList.isEmpty())
			{
				KinderFeeInfo feeInfo = feeInfoList.get(0);
				pstmtFee.setInt(1, feeInfo.getFeeTemplateId());
				pstmtFee.setString(2, feeInfo.getKinderId());
				pstmtFee.setInt(3, feeInfo.getFeeDays());
				pstmtFee.setDate(4, new java.sql.Date(feeInfo.getFeeTime().getTime()));
				pstmtFee.setDate(5, new java.sql.Date(feeInfo.getFeeExpireTime().getTime()));
				pstmtFee.setString(6, feeInfo.getOperator().getUserId());
				pstmtFee.setDate(7, new java.sql.Date(feeInfo.getOperTime().getTime()));
				pstmtFee.setDouble(8, feeInfo.getPrivilegeMoney());
				pstmtFee.setDouble(9, feeInfo.getOtherMoney());
				pstmtFee.setDouble(10, feeInfo.getActualMoney());
				pstmtFee.setInt(11, feeInfo.getFeeVoucherStatus());
				pstmtFee.setString(12, feeInfo.getFeeReason());
				pstmtFee.setString(13, feeInfo.getFeeType());
				pstmtFee.setDouble(14, feeInfo.getDeductionPreFee());
				pstmtFee.execute();
			}
			connection.commit();
			connection.setAutoCommit(true);
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
			DbUtils.closeQuietly(pstmt1);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(pstmtFee);
			DbUtils.closeQuietly(connection);
		}
	}
}
