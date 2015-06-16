package com.galileo.engine;

import org.bson.types.ObjectId;

public abstract class PersistGlobal implements IPersist
{
	ObjectId _id;
	String delta;
	
	@Override
	public void save()
	{
		if (_id == null)
		{
			_id = ObjectId.get();
			com.galileo.engine.GlobalProcedures.createOrFailIfExists(this);
			this.delta = com.galileo.engine.GlobalProcedures.objectToJson(this);
		}
		else
		{
			com.galileo.engine.GlobalProcedures.update(this);
			this.delta = com.galileo.engine.GlobalProcedures.objectToJson(this);
		}
	}

	public static IPersist loadSingle(IPersist queryFilter)
	{
		return com.galileo.engine.GlobalProcedures.loadSingle(queryFilter);
	}

	@Override
	public ObjectId getID()
	{
		return _id;
	}

	@Override
	public String getDelta()
	{
		return delta;
	}
	

}
