package org.weiping.study;

import java.nio.charset.Charset;
import java.util.Date;


/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws Exception {
		System.out.println("string".getBytes("utf-8").length);
		System.out.println("string".getBytes("gbk").length);
		System.out.println("string".getBytes("iso8859-1").length);
		System.out.println("你好".getBytes("utf-8").length);
		System.out.println("你好".getBytes("gbk").length);
		System.out.println("你好".getBytes("iso8859-1").length);
		
		System.out.println(Charset.defaultCharset());
		System.out.println("string".getBytes().length);
		System.out.println("你好".getBytes().length);
	}
}
