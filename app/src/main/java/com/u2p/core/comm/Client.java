package com.u2p.core.comm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.os.Environment;
import android.util.Log;

import com.u2p.core.db.DbDataSource;
import com.u2p.core.db.DbFile;
import com.u2p.core.db.DbUser;
import com.u2p.events.ActivityEvents;
import com.u2p.events.ActivityEventsListener;
import com.u2p.events.FileEvent;
import com.u2p.events.ListEvent;
import com.u2p.events.NewClientEvent;
import com.u2p.events.NewGroupList;
import com.u2p.events.VoteEvent;
import com.u2p.messages.ACK;
import com.u2p.messages.Authentication;
import com.u2p.messages.FileAnswer;
import com.u2p.messages.FileRequest;
import com.u2p.messages.ListAnswer;
import com.u2p.messages.ListRequest;
import com.u2p.messages.VoteFile;
import com.u2p.ui.component.ItemFile;

public class Client extends Thread implements ActivityEventsListener{
	private InetAddress address;
	private Socket socket;
	private OutputStream out;
	private ObjectOutputStream oos;
	private InputStream in;
	private ObjectInputStream ois;
	private boolean end;
	private static final String TAG="ServerClient";
	private String userName;
	private DbDataSource datasource;
	private Server parent;
	private List<String> commons;
	private int port;
	private boolean sendAuthentication;
	private boolean client;
	
	public Client(Socket socket,InetAddress address, DbDataSource datasource, Server parent,boolean client){
		this.socket=socket;
		this.address=address;
		this.datasource=datasource;
		this.parent=parent;
		end=false;
		this.userName=datasource.getUser(1).getUser();
		this.commons=new ArrayList<String>();
		this.sendAuthentication=false;
		this.client=client;
	}
	public Client(InetAddress address, int port,DbDataSource datasource, Server parent,boolean client){
		this.address=address;
		this.datasource=datasource;
		this.parent=parent;
		end=false;
		this.port=port;
		
		this.userName=datasource.getUser(1).getUser();
		this.commons=new ArrayList<String>();
		this.sendAuthentication=false;
		this.client=client;
	}	

	public void close() throws IOException{
		//Deberiamos enviar un ACK para cerrar la conexión con el otro extremo
		if(!end){
			ACK ack=new ACK(Server.ACK_END);
			oos.writeObject(ack);
			Log.d(TAG,"Sending ACK END to "+address);
		}
		Log.d(TAG, "Close comunication");
		ois.close();
		in.close();
		oos.close();
		out.close();
		socket.close();
	}
	
	public List<String> compareGroups(HashMap<String,String> socketGroups,HashMap<String,String> userGroups){
	    List<String> commons=new ArrayList<String>();
	    Log.d(TAG,"Compare groups");
		for (Entry<String, String> entry : socketGroups.entrySet()) {
	        String key = entry.getKey();
	        String hash=entry.getValue();
	        Log.d(TAG,"Key socket "+key+" value "+hash);
	        Log.d(TAG,"User contains key "+userGroups.containsKey(key)+" hash "+userGroups.get(key));
	        if(userGroups.containsKey(key) && hash.equals(userGroups.get(key))){
	        	commons.add(key);
	        }
	    }
		return commons;
	}
	
	public List<String> getCommonsGroup(){
		return this.commons;
	}
	
	public InetAddress getAddress(){
		return this.address;
	}
	public Authentication makeAuthenticationMessage(){
		DbUser user=datasource.getUser(1);
		Authentication autms=new Authentication(user.getUser());
		List<String> userGroups=datasource.getAllGroups();
		
		String hashtmp;
		for(String group:userGroups){
			hashtmp=datasource.getHashGroup(group);
			if(hashtmp!=null){
				autms.addGroup(group, hashtmp);
			}
		}
		return autms;
	}
	
