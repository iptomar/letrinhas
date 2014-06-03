package com.example.demobdf;

public class Obj {
	private long id;
	private String type;
	private Object obj;
	private byte[] data;
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public Object getObj(){
		return obj;
	}
	
	public void setObj(Object obj){
		this.obj = obj;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public byte[] getData(){
		return data;
	}
	
	public void setData(byte[] data){
		this.data = data;
	}
}
