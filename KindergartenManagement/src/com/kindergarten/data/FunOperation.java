package com.kindergarten.data;

import java.io.Serializable;

public class FunOperation implements Serializable
{
	/**
	 */
	private static final long serialVersionUID = 1L;
	private String operationId;
	private String operationName;

	private String funId;// for the tree display

	public String getFunId()
	{
		return funId;
	}

	public void setFunId(String funId)
	{
		this.funId = funId;
	}

	public String getOperationId()
	{
		return operationId;
	}

	public void setOperationId(String operationId)
	{
		this.operationId = operationId;
	}

	public String getOperationName()
	{
		return operationName;
	}

	public void setOperationName(String operationName)
	{
		this.operationName = operationName;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funId == null) ? 0 : funId.hashCode());
		result = prime * result + ((operationId == null) ? 0 : operationId.hashCode());
		result = prime * result + ((operationName == null) ? 0 : operationName.hashCode());
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
		FunOperation other = (FunOperation) obj;
		if (funId == null)
		{
			if (other.funId != null)
				return false;
		} else if (!funId.equals(other.funId))
			return false;
		if (operationId == null)
		{
			if (other.operationId != null)
				return false;
		} else if (!operationId.equals(other.operationId))
			return false;
		if (operationName == null)
		{
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		return true;
	}
}
