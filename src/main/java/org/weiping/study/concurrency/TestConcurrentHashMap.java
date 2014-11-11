package org.weiping.study.concurrency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestConcurrentHashMap {

	public static void main(String[] args) throws InterruptedException {
		final ConcurrentHashMap<Integer, Integer> map = 
				new ConcurrentHashMap<Integer, Integer>();
		for (int i = 0; i < 20; i++) {
			map.put(i, i);
		}
		
		Thread t1 = new Thread("t1") {
			public void run() {
				int i = 0;
				for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
					++i;
					if (i % 2 == 0) {
						map.remove(i);
						System.out.println(getName() + " -- " + "rmv " + i + " -- " + map);
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		t1.start();
		

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		
		Thread t2 = new Thread("t2") {
			public void run() {
				for (int i = 20; i < 40; i++) {
					map.put(i, i);
					System.out.println(getName() + " -- " + "put " + i + " -- " + map);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		t2.start();
		
		Thread.sleep(Integer.MAX_VALUE);
	}

}
