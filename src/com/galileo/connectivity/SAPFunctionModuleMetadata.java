package com.galileo.connectivity;

import java.util.ArrayList;
import java.util.List;

import com.galileo.engine.IMetadata;
import com.galileo.engine.IPersist;
import com.galileo.engine.PersistGlobal;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRuntimeException;

public class SAPFunctionModuleMetadata extends PersistGlobal implements IMetadata
{
	private String functionName;
	private List<SAPParameter> tableParameters;
	private List<SAPParameter> importingParameters;
	private List<SAPParameter> exportingParameters;
	private List<SAPParameter> changingParameters;
	private List<SAPException> namedExceptions;

	public SAPFunctionModuleMetadata(String createFunctionName)
	{
		functionName = createFunctionName;
	}

	public static SAPFunctionModuleMetadata create(String createFunctionName, SAPConnection sapConnection)
	{
		
		JCoDestination sapDestination = SAPFunctionModuleMetadata.getSAPConnection(sapConnection);
		if (sapConnection != null)
		{
			try
			{
				SAPFunctionModuleMetadata functionModule = new SAPFunctionModuleMetadata(createFunctionName);
				functionModule.getFunctionModuleMetadata(sapDestination);
				return functionModule;
			}
			catch (JCoException ex)
			{
				return null;
			}
		}
		return null;
	}

	public static JCoDestination getSAPConnection(SAPConnection sapConnection)
	{
		JCoDestination destination;
		try
		{
			SAPJcoDestinationProvider provider = new SAPJcoDestinationProvider();
			provider.addDestination(sapConnection.getName(), sapConnection.getConnectionProperties());
			com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(provider);
			destination = JCoDestinationManager.getDestination(sapConnection.getName());
		}
		catch (JCoException e)
		{
			return null;
		}
		return destination;
	}

	private void getFunctionModuleMetadata(JCoDestination destination) throws JCoException
	{
		JCoFunction function = destination.getRepository().getFunction(functionName);
		if(function == null){
			throw new JCoException(0, "Function not found on repository.");
		}
		JCoParameterList functionTableParams, functionImportingParams, functionChangingParams, functionExportingParams;
		JCoFieldIterator iterator;
		// IMPORTING PARAMETERS------------------
		functionImportingParams = function.getImportParameterList();
		if (functionImportingParams != null)
		{
			iterator = functionImportingParams.getFieldIterator();
			while (iterator.hasNextField())
			{
				getImportingParameters().add(BuildTableParameter(iterator.nextField()));
			}
		}
		// IMPORTING PARAMETERS------------------

		// TABLE PARAMETERS------------------
		functionTableParams = function.getTableParameterList();
		if (functionTableParams != null)
		{
			iterator = functionTableParams.getFieldIterator();
			while (iterator.hasNextField())
			{
				getTableParameters().add(BuildTableParameter(iterator.nextField()));
			}
		}
		// TABLE PARAMETERS------------------

		// CHANGING PARAMETERS
		functionChangingParams = function.getChangingParameterList();
		if (functionChangingParams != null)
		{
			iterator = functionChangingParams.getFieldIterator();
			while (iterator.hasNextField())
			{
				getChangingParameters().add(BuildTableParameter(iterator.nextField()));
			}
		}
		// CHANGING PARAMETERS

		// EXPORTING PARAMETERS
		functionExportingParams = function.getExportParameterList();
		if (functionExportingParams != null)
		{
			iterator = functionExportingParams.getFieldIterator();
			while (iterator.hasNextField())
			{
				getExportingParameters().add(BuildTableParameter(iterator.nextField()));
			}
		}
		// EXPORTING PARAMETERS

		// NAMED EXCEPTIONS
		AbapException[] namedExceptions = function.getExceptionList();
		if (namedExceptions != null)
		{
			for (int i = 0; i < namedExceptions.length; i++)
			{
				getNamedExceptions().add(new SAPException(namedExceptions[i].getKey(), namedExceptions[i].getMessage()));
			}
		}
	}

	private SAPParameter BuildTableParameter(JCoField field)
	{
		if (field.isTable())
		{
			SAPParameter newTableParam = new SAPParameter(SAPParameterTypes.TABLE, field.getName(), null);
			SAPParameter newVarParam;
			JCoFieldIterator tableIterator = field.getTable().getFieldIterator();
			JCoField tableField;
			while (tableIterator.hasNextField())
			{
				tableField = tableIterator.nextField();
				newVarParam = BuildTableParameter(tableField);
				try
				{
					newVarParam.setReferenceType(field.getRecordMetaData().getRecordTypeName(newVarParam.getName()));
				}
				catch (JCoRuntimeException jcoException)
				{
					// Registro não encontrado nas referências.
					// Irrelevante tratar a exceção
				}
				newTableParam.getTableParameters().add(newVarParam);
			}
			return newTableParam;
		}
		else if (field.isStructure())
		{
			SAPParameter newStructParam = new SAPParameter(SAPParameterTypes.STRUCTURE, field.getName(), null);
			SAPParameter newVarParam;
			JCoFieldIterator structIterator = field.getStructure().getFieldIterator();
			JCoField structField;

			while (structIterator.hasNextField())
			{
				structField = structIterator.nextField();
				newVarParam = BuildTableParameter(structField);
				try
				{
					newVarParam.setReferenceType(field.getRecordMetaData().getRecordTypeName(newVarParam.getName()));
				}
				catch (JCoRuntimeException jcoException)
				{
					// Registro não encontrado nas referências.
					// Irrelevante tratar a exceção
				}
				newStructParam.getTableParameters().add(newVarParam);
			}
			return newStructParam;
		}
		else
		{
			SAPParameter newVarParam = new SAPParameter(SAPParameterTypes.VARIABLE, field.getName(), field.getTypeAsString(),
					field.getLength(), null);
			newVarParam.setDocumentation(field.getDescription());
			return newVarParam;
		}
	}

	public List<SAPParameter> getTableParameters()
	{
		if (tableParameters == null) tableParameters = new ArrayList<SAPParameter>();
		return tableParameters;
	}

	public List<SAPParameter> getImportingParameters()
	{
		if (importingParameters == null) importingParameters = new ArrayList<SAPParameter>();
		return importingParameters;
	}

	public List<SAPParameter> getExportingParameters()
	{
		if (exportingParameters == null) exportingParameters = new ArrayList<SAPParameter>();
		return exportingParameters;
	}

	public List<SAPParameter> getChangingParameters()
	{
		if (changingParameters == null) changingParameters = new ArrayList<SAPParameter>();
		return changingParameters;
	}

	public List<SAPException> getNamedExceptions()
	{
		if (namedExceptions == null) namedExceptions = new ArrayList<SAPException>();
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
