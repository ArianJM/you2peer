package com.u2p.events;

import java.net.InetAddress;
import java.util.EventObject;
import java.util.List;

import com.u2p.ui.component.ItemFile;

public class NewGroupList extends EventObject{
	private InetAddress address;
	private String group;
	private List<ItemFile> files;
	
	public NewGroupList(Object source,InetAddress address,String group,List<ItemFile> files) {
		super(source);
        setAddress(address);
        setGroup(group);
        setFiles(files);
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<ItemFile> getFiles() {
		return files;
	}

	public void setFiles(List<ItemFile> files) {
		this.files = files;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
