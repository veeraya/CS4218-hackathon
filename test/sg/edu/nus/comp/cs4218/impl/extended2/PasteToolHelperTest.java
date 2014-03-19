package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasteToolHelperTest {

	PasteTool pasteTool;
	
	@Before
	public void before() {
		pasteTool = new PasteTool(null);
	}

	@After
	public void after() {
		pasteTool = null;
	}
	
	/**
	 * Positive test
	 * getMaxLineCount with valid filesContent
	 */
	@Test
	public void getMaxLineCountTest() {
		ArrayList<String[]> filesContent = new ArrayList<String[]>();
		
		String[] oneLine = {"line1"};
		String[] twoLines = {"line1", "line2"};
		String[] threeLines = {"line1", "line2", "line3"};
		
		filesContent.add(oneLine);
		filesContent.add(threeLines);
		filesContent.add(twoLines);
		
		assertEquals(3, PasteTool.getMaxLineCount(filesContent));
	}
	
	/**
	 * Negative test
	 * getMaxLineCount with null argument
	 */
	@Test
	public void getMaxLineCountNullArgTest() {
		assertEquals(0, PasteTool.getMaxLineCount(null));
	}
	
	/**
	 * Negative test
	 * getMaxLineCount with empty ArrayList argument
	 */
	@Test
	public void getMaxLineCountEmptyListTest() {
		ArrayList<String[]> filesContent = new ArrayList<String[]>();
		assertEquals(0, PasteTool.getMaxLineCount(filesContent));
	}
	
	/**
	 * Positive test
	 * getContentFromSameLineNumber with valid args
	 */
	@Test
	public void getContentFromSameLineNumberTest() {
		ArrayList<String[]> filesContent = new ArrayList<String[]>();
		
		String[] oneLine = {"line1"};
		String[] twoLines = {"line1", "line2"};
		String[] threeLines = {"line1", "line2", "line3"};
		
		filesContent.add(oneLine);
		filesContent.add(threeLines);
		filesContent.add(twoLines);
		
		String[] expectedOutput = {"", "line2", "line2"};
		
		assertArrayEquals(expectedOutput, 
				pasteTool.getContentFromSameLineNumber(filesContent, 1));
	}

	/**
	 * Negative test
	 * getContentFromSameLineNumber with null fileContent
	 */
	@Test
	public void getContentFromSameLineNumberNullFileContentTest() {
		assertArrayEquals(null, 
				pasteTool.getContentFromSameLineNumber(null, 1));
	}
	
	
}
