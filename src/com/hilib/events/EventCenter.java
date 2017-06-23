package com.hilib.events;
import java.util.ArrayList;
import java.util.HashMap;

public class EventCenter {

	private static HashMap<IEventDispatcher, HashMap<EventType, ListenerList>> regTable = new HashMap<IEventDispatcher, HashMap<EventType, ListenerList>>();
	
	
	static public void register(IEventDispatcher target, EventType eventType, IEventListener listener) {
		
		register(target, eventType, listener, false, 0);
	}
	
    static public void register(IEventDispatcher target, EventType eventType, IEventListener listener, boolean once, int priority) {

    	HashMap<EventType, ListenerList> eventMap = regTable.get(target);
    	if(eventMap == null)
    	{
    		eventMap = new HashMap<EventType, ListenerList>();
    		regTable.put(target, eventMap);
    	}
    	
    	ListenerList listenerList = eventMap.get(eventType);
        if(null == listenerList) {
            listenerList = new ListenerList();
            eventMap.put(eventType, listenerList);
        }

        if(!listenerList.hasListener(listener))
        {
        	listenerList.add(listener, once, priority);
        }
        
    }



	public static boolean dispatchEvent(IEventDispatcher target, BaseEvent event) {
		
		if(!hasEventListener(target, event.type))
			return false;
    	
    	ListenerList listenerList = regTable.get(target).get(event.type); 
    	
    	
    	boolean isDispatch = false;
    	for (int i = 0; i < listenerList.size(); i++) {
    		IEventListener listener = listenerList.get(i);
    		if(listener!=null){
    			listener.handler(event);
    			isDispatch = true;
    		}
		}
    	
    	if(isDispatch)
    	{
    		//删除once
        	for(int i=listenerList.size()-1; i>=0; i--)
        	{
        		IEventListener listener = listenerList.get(i);
        		if(listenerList.isOnce(listener))
        		{
        			unregister(target, event.type, listener);
        		}
        	}
    	}
    	
    	return isDispatch;
	}



	public static boolean hasEventListener(IEventDispatcher target, EventType type) {
		
		HashMap<EventType, ListenerList> eventMap = regTable.get(target);
    	if(eventMap == null)
    	{
    		return false;
    	}
    	
    	ListenerList listenerList =  eventMap.get(type);
    	if(listenerList==null || listenerList.size()<=0)
    	{
    		return false;
    	}
  		return true;
	}



	public static void unregister(IEventDispatcher target, EventType type, IEventListener listener) {
		
		if(!hasEventListener(target, type))
			return;
		
		HashMap<EventType, ListenerList> eventMap = regTable.get(target);
		ListenerList listenerList = eventMap.get(type);
		listenerList.remove(listener);
		
		if(listenerList.size()<=0){
			eventMap.remove(type);
			listenerList.dispose();
			listenerList = null;
		}
		
		if(eventMap.size()<=0){
			regTable.remove(target);
			eventMap = null;
		}
	}
	
}



class ListenerList
{
	private ArrayList<ListenerConf> _list = new ArrayList<ListenerConf>();
	private HashMap<IEventListener, ListenerConf> _dic = new HashMap<IEventListener, ListenerConf>();
	
	public void add(IEventListener listener, boolean once, int priority){
		
		ListenerConf conf = _dic.get(listener);
		
		//注册过监听的情况
		if(conf!=null)
		{
			conf.once = once;
			
			//判断优先级是否改变，如果改变，从_list中移除，重新插入。
			if(conf.priority != priority)
			{
				conf.priority = priority;
				_list.remove(conf);
			}
			else
			{
				//什么值都没变，退出
				return;
			}
		}
		else
		{
			//首次注册监听的情况
			conf = new ListenerConf();
			conf.listener = listener;
			conf.once = once;
			conf.priority = priority;
		}
		
		
		//从后往前遍历，直到找到 priority 相等的位置插入
		boolean isFind = false;
		for(int i=_list.size()-1; i>=0; i--){
			
			if(_list.get(i).priority == priority)
			{
				isFind = true;
				_list.add(i+1, conf);
				break;
			}
		}
		
		//优先级最大，放到数组首
		if(!isFind)
		{
			_list.add(0, conf);
		}
		
		//更新字典
		_dic.put(listener, conf);
	}
	
	public void remove(IEventListener listener){
		
		ListenerConf conf =  _dic.get(listener);
		if(conf==null){
			//没有注册过这个监听，退出。
			return;
		}
		
		_list.remove(conf);
		_dic.remove(listener, conf);
		conf.dispose();
	}
	
	public boolean hasListener(IEventListener listener){
		return _dic.get(listener)!=null;
	}
	
	public boolean isOnce(IEventListener listener){
		if(!this.hasListener(listener))
			return false;
		return _dic.get(listener).once;
	}
	
	public IEventListener get(int index){
		return _list.get(index).listener;
	}
	
	public int size(){
		if(_list==null)
			return 0;
		return _list.size();
	}
	
	public void dispose(){
		_list.clear();
		_list = null;
		_dic.clear();
		_dic = null;
	}
}


/**
 * 监听器配置
 * @author blues
 *
 */
class ListenerConf
{
	/**
	 * (default = false) — 监听器是否只注册一次
	 */
	public boolean once;
	
	/**
	 * (default = 0) — 事件侦听器的优先级
	 */
	public int priority;
	
	public IEventListener listener;
	
	public void dispose(){
		listener = null;
	}
}