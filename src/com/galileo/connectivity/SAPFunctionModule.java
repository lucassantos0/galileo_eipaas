package com.galileo.connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SAPFunctionModule
{
	Properties connectionProperties;
	public void setConnectionProperties(Properties connectionProperties)
	{
		this.connectionProperties = connectionProperties;
	}

	private String functionName;
	private List<SAPParameter> tableParameters;
	private List<SAPParameter> importingParameters;
	private List<SAPParameter> exportingParameters;
	private List<SAPParameter> changingParameters;
	private List<SAPException> namedExceptions;
	
	public SAPFunctionModule(String createFunctionName){
		functionName = createFunctionName;
	}

	public List<SAPParameter> getTableParameters()
	{
		if(tableParameters == null) tableParameters = new ArrayList<SAPParameter>();
		return tableParameters;
	}

	public List<SAPParameter> getImportingParameters()
	{
		if(importingParameters == null) importingParameters = new ArrayList<SAPParameter>();
		return importingParameters;
	}

	public List<SAPParameter> getExportingParameters()
	{
		if(exportingParameters == null) exportingParameters = new ArrayList<SAPParameter>();
		return exportingParameters;
	}

	public List<SAPParameter> getChangingParameters()
	{
		if(changingParameters == null) changingParameters = new ArrayList<SAPParameter>();
		return changingParameters;
	}
	
	public List<SAPException> getNamedExceptions()
	{
		if(namedExceptions == null) namedExceptions = new ArrayList<SAPException>();
		return namedExceptions;
	}

	public String getFunctionName()
	{
		return functionName;
	}

	public void setTableParameters(List<SAPParameter> tableParameters)
	{
		this.tableParameters = tableParameters;
	}
	
}
