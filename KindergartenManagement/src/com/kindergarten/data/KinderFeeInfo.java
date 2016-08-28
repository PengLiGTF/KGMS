package com.kindergarten.data;

import java.util.Date;

public class KinderFeeInfo
{
	public static final int AUDITED = 0;
	public static final int UNAUDITED = 1;

	// kinder_fee_history历史表中主键
	private int kinderFeeInfoId;
	// 抵扣的预缴费用
	private double deductionPreFee;

	public double getDeductionPreFee()
	{
		return deductionPreFee;
	}

	public void setDeductionPreFee(double deductionPreFee)
	{
		this.deductionPreFee = deductionPreFee;
	}

	public int getKinderFeeInfoId()
	{
		return kinderFeeInfoId;
	}

	public void setKinderFeeInfoId(int kinderFeeInfoId)
	{
		this.kinderFeeInfoId = kinderFeeInfoId;
	}

	private String kinderId;
	private String kinderName;
	private int feeTemplateId;
	private int feeDays;
	private Date feeTime;
	private Date feeExpireTime;
	private double privilegeMoney;
	private double otherMoney;
	private double actualMoney;

	private int kinderGradeId;
	private int kinderClassId;

	// 学生状态 入园、为入园、一毕业、退学
	private String kinderStatus;

	public String getKinderStatus()
	{
		return kinderStatus;
	}

	public void setKinderStatus(String kinderStatus)
	{
		this.kinderStatus = kinderStatus;
	}

	private String feeReason;

	public String getFeeReason()
	{
		return feeReason;
	}

	public void setFeeReason(String feeReason)
	{
		this.feeReason = feeReason;
	}

	private String feeType;

	public String getFeeType()
	{
		return feeType;
	}

	public void setFeeType(String feeType)
	{
		this.feeType = feeType;
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

	private double preFeeMoney = 0.00;

	private char sex;

	public char getSex()
	{
		return sex;
	}

	public void setSex(char sex)
	{
		this.sex = sex;
	}

	private User operator;
	private Date operTime;

	// 收费记录状态
	private int feeVoucherStatus;

	private String feeEvent;

	public double getPreFeeMoney()
	{
		return preFeeMoney;
	}

	public void setPreFeeMoney(double preFeeMoney)
	{
		this.preFeeMoney = preFeeMoney;
	}

	public String getKinderName()
	{
		return kinderName;
	}

	public void setKinderName(String kinderName)
	{
		this.kinderName = kinderName;
	}

	public String getFeeEvent()
	{
		return feeEvent;
	}

	public void setFeeEvent(String feeEvent)
	{
		this.feeEvent = feeEvent;
	}

	public int getFeeVoucherStatus()
	{
		return feeVoucherStatus;
	}

	public void setFeeVoucherStatus(int feeVoucherStatus)
	{
		this.feeVoucherStatus = feeVoucherStatus;
	}

	public String getKinderId()
	{
		return kinderId;
	}

	public void setKinderId(String kinderId)
	{
		this.kinderId = kinderId;
	}

	public int getFeeTemplateId()
	{
		return feeTemplateId;
	}

	public void setFeeTemplateId(int feeTemplateId)
	{
		this.feeTemplateId = feeTemplateId;
	}

	public int getFeeDays()
	{
		return feeDays;
	}

	public void setFeeDays(int feeDays)
	{
		this.feeDays = feeDays;
	}

	public Date getFeeTime()
	{
		return feeTime;
	}

	public void setFeeTime(Date feeTime)
	{
		this.feeTime = feeTime;
	}

	public Date getFeeExpireTime()
	{
		return feeExpireTime;
	}

	public void setFeeExpireTime(Date feeExpireTime)
	{
		this.feeExpireTime = feeExpireTime;
	}

	public double getPrivilegeMoney()
	{
		return privilegeMoney;
	}

	public void setPrivilegeMoney(double privilegeMoney)
	{
		this.privilegeMoney = privilegeMoney;
	}

	public double getOtherMoney()
	{
		return otherMoney;
	}

	public void setOtherMoney(double otherMoney)
	{
		this.otherMoney = otherMoney;
	}

	public double getActualMoney()
	{
		return actualMoney;
	}

	public void setActualMoney(double actualMoney)
	{
		this.actualMoney = actualMoney;
	}

	public User getOperator()
	{
		return operator;
	}

	public void setOperator(User operator)
	{
		this.operator = operator;
	}

	public Date getOperTime()
	{
		return operTime;
	}

	public void setOperTime(Date operTime)
	{
		this.operTime = operTime;
	}
}
