/**
 * 
 */
package com.flatironschool.javacs;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author downey
 *
 */
public class WikiPhilosophyTest {


	/**
	 * Test method for {@link com.flatironschool.javacs.WikiPhilosophy#main(java.lang.String[])}.
	 */
	@Test
	public void testProgrammingLang() {
		// Because this lab is more open-ended than others, we can't provide unit
		// tests.  Instead, we just check that you've modified WikiPhilosophy.java
		// so it doesn't throw an exception.
		String[] args = {"https://en.wikipedia.org/wiki/Java_(programming_language)"};
		try {
			WikiPhilosophy.main(args);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testData() {
		// Because this lab is more open-ended than others, we can't provide unit
		// tests.  Instead, we just check that you've modified WikiPhilosophy.java
		// so it doesn't throw an exception.
		String[] args = {"https://en.wikipedia.org/wiki/Data"};
		try {
			WikiPhilosophy.main(args);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
