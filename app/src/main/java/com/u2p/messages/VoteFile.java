package com.u2p.messages;

import java.io.Serializable;

public class VoteFile implements Serializable{
	private int vote;
	private String group,file;
	
	public VoteFile(String group,String file,int vote){
		this.group=group;
		this.file=file;
		this.vote=vote;
	}
	
	public int getVote() {
		return vote;
	}

	public String getGroup() {
		return group;
	}

	public String getFile() {
		return file;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
