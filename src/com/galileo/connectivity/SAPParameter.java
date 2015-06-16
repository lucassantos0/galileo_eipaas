package com.galileo.connectivity;

import java.util.ArrayList;
import java.util.List;

import com.galileo.engine.PersistGlobal;

public class SAPParameter extends PersistGlobal
{
	private String name;
	private SAPParameterTypes type;
	private String typeName;
	private String referenceType;
	private Integer length;
	private String documentation;
	private List<SAPParameter> tableParameters;

	public SAPParameter(SAPParameterTypes createType, String createName, String createTypeName, Integer createLength,
			String createReferenceType)
	{
		name = createName;
		typeName = createTypeName;
		type = createType;
		referenceType = createReferenceType;
		length = createLength;
		tableParameters = null;
	}

	public void setReferenceType(String referenceType)
	{
		this.referenceType = referenceType;
	}

	public SAPParameter(SAPParameterTypes createType, String createName, String createReferenceType)
	{
		name = createName;
		referenceType = createReferenceType;
		type = createType;
		tableParameters = new ArrayList<SAPParameter>();
	}

	public List<SAPParameter> getTableParameters()
	{
		return tableParameters;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public SAPParameterTypes getType()
	{
		return type;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public Integer getLength()
	{
		return length;
	}

	public void setLength(Integer length)
	{
		this.length = length;
	}

	public String getReferenceType()
	{
		return referenceType;
	}

	public String getDocumentation()
	{
		return documentation;
	}

	public void setDocumentation(String documentation)
	{
		this.documentation = documentation;
	}

}
