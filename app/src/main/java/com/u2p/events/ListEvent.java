package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;

public class ListEvent extends EventObject implements ActivityEvents{
	private static final long serialVersionUID = 1L;

	private InetAddress address;
	private String group;
	
	public ListEvent(Object source,InetAddress address) {
		super(source);
		this.address=address;
	}

	public void setGroup(String group){
		this.group=group;
	}
	
	public String getGroup(){
		return this.group;
	}
	public InetAddress getAddress() {
		return this.address;
	}
}
