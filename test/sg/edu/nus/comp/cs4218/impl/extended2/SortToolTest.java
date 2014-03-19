package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;

public class SortToolTest {
	// TODO Always test against the interface!
	private static ISortTool sortTool;
	
	final String SORT_HELP = "sort : sort lines of text files"
			+ System.lineSeparator() + "Command Format - sort [OPTIONS] [FILE]"
			+ System.lineSeparator() + "FILE - Name of the file"
			+ System.lineSeparator() + "-c : Check whether the given file is"
			+ " already sorted, if it is not all sorted, print a diagnostic"
			+ " containing the first line that is out of order"
			+ System.lineSeparator()
			+ "-help : Brief information about supported options";

	public static void writeFile(String fileName, String s) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write(s);
		out.close();
	}

	@BeforeClass
	public static void initialize() throws IOException {

		// creating testFile of sorted and unsorted order
		File myFile1 = new File("unSortFile.txt");
		myFile1.createNewFile();
		writeFile("unSortFile.txt", "zzz\r\nbbb\r\naaa\r\nggg\r\nfff");

		// creating testFile 1/2 with sorted order
		File myFile2 = new File("sortFile.txt");
		myFile2.createNewFile();
		writeFile("sortFile.txt", "aaa\r\nbbb\r\nccc\r\nddd\r\neee");

	}

	@Before
	public void before() throws IOException {
		sortTool = new SortTool(null);
	}

	@After
	public void after() {
		sortTool = null;
	}

	@AfterClass
	public static void method() {

		File file1 = new File("sortFile.txt");
		if (file1.exists()) {
			file1.delete();
		}

		File file2 = new File("unSortFile.txt");
		if (file2.exists()) {
			file2.delete();
		}
	}

	/*
	 * =========================================================================
	 * The Following test method verify for the correctness of the core function
	 * of the class
	 * =========================================================================
	 */

	@Corrected
	// Swap result and expected
	// No new line at result
	// Change the "\n" to System.lineSeparator
	@Test
	public void sortFileTestForUnsortedFile() {
		String result = sortTool.sortFile("unSortFile.txt");
		assertEquals(
				"aaa" + System.lineSeparator() + "bbb" + System.lineSeparator()
						+ "fff" + System.lineSeparator() + "ggg"
						+ System.lineSeparator() + "zzz", result);
	}

	@Corrected
	// Swap result and expected
	// No new line at result
	// Wrong output
	@Test
	public void checkIfSortedTestForUnsortedFile() {
		String result = sortTool.checkIfSorted("unSortFile.txt");
		assertEquals("sort: unSortFile.txt:2: disorder: bbb", result);
	}

	@Corrected
	// Swap result and expected
	// No new line at result
	// Use System.lineSeparator instead of "\n"
	@Test
	public void sortFileTestForSortedFile() {
		String result = sortTool.sortFile("sortFile.txt");
		assertEquals(
				"aaa" + System.lineSeparator() + "bbb" + System.lineSeparator()
						+ "ccc" + System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "eee", result);
	}

	@Test
	public void checkIfSortedTestForSortedFile() {
		String result = sortTool.checkIfSorted("sortFile.txt");
		assertEquals(result, "");
	}

	@Corrected
	//Change the output of the help message
	@Test
	public void getHelpTest() {
		assertEquals(SORT_HELP, sortTool.getHelp());
	}
	
	@Test
	public void checkIfSorted_InvalidFile_FileNotFoundError(){
		assertEquals(
				"sort: open failed: notExist.txt: No such file or directory",
				sortTool.checkIfSorted("notExist.txt"));
	}
	
	@Test
	public void sortFile_InvalidFile_FileNotFoundError(){
		assertEquals(
				"sort: open failed: notExist.txt: No such file or directory",
				sortTool.sortFile("notExist.txt"));
	}
	
	/**
	 * Helper class
	 */
//	@Test
//	public void sortMultipleFiles_InvalidFile_FileNotFoundError(){
//		SortTool concreteSortTool = new SortTool(null);
//		
//		List<String> fileList = new Vector<>();
//		fileList.add("notExist.txt");
//		
//		assertEquals(
//				"sort: open failed: notExist.txt: No such file or directory",
//				concreteSortTool.sortMultipleFiles(fileList));
//	}

	/*
	 * ======================================================================
	 * The Following test method verify for the error check of the
	 * arguments/ file not exist etc ...
	 * ======================================================================
	 */

	@Test
	public void fileNotExist() throws IOException {

		String[] argument = { "notExist.txt" };

		sortTool = new SortTool(argument);

		File myFile = new File("user.dir");
		String result = sortTool.execute(myFile, null);
		assertEquals(
				"sort: open failed: notExist.txt: No such file or directory",
				result);
	}

	@Corrected
	// Swap result and expected
	// Split the test case into individual test case
	@Test
	public void execute_FileNotExistWithOption_OpenFailError() {
		String[] argument2 = { "-c", "notExist.txt" };

		sortTool = new SortTool(argument2);

		String result2 = sortTool.execute(new File("user.dir"), null);
		assertEquals(
				"sort: open failed: notExist.txt: No such file or directory",
				result2);
	}

	@Corrected
	// Swap result and expected
	// Split the test case into individual test case
	@Test
	public void invalidOptionFileExist() throws IOException {

		String[] argument1 = { "unknown", "sortFile.txt" };

		sortTool = new SortTool(argument1);

		String result = sortTool.execute(new File("user.dir"), null);
		assertEquals("sort: open failed: unknown: No such file or directory",
				result);
	}

	@Corrected
	//Use System.lineSeparator instead of \n
	@Test
	public void execute_InvalidOption_OptionError() {
		String[] argument2 = { "-unknown", "sortFile.txt" };

		sortTool = new SortTool(argument2);
		String result2 = sortTool.execute(new File("user.dir"), null);

		assertEquals("sort: unknown option -- u" + System.lineSeparator()
				+ "Try 'sort -help' for more information", result2);
	}

	@Corrected
	// Swap result and expected
	// Split the test case into individual test case
	@Test
	public void invalidArguments() throws IOException {

		String[] argument1 = { "unknown", "unknown" };

		sortTool = new SortTool(argument1);

		String result = sortTool.execute(new File("user.dir"), null);
		assertEquals("sort: open failed: unknown: No such file or directory",
				result);
	}

	@Test
	public void execute_3InvalidFile_OpenFailError() {
		String[] argument2 = { "unknown", "unknown", "unknown" };

		sortTool = new SortTool(argument2);

		String result2 = sortTool.execute(new File("user.dir"), null);
		assertEquals("sort: open failed: unknown: No such file or directory",
				result2);
	}

	@Corrected
	//Use System.lineSeparator instead of \n
	@Test
	public void nullArguments() throws IOException {

		String[] argument = {};

		sortTool = new SortTool(argument);

		String result = sortTool.execute(new File("user.dir"), null);
		assertEquals("sort: missing operand "+ System.lineSeparator()
				+ "Try 'sort -help' for more information", result);
	}
}
