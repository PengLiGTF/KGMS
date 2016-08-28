package com.kindergarten.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kindergarten.data.ClassModel;
import com.kindergarten.data.Grade;
import com.kindergarten.util.DbUtils;

public class ClassManageService
{

	/**
	 * 删除班级信息
	 * */
	public void deleteClass(List<ClassModel> classModelList)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" delete from classes where class_id = ?");
			pstmt = connection.prepareStatement(sb.toString());
			for (ClassModel model : classModelList)
			{
				pstmt.setInt(1, model.getClassId());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
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
	 * 更新班级信息
	 * */
	public int updateClass(ClassModel classModel)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		int result = 0;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" update classes set class_name = ?, grade_id = ? where class_id = ?");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, classModel.getClassName());
			pstmt.setInt(2, classModel.getGradeId());
			pstmt.setInt(3, classModel.getClassId());
			result = pstmt.executeUpdate();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(connection);
		}
		return result;
	}

	/**
	 * 新增班级信息
	 * */
	public void addClass(String className, int gradeId)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" insert into classes(class_name, grade_id) values(?,?)");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, className);
			pstmt.setInt(2, gradeId);
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

	public Map<Grade, List<ClassModel>> gradeClassesMap;

	public Map<Grade, List<ClassModel>> queryAllGradeClasses()
	{
		Map<Grade, List<ClassModel>> map = new HashMap<Grade, List<ClassModel>>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select c.*, count(d.kinder_id) kinders from (");
		sb.append(" select a.class_id, a.class_name,b.grade_id,b.grade_name ");
		sb.append(" from grade b left join classes a ");
		sb.append(" on a.grade_id = b.grade_id ");
		sb.append(" ) c left join  kinder d on c.class_id = d.kinder_class_id ");
		sb.append(" group by c.class_id");

		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Grade grade = new Grade();
				grade.setGradeId(resultSet.getInt("grade_id"));
				grade.setGradeName(resultSet.getString("grade_name"));
				if (!map.containsKey(grade))
				{
					map.put(grade, new ArrayList<ClassModel>());
				}
				Integer clssId = resultSet.getInt("class_id");
				if (clssId != null)
				{
					ClassModel cModel = new ClassModel();
					cModel.setClassId(clssId);
					cModel.setClassName(resultSet.getString("class_name"));
					cModel.setGradeId(grade.getGradeId());
					cModel.setKinders(resultSet.getInt("kinders"));
					map.get(grade).add(cModel);
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
		return map;
	}

	public Map<Grade, List<ClassModel>> queryGradeClasses(String gradeName)
	{
		Map<Grade, List<ClassModel>> map = new HashMap<Grade, List<ClassModel>>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select a.class_id, a.class_name,b.grade_id,b.grade_name ");
		sb.append(" from classes a, grade b ");
		sb.append(" where a.grade_id = b.grade_id ");
		sb.append(" and b.grade_name=?");

		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, gradeName);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Grade grade = new Grade();
				grade.setGradeId(resultSet.getInt("grade_id"));
				grade.setGradeName(resultSet.getString("grade_name"));
				if (!map.containsKey(grade))
				{
					map.put(grade, new ArrayList<ClassModel>());
				}
				ClassModel cModel = new ClassModel();
				cModel.setClassId(resultSet.getInt("class_id"));
				cModel.setClassName(resultSet.getString("class_name"));
				cModel.setGradeId(grade.getGradeId());
				map.get(grade).add(cModel);
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
		return map;
	}

	public Grade addGrade(String gradeName)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmtQ = null;
		ResultSet resultSet = null;
		Grade grade = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" insert into grade(grade_name) values(?)");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, gradeName);
			pstmt.execute();
			sb = new StringBuilder();
			sb.append(" select grade_id, grade_name from grade where grade_name = ?");
			pstmtQ = connection.prepareStatement(sb.toString());
			pstmtQ.setString(1, gradeName);
			resultSet = pstmtQ.executeQuery();
			while (resultSet.next())
			{
				grade = new Grade();
				grade.setGradeId(resultSet.getInt("grade_id"));
				grade.setGradeName(resultSet.getString("grade_name"));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstmt);
			DbUtils.closeQuietly(pstmtQ);
			DbUtils.closeQuietly(connection);
		}
		return grade;
	}

	public List<ClassModel> queryClassesByGradeId(int gradeId)
	{
		List<ClassModel> list = new ArrayList<ClassModel>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();

			sb.append(" SELECT a.*,COUNT(b.kinder_id) kinders FROM   (");
			sb.append("  select * from classes where grade_id = ? ) a left join kinder b ");
			sb.append(" on a.`class_id` = b.`kinder_class_id` ");
			sb.append(" GROUP BY a.`class_id` ORDER BY kinders DESC ");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setInt(1, gradeId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				ClassModel classModel = new ClassModel();
				classModel.setGradeId(resultSet.getInt("grade_id"));
				classModel.setClassName(resultSet.getString("class_name"));
				classModel.setClassId(resultSet.getInt("class_id"));
				classModel.setKinders(resultSet.getInt("kinders"));
				list.add(classModel);
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

	public ClassModel getClassModelById(int classModelId)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ClassModel classModel = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from classes where class_id = ?");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setInt(1, classModelId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				classModel = new ClassModel();
				classModel.setClassId(classModelId);
				classModel.setClassName(resultSet.getString("class_name"));
				classModel.setGradeId(resultSet.getInt("grade_id"));
				break;
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
		return classModel;
	}

	public Grade getGradeByGradeId(int gradeId)
	{
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		Grade grade = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from grade where grade_id = ?");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setInt(1, gradeId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				grade = new Grade();
				grade.setGradeId(resultSet.getInt("grade_id"));
				grade.setGradeName(resultSet.getString("grade_name"));
				break;
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
		return grade;
	}

	public List<Grade> queryGradeByName(String gradeName)
	{
		List<Grade> list = new ArrayList<Grade>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from grade where grade_name=?");
			pstmt = connection.prepareStatement(sb.toString());
			pstmt.setString(1, gradeName);
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Grade grade = new Grade();
				grade.setGradeId(resultSet.getInt("grade_id"));
				grade.setGradeName(resultSet.getString("grade_name"));
				list.add(grade);
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

	public List<Grade> queryAllGrade()
	{
		List<Grade> list = new ArrayList<Grade>();
		Connection connection = DbUtils.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from grade ");
			pstmt = connection.prepareStatement(sb.toString());
			resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				Grade grade = new Grade();
				grade.setGradeId(resultSet.getInt("grade_id"));
				grade.setGradeName(resultSet.getString("grade_name"));
				list.add(grade);
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