	public void run(){
		try{
			if(client){
				try {
					this.address=address;
					this.socket=new Socket();
					
					this.socket.connect(new InetSocketAddress(this.address,this.port),5000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, "Error connect with the client "+address);
				}
			}
			Log.d(TAG, "Client start, openning streams");
			out=socket.getOutputStream();
			oos=new ObjectOutputStream(out);
			
			in=socket.getInputStream();
			ois=new ObjectInputStream(in);
			try{
				
				if(!parent.isService()){
					//Mandamos primer mensaje
					oos.writeObject(makeAuthenticationMessage());
					this.sendAuthentication=true;
					Log.i(TAG,"Send Authentication message to "+address);
				}
				while(!end){
					Object aux=null;
					Log.d(TAG, "Wait for objects");
					aux=ois.readObject();
					Log.d(TAG, "New object received");
					if(aux instanceof ACK){
						ACK ack=(ACK)aux;
						Log.d(TAG,"Received ACK, type:"+ack.getACKType()+" from "+address);
						
						switch(ack.getACKType()){
							case Server.ACK_END:
								end=true;
								Log.d(TAG,"End comunication with "+address);
								break;
							case Server.ACK_ERROR_FILE:
								Log.e(TAG,"Error file "+address);
								parent.ToastIt("Error downloading file");
								break;
							case Server.ACK_LIST_REQUEST_ERROR:
								Log.e(TAG,"List request error "+address);
								break;
							default:
								break;
						}
						continue;
					}
					if(aux instanceof Authentication){
						Log.d(TAG,"Received Authentication message from "+address);
						Authentication aut=(Authentication)aux;
						HashMap<String,String> socketGroups=aut.getGroups();
						List<String> userGroups=datasource.getAllGroups();
						HashMap<String,String> serverGroups=new HashMap<String,String>();
						
						String hashtmp;
						for(String group:userGroups){
							hashtmp=datasource.getHashGroup(group);
							if(hashtmp!=null){
								serverGroups.put(group, hashtmp);
							}
						}
						//Comparar los grupos que se reciben para ver si pertenecemos a alguno de esos grupos
						commons=this.compareGroups(socketGroups,serverGroups);
						Log.d(TAG, "Commons groups with "+address+" "+commons.size());
						for(String str:commons){
							Log.d(TAG, "Common group: "+str);
						}
						if(commons.size()>0){
							//Si tenemos algún grupo en común lo guardamos como cliente
							parent.addActiveClient(address, this);
							//Enviamos un message Authentication
							if(!sendAuthentication){
								oos.writeObject(makeAuthenticationMessage());
								Log.d(TAG, "Send Authentication message with commons groups to "+address);
							}
							NewClientEvent event=new NewClientEvent(parent.getGenerator(),address,commons);
							parent.getGenerator().addEvent(event);
						}else{
							//Si no enviamos ACK para acabar la comunicación
							ACK ack=new ACK(Server.ACK_END);
							oos.writeObject(ack);
							Log.d(TAG, "No commons group, cancel communication to "+address);
						}
	
						//Lanzar evento al activity principal de nuevo cliente
						//Si además de servidor también es el que oferta el servicio, cada vez que reciba un mensaje 
						//de estos tendrá que avisar a todos los clientes (menos al último) que le han contactado con los datos 
						//del último cliente que se haya conectado
						if(parent.isService()){
							
						}
						continue;
					}
					if(aux instanceof FileRequest){
						Log.d(TAG,"Received FileRequest message from "+address);
						//Petición para el envio de un archivo
						FileRequest fileR=(FileRequest)aux;
						//Comprobamos que el archivo existe
						if(datasource.existsFile(fileR.getGroup(),fileR.getName())){
							//Si da tiempo lo ciframos
							FileAnswer file=new FileAnswer(fileR.getName(),fileR.getGroup());
							String uri=datasource.getFile(fileR.getGroup(),fileR.getName()).getUri();
							file.read(uri);
							oos.writeObject(file);
							Log.d(TAG,"Send FileAnswer message to "+address);
							continue;
						}else{
							ACK ack=new ACK(Server.ACK_ERROR_FILE);
							oos.writeObject(ack);
							Log.e(TAG, "File "+fileR.getName()+" not found in group "+fileR.getGroup());
						}
						continue;
					}
					if(aux instanceof ListRequest){
						ListRequest list=(ListRequest)aux;
						String group=list.getGroup();
						Log.d(TAG,"Received ListRequest message from: "+address+" for group: "+group);
						
						if(datasource.groupExist(group)){
							List<DbFile> files=datasource.getFilesGroup(group);
							List<ItemFile> items=new ArrayList<ItemFile>();
							
							for(DbFile file:files){
								ItemFile auxItem=new ItemFile(file,this.userName,datasource.getFileType(file.getType()));
								auxItem.setGroup(group);
								auxItem.setAddress(socket.getLocalAddress());
								items.add(auxItem);
								Log.d(TAG, "Added to ListAnswer: "+file.toString());
							}
							ListAnswer listA=new ListAnswer((ArrayList<ItemFile>)items,group);
							oos.writeObject(listA);
							Log.d(TAG, "Send ListAnswer message to "+address);
						}else{
							ACK ack=new ACK(Server.ACK_LIST_REQUEST_ERROR);
							oos.writeObject(ack);
							Log.d(TAG,"Group not found");
						}
						continue;
					}
					if(aux instanceof VoteFile){
						Log.d(TAG,"Received VoteFile message from "+address);
						VoteFile vote=(VoteFile)aux;
						Log.d(TAG, "Vote "+vote.getFile()+" group "+vote.getGroup()+" "+vote.getVote());
						datasource.voteFile(vote.getGroup(),vote.getFile(),vote.getVote());
						VoteEvent voteEvent=new VoteEvent(parent.getGenerator(),this.address);
						voteEvent.setGroupAndFile(vote.getGroup(),vote.getFile());
						voteEvent.vote(vote.getVote());
						parent.getGenerator().addEvent(voteEvent);
						continue;
					}
					if(aux instanceof FileAnswer){
						Log.d(TAG,"Received FileAnswer message from "+address);
						//Recibimos un archivo
						FileAnswer fileA=(FileAnswer)aux;
						//Si da tiempo lo desciframos
						//Escribimos archivo
						File path=new File(Environment.getExternalStorageDirectory()+""+datasource.getMaindir()+""+fileA.getGroup());//<-Falta una barra?no
						fileA.write(path.getPath());
						//Enviamos ACK
						Log.d(TAG,"Write file "+fileA.getFilename());
						continue;
					}
					if(aux instanceof ListAnswer){
						Log.d(TAG,"Received ListAnswer message from "+address);
						ListAnswer list=(ListAnswer)aux;
						NewGroupList event=new NewGroupList(parent.getGenerator(),address,list.getGroup(),list.getItemsList());
						parent.getGenerator().addEvent(event);
						Log.d(TAG,"Send ListAnswer ACK message to "+address);
						continue;
					}					
					
				}
				this.close();
			}catch(ClassNotFoundException e){
				Log.e("ServerClient","ClassNotFoundException");
			}
		}catch(IOException ew){
			Log.e("ServerClient","IOException");
		}
	}
	private void requestFile(String group,String file) throws IOException{
		FileRequest fileR=new FileRequest(file,group);
		oos.writeObject(fileR);
		Log.d(TAG,"Sending FileRequest message to "+this.address);
	}
	
