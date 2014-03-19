package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UniqToolHelperTest {
	
	UniqTool uniqTool = new UniqTool();
	
	@Before
	public void before() {
		uniqTool = new UniqTool(null);
	}

	@After
	public void after() {
		uniqTool = null;
	}

	/**
	 * Positive test 
	 * getStringAfterSkippingFields() with valid args
	 */
	@Test
	public void getStringAfterSkippingFieldsTest() {
		String line = "Who let the dogs out!";
		int fieldCount = 2;

		String expected = "the dogs out!";
		assertEquals(expected,
				uniqTool.getStringAfterSkippingFields(line, fieldCount));
	}
	
	/**
	 * Negative test 
	 * getStringAfterSkippingFields() with line = null
	 */
	@Test
	public void getStringAfterSkippingFieldsNullLineTest() {
		String expected = null;
		assertEquals(expected,
				uniqTool.getStringAfterSkippingFields(null, 1));
	}
	
	/**
	 * Negative test 
	 * getStringAfterSkippingFields() with fieldCount > total fields in line
	 */
	@Test
	public void getStringAfterSkippingFieldsTooHighFieldCountTest() {
		String line = "Who let the dogs out!";
		int fieldCount = 10;

		String expected = null;
		assertEquals(expected,
				uniqTool.getStringAfterSkippingFields(line, fieldCount));
	}
	
	/**
	 * Negative test 
	 * getStringAfterSkippingFields() with -ve fieldCount
	 */
	@Test
	public void getStringAfterSkippingFieldsNegativeFieldCountTest() {
		String line = "Who let the dogs out!";
		int fieldCount = -1;

		String expected = line;
		assertEquals(expected,
				uniqTool.getStringAfterSkippingFields(line, fieldCount));
	}

}
