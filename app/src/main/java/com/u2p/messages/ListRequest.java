package com.u2p.messages;

import java.io.Serializable;

public class ListRequest implements Serializable{

	private static final long serialVersionUID = 1L;

	private String group;
	
	public ListRequest(String group){
		setGroup(group);
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