	private void requestList(String group) throws IOException{
		ListRequest list=new ListRequest(group);
		oos.writeObject(list);
		Log.d(TAG,"Sending ListRequest message to "+this.address);
	}
	
	private void voteFile(String group,String file, int vote) throws IOException{
		VoteFile votem=new VoteFile(group,file,vote);
		oos.writeObject(votem);
		Log.d(TAG,"Sending VoteFile message to "+this.address);
	}
	
	public void handleActivityEventsListener(EventObject e) {
		// TODO Auto-generated method stub
		ActivityEvents event=(ActivityEvents)e;
		Log.d(TAG,"Receive event to "+event.getAddress());
		Log.d(TAG,"My address is "+this.address);
		if(event.getAddress().toString().equals(this.address.toString())){
			//El evento es para este cliente, por tanto manejamos el evento
			try{
				if(event instanceof FileEvent){
					Log.d(TAG,"Receive event FileEvent");
					FileEvent file=(FileEvent)event;
					Log.d(TAG,"Request file "+file.getFile()+" group "+file.getGroup());
					this.requestFile(file.getGroup(), file.getFile());
				}
				if(event instanceof ListEvent){
					Log.d(TAG,"Receive event ListEvent");
					ListEvent list=(ListEvent)event;
					this.requestList(list.getGroup());
				}
				if(event instanceof VoteEvent){
					Log.d(TAG,"Receive event VoteEvent");
					VoteEvent vote=(VoteEvent)event;
					this.voteFile(vote.getGroup(),vote.getFile(),vote.getVote());
				}
			}catch(IOException e1){
				Log.e(TAG,"IOException handle activity events");
			}
		}
	}
}
