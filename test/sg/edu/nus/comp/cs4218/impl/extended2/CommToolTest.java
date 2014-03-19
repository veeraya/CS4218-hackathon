package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;

public class CommToolTest {

	private static ICommTool commTool;
	private final String COMM_HELP_MESSAGE = "comm : Compares two sorted files "
			+ "line by line. With no options, produce three-column "
			+ "output. Column one contains lines unique to FILE1, "
			+ "column two contains lines unique to FILE2, and column "
			+ "three contains lines common to both files."
			+ System.lineSeparator()
			+ "Command Format - comm [OPTIONS] FILE1 FILE2"
			+ System.lineSeparator()
			+ "FILE1 - Name of the file 1"
			+ System.lineSeparator()
			+ "FILE2 - Name of the file 2"
			+ System.lineSeparator()
			+ "-c : check that the input is correctly sorted, even if "
			+ "all input lines are pairable"
			+ System.lineSeparator()
			+ "-d : do not check that the input is correctly sorted"
			+ System.lineSeparator()
			+ "-help : Brief information about supported options";

	public static void writeFile(String fileName, String s) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write(s);
		out.close();
	}

	@BeforeClass
	public static void initialize() throws IOException {

		// creating testFile 1 & 2 in sorted order
		File myFile1 = new File("testFile1.txt");
		myFile1.createNewFile();
		writeFile("testFile1.txt", "aaa" + System.lineSeparator() + "bbb"
				+ System.lineSeparator() + "ccc" + System.lineSeparator()
				+ "ddd");

		File myFile2 = new File("testFile2.txt");
		myFile2.createNewFile();
		writeFile("testFile2.txt", "aaf" + System.lineSeparator() + "abb"
				+ System.lineSeparator() + "ccc" + System.lineSeparator()
				+ "fff");

		// testFile 3 will be the file in unsorted order
		File myFile3 = new File("testFile3.txt");
		myFile3.createNewFile();
		writeFile("testFile3.txt", "zzz" + System.lineSeparator() + "ccc"
				+ System.lineSeparator() + "aaa" + System.lineSeparator()
				+ "bbb");

		// testFile 4 & 5 will be empty file
		File myFile4 = new File("testFile4.txt");
		myFile4.createNewFile();
		writeFile("testFile4.txt", "");

		File myFile5 = new File("testFile5.txt");
		myFile5.createNewFile();
		writeFile("testFile5.txt", "");
	}

	@AfterClass
	public static void deInitialize() {

		// creating testFile 1 & 2 in sorted order
		File myFile1 = new File("testFile1.txt");
		myFile1.delete();

		File myFile2 = new File("testFile2.txt");
		myFile2.delete();
		// testFile 3 will be the file in unsorted order
		File myFile3 = new File("testFile3.txt");
		myFile3.delete();
		// testFile 4 & 5 will be empty file
		File myFile4 = new File("testFile4.txt");
		myFile4.delete();

		File myFile5 = new File("testFile5.txt");
		myFile5.delete();
	}

	@Before
	public void before() throws IOException {
		commTool = new CommTool(null);
	}

	@After
	public void after() {
		commTool = null;
	}

	/*
	 * ========================================================================
	 * The Following test method verify for the correctness of the core function
	 * of the class
	 * ========================================================================
	 */

	// test for comm File1 File2; both files are in sorted order
	@Corrected
	// Swap result and expected, change to line separator
	@Test
	public void compareFilesSortedFile() {
		String result = commTool.compareFiles("testFile1.txt", "testFile2.txt");
		assertEquals(
				"aaa" + System.lineSeparator() + "\taaf"
						+ System.lineSeparator() + "\tabb"
						+ System.lineSeparator() + "bbb"
						+ System.lineSeparator() + "\t\tccc"
						+ System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "\tfff"
						+ System.lineSeparator() + "", result);
	}

	// test for comm File1 File2; both files are not in sorted order
	@Corrected
	// Swap result and expected
	@Test
	public void compareFilesNotSortedFile() {
		String result = commTool.compareFiles("testFile1.txt", "testFile3.txt");
		assertEquals(
				"aaa" + System.lineSeparator() + "bbb" + System.lineSeparator()
						+ "ccc" + System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "\tzzz"
						+ System.lineSeparator() + ""
						+ "comm: file 2 is not in sorted order"
						+ System.lineSeparator() + "\tccc"
						+ System.lineSeparator() + "\taaa"
						+ System.lineSeparator() + "\tbbb"
						+ System.lineSeparator() + "", result);
	}

	// with options -d and File is sorted order
	@Corrected
	// Swap result and expected
	@Test
	public void compareFilesDoNotCheckSortStatusSortedFile() throws IOException {
		String result = commTool.compareFilesDoNotCheckSortStatus(
				"testFile1.txt", "testFile2.txt");
		assertEquals(
				"aaa" + System.lineSeparator() + "\taaf"
						+ System.lineSeparator() + "\tabb"
						+ System.lineSeparator() + "bbb"
						+ System.lineSeparator() + "\t\tccc"
						+ System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "\tfff"
						+ System.lineSeparator() + "", result);
	}

	// when options is -d but file is not sorted in order
	@Corrected
	// Swap result and expected
	@Test
	public void compareFilesDoNotCheckSortStatusNotSortedFile()
			throws IOException {
		String result = commTool.compareFilesDoNotCheckSortStatus(
				"testFile1.txt", "testFile3.txt");
		assertEquals(
				"aaa" + System.lineSeparator() + "bbb" + System.lineSeparator()
						+ "ccc" + System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "\tzzz"
						+ System.lineSeparator() + "\tccc"
						+ System.lineSeparator() + "\taaa"
						+ System.lineSeparator() + "\tbbb"
						+ System.lineSeparator() + "", result);

	}

	// with options -c and file is sorted order
	@Corrected
	// Swap result and expected
	@Test
	public void compareFilesCheckSortStatusSortedFile() throws IOException {

		String result = commTool.compareFilesCheckSortStatus("testFile1.txt",
				"testFile2.txt");
		assertEquals(
				"aaa" + System.lineSeparator() + "\taaf"
						+ System.lineSeparator() + "\tabb"
						+ System.lineSeparator() + "bbb"
						+ System.lineSeparator() + "\t\tccc"
						+ System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "\tfff"
						+ System.lineSeparator() + "", result);
	}

	// when options is -c but file is not sorted in order
	@Corrected
	// Swap result and expected
	@Test
	public void compareFilesCheckSortStatusNotSortedFile() throws IOException {
		String result = commTool.compareFilesCheckSortStatus("testFile1.txt",
				"testFile3.txt");
		assertEquals(
				"aaa" + System.lineSeparator() + "bbb" + System.lineSeparator()
						+ "ccc" + System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "\tzzz"
						+ System.lineSeparator() + ""
						+ "comm: file 2 is not in sorted order"
						+ System.lineSeparator() + "", result);
	}

	// Empty File Test
	@Corrected
	// Wrong behavior from previous TDD test case, result should be empty string
	// and not next line.
	@Test
	// FIXME: Bug: method should not have returned null.
	public void compareFile() throws IOException {
		String result = commTool.compareFilesDoNotCheckSortStatus(
				"testFile4.txt", "testFile5.txt");
		assertEquals(new String(), result);
	}

	@Corrected
	// Swap result and expected, change the output of the help message
	@Test
	public void getHelp() throws IOException {
		assertEquals(COMM_HELP_MESSAGE, commTool.getHelp());
	}

	/*
	 * ==========================================================================
	 * ========================== The Following test method verify for the error
	 * check of the arguments/file not exist etc ...
	 * ============================
	 * ========================================================================
	 */

	@Corrected
	// No new line after error message
	// Swap result and expected
	@Test
	public void withoutOptionsfileNotExist() throws IOException {
		String[] argument2 = { "notExist.txt", "testFile2.txt" };
		commTool = new CommTool(argument2);
		File myFile = new File("user.dir");
		String result2 = commTool.execute(myFile, null);
		assertEquals("comm: notExist.txt: No such file or directory", result2);
	}

	@Corrected
	// Split test case into individual testing
	@Test
	public void execute_WithoutOptionsSecondfileNotExist_FileNotExistError()
			throws IOException {
		String[] argument = { "testFile1.txt", "notExist.txt" };
		commTool = new CommTool(argument);
		File myFile = new File("user.dir");
		String result = commTool.execute(myFile, null);
		assertEquals("comm: notExist.txt: No such file or directory", result);
	}

	// Though it seem repetitive as compared to the above test getHelp(), it
	// re-affirm the argument that is being entered to execute the getHelp
	// function
	// Example, "comm help" won't works ; "comm -help" works
	@Corrected
	// Swap result and expected
	@Test
	public void printHelp() throws IOException {
		String[] argument = { "-help" };
		commTool = new CommTool(argument);
		File myFile = new File("user.dir");
		String result = commTool.execute(myFile, null);
		assertEquals(commTool.getHelp(), result);
	}

	@Corrected
	// Swap result and expected
	// Split the test case into smaller individual testing
	@Test
	public void withCorrectOptionsFileNotExist() throws IOException {

		String[] argument = { "-c", "testFile1.txt", "notExist.txt" };

		commTool = new CommTool(argument);

		File myFile = new File("user.dir");
		String result = commTool.execute(myFile, null);
		assertEquals("comm: notExist.txt: No such file or directory", result);

		String[] argument4 = { "-d", "testFile1.txt", "notExist.txt" };

		commTool = new CommTool(argument4);
		String result4 = commTool.execute(new File("user.dir"), null);
		assertEquals("comm: notExist.txt: No such file or directory", result4);
	}

	@Corrected
	// Swap result and expected
	// Split test case into individual test case
	@Test
	public void optionsErrorFileExisted() throws IOException {

		String[] argument = { "unreadable", "testFile1.txt", "testFile2.txt" };
		commTool = new CommTool(argument);
		String result = commTool.execute(new File("user.dir"), null);
		assertEquals("comm: extra operand '" + "testFile2.txt" + "'" + ""
				+ System.lineSeparator() + ""
				+ "Try comm '-help' for more information", result);
	}

	@Corrected
	// Swap result and expected
	@Test
	public void optionsErrorFileNotExist() throws IOException {

		String[] argument = { "unreadable", "notExist.txt", "notExist.txt" };

		commTool = new CommTool(argument);
		String result = commTool.execute(new File("user.dir"), null);
		assertEquals("comm: extra operand '" + "notExist.txt" + "'" + ""
				+ System.lineSeparator() + ""
				+ "Try comm '-help' for more information", result);

	}

	@Corrected
	// Swap result and expected
	@Test
	public void unknowOptionsFileNotExist() throws IOException {

		String[] argument = { "unreadable", "notExist.txt", "notExist.txt" };

		commTool = new CommTool(argument);
		String result = commTool.execute(new File("user.dir"), null);
		assertEquals("comm: extra operand '" + "notExist.txt" + "'" + ""
				+ System.lineSeparator() + ""
				+ "Try comm '-help' for more information", result);

	}

	@Corrected
	// No new line after error message
	@Test
	public void moreThanThreeArgument() throws IOException {

		String[] argument = { "notExist1.txt", "notExist2.txt",
				"notExist3.txt", "notExist4.txt" };

		commTool = new CommTool(argument);
		String result = commTool.execute(new File("user.dir"), null);
		assertEquals("comm: extra operand '" + "notExist3.txt" + "'" + ""
				+ System.lineSeparator() + ""
				+ "Try comm '-help' for more information", result);

	}

	@Corrected
	// No new line after error message
	@Test
	public void noArgument() throws IOException {

		String[] argument = {};

		commTool = new CommTool(argument);
		String result = commTool.execute(new File("user.dir"), null);
		assertEquals("comm: missing operand " + System.lineSeparator() + ""
				+ "Try comm '-help' for more information", result);

	}

}
