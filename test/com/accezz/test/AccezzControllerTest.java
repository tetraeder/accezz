package com.accezz.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.accezz.main.controller.AccezzController;
import com.accezz.main.utils.AccezzCLI;

public class AccezzControllerTest {

	private static final String PATH = "C:\\Devhome\\Accezz\\Accezz\\test\\resources\\";

	@Before
	public void initDB() {
	}

	@Test
	public void testMix() {
		AccezzCLI.main(new String[] { PATH + "oneEach.txt" });
	}

	@Test
	public void testOneHttp() {
		AccezzCLI.main(new String[] { PATH + "oneHttp.txt" });
	}

	@Test
	public void testOneHttpS() {
		AccezzController accessController = new AccezzController();
		accessController.request(new File(PATH + "oneHttps.txt"));
	}

	@Test
	public void testOneDns() {
		AccezzCLI.main(new String[] { PATH + "oneDns.txt" });

	}

	@Test
	public void testDnsSameTwice() {
		AccezzController accessController = new AccezzController();
		accessController.request(new File(PATH + "DnsSameTwice.txt"));
	}

	@Test
	public void testHttpSameTwice() {
		AccezzCLI.main(new String[] { PATH + "HttpSameTwice.txt" });
	}

	@Test
	public void testHttpSameMany() {
		AccezzCLI.main(new String[] { PATH + "HttpSameMany.txt" });
	}

	@Test
	public void testHttpsSameTwice() {
		AccezzController accessController = new AccezzController();
		accessController.request(new File(PATH + "HttpsSameTwice.txt"));
	}

	@Test
	public void testManyDns() {
		AccezzController accessController = new AccezzController();
		accessController.request(new File(PATH + "ManyDns.txt"));
	}

	@Test
	public void testManyHttp() {
		AccezzCLI.main(new String[] { PATH + "ManyHttp.txt" });
	}

	@Test
	public void testManyHttps() {
		AccezzController accessController = new AccezzController();
		accessController.request(new File(PATH + "ManyHttps.txt"));
	}

	/*
	 * @Test public void testManyMixedWithReturns() { AccezzController
	 * accessController = new AccezzController(); accessController.request(new
	 * File(PATH + "manyMixedWithReturns.txt")); }
	 */

	/*
	 * @Test public void testSeveralCommandsIncludingErrors() { AccezzController
	 * accessController = new AccezzController(); accessController.request(new
	 * File(PATH + "severalCommandsIncludingErrors.txt")); }
	 */

}
