package com.galileo.connectivity;

import java.util.Properties;

import org.bson.types.ObjectId;

import com.galileo.engine.*;

public class SAPConnection extends PersistGlobal
{
	String name;
	Properties connectionProperties;

	public SAPConnection(String createName)
	{
		name = createName;
	}

	public String getName()
	{
		return name;
	}

	public Properties getConnectionProperties()
	{
		if (connectionProperties == null)
		{
			connectionProperties = new Properties();
		}
		return connectionProperties;
	}

}
