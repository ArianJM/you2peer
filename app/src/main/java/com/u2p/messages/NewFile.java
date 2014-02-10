package com.u2p.messages;

import java.io.Serializable;

import com.u2p.ui.component.ItemFile;

public class NewFile implements Serializable{

	private static final long serialVersionUID = 1L;

	private ItemFile newFile;
	private String owner;
	private String group;
	
	public NewFile(ItemFile newFile, String owner, String group){
		setNewFile(newFile);
		setOwner(owner);
		setGroup(group);
	}

	public ItemFile getNewFile() {
		return newFile;
	}

	public void setNewFile(ItemFile newFile) {
		this.newFile = newFile;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
