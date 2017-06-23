package com.hilib.events;

public class EventDispatcher implements IEventDispatcher {

	
	@Override
	public void addEventListener(EventType type, IEventListener listener) {
		this.addEventListener(type, listener, 0);
	}
	
	@Override
	public void addEventListener(EventType type, IEventListener listener, int priority) {
		EventCenter.register(this, type, listener, false, priority);
	}

	@Override
	public boolean dispatchEvent(BaseEvent event) {
		return EventCenter.dispatchEvent(this, event);
	}

	@Override
	public boolean hasEventListener(EventType type) {
		return EventCenter.hasEventListener(this, type);
	}

	@Override
	public void removeEventListener(EventType type, IEventListener listener) {
		EventCenter.unregister(this, type, listener);
	}

	@Override
	public void once(EventType type, IEventListener listener, int priority) {
		EventCenter.register(this, type, listener, true, priority);
	}

	@Override
	public void once(EventType type, IEventListener listener) {
		this.once(type, listener, 0);
	}

	

}
