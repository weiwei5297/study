package org.weiping.study;

import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	public AppTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testRegex() {
		System.out.println(
				Pattern.matches(".*houmanwx/getbuildingview\\.do.*", 
						"/houmanwx/getbuildingview.do?buildingId=5935&from=timeline&isappinstalled=0&nsukey=rNzhV5WHmX51vVynmqPTs52Lpm17HzVvwTF%2BKWSrg53%2F3MEvBPDss53FKgpGis0304SKVZsyaw8DTucy0yfHqg%3D%3D"));
	}
	
	static class User {
		public User(String name) {
			this.name = name;
		}
		String name;
	}
}
