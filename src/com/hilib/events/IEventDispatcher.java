package com.hilib.events;

public interface IEventDispatcher {
	
	void addEventListener(EventType type, IEventListener listener);
	void addEventListener(EventType type, IEventListener listener, int priority);
	
	void once(EventType type, IEventListener listener);
	void once(EventType type, IEventListener listener, int priority);
	
	boolean dispatchEvent(BaseEvent event);
	
	boolean hasEventListener(EventType type);
	
	void removeEventListener(EventType type, IEventListener listener);
	
}
