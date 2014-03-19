/**
 * Additional unit tests on top of the original
 * test cases for test driven development.
 */

package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UniqToolExtraTest {
	private UniqTool uniqTool;
	private static final String NEW_LINE = System.lineSeparator();
	private static final String INPUT_FILE_PATH = "__test__input__";
	
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
	}
	
	@Test
	/**
	 * Positive test
	 * getUnique() with all valid params
	 */
	public void getUniqueWithCheckCase() {
		String input = "word1 word2 word3" + NEW_LINE +
					   "word1 word2 word3" + NEW_LINE +
				       "word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
				"word1 word2";
		
		assertEquals(expectedOutput, uniqTool.getUnique(true, input));
	}
	
	@Test
	/**
	 * Negative test
	 * getUnique() with input = ""
	 */
	public void getUniqueWithEmptyInput() {
		assertEquals("",uniqTool.getUnique(false, ""));
	}
	
	@Test
	/**
	 * Positive test
	 * getUnique() with checkCase = false
	 */
	public void getUniqueCaseInsensitive() {
		String input = "word1 word2 word3" + NEW_LINE +
				   	   "woRd1 wOrd2 word3" + NEW_LINE +
			           "word1 word2";
	
		String expectedOutput = 
			"word1 word2 word3" + NEW_LINE +
			"word1 word2";
	
		assertEquals(expectedOutput, uniqTool.getUnique(false, input));
	}
	
	@Test
	/**
	 * Positive test
	 * getUnique() with checkCase = true
	 */
	public void getUniqueCaseSensitive() {
		String input = "word1 word2 word3" + NEW_LINE +
			   	       "woRd1 wOrd2 word3" + NEW_LINE +
		               "word1 word2";
	
		String expectedOutput = 
		 	"word1 word2 word3" + NEW_LINE +
	   	   	"woRd1 wOrd2 word3" + NEW_LINE +
	   	   	"word1 word2";
	
		assertEquals(expectedOutput, uniqTool.getUnique(true, input));
	}
	
	@Test
	/**
	 * Positive test
	 * getUniqueSkipNum() with NUM > than no of fieds on some lines
	 */
	public void getUniqueSkipNum() {
		String input = 	"word1 word2 word3" + NEW_LINE +
		   	       		"woRd1 word2 word3 word4" + NEW_LINE +
		   	       		"word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
				"woRd1 word2 word3 word4" + NEW_LINE +
				"word1 word2";
		
		assertEquals(expectedOutput, uniqTool.getUniqueSkipNum(3, false, input));
	}
	
	@Test
	/**
	 * Positive test
	 * getUniqueSkipNum() with NUM > than no of fieds on some lines
	 */
	public void getUniqueSkipNumCheckCase() {
		String input = 	"word1 word2 word3 word4" + NEW_LINE +
		   	       		"woRd1 word2 word3 word4" + NEW_LINE +
		   	       		"word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3 word4" + NEW_LINE +
				"word1 word2";
		
		assertEquals(expectedOutput, uniqTool.getUniqueSkipNum(2, true, input));
	}
	
	@Test
	/**
	 * Positive test
	 * getUniqueSkipNum() with NUM > than the no of fields in all lines
	 */
	public void getUniqueSkipHugeNum() {
		String input = 	"word1 word2 word3" + NEW_LINE +
		   	       		"woRd1 word2 word3" + NEW_LINE +
		   	       		"word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3";
		
		assertEquals(expectedOutput, uniqTool.getUniqueSkipNum(100, false, input));
	}
	
	
	@Test
	/**
	 * Positive test
	 * getUniqueSkipNum() with checkCase = true
	 */
	public void getUniqueSkipNumCaseSensitive() {
		String input = 	"word1 word2 word3" + NEW_LINE +
		   	       		"woRd1 wOrd2 word3" + NEW_LINE +
		   	       		"word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
				"woRd1 wOrd2 word3" + NEW_LINE +
   	       		"word1 word2";
		assertEquals(expectedOutput, uniqTool.getUniqueSkipNum(1, true, input));
	}
	
	@Test
	/**
	 * Positive test
	 * getUniqueSkipNum() with checkCase = false
	 */
	public void getUniqueSkipNumCaseInsensitive() {
		String input = 	"word1 word2 word3" + NEW_LINE +
		   	       		"woRd1 wOrd2 word3" + NEW_LINE +
		   	       		"word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
   	       		"word1 word2";
		assertEquals(expectedOutput, uniqTool.getUniqueSkipNum(1, false, input));
	}
	
	@Test
	/**
	 * Positive test
	 * getUniqueSkipNum() with NUM = 0
	 */
	public void getUniqueSkipNumWithZeroNum() {
		String input = 	"word1 word2 word3" + NEW_LINE +
		   	       		"word1+ word2 word3" + NEW_LINE +
		   	       		"word1 word2";
		
		String expectedOutput = 
				"word1 word2 word3" + NEW_LINE +
   	       		"word1+ word2 word3" + NEW_LINE +
   	       		"word1 word2";
		assertEquals(expectedOutput, uniqTool.getUniqueSkipNum(0, false, input));
	}
	
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
	 * Positive test
	 * getUniqueSkipNum() with input = ""
	 */
	public void getUniqueSkipNumWithEmptyInput() {
		assertEquals("",uniqTool.getUniqueSkipNum(5, false, ""));
	}
	
	@Corrected
	@Test
	/**
	 * Negative test
	 * getUniqueSkipNum() with input = null
	 */
	public void getUniqueSkipNumWithNullInput() {
		assertEquals("",uniqTool.getUniqueSkipNum(5, false, null));
	}
	
	@Test
	/**
	 * Positive test
	 * execute()
	 */
	public void executeWithInputFile() {
		String[] args = {INPUT_FILE_PATH};
		uniqTool = new UniqTool(args);
	
		String expectedOutput = 
			"word1 word2 word3" + NEW_LINE +
			"word1+ word2 word3";
	
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
	}
	
	
	@Test
	/**
	 * Positive test
	 * execute() with no argument -> default to standard input
	 */
	public void executeWithNoArgument() {
		String input = 	"word1 word2 word3" + NEW_LINE +
				   		"word1 word2 word3" + NEW_LINE +
				   		"word1 word2";
	
		String expectedOutput = 
			"word1 word2 word3" + NEW_LINE +
			"word1 word2";
	
		assertEquals(expectedOutput, uniqTool.execute(new File(""), input));
	}
	
	@Test
	/**
	 * Positive test
	 * execute() with option -i
	 */
	public void executeWithCaseSensitvity() {
		String[] args = {"-i"};
		uniqTool = new UniqTool(args);
		
		String input = 	
				"word1 word2 word3" + NEW_LINE +
		   		"wOrd1 word2 word3" + NEW_LINE +
		   		"word1 word2";
		String expectedOutput = 	
				"word1 word2 word3" + NEW_LINE +
		   		"wOrd1 word2 word3" + NEW_LINE +
		   		"word1 word2";
		assertEquals(expectedOutput, uniqTool.execute(new File(""), input));
	}
	
	@Test
	/**
	 * Positive test
	 * execute() with standard input
	 */
	public void executeWithStandardInput() {
		String[] args = {"-"};
		uniqTool = new UniqTool(args);
		
		String input = 	"word1 word2 word3" + NEW_LINE +
				   		"word1 word2 word3" + NEW_LINE +
				   		"word1 word2";
	
		String expectedOutput = 
			"word1 word2 word3" + NEW_LINE +
			"word1 word2";
		
		assertEquals(expectedOutput, uniqTool.execute(new File(""), input));
	}

	@Test
	/**
	 * Positive test
	 * execute() with option -help
	 */
	public void executeWithHelpOption() {
		String[] args = {"-help"};
		uniqTool = new UniqTool(args);
		
		String expectedOutput = uniqTool.getHelp();
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
	}
	
	@Test
	/**
	 * Negative test
	 * execute() with file input that does not exist
	 */
	public void executeWithInvalidFileInput() {
		String[] args = {"_invalid_file_"};
		uniqTool = new UniqTool(args);
		
		String expectedOutput = String.format(UniqTool.ERR_MSG_FILE_NOT_FOUND, args[0]);
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
		assertEquals(UniqTool.ERR_CODE_FILE_NOT_FOUND, uniqTool.getStatusCode());
	}
	
	@Test
	/**
	 * Negative test
	 * execute() with missing integer after -f option
	 */
	public void executeWithMissingNum() {
		String[] args = {"-f"};
		uniqTool = new UniqTool(args);
		
		String expectedOutput = String.format(UniqTool.ERR_MSG_MISSING_OPTION_ARG, args[0]);
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
		assertEquals(UniqTool.ERR_CODE_MISSING_OPTION_ARG, uniqTool.getStatusCode());
	}

	@Test
	/**
	 * Negative test
	 * execute() with NUM < 0
	 */
	public void executeWithNegativeNum() {
		String[] args = {"-f", "-1" };
		uniqTool = new UniqTool(args);
		
		String expectedOutput = String.format(UniqTool.ERR_MSG_INVALID_NUM_OF_FIELDS, args[1]);
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
		assertEquals(UniqTool.ERR_CODE_INVALID_NUM_OF_FIELDS, uniqTool.getStatusCode());
	}
	
	@Test
	/**
	 * Negative test
	 * execute() with NUM not an int
	 */
	public void executeWithNumNotAnInt() {
		String[] args = {"-f", "a" };
		uniqTool = new UniqTool(args);
		
		String expectedOutput = String.format(UniqTool.ERR_MSG_INVALID_NUM_OF_FIELDS, args[1]);
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
		assertEquals(UniqTool.ERR_CODE_INVALID_NUM_OF_FIELDS, uniqTool.getStatusCode());
	}
	
	@Test
	/**
	 * Negative test
	 * execute() with extra operand
	 */
	public void executeWithExtraOperand() {
		String[] args = {"-", "-", "-"};
		uniqTool = new UniqTool(args);
		
		String expectedOutput = String.format(UniqTool.ERR_MSG_EXTRA_OPERAND, args[2]);
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
		assertEquals(UniqTool.ERR_CODE_EXTRA_OPERAND, uniqTool.getStatusCode());
	}
	
	@Test
	/**
	 * Negative test
	 * execute() with invalid option
	 */
	public void executeWithInvalidOption() {
		String[] args = {"-a"};
		uniqTool = new UniqTool(args);
		
		String expectedOutput = String.format(UniqTool.ERR_MSG_INVALID_OPTION, args[0]);
		assertEquals(expectedOutput, uniqTool.execute(new File(""), ""));
		assertEquals(UniqTool.ERR_CODE_INVALID_OPTION, uniqTool.getStatusCode());
	}
	
	
	/**
	 * Helper method to create temporary file
	 * @param path
	 * @param content
	 */
	public void createFile(String path, String content) {
		uniqTool.writeToFile(path, content);
	}
	
	/**
	 * Helper method to delete temporary file
	 * @param path
	 */
	public void deleteFile(String path) {
		File file = new File(path);
		if (!file.delete()) {
			System.err.println(path + " deletion fails.");
			System.err.println("Please delete the file manually.");
		}
	}
	
}
