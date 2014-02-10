package com.u2p.ui.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

public class GroupListFile {
	private HashMap<String,ArrayList<ItemFile>> listGroup;
	
	public GroupListFile(){
		listGroup=new HashMap<String,ArrayList<ItemFile>>();
	}
	
	public void addListFileToGroup(String group,ArrayList<ItemFile> files){
		if(!listGroup.containsKey(group)){
			listGroup.put(group, files);
		}else{
			List<ItemFile> aux=listGroup.get(group);
			List<String> auxNames = new ArrayList<String>();
			for(ItemFile i: aux){
				Log.d("GL", "AUX"+i.getName()+ " is in list PREVIOUS");
				auxNames.add(i.getName());
			}
			for(ItemFile i: files)
				Log.d("GL", "FILES: "+i.getName()+ " is in lis PREVIOUS");
			if(aux.size()!=0){
				for(ItemFile file:files){
					if(!auxNames.contains(file.getName())){
						aux.add(file);
						Log.d("GL", file.getName()+" ADDED to list");
					}
				}
			}else{
				aux.addAll(files);
			}
			for(ItemFile i:aux)
				Log.d("GL", i.getName() + " is in FINAL list");
			/*List<ItemFile> toAdd = new ArrayList<ItemFile>();
			if(aux.size()!=0){
				for(ItemFile i:aux){
					for(ItemFile f:files){
						if(!aux.contains(f) && !toAdd.contains(f))
							toAdd.add(f);
					}
				}
				aux.addAll(toAdd);
			}else{
				aux.addAll(files);
			}*/
		}
	}
	
	public ArrayList<ItemFile> getListFile(String group){
		return this.listGroup.get(group);
	}
	
	public ItemFile getFile(String group,String name){
		if(listGroup.containsKey(group)){
			List<ItemFile> aux=listGroup.get(group);
			for(ItemFile file:aux){
				if(file.getName().equals(name))
					return file;
			}
		}
		return null;
	}
	
	public void addFileToGroup(ItemFile file){
		if(listGroup.containsKey(file.getGroup())){
			List<ItemFile> aux=listGroup.get(file.getGroup());
			ItemFile auxFile = null;
			for(ItemFile f:aux){
				Log.d("DB","f "+f.getName()+" "+file.getName());
				if(f.getName().equals(file.getName())){
					Log.d("DB","equals");
					auxFile = f;
					break;
				}
				
			}
			if(auxFile != null){
				aux.remove(auxFile);
				Log.d("DB","remove");
				aux.add(file);
				Log.d("DB","add");
			}
		}
	}
}
