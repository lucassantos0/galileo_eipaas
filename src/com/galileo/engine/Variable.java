package com.galileo.engine;

public class Variable extends PersistGlobal
{
	String _id;
	String name;
	String value;
	
	public Variable(String createName){
		this.name = createName;
	}
	public Variable(String createName, String createValue){
		this.name = createName;
		this.value = createValue;
		
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getName()
	{
		return name;
	}
	
}
