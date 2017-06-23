import com.hilib.events.BaseEvent;
import com.hilib.events.EventDispatcher;
import com.hilib.events.IEventListener;

public class Entry implements IEventListener{

	
	public static void main(String[] args){
		
		new Entry();
		
	}
	
	public Entry(){
		
		Plain plain = new Plain();
		plain.addEventListener(BaseEvent.CHANGE, this);
		plain.run();
		
		Car car = new Car();
		
		IEventListener listener;
		car.addEventListener(BaseEvent.CHANGE, listener = new IEventListener(){

			@Override
			public void handler(BaseEvent event) {
				onCarRun();
			}});
		
		car.addEventListener(BaseEvent.CHANGE, listener);
//		car.run();
//		car.removeEventListener(BaseEvent.CHANGE, listener);
//		car.run();
		
		//car.addEventListener(BaseEvent.CHANGE, this);
		car.once(BaseEvent.CHANGE, this, 1);
		car.addEventListener(BaseEvent.OPEN, this);
		car.run();
		System.out.println("-------");
		car.run();
		
		System.out.println("has change: " + car.hasEventListener(BaseEvent.CHANGE));
		car.removeEventListener(BaseEvent.CHANGE, listener);
		System.out.println("has change: " + car.hasEventListener(BaseEvent.CHANGE));
		
		car.openDoor();
		
		plain.run();
		
	}
	
	private void onCarRun()
	{
		System.out.println("onCarRun");
	}

	@Override
	public void handler(BaseEvent event) {
		System.out.println(event.getSource()+" "+event.type.getType());
	}
	
}

class Car extends EventDispatcher
{
	public void run()
	{
		BaseEvent event = new BaseEvent(this,BaseEvent.CHANGE);
		this.dispatchEvent(event);
	}
	
	public void openDoor()
	{
		BaseEvent event = new BaseEvent(this,BaseEvent.OPEN);
		this.dispatchEvent(event);
	}
	
}


class Plain extends EventDispatcher
{
	public void run()
	{
		BaseEvent event = new BaseEvent(this,BaseEvent.CHANGE);
		this.dispatchEvent(event);
	}
}

