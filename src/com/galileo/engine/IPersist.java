package com.galileo.engine;

import org.bson.types.ObjectId;

import com.galileo.connectivity.SAPConnection;

public interface IPersist
{
	public void save();
	public ObjectId getID();
	public String getDelta();
}
