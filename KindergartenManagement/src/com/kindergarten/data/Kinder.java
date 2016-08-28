package com.kindergarten.data;

import java.util.ArrayList;
import java.util.List;

public class Kinder
{
	private String kinderId;
	private String kinderName;
	private char kinderSex = 'M';//default
	private int kinderGradeId;
	private int kinderClassId;
	private String kinderStatusCode;
	private List<KinderFeeInfo> kinderFeeInfoList = new ArrayList<KinderFeeInfo>();

	public List<KinderFeeInfo> getKinderFeeInfoList()
	{
		return kinderFeeInfoList;
	}

	public void setKinderFeeInfoList(List<KinderFeeInfo> kinderFeeInfoList)
	{
		this.kinderFeeInfoList = kinderFeeInfoList;
	}

	public String getKinderId()
	{
		return kinderId;
	}

	public void setKinderId(String kinderId)
	{
		this.kinderId = kinderId;
	}

	public String getKinderName()
	{
		return kinderName;
	}

	public void setKinderName(String kinderName)
	{
		this.kinderName = kinderName;
	}

	public char getKinderSex()
	{
		return kinderSex;
	}

	public void setKinderSex(char kinderSex)
	{
		this.kinderSex = kinderSex;
	}

	public int getKinderGradeId()
	{
		return kinderGradeId;
	}

	public void setKinderGradeId(int kinderGradeId)
	{
		this.kinderGradeId = kinderGradeId;
	}

	public int getKinderClassId()
	{
		return kinderClassId;
	}

	public void setKinderClassId(int kinderClassId)
	{
		this.kinderClassId = kinderClassId;
	}

	public String getKinderStatusCode()
	{
		return kinderStatusCode;
	}

	public void setKinderStatusCode(String kinderStatusCode)
	{
		this.kinderStatusCode = kinderStatusCode;
	}
}
