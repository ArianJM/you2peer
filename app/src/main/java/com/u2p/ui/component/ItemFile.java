package com.u2p.ui.component;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;

import com.u2p.core.db.DbFile;

public class ItemFile implements Serializable{

	private static final long serialVersionUID = 8858451646619645528L;
	private long id;
	private int pos,neg;
	private String rutaImagen;
	private String name;
	private String user;
	private String size;
	private String rating;
	private String group;
	private InetAddress address;
	

	public ItemFile(DbFile file, String user, String rutaImagen){
        setId(file.getId());
        setRutaImagen(rutaImagen);
        setName(file.getName());
        setUser(user);
        File f = new File(file.getUri());
        setSize(Integer.toString((int)f.length()));
		this.pos=file.getPositive();
		this.neg=file.getNegative();
		int total = pos+neg;
        setRating(Integer.toString(pos) + "/" + Integer.toString(total));
        setGroup(file.getGroup());
    }
	

	public int getPositives() {
		return pos;
	}

	public void setPositives() {
		this.pos = this.pos+1;
	}

	public int getNegatives() {
		return neg;
	}

	public void setNegatives() {
		this.neg = this.neg+1;
	}

	public void setAddress(InetAddress address){
		this.address=address;
	}
	
	public InetAddress getAddress(){
		return this.address;
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getRating() {
		int total = pos+neg;
		this.rating = Integer.toString(pos)+"/"+Integer.toString(total);
		return this.rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
