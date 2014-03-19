/**
 * Additional unit tests on top of the original
 * test cases for test driven development.
 */

package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasteToolExtraTest {
	private PasteTool pasteTool;
	private static final String NEW_LINE = System.lineSeparator();
	private static final String INPUT_FILE_PATH_1 = "__test__input__1__";
	private static final String INPUT_FILE_PATH_2 = "__test__input__2__";
	private static final String INPUT_FILE_PATH_3 = "__test__input__3__";

	@Before
	public void before() {
		pasteTool = new PasteTool(null);
		String content1 = "1" + NEW_LINE + "2";
		String content2 = "a" + NEW_LINE + "b" + NEW_LINE + "c";
		String content3 = "1" + NEW_LINE + "2" + NEW_LINE + "3" + NEW_LINE
				+ "4";

		createFile(INPUT_FILE_PATH_1, content1);
		createFile(INPUT_FILE_PATH_2, content2);
		createFile(INPUT_FILE_PATH_3, content3);
	}

	@After
	public void after() {
		pasteTool = null;

		deleteFile(INPUT_FILE_PATH_1);
		deleteFile(INPUT_FILE_PATH_2);
		deleteFile(INPUT_FILE_PATH_3);
	}

	@Test
	/**
	 * Negative test
	 * pasteSerial() with null arg
	 */
	public void pasteSerial_NullArgument_StatusCodeZero() {
		assertEquals("", pasteTool.pasteSerial(null));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Negative test
	 * pasteSerial() with null string in the arary
	 */
	public void pasteSerial_ArrayWithNullString_StatusCodeZero() {
		assertEquals("", pasteTool.pasteSerial(new String[4]));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Negative test
	 * pasteSerial() with empty string in the arary
	 */
	public void pasteSerial_ArrayWithEmptyString_StatusCodeZero() {
		String[] input = { "", "" };
		assertEquals("\t", pasteTool.pasteSerial(input));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * pasteSerial() with one string in the input array
	 */
	public void pasteSerial_ArrayWithOneString_StatusCodeZero() {
		String[] input = { "quorra" };
		assertEquals("quorra", pasteTool.pasteSerial(input));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * pasteSerial() with some empty string in the array
	 */
	public void pasteSerial_ArrayWithMultipleAndEmptyString_StatusCodeZero() {
		String[] input = { "", "quorra", "2" };
		String output = "\tquorra\t2";

		assertEquals(output, pasteTool.pasteSerial(input));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Negative test
	 * pasteUseDelimiter() with null delimiter arg
	 */
	public void pasteUseDelimiter_NullDelimiter_StatusCodeZero() {
		String[] input = { "1", "2", "3", "4", "5", "6" };
		String output = "1\t2\t3\t4\t5\t6";

		assertEquals(output, pasteTool.pasteUseDelimiter(null, input));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * pasteUseDelimiter() with delimiter = ""
	 */
	public void pasteUseDelimiter_EmptyStringDelimiter_StatusCodeZero() {
		String[] input = { "1", "2", "3", "4", "5", "6" };
		String output = "123456";

		assertEquals(output, pasteTool.pasteUseDelimiter("", input));
		assertEquals(0, pasteTool.getStatusCode());
	}
	

	

	@Test
	public void pasteUseDelimiter_DelimiterContainsBackslash_StatusCodeZero() {
		String[] input = { "1", "2", "3", "4", "5", "6" };
		String output = "1;2;3;4;5;6";

		assertEquals(output, pasteTool.pasteUseDelimiter("\\;", input));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Negative test
	 * pasteUseDelimiter() with input = null
	 */
	public void pasteUseDelimiter_NullInput_StatusCodeZero() {
		assertEquals("", pasteTool.pasteUseDelimiter("\t", null));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Negative test
	 * execute() with invalid option
	 */
	public void execute_InvalidOption_StatusCodeOne() {
		String[] args = { "-a" };
		pasteTool = new PasteTool(args);

		String expectedOutput = String.format(PasteTool.ERR_MSG_INVALID_OPTION,
				args[0]);

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(PasteTool.ERR_CODE_INVALID_OPTION,
				pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Negative test
	 * execute() with invalid option arg
	 * for example -d option without specifying delimiter
	 */
	public void execute_InvalidOptionArg_StatusCodeTwo() {
		String[] args = { "-d" };
		pasteTool = new PasteTool(args);

		String expectedOutput = String.format(
				PasteTool.ERR_MSG_INVALID_OPTION_ARG, args[0]);

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(PasteTool.ERR_CODE_INVALID_OPTION_ARG,
				pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Negative test
	 * execute() with file that does not exist
	 */
	public void execute_FileNotExists_StatusCodeThree() {
		// Get a file name that does not point to any file
		int label = 0;
		String fileName = "__tmp__" + label;
		File invalidFile = new File(fileName);

		while (invalidFile.exists()) {
			label += 1;
			fileName = "__tmp__" + label;
			invalidFile = new File(fileName);
		}

		String[] args = { fileName };
		pasteTool = new PasteTool(args);

		String expectedOutput = String.format(PasteTool.ERR_MSG_FILE_NOT_EXIST,
				fileName);

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(PasteTool.ERR_CODE_FILE_NOT_EXIST,
				pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * execute() with null arg i.e. no input file given
	 * should redirect input to stdin
	 */
	public void execute_NullArg_StatusCodeZero() {
		String input = "1" + NEW_LINE + "2";
		String expectedOutput = input;

		assertEquals(expectedOutput, pasteTool.execute(new File(""), input));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * execute() with one file input
	 */
	public void execute_OneFile_StatusCodeZero() {
		String[] args = { INPUT_FILE_PATH_1 };
		pasteTool = new PasteTool(args);

		String expectedOutput = pasteTool.readFile(INPUT_FILE_PATH_1,
				Charset.forName("UTF-8"));

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * execute() with two file inputs
	 */
	public void execute_TwoFiles_StatusCodeZero() {
		String[] args = { INPUT_FILE_PATH_1, INPUT_FILE_PATH_2 };
		pasteTool = new PasteTool(args);

		String expectedOutput = "1\ta" + NEW_LINE + "2\tb" + NEW_LINE + "\tc";

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * execute() with three file input
	 */
	public void execute_ThreeFiles_StatusCodeZero() {
		String[] args = { INPUT_FILE_PATH_1, INPUT_FILE_PATH_2,
				INPUT_FILE_PATH_3 };
		pasteTool = new PasteTool(args);

		String expectedOutput = "1\ta\t1" + NEW_LINE + "2\tb\t2" + NEW_LINE
				+ "\tc\t3" + NEW_LINE + "\t\t4";

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * execute() with option -s
	 */
	public void execute_SerialOption_StatusCodeZero() {
		String[] args = { "-s", INPUT_FILE_PATH_1, INPUT_FILE_PATH_2 };
		pasteTool = new PasteTool(args);

		String expectedOutput = "1\t2" + NEW_LINE + "a\tb\tc";

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(0, pasteTool.getStatusCode());

	}

	@Test
	/**
	 * Positive test
	 * execute() with option -d DELIM-LIST 
	 */
	public void execute_DelimOption_StatusCodeZero() {
		String[] args = { "-d", "*;", INPUT_FILE_PATH_1, INPUT_FILE_PATH_2,
				INPUT_FILE_PATH_3 };
		pasteTool = new PasteTool(args);

		String expectedOutput = "1*a;1" + NEW_LINE + "2*b;2" + NEW_LINE
				+ "*c;3" + NEW_LINE + "*;4";

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(0, pasteTool.getStatusCode());

	}

	@Test
	/**
	 * Positive test
	 * With delimiter and serial options
	 * execute() with option -d DELIM-LIST -s
	 */
	public void execute_DelimSerialOption_StatusCodeZero() {
		String[] args = { "-d", "*;", "-s", INPUT_FILE_PATH_1,
				INPUT_FILE_PATH_2, INPUT_FILE_PATH_3 };
		pasteTool = new PasteTool(args);

		String expectedOutput = "1*2" + NEW_LINE + "a*b;c" + NEW_LINE
				+ "1*2;3*4";

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * execute() with option -h option
	 */
	public void execute_HelpOption_StatusCodeZero() {
		String[] args = { "-help" };
		pasteTool = new PasteTool(args);

		String expectedOutput = pasteTool.readFile(PasteTool.FILE_PATH_HELP,
				Charset.forName("UTF-8"));

		assertEquals(expectedOutput, pasteTool.execute(new File(""), ""));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * execute() with option - 
	 */
	public void execute_FromStdin_StatusCodeZero() {
		String[] args = { "-", INPUT_FILE_PATH_2 };
		pasteTool = new PasteTool(args);

		String expectedOutput = "1\ta" + NEW_LINE + "2\tb" + NEW_LINE + "\tc";

		String stdin = "1" + NEW_LINE + "2";

		assertEquals(expectedOutput, pasteTool.execute(new File(""), stdin));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * execute() with multiple - -
	 * Both - will refer to same stdin
	 */
	public void execute_FromMultipleStdin_StatusCodeZero() {
		String[] args = { "-", INPUT_FILE_PATH_2, "-" };
		pasteTool = new PasteTool(args);

		String expectedOutput = "1\ta\t1" + NEW_LINE + "2\tb\t2" + NEW_LINE
				+ "\tc";

		String stdin = "1" + NEW_LINE + "2";

		assertEquals(expectedOutput, pasteTool.execute(new File(""), stdin));
		assertEquals(0, pasteTool.getStatusCode());
	}

	@Test
	/**
	 * Positive test
	 * With delimiter and serial options and standard input options
	 * execute with -option -d DELIM-LIST -s -
	 */
	public void execute_WithOptionsFromStdin_StatusCodeZero() {
		String[] args = { "-d", "*;", "-s", INPUT_FILE_PATH_1, "-",
				INPUT_FILE_PATH_3 };
		pasteTool = new PasteTool(args);

		String input = "a" + NEW_LINE + "b" + NEW_LINE + "c";
		String expectedOutput = "1*2" + NEW_LINE + "a*b;c" + NEW_LINE
				+ "1*2;3*4";

		assertEquals(expectedOutput, pasteTool.execute(new File(""), input));
		assertEquals(0, pasteTool.getStatusCode());
	}

	/**
	 * Helper method to create temporary file
	 * 
	 * @param path
	 * @param content
	 */
	public void createFile(String path, String content) {
		pasteTool.writeToFile(path, content);
	}

	/**
	 * Helper method to delete temporary file
	 * 
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
