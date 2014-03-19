package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UniqToolTest {
	private UniqTool uniqTool;
	private static final String NEW_LINE = System.lineSeparator();
	private static final String INPUT_FILE_PATH = "__test__input__";
	private static final String OUTPUT_FILE_PATH = "__test__ouput__";
	
	
	@Before
	public void before() {
		uniqTool = new UniqTool(null);
		
		String content =
				"word1 word2 word3" + NEW_LINE +
				"word1 word2 word3" + NEW_LINE +
				"word1+ word2 word3";
		
		File file = new File(INPUT_FILE_PATH);
		if (file.exists()) {
			file.delete();
		}
		createFile(INPUT_FILE_PATH, content);
	}

	@After
	public void after() {
		uniqTool = null;
		deleteFile(INPUT_FILE_PATH);
		deleteFile(OUTPUT_FILE_PATH);
	}
	
	@Corrected
	/**
	 * Original input string only contains one line of string
	 * while uniq is only positively testable if it contains multiple lines.
	 */
	@Test
	/**
	 * Positive test
	 * getUnique() with all valid params
	 */
	public void getUnique() {
		String input = "word1 word2 word3" + NEW_LINE +
					   "word1 word2 word3" + NEW_LINE +
				       "word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
				"word1 word2";
		
		assertEquals(expectedOutput, uniqTool.getUnique(false, input));
	}
	
	@Test
	/**
	 * Negative test
	 * getUnique() with input = null
	 */
	public void getUniqueWithNullInput() {
		assertEquals("",uniqTool.getUnique(false, null));
	}
	
	@Corrected
	/**
	 * Original input string only contains one line of string
	 * while uniq is only positively testable if it contains multiple lines.
	 */
	@Test
	/**
	 * Positive test
	 * getUniqueSkipNum() with all valid params
	 */
	public void getUniqueSkipNum() {
		String input = 	"word1 word2 word3" + NEW_LINE +
		   	       		"woRd1 word2 word3" + NEW_LINE +
		   	       		"word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
   	       		"word1 word2";
		assertEquals(expectedOutput, uniqTool.getUniqueSkipNum(1, false, input));
	}
	
	@Corrected
	/**
	 * The valid range for NUM for uniq is non negative integer
	 * but it is not the case in the original test case
	 */
	@Test
	/**
	 * Negative test
	 * getUniqueSkipNum() with NUM < 0
	 */
	public void getUniqueSkipNumWithNegativeNum() {
		int NUM = -1;
		String input = 	"word1 word2 word3" + NEW_LINE +
		   	       		"word1+ word2 word3" + NEW_LINE +
		   	       		"word1 word2";
		
		String expectedOutput = String.format(UniqTool.ERR_MSG_INVALID_NUM_OF_FIELDS, NUM);
		assertEquals(expectedOutput, uniqTool.getUniqueSkipNum(NUM, false, input));
		assertEquals(UniqTool.ERR_CODE_INVALID_NUM_OF_FIELDS, uniqTool.getStatusCode());
	}
	
	
	@Test
	/**
	 * Negative Test
	 * getUniqueSkipNum() with null input
	 */
	public void getUniqueSkipNumForNullTest() {
		assertEquals("",uniqTool.getUniqueSkipNum(1,true, null));
	}
	
	@Corrected
	/**
	 * Original test cases refer to input files that are not available.
	 * Modified to use the input file created at runtime with the helper method.
	 */
	@Test
	/**
	 * Test execute take input from file and output to console
	 */
	public void executeWithFileInputToStandardOutput() {
		String[] args = {INPUT_FILE_PATH, "-"};
		uniqTool = new UniqTool(args);
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
				"word1+ word2 word3";
		
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
	}
	
	@Corrected
	/**
	 * Original test cases refer to input files that are not available.
	 * Modified to use the input file created at runtime with the helper method.
	 */
	@Test
	/**
	 * Positive test
	 * execute() with standard input and file output
	 */
	public void executeWithStandardInputToFileOutput() {
		String[] args = {"-", OUTPUT_FILE_PATH};
		uniqTool = new UniqTool(args);
		
		String input = 
				"word1 word2 word3" + NEW_LINE +
				"word1 word2 word3" + NEW_LINE +
				"word1+ word2 word3";
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
				"word1+ word2 word3";
		
		uniqTool.execute(new File(""), input);
		String actualOutput = uniqTool.readFile(OUTPUT_FILE_PATH, Charset.forName("UTF8"));
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Corrected
	/**
	 * Original test cases refer to input files that are not available.
	 * Modified to use the input file created at runtime with the helper method.
	 */
	@Test
	/**
	 * Positive test
	 * execute() with file input and file output
	 */
	public void executeWithFileInputToFileOutput() {
		String[] args = {INPUT_FILE_PATH, OUTPUT_FILE_PATH};
		uniqTool = new UniqTool(args);
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
				"word1+ word2 word3";
		
		uniqTool.execute(new File(""), "");
		String actualOutput = uniqTool.readFile(OUTPUT_FILE_PATH, Charset.forName("UTF8"));
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Corrected
	/**
	 * Original input string only contains one line of string
	 * while uniq is only positively testable if it contains multiple lines.
	 */
	@Test
	/**
	 * Positive test
	 * execute() with standard input
	 */
	public void executeWithStandardInputToStandardOutput() {
		String args[] = {"-", "-"};
		uniqTool = new UniqTool(args);
		
		String input = 	"word1 word2 word3" + NEW_LINE +
				   		"word1 word2 word3" + NEW_LINE +
				   		"word1 word2";
	
		String expectedOutput = 
			"word1 word2 word3" + NEW_LINE +
			"word1 word2";
		
		assertEquals(expectedOutput, uniqTool.execute(new File(""), input));
	}
	
	/**
	 * Helper method to create temporary file
	 * @param path
	 * @param content
	 */
	public void createFile(String path, String content) {
		File file = new File(path);
		if (file.exists()) {
			System.err.println("Cannot create file in JUnit test for uniq.");
			System.err.println(path + " already exists.");
			System.err.println("Please enter another name for test input file.");
		}
		
		uniqTool.writeToFile(path, content);
	}
	
	/**
	 * Helper method to delete temporary file
	 * @param path
	 */
	public void deleteFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		
		if (!file.delete()) {
			System.err.println(path + " deletion fails.");
			System.err.println("Please delete the file manually.");
		}
	}
	
}
