package com.u2p.messages;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import android.util.Log;

public class FileAnswer implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String TAG="ListAnswer";
	private String filename;
	private String group;
	private byte[] bytes;
	
	public FileAnswer(String filename,String group){
		this.filename=filename;
		this.group=group;
	}
	
	public String getGroup(){
		return this.group;
	}
	
	public String getFilename(){
		return filename;
	}
	
	public void read(String uri){
		File file = new File(uri);
		bytes = new byte[(int)file.length()];
		try {
			InputStream input = null;
			try {
				int totalBytesRead = 0;
				input = new BufferedInputStream(new FileInputStream(file));
				while(totalBytesRead < bytes.length){
					int bytesRemaining = bytes.length - totalBytesRead;
					//input.read() returns -1, 0, or more :
					int bytesRead = input.read(bytes, totalBytesRead, bytesRemaining); 
					if (bytesRead > 0){
						totalBytesRead = totalBytesRead + bytesRead;
					}
				}
				/*
		         the above style is a bit tricky: it places bytes into the 'result' array; 
		         'result' is an output parameter;
		         the while loop usually has a single iteration only.
				 */
			}
			finally {
				input.close();
			}
		}
		catch (FileNotFoundException ex) {
			Log.e(TAG,"File not found");
		}
		catch (IOException ex) {
			Log.e(TAG,"IOException");
		}
	}
	
	public void write(String uri){
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(uri+"/"+filename));
				output.write(bytes);
			}
			finally {
				output.close();
			}
		}
		catch(FileNotFoundException ex){
			Log.e(TAG,"File not found");
		}
		catch(IOException ex){
			Log.e(TAG,"IOException");
		}
	}

	
}
