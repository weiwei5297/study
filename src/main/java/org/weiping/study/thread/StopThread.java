package org.weiping.study.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StopThread extends Thread {
	private volatile boolean isInterrupted = false;
	private static BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	@Override
	public void run() {
		while (!isInterrupted) {
			try {
//				System.out.println("doing sth.");
//				Thread.sleep(1000);
				System.out.println("waiting for queue...");
				queue.take();
			} catch (InterruptedException e) {
				System.out.println("Interrupted!");
				isInterrupted = true;
//				break;
			}
		}
		System.out.println(getName() + " quit...");
	}
	
	@Override
	public void interrupt() {
		isInterrupted = true;
		super.interrupt();
	}
	
	public static void main(String[] args) {
		StopThread st = new StopThread();
		
		st.start();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		st.interrupt();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
