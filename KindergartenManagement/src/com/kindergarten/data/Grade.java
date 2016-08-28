package com.kindergarten.data;

/**
 * 年级对象模型
 * */
public class Grade
{
	private int gradeId;
	private String gradeName;

	public int getGradeId()
	{
		return gradeId;
	}

	public void setGradeId(int gradeId)
	{
		this.gradeId = gradeId;
	}

	public String getGradeName()
	{
		return gradeName;
	}

	public void setGradeName(String gradeName)
	{
		this.gradeName = gradeName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + gradeId;
		result = prime * result + ((gradeName == null) ? 0 : gradeName.hashCode());
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
		Grade other = (Grade) obj;
		if (gradeId != other.gradeId)
			return false;
		if (gradeName == null)
		{
			if (other.gradeName != null)
				return false;
		} else if (!gradeName.equals(other.gradeName))
			return false;
		return true;
	}
}
