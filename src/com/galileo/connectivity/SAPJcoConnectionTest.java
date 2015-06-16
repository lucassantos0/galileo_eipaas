package com.galileo.connectivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.bson.Document;

import com.galileo.engine.Container;
import com.galileo.engine.Mapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SAPJcoConnectionTest
{

	public static void main(String args[])
	{
		try
		{

			SAPConnection newConn = new SAPConnection("FUSION_CONNECTION");
			newConn.getConnectionProperties().setProperty("jco.client.lang", "EN");
			newConn.getConnectionProperties().setProperty("jco.client.client", "201");
			newConn.getConnectionProperties().setProperty("jco.client.passwd", "fusion123");
			newConn.getConnectionProperties().setProperty("jco.client.user", "lsantos");
			newConn.getConnectionProperties().setProperty("jco.client.sysnr", "00");
			newConn.getConnectionProperties().setProperty("jco.client.ashost", "fusionconsultoria.ddns.net");

			newConn.save();

			//SAPFunctionModuleMetadata metadata = SAPFunctionModuleMetadata.create("BAPI_CONTRACT_CREATE", newConn);
			//System.out.println(metadata.getImportingParameters().get(0).getName());
			//metadata.save();
//			System.out.println(metadata.getID());
			
			
			SAPFunctionModuleMetadata metadata = (SAPFunctionModuleMetadata) SAPFunctionModuleMetadata
					.loadSingle(new SAPFunctionModuleMetadata("BAPI_CONTRACT_CREATE"));
//			System.out.println(metadata.getID());
			
			// metadata.save();
			// System.out.println(metadata.getID().toString());

			/*
			 * "connectionProperties": { "jco.client.lang": "EN",
			 * "jco.client.client": "201", "jco.client.passwd": "fusion123",
			 * "jco.client.user": "lsantos", "jco.client.sysnr": "00",
			 * "jco.client.ashost": "fusionconsultoria.ddns.net"
			 */

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveTemplate(String filename, String content)
	{
		try
		{
			File jsonFile = new File(filename);
			FileWriter fWriter = new FileWriter(jsonFile);
			fWriter.write(content);
			fWriter.close();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String loadTemplate(String filename)
	{
		File jsonFile = new File(filename);
		byte[] encoded;
		try
		{
			encoded = Files.readAllBytes(Paths.get(jsonFile.getAbsolutePath()));
			return new String(encoded, Charset.defaultCharset());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
