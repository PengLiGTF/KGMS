package com.kindergarten.data;

import java.util.Date;

public class KinderLeaveInfo
{
	private String kinderId;
	private String kinderName;
	private int leaveDays;
	private String leaveDesc;
	private String operId;
	private Date operTime;
	private Date leaveStartTime;
	private Date leaveEndTime;

	public Date getLeaveStartTime()
	{
		return leaveStartTime;
	}

	public void setLeaveStartTime(Date leaveStartTime)
	{
		this.leaveStartTime = leaveStartTime;
	}

	public Date getLeaveEndTime()
	{
		return leaveEndTime;
	}

	public void setLeaveEndTime(Date leaveEndTime)
	{
		this.leaveEndTime = leaveEndTime;
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

	public int getLeaveDays()
	{
		return leaveDays;
	}

	public void setLeaveDays(int leaveDays)
	{
		this.leaveDays = leaveDays;
	}

	public String getLeaveDesc()
	{
		return leaveDesc;
	}

	public void setLeaveDesc(String leaveDesc)
	{
		this.leaveDesc = leaveDesc;
	}

	public String getOperId()
	{
		return operId;
	}

	public void setOperId(String operId)
	{
		this.operId = operId;
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
