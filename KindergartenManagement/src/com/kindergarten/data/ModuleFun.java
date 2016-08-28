package com.kindergarten.data;

import java.util.ArrayList;
import java.util.List;

public class ModuleFun
{
	private String funId;
	private String moduleId;
	private String funName;
	private List<FunOperation> funOperationList = new ArrayList<FunOperation>();

	public List<FunOperation> getFunOperationList()
	{
		return funOperationList;
	}

	public void setFunOperationList(List<FunOperation> funOperationList)
	{
		this.funOperationList = funOperationList;
	}

	public String getFunId()
	{
		return funId;
	}

	public void setFunId(String funId)
	{
		this.funId = funId;
	}

	public String getModuleId()
	{
		return moduleId;
	}

	public void setModuleId(String moduleId)
	{
		this.moduleId = moduleId;
	}

	public String getFunName()
	{
		return funName;
	}

	public void setFunName(String funName)
	{
		this.funName = funName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funId == null) ? 0 : funId.hashCode());
		result = prime * result + ((funName == null) ? 0 : funName.hashCode());
		result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
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
		ModuleFun other = (ModuleFun) obj;
		if (funId == null)
		{
			if (other.funId != null)
				return false;
		} else if (!funId.equals(other.funId))
			return false;
		if (funName == null)
		{
			if (other.funName != null)
				return false;
		} else if (!funName.equals(other.funName))
			return false;
		if (moduleId == null)
		{
			if (other.moduleId != null)
				return false;
		} else if (!moduleId.equals(other.moduleId))
			return false;
		return true;
	}

}
