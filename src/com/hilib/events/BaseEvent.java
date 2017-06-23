package com.hilib.events;
import java.util.EventObject;

public class BaseEvent extends EventObject {

	
	public static final EventType ADDED = new EventType("added");
	public static final EventType REMOVED = new EventType("removed");
	
	public static final EventType CHANGE = new EventType("change");
	public static final EventType COMPLETE = new EventType("complete");
	public static final EventType OPEN = new EventType("open");
	public static final EventType SELECT = new EventType("select");
	public static final EventType INIT = new EventType("init");
	
	
	private static final long serialVersionUID = 7487774148363980868L;
	
	public EventType type;
	
	public BaseEvent(Object source, EventType type) {
		super(source);
		this.type = type;
	}

}
