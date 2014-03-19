package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasteToolTest {
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
	
	@Corrected
	/**
	 * For serial, the default delimiter is tab rather than space.
	 */
	@Test
	/**
	 * Positive test
	 * pasteSerial() with multiple strings
	 */
	public void pasteSerial_ArrayWithMultipleString_StatusCodeZero() {
		String[] input = { "quorra", "1", "2" };
		String output = "quorra\t1\t2";

		assertEquals(output, pasteTool.pasteSerial(input));
		assertEquals(0, pasteTool.getStatusCode());
	}
	
	@Corrected
	/**
	 * The delimiter should not be included after the last string in the input
	 */
	@Test
	/**
	 * Positive test
	 * pasteUseDelimiter() with delimiter is a single char
	 */
	public void pasteUseDelimiter_SingleDelimiter_StatusCodeZero() {
		String[] input = { "1", "2", "3", "4", "5", "6" };
		String output = "1;2;3;4;5;6";

		assertEquals(output, pasteTool.pasteUseDelimiter(";", input));
		assertEquals(0, pasteTool.getStatusCode());
	}
	
	@Corrected
	/**
	 * The delimiter should not be included after the last string in the input
	 */
	@Test
	/**
	 * Positive test
	 * pasteUseDelimiter() with multiple delimiters
	 */
	public void pasteUseDelimiter_MultipleDelimiter_StatusCodeZero() {
		String[] input = { "1", "2", "3", "4", "5", "6" };
		String output = "1|2.3;4|5.6";

		assertEquals(output, pasteTool.pasteUseDelimiter("|.;", input));
		assertEquals(0, pasteTool.getStatusCode());
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