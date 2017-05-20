package com.util;

import java.util.Iterator;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimerUtil {

	class TimerItem {
		TimerIterface obj;
		long delay;
		long startTime;
		int id; // timerItem 唯一的id
	}
	
	private static volatile TimerUtil timerUtil = null; 
	private Queue<TimerItem> registeredItems = new ConcurrentLinkedQueue<>();
	private Queue<TimerItem> itemPool = new ConcurrentLinkedQueue<>();
	private long checkInterval = 10;
	
	public static TimerUtil getInstance() {
		if(timerUtil == null) {
			synchronized (TimerUtil.class) {
				timerUtil = new TimerUtil();
			}
		}
		return timerUtil;
	}
	
	private TimerUtil() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				check();
			}
		}, 0, checkInterval);
		
	}

	private void check() {
		long curTime = System.currentTimeMillis();
		Iterator<TimerItem> ite = registeredItems.iterator();
		while(ite.hasNext()) {
			TimerItem timerItem = ite.next();
			if(curTime - timerItem.startTime >= timerItem.delay) {
				ite.remove();
				timerItem.obj.timeOut();
				itemPool.add(timerItem);
			}
		}
	}
	
	public void startTimer(TimerIterface obj, int delay, int id) {
		TimerItem timerItem = itemPool.poll();
		if(timerItem == null) {
			timerItem = new TimerItem();
		}
		timerItem.startTime = System.currentTimeMillis();
		timerItem.obj = obj;
		timerItem.delay = delay;
		timerItem.id = id;
		registeredItems.add(timerItem);
	}
	
	public void cancelTimer(int id) {
		Iterator<TimerItem> ite = registeredItems.iterator();
		while(ite.hasNext()) {
			TimerItem timerItem = ite.next();
			if(id == timerItem.id) {
				registeredItems.remove(timerItem);
				itemPool.add(timerItem);
				break;
			}
		}
	}
}
