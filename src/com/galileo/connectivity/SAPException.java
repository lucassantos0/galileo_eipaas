package com.galileo.connectivity;

public class SAPException
{
	private String name;
	public String getName()
	{
		return name;
	}

	public String getDetailedMessage()
	{
		return detailedMessage;
	}

	private String detailedMessage;
	
	public SAPException(String createName, String createDetailedMessage){
		name = createName;
		detailedMessage = createDetailedMessage;
	}
}
