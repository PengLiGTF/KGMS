package com.kindergarten.data;

/**
 * 班级对象模型
 * */
public class ClassModel
{
	private int classId;
	private String className;
	private int gradeId;
	private int kinders;

	public int getKinders()
	{
		return kinders;
	}

	public void setKinders(int kinders)
	{
		this.kinders = kinders;
	}

	public int getClassId()
	{
		return classId;
	}

	public void setClassId(int classId)
	{
		this.classId = classId;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public int getGradeId()
	{
		return gradeId;
	}

	public void setGradeId(int gradeId)
	{
		this.gradeId = gradeId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + classId;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + gradeId;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassModel other = (ClassModel) obj;
		if (classId != other.classId)
			return false;
		if (className == null)
		{
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (gradeId != other.gradeId)
			return false;
		return true;
	}
}
