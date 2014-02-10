package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;
import java.util.List;

public class NewClientEvent extends EventObject{
	private InetAddress address;
	private List<String> commons;
	
	public NewClientEvent(Object source,InetAddress address,List<String> commons) {
		super(source);
		this.address=address;
		this.commons=commons;
	}

	private static final long serialVersionUID = 1L;

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public List<String> getCommons() {
		return commons;
	}

	public void setCommons(List<String> commons) {
		this.commons = commons;
	}
}
