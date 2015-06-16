package com.galileo.engine;

public class Step
{
	private String _id;
	private String name;
	private String description;
	private StepType type;
	
	private IMetadata metadata;
	private IConnection connection;
	
	private String idContainerEntrada;
	private String idContainerSaida;
	private String idMappingEntrada;
	private String idMappingSaida;
	
	
	public void run(){
		//execução
	}
	
	public void build(){
		//construção baseado no container de entrada + metadata + mapping
	}
}
