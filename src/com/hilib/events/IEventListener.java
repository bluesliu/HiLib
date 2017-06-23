package com.hilib.events;
import java.util.EventListener;

public interface IEventListener extends EventListener {
	
	public void handler(BaseEvent event);

}
