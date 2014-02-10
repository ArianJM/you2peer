package com.u2p.core.comm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.u2p.core.db.DbDataSource;
import com.u2p.events.ServerEventsGenerator;
import com.u2p.ui.MainActivity;

public class Server extends Thread{
	private ServerSocket ssocket;
	private ServerEventsGenerator eventsGenerator;
	private HashMap<InetAddress,Client> activeClients;
	private HashMap<InetAddress,List<String>> groupsCommons;
	private MainActivity activity;
	private int port;
	private static final String TAG="Server";
	private DbDataSource datasource;
	private boolean isService;
	
	public static final int ACK_END=-1;
	public static final int ACK_LIST_REQUEST_ERROR=1;
	public static final int LIST_SEND=2;
	public static final int FILE_SEND=3;
	public static final int ACK_ERROR_FILE=4;

	public Server(int port,DbDataSource datasource,MainActivity activity){
		this.port=port;
		this.activity=activity;
		this.datasource=datasource;
		activeClients=new HashMap<InetAddress,Client>();
		groupsCommons=new HashMap<InetAddress,List<String>>();
		this.eventsGenerator=new ServerEventsGenerator();
		eventsGenerator.addListener(activity);
	}
	public void ToastIt(String message){
		activity.ToasIt(message);
	}
	public void setService(boolean service){
		this.isService=service;
	}
	

	public boolean isService(){
		return this.isService;
	}
	public ServerEventsGenerator getGenerator(){
		return this.eventsGenerator;
	}
	
	public synchronized void addActiveClient(InetAddress address,Client client){
		if(!activeClients.containsKey(address)){
			activeClients.put(address,client);
			Log.d(TAG, "Add new active client "+address);
		}
	}
	
	public synchronized void addGroupCommon(InetAddress address,List<String> commons){
		//Si ya tiene el address en groupsCommons, ¿no puedes añadir más grupos comunes?
		if(!groupsCommons.containsKey(address)){
			groupsCommons.put(address,commons);
			Log.d(TAG, "Add new list commons to client "+address);
		}
	}
	
	public List<String> getGroupsCommons(InetAddress address){
		if(!groupsCommons.containsKey(address)){
			return groupsCommons.get(address);
		}
		return null;
	}
	
	public List<InetAddress> getCommonsClient(String group){
		List<InetAddress> clients=new ArrayList<InetAddress>();
		List<InetAddress> keys=new ArrayList<InetAddress>(groupsCommons.keySet());
		
		for(InetAddress inet:keys){
			List<String> values=groupsCommons.get(inet);
			for(String gr:values){
				if(gr.equals(group)){
					clients.add(inet);
				}
			}
		}
		return clients;
	}
	
	public void deleteActiveClient(InetAddress address){
		if(!activeClients.containsKey(address)){
			Client client = activeClients.get(address);
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "Exception close client "+address);
			}
			activeClients.remove(address);
			Log.d(TAG, "Delete active client "+address);
		}
	}
	
	public void deleteAllActiveClients(){
		List<Thread> clients=new ArrayList<Thread>(activeClients.values());
		Client sClient=null;
		if(clients!=null){
			for(Thread client:clients){
				sClient=(Client)client;
				this.deleteActiveClient(sClient.getAddress());
			}
		}
	}
	public List<InetAddress> getAddressClientsActive(){
		return new ArrayList<InetAddress>(activeClients.keySet());
	}
	public HashMap<InetAddress,Client> getAllActiveClient(){
		return this.activeClients;
	}
	
	public Client getActiveClient(InetAddress client){
		if(activeClients.containsKey(client)){
			return activeClients.get(client);
		}
		return null;
	}
	
	public void close(){
		try{
			this.deleteAllActiveClients();
			this.ssocket.close();
		Log.d(TAG,"Close server");
		}catch(IOException e){
			Log.d(TAG,"Close server exception");
		}
	}
	public void run(){
		try{
			ssocket=new ServerSocket(port);
			Log.d(TAG,"Server listening on port "+port);
			while(true){
				Socket socket=ssocket.accept();
				Client sClient=new Client(socket,socket.getInetAddress(),datasource,this,false);
				sClient.start();
				Log.d(TAG,"New client connected: "+socket.getInetAddress());
			}
		}catch(IOException e){
			Log.e(TAG,"IOException");
		}
	}
}
