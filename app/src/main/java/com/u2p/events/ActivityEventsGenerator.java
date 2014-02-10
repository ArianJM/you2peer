package com.u2p.events;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

public class ActivityEventsGenerator {
	private List _listener=new ArrayList();
	private EventObject event;
	
	public synchronized void addEvent(EventObject event){
		this.event=event;
		_fireEvent();
	}
	
	public synchronized void addListener(ActivityEventsListener al){
		_listener.add(al);
	}
	
	public synchronized void removeListener(ActivityEventsListener al){
		_listener.remove(al);
	}	

	private void _fireEvent() {
		// TODO Auto-generated method stub
		Iterator listeners=_listener.iterator();
		while(listeners.hasNext()){
			((ActivityEventsListener)listeners.next()).handleActivityEventsListener(event);
		}
	}
}
