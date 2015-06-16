package com.galileo.engine;

import java.util.ArrayList;
import java.util.List;

public class Container extends PersistGlobal
{
	private String name;
	private String description;
	private List<Variable> variablesContainer; // <_id variable, _id parameter>
	
	public Container(String createName){
		name = createName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<Variable> getVariablesContainer()
	{
		if(variablesContainer == null){
			variablesContainer = new ArrayList<Variable>();
		}
		return variablesContainer;
	}
}
