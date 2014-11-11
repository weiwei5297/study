package org.weiping.study.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class MXBeanTest {

	public static void main(String[] args) throws Exception {
		RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();
		Thread.sleep(1000);
		System.out.println(mxbean.getUptime());
	}

}
