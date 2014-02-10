package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;

public class FileEvent extends EventObject implements ActivityEvents{
	private InetAddress address;
	private String group,file;
	
	public FileEvent(Object source,InetAddress address) {
		super(source);
		this.address=address;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	
	public void setGroupAndFile(String group,String file){
		this.group=group;
		this.file=file;
	}
	
	public String getGroup(){
		return this.group;
	}
	
	public String getFile(){
		return this.file;
	}
	
	private static final long serialVersionUID = 1L;

	public InetAddress getAddress() {
		// TODO Auto-generated method stub
		return address;
	}

}
