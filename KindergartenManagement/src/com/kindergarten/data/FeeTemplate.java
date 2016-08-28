package com.kindergarten.data;

public class FeeTemplate
{
	private int id;
	private String feeTemplateName;
	private double feeAmount;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getFeeTemplateName()
	{
		return feeTemplateName;
	}

	public void setFeeTemplateName(String feeTemplateName)
	{
		this.feeTemplateName = feeTemplateName;
	}

	public double getFeeAmount()
	{
		return feeAmount;
	}

	public void setFeeAmount(double feeAmount)
	{
		this.feeAmount = feeAmount;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(feeAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((feeTemplateName == null) ? 0 : feeTemplateName.hashCode());
		result = prime * result + id;
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
		FeeTemplate other = (FeeTemplate) obj;
		if (Double.doubleToLongBits(feeAmount) != Double.doubleToLongBits(other.feeAmount))
			return false;
		if (feeTemplateName == null)
		{
			if (other.feeTemplateName != null)
				return false;
		} else if (!feeTemplateName.equals(other.feeTemplateName))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

}
