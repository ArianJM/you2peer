package com.u2p.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DbGroups {
	private HashMap<String,List<DbFile>> groups;
	
	public DbGroups(){
		groups=new HashMap<String,List<DbFile>>();
	}
	
	public boolean addGroup(String group){
		if(!groups.containsKey(group)){
			groups.put(group,new ArrayList<DbFile>());
			return true;
		}
		return false;
	}
	
	public boolean addFileToGroup(String group,DbFile file){
		if(groups.containsKey(group)){
			List<DbFile> files=groups.get(group);
			files.add(file);
			groups.put(group, files);
			return true;
		}
		return false;
	}
	
	public List<DbFile> getFilesGroup(String group){
		if(groups.containsKey(group)){
			return groups.get(group);
		}
		return null;
	}
	
	public boolean deleteFile(String group,DbFile file){
		if(groups.containsKey(group)){
			List<DbFile> files=groups.get(group);
			files.remove(file);
			return true;
		}
		return false;
	}
}
