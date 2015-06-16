package com.galileo.connectivity;

import com.galileo.engine.Container;
import com.galileo.engine.Mapping;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;

public class SAPFunctionModule
{

	private Container sourceContainer;
	private Mapping mapping;
	private SAPFunctionModuleMetadata functionMetadata;
	private SAPConnection sapConnection;

	public SAPFunctionModule(Container createSourceContainer, Mapping createMapping, SAPFunctionModuleMetadata createFunctionMetadata,
			SAPConnection createSapConnection)
	{
		this.sourceContainer = createSourceContainer;
		this.mapping = createMapping;
		this.functionMetadata = createFunctionMetadata;
		this.sapConnection = createSapConnection;
	}

	public void run()
	{
		JCoDestination sapDestination = SAPFunctionModuleMetadata.getSAPConnection(sapConnection);
		try
		{
			JCoFunction function = sapDestination.getRepository().getFunction(functionMetadata.getFunctionName());
			for (int i = 0; i < functionMetadata.getImportingParameters().size(); i++)
			{
				SAPParameter sapParam = functionMetadata.getImportingParameters().get(i);
				if(mapping.getIdStepContainerMapping().containsKey(sapParam.getID())){
					String containerParam = mapping.getIdStepContainerMapping().get(sapParam.getID());
					for(int j = 0; j< sourceContainer.getVariablesContainer().size(); j++){
						if(sourceContainer.getVariablesContainer().get(j).getID().equals(containerParam)){
							function.getImportParameterList().setValue(sapParam.getName(), sourceContainer.getVariablesContainer().get(j).getValue());
						}
					}
				}
			}
			function.execute(sapDestination);
			System.out.println(function.toXML());
		}
		catch (JCoException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
