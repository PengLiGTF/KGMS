package com.kindergarten.data;

import java.util.Date;

public class FeeExpireKinder
{
	private String kinderId;
	private String kinderName;
	private String kinderGradeName;
	private String kinderClassName;
	private Date feeTime;
	private int feeDays;
	private Date feeExpireTime;

	private double preFeeMoney = 0.00D;
	private double deductionPreFeeMoney = 0.00D;

	public double getDeductionPreFeeMoney()
	{
		return deductionPreFeeMoney;
	}

	public void setDeductionPreFeeMoney(double deductionPreFeeMoney)
	{
		this.deductionPreFeeMoney = deductionPreFeeMoney;
	}

	public double getPreFeeMoney()
	{
		return preFeeMoney;
	}

	public void setPreFeeMoney(double preFeeMoney)
	{
		this.preFeeMoney = preFeeMoney;
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

	public String getKinderGradeName()
	{
		return kinderGradeName;
	}

	public void setKinderGradeName(String kinderGradeName)
	{
		this.kinderGradeName = kinderGradeName;
	}

	public String getKinderClassName()
	{
		return kinderClassName;
	}

	public void setKinderClassName(String kinderClassName)
	{
		this.kinderClassName = kinderClassName;
	}

	public Date getFeeTime()
	{
		return feeTime;
	}

	public void setFeeTime(Date feeTime)
	{
		this.feeTime = feeTime;
	}

	public int getFeeDays()
	{
		return feeDays;
	}

	public void setFeeDays(int feeDays)
	{
		this.feeDays = feeDays;
	}

	public Date getFeeExpireTime()
	{
		return feeExpireTime;
	}

	public void setFeeExpireTime(Date feeExpireTime)
	{
		this.feeExpireTime = feeExpireTime;
	}
}
