package com.galileo.engine;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class GlobalProcedures
{
	public static SecureRandom random;
	private static GsonBuilder gsonBuilder = new GsonBuilder();
	private static Gson gson = gsonBuilder.setPrettyPrinting().create();

	private static MongoClient mongoClient = new MongoClient();;
	public static MongoDatabase databaseInstance = mongoClient.getDatabase("Galileo");;

	public static Object loadSingleDocument(String jsonQuery, Class<?> objectClass)
	{
		FindIterable<Document> iterable = databaseInstance.getCollection(objectClass.getName())
				.find(new Document(Document.parse(jsonQuery)));
		String jsonReturn = iterable.first().toJson();
		if (jsonReturn != null) { return gson.fromJson(jsonReturn, objectClass); }
		return null;
	}

	public static String objectToJson(Object fromObject)
	{
		return gson.toJson(fromObject);
	}

	public static void createOrFailIfExists(IPersist jsonDocumentBinary)
	{
		try
		{
			databaseInstance.createCollection(jsonDocumentBinary.getClass().getSimpleName());
			System.out.println(jsonDocumentBinary.getClass().getSimpleName());
		}
		catch (Exception ex)
		{
			//
		}
		String jsonDocument = gson.toJson(jsonDocumentBinary);
		Document saveDocument = new Document(Document.parse(jsonDocument));

		try
		{
			databaseInstance.getCollection(jsonDocumentBinary.getClass().getSimpleName()).insertOne(saveDocument);
		}
		catch (Exception ex)
		{
			System.out.println("Começou... " + ex.getMessage());
		}
	}

	public static void update(IPersist jsonDocumentBinary)
	{
		try
		{
			databaseInstance.createCollection(jsonDocumentBinary.getClass().getSimpleName());
		}
		catch (Exception ex)
		{
			// TODO collection já existe, mas melhora essa porra
		}
		String jsonDocument = gson.toJson(jsonDocumentBinary);
		Document saveDocument = new Document(Document.parse(jsonDocument));
		databaseInstance.getCollection(jsonDocumentBinary.getClass().getSimpleName())
				.updateOne(Document.parse(jsonDocumentBinary.getDelta()), saveDocument);
	}

	public static IPersist loadSingle(IPersist jsonDocumentFilter)
	{
		String jsonDocument = gson.toJson(jsonDocumentFilter);
		Document filterDocument = new Document(Document.parse(jsonDocument));
		Document searchResult = databaseInstance.getCollection(jsonDocumentFilter.getClass().getSimpleName()).find(filterDocument).first();
		if (searchResult != null)
		{
			return gson.fromJson(searchResult.toJson(), jsonDocumentFilter.getClass());
		}
		else
		{
			return null;
		}
	}
}