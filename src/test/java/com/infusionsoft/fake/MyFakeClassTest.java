package com.infusionsoft.fake;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MyFakeClassTest {

	private static MyFakeClass myFakeClass;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myFakeClass = new MyFakeClass();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetOne() {
		System.out.println("Starting test...");
		
		assertTrue(myFakeClass.getOne() == 1);
		
		System.out.println("Test finished.");
	}

}
