package com.kindergarten.data;

import java.util.ArrayList;
import java.util.List;

public class Module
{
	private String moduleId;
	private String moduleName;
	private List<ModuleFun> moduleFuns = new ArrayList<ModuleFun>();

	public List<ModuleFun> getModuleFuns()
	{
		return moduleFuns;
	}

	public void setModuleFuns(List<ModuleFun> moduleFuns)
	{
		this.moduleFuns = moduleFuns;
	}

	public String getModuleId()
	{
		return moduleId;
	}

	public void setModuleId(String moduleId)
	{
		this.moduleId = moduleId;
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
		result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
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
		Module other = (Module) obj;
		if (moduleId == null)
		{
			if (other.moduleId != null)
				return false;
		} else if (!moduleId.equals(other.moduleId))
			return false;
		if (moduleName == null)
		{
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		return true;
	}
}
