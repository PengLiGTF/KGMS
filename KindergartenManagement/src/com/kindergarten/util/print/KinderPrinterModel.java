package com.kindergarten.util.print;

import java.util.Date;

import com.ibm.icu.util.Calendar;

public class KinderPrinterModel
{

	private String checkId = "";
	private String kinderName = "";
	private String kinderId = "";
	private String gradeName = "";
	private String className = "";
	private String feeTemplate = "";
	private String feeDays = "";
	private String privelegeMoney = "0.00";
	private String otherMoney = "0.00";
	private String preFeeMoney = "0.00";
	private String deductionPreFeeMoney = "0.00";
	private String amountMoney = "0.00";
	private String operatorName = "";
	private Date operDate = Calendar.getInstance().getTime();

	public String getCheckId()
	{
		return checkId;
	}

	public void setCheckId(String checkId)
	{
		this.checkId = checkId;
	}

	public String getKinderName()
	{
		return kinderName;
	}

	public void setKinderName(String kinderName)
	{
		this.kinderName = kinderName;
	}

	public String getKinderId()
	{
		return kinderId;
	}

	public void setKinderId(String kinderId)
	{
		this.kinderId = kinderId;
	}

	public String getGradeName()
	{
		return gradeName;
	}

	public void setGradeName(String gradeName)
	{
		this.gradeName = gradeName;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getFeeTemplate()
	{
		return feeTemplate;
	}

	public void setFeeTemplate(String feeTemplate)
	{
		this.feeTemplate = feeTemplate;
	}

	public String getFeeDays()
	{
		return feeDays;
	}

	public void setFeeDays(String feeDays)
	{
		this.feeDays = feeDays;
	}

	public String getPrivelegeMoney()
	{
		return privelegeMoney;
	}

	public void setPrivelegeMoney(String privelegeMoney)
	{
		this.privelegeMoney = privelegeMoney;
	}

	public String getOtherMoney()
	{
		return otherMoney;
	}

	public void setOtherMoney(String otherMoney)
	{
		this.otherMoney = otherMoney;
	}

	public String getPreFeeMoney()
	{
		return preFeeMoney;
	}

	public void setPreFeeMoney(String preFeeMoney)
	{
		this.preFeeMoney = preFeeMoney;
	}

	public String getDeductionPreFeeMoney()
	{
		return deductionPreFeeMoney;
	}

	public void setDeductionPreFeeMoney(String deductionPreFeeMoney)
	{
		this.deductionPreFeeMoney = deductionPreFeeMoney;
	}

	public String getAmountMoney()
	{
		return amountMoney;
	}

	public void setAmountMoney(String amountMoney)
	{
		this.amountMoney = amountMoney;
	}

	public String getOperatorName()
	{
		return operatorName;
	}

	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}

	public Date getOperDate()
	{
		return operDate;
	}

	public void setOperDate(Date operDate)
	{
		this.operDate = operDate;
	}
}
