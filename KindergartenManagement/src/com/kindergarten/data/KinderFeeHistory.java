package com.kindergarten.data;

import java.util.Date;

public class KinderFeeHistory
{
	private String kinderId;
	private String kinderName;
	private String feeChangeReason;
	private int feeDays;
	private Date feeStartTime;
	private Date feeEndTime;

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

	public String getFeeChangeReason()
	{
		return feeChangeReason;
	}

	public void setFeeChangeReason(String feeChangeReason)
	{
		this.feeChangeReason = feeChangeReason;
	}

	public int getFeeDays()
	{
		return feeDays;
	}

	public void setFeeDays(int feeDays)
	{
		this.feeDays = feeDays;
	}

	public Date getFeeStartTime()
	{
		return feeStartTime;
	}

	public void setFeeStartTime(Date feeStartTime)
	{
		this.feeStartTime = feeStartTime;
	}

	public Date getFeeEndTime()
	{
		return feeEndTime;
	}

	public void setFeeEndTime(Date feeEndTime)
	{
		this.feeEndTime = feeEndTime;
	}

}
