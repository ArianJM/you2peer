package com.u2p.messages;

import java.io.Serializable;
import java.util.HashMap;

public class Authentication implements Serializable {
	private HashMap<String,String> groups;
	private String user;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Authentication(String user){
		this.user=user;
		groups=new HashMap<String,String>();
	}
		
	public void setGroups(HashMap<String,String> groups){
		this.groups=groups;
	}

	
	public void addGroup(String group,String hash){
		if(!groups.containsKey(group)){
			groups.put(group, hash);
		}
	}
	
	public HashMap<String,String> getGroups(){
		return this.groups;
	}
	
	public String getUser(){
		return this.user;
	}
	
	

}
