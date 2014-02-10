package com.u2p.events;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

public class ServerEventsGenerator {
	private List _listener=new ArrayList();
	private EventObject event;
	
	public synchronized void addEvent(EventObject event){
		this.event=event;
		_fireEvent();
	}
	
	public synchronized void addListener(ServerEventsListener al){
		_listener.add(al);
	}
	
	public synchronized void removeListener(ServerEventsListener al){
		_listener.remove(al);
	}	

	private void _fireEvent() {
		// TODO Auto-generated method stub
		Iterator listeners=_listener.iterator();
		while(listeners.hasNext()){
			((ServerEventsListener)listeners.next()).handleServerEventsListener(event);
		}
	}
}
