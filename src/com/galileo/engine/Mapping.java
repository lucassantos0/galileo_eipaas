package com.galileo.engine;

import java.util.HashMap;
import java.util.Map;

public class Mapping
{
	String _id;
	String name;
	String idContainerSource;
	String idContainerTarget;
	private Map<String, String> idStepContainerMapping; //Store <parameter _id SAIDA, parameter _id ENTRADA>
	public Mapping(String createName, String createIdContainerSource, String createIdContainerTarget)
	{
		this.name = createName;
		this.idContainerSource = createIdContainerSource;
		this.idContainerTarget = createIdContainerTarget;
	}
		
	public String getName()
	{
		return name;
	}

	public String getID()
	{
		return _id;
	}

	public Map<String, String> getIdStepContainerMapping()
	{
		if(idStepContainerMapping == null)
		{
			idStepContainerMapping = new HashMap<String,String>();
		}
		return idStepContainerMapping;
	}	
}

