package com.kindergarten.data;

public class KinderStatus
{
	private String statusCode;
	private String statusName;

	public KinderStatus(String statusCode, String statusName)
	{
		super();
		this.statusCode = statusCode;
		this.statusName = statusName;
	}

	public String getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}

	public String getStatusName()
	{
		return statusName;
	}

	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((statusCode == null) ? 0 : statusCode.hashCode());
		result = prime * result + ((statusName == null) ? 0 : statusName.hashCode());
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
		KinderStatus other = (KinderStatus) obj;
		if (statusCode == null)
		{
			if (other.statusCode != null)
				return false;
		} else if (!statusCode.equals(other.statusCode))
			return false;
		if (statusName == null)
		{
			if (other.statusName != null)
				return false;
		} else if (!statusName.equals(other.statusName))
			return false;
		return true;
	}
}
