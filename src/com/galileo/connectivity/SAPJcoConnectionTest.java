package com.galileo.connectivity;

import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRuntimeException;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SAPJcoConnectionTest
{
	static Properties jcoProperties = null;

	static
	{
		jcoProperties = new Properties();
		jcoProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "fusionconsultoria.ddns.net");
		jcoProperties.setProperty(DestinationDataProvider.JCO_SYSNR, "00");
		jcoProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "201");
		jcoProperties.setProperty(DestinationDataProvider.JCO_USER, "lsantos");
		jcoProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "fusion123");
		jcoProperties.setProperty(DestinationDataProvider.JCO_LANG, "EN");
	}

	public void getFunctionModuleMetadata(String functionModuleName, JCoDestination destination) throws JCoException
	{
	
		GsonBuilder gBuilder = new GsonBuilder();
		Gson gson = gBuilder.setPrettyPrinting().create();
		//String serialized = gson.toJson(destination);
		//JCoDestination destination2 = new Gson().fromJson(serialized, JCoDestination.class);
//		System.out.println(gson.toJson(jcoProperties));
		
		JCoFunction function = destination.getRepository().getFunction(functionModuleName);
		SAPFunctionModuleMetadata functionModule = new SAPFunctionModuleMetadata(functionModuleName);
		JCoParameterList functionTableParams, functionImportingParams, functionChangingParams, functionExportingParams;
		JCoFieldIterator iterator;

		// IMPORTING PARAMETERS------------------
		functionImportingParams = function.getImportParameterList();
		if (functionImportingParams != null)
		{
			iterator = functionImportingParams.getFieldIterator();
			while (iterator.hasNextField())
			{
				functionModule.getImportingParameters().add(BuildTableParameter(iterator.nextField()));
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
				functionModule.getTableParameters().add(BuildTableParameter(iterator.nextField()));
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
				functionModule.getChangingParameters().add(BuildTableParameter(iterator.nextField()));
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
				functionModule.getExportingParameters().add(BuildTableParameter(iterator.nextField()));
			}
		}
		// EXPORTING PARAMETERS

		// NAMED EXCEPTIONS
		AbapException[] namedExceptions = function.getExceptionList();
		if (namedExceptions != null)
		{
			for (int i = 0; i < namedExceptions.length; i++)
			{
				functionModule.getNamedExceptions().add(new SAPException(namedExceptions[i].getKey(), namedExceptions[i].getMessage()));
			}
		}
		String serialized = gson.toJson(functionModule);
		System.out.println(serialized);
		
//		SAPFunctionModuleMetadata destination2 = new Gson().fromJson(serialized, SAPFunctionModuleMetadata.class);
//		System.out.println(gson.toJson(jcoProperties));
//		System.out.println(destination2.getFunctionName());
	}

	public static void main(String args[])
	{
		JCoDestination destination;
		SAPJcoConnectionTest abapHandlerClass = new SAPJcoConnectionTest();
		try
		{
			SAPJcoDestinationProvider provider = new SAPJcoDestinationProvider();
			provider.addDestination("FUSION_ERP_CONNECTION", jcoProperties);
			com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(provider);
			destination = JCoDestinationManager.getDestination("FUSION_ERP_CONNECTION");
			abapHandlerClass.getFunctionModuleMetadata("BAPI_PO_CREATE", destination);

		}
		catch (JCoException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SAPParameter BuildTableParameter(JCoField field)
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
			SAPParameter newVarParam = new SAPParameter(SAPParameterTypes.VARIABLE, field.getName(), field.getTypeAsString(), field.getLength(), null); 
			newVarParam.setDocumentation(field.getDescription());
			return newVarParam;
		}
	}

}
