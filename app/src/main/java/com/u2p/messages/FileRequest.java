package com.u2p.messages;

import java.io.Serializable;

public class FileRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String group;
	
	
	public FileRequest(String name, String group){
		setName(name);
		setGroup(group);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}


}
