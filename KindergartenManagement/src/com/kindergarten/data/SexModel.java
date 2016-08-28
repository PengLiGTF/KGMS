package com.kindergarten.data;

public class SexModel
{
	private String name;
	private char code;

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SexModel other = (SexModel) obj;
		if (code != other.code)
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName()
	{
		return name;
	}

	public SexModel(String name, char code)
	{
		super();
		this.name = name;
		this.code = code;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public char getCode()
	{
		return code;
	}

	public void setCode(char code)
	{
		this.code = code;
	}
}
