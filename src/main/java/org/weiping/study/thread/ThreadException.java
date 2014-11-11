package org.weiping.study.thread;

public class ThreadException extends Thread {

	public ThreadException(String name) {
		super(name);
	}
	
	@Override
	public void run() {
		Object[] objs = new Object[]{};
		System.out.println(objs[1]);
	}

	public static void main(String[] args) {
		setDefaultUncaughtExceptionHandler(
				new LoggingThreadGroup("LoggingThreadGroup"));
		
		ThreadException st = new ThreadException("Test-T");

//		st.setUncaughtExceptionHandler(
//				new LoggingThreadGroup("LoggingThreadGroup"));
		
		st.start();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Object[] objs = new Object[]{};
		System.out.println(objs[1]);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " quit...");
	}

	static class LoggingThreadGroup extends ThreadGroup {

		public LoggingThreadGroup(String name) {
			super(name);
		}

		public void uncaughtException(Thread t, Throwable e) {
			System.out.println("in LoggingThreadGroup");
			System.out.println(t.getName() + ", " + e.getStackTrace());
		}
	}

}
