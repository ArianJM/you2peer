package com.u2p.core.db;

public class DbUser {
	private long id;
	private String user,group,hash;
	
	public DbUser(String user, String group, String hash) {
		super();
        setUser(user);
		setGroup(group);
		setHash(hash);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public String toString() {
		return "BdUser [id=" + id + ", user=" + user + ", group=" + group
				+ ", hash=" + hash + "]";
	}
}
