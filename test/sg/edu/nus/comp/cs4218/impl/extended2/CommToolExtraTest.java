package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;

public class CommToolExtraTest {
	private ICommTool commTool;

	private static final String USER_DIRECTORY = "user.dir";
	private static File defaultWorkingDirectory;
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

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		defaultWorkingDirectory = new File(System.getProperty(USER_DIRECTORY));

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

		//Empty file
		File myFile3 = new File("testFile3.txt");
		myFile3.createNewFile();

		// testFile 4 & 5 will be the file in unsorted order
		File myFile4 = new File("testFile4.txt");
		myFile4.createNewFile();
		writeFile("testFile4.txt", "zzz" + System.lineSeparator() + "ccc"
				+ System.lineSeparator() + "aaa" + System.lineSeparator()
				+ "bbb");


		File myFile5 = new File("testFile5.txt");
		myFile5.createNewFile();
		writeFile("testFile5.txt", "aaa" + System.lineSeparator() + "ccc"
				+ System.lineSeparator() + "aab" + System.lineSeparator()
				+ "ccc" + System.lineSeparator() + "aaa");
	}

	@AfterClass
	public static synchronized void tearDownAfterClass() throws Exception {
		// creating testFile 1 & 2 in sorted order
		File myFile1 = new File("testFile1.txt");
		Files.delete(myFile1.toPath());

		File myFile2 = new File("testFile2.txt");
		Files.delete(myFile2.toPath());

		File myFile3 = new File("testFile3.txt");
		Files.delete(myFile3.toPath());

		File myFile4 = new File("testFile4.txt");
		Files.delete(myFile4.toPath());

		File myFile5 = new File("testFile5.txt");
		Files.delete(myFile5.toPath());
	}

	@Before 
	public void setUp() throws Exception {
		commTool = new CommTool(null);
	}

	@After
	public void tearDown() throws Exception {
		commTool = null;
	}

	public static void writeFile(String fileName, String s) {
		FileWriter out;
		try {
			out = new FileWriter(fileName);
			out.write(s);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void nothing(){

	}

	@Corrected
	// Swap result and expected
	@Test
	public void compareFiles_BothFileNotSorted_2ErrorShownInCorrectOrder() {
		String result = commTool.compareFiles("testFile4.txt", "testFile5.txt");
		assertEquals(
				"\taaa" + System.lineSeparator() + "\tccc"
						+ System.lineSeparator()
						+ "comm: file 2 is not in sorted order"
						+ System.lineSeparator() + "\taab"
						+ System.lineSeparator() + "\tccc"
						+ System.lineSeparator() + "\taaa"
						+ System.lineSeparator() + "zzz"
						+ System.lineSeparator()
						+ "comm: file 1 is not in sorted order"
						+ System.lineSeparator() + "ccc"
						+ System.lineSeparator() + "aaa"
						+ System.lineSeparator() + "bbb"
						+ System.lineSeparator(), result);
	}

	@Test
	public void execute_ShowHelpMessage_HelpShownOnOutput() {
		String[] argument = { "-help" };
		commTool = new CommTool(argument);
		File myFile = new File("user.dir");
		String result = commTool.execute(myFile, null);
		assertEquals(COMM_HELP_MESSAGE, result);
	}

	@Test
	public void execute_MinusCCorrectOptionSecondFileNotExists_NotSuchFileError() {
		String[] argument2 = { "-c", "testFile1.txt", "notExist.txt" };
		File myFile = new File("user.dir");
		commTool = new CommTool(argument2);
		String result2 = commTool.execute(myFile, null);
		assertEquals("comm: notExist.txt: No such file or directory", result2);
	}

	@Test
	public void execute_MinusDCorrectOptionSecondFileNotExists_NotSuchFileError() {
		String[] argument3 = { "-d", "testFile1.txt", "notExist.txt" };
		commTool = new CommTool(argument3);
		String result3 = commTool.execute(new File("user.dir"), null);
		assertEquals("comm: notExist.txt: No such file or directory", result3);
	}

	@Test
	public void execute_unknowOption_UnknownOptionError() {
		String[] argument2 = { "-unreadable", "testFile1.txt", "testFile2.txt" };

		commTool = new CommTool(argument2);
		String result2 = commTool.execute(new File("user.dir"), null);
		assertEquals(
				"comm: unknown option -- unreadable" + ""
						+ System.lineSeparator()
						+ "Try comm '-help' for more information", result2);
	}

	@Test
	public void execute_1stFileWrongMissing2ndFile_MissingOperand() {
		String[] argument = { "notExist.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"comm: missing operand after 'notExist.txt'"
						+ System.lineSeparator()
						+ "Try comm '-help' for more information",
						commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_NoOptionPositive_Output() {
		String[] argument = { "testFile1.txt", "testFile2.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"aaa" + System.lineSeparator() + "\taaf"
						+ System.lineSeparator() + "\tabb"
						+ System.lineSeparator() + "bbb"
						+ System.lineSeparator() + "\t\tccc"
						+ System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "\tfff"
						+ System.lineSeparator(),
						commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_1Option1FileOnly_MissingOperand() {
		String[] argument = { "-c", "testFile1.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"comm: missing operand after 'testFile1.txt'"
						+ System.lineSeparator()
						+ "Try comm '-help' for more information",
						commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_1Option1stFileWrong_NoSuchFile() {
		String[] argument = { "-c", "NotExist.txt", "testFile1.txt" };
		commTool = new CommTool(argument);
		assertEquals("comm: NotExist.txt: No such file or directory",
				commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_HelpOption2CorrectFile_HelpMessage() {
		String[] argument = { "-help", "testFile1.txt", "testFile2.txt" };
		commTool = new CommTool(argument);
		assertEquals(COMM_HELP_MESSAGE,
				commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_3OptionFile1Only_MissingOperand() {
		String[] argument = { "-c", "-d", "-c", "testFile1.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"comm: missing operand after 'testFile1.txt'"
						+ System.lineSeparator()
						+ "Try comm '-help' for more information",
						commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_4Options1stFileInvalid_NoSuchFile() {
		String[] argument = { "-c", "-c", "-c", "-c", "NotExist.txt",
		"testFile1.txt" };
		commTool = new CommTool(argument);
		assertEquals("comm: NotExist.txt: No such file or directory",
				commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_5Options2ndFileInvalid_NoSuchFile() {
		String[] argument = { "-c", "-d", "-c", "-d", "-c", "testFile1.txt",
		"NotExist.txt" };
		commTool = new CommTool(argument);
		assertEquals("comm: NotExist.txt: No such file or directory",
				commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_HelpWithOtherOption_InvalidOption() {
		String[] argument = { "-helpc", "testFile1.txt", "testFile2.txt" };
		commTool = new CommTool(argument);
		assertEquals("comm: unknown option -- helpc" + System.lineSeparator()
				+ "Try comm '-help' for more information",
				commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	// TODO:Feature not implemented for now
	public void execute_2SameFile_Output() {
		String[] argument = { "testFile1.txt", "testFile1.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"\t\taaa" + System.lineSeparator() + "\t\tbbb"
						+ System.lineSeparator() + "\t\tccc"
						+ System.lineSeparator() + "\t\tddd"
						+ System.lineSeparator(),
						commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_1EmptyFile_Output() {
		String[] argument = { "testFile1.txt", "testFile3.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"aaa" + System.lineSeparator() + "bbb"
						+ System.lineSeparator() + "ccc"
						+ System.lineSeparator() + "ddd"
						+ System.lineSeparator(),
						commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_MinusCPositiveTest_Output() {
		String[] argument = { "-c", "testFile4.txt", "testFile5.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"\taaa" + System.lineSeparator() + "\tccc"
						+ System.lineSeparator()
						+ "comm: file 2 is not in sorted order"
						+ System.lineSeparator(),
						commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_MinusDPositiveTest_Output() {
		String[] argument = { "-d", "testFile5.txt", "testFile4.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"aaa" + System.lineSeparator() + "ccc" + System.lineSeparator()
				+ "aab" + System.lineSeparator() + "ccc"
				+ System.lineSeparator() + "aaa"
				+ System.lineSeparator() + "\tzzz"
				+ System.lineSeparator() + "\tccc"
				+ System.lineSeparator() + "\taaa"
				+ System.lineSeparator() + "\tbbb"
				+ System.lineSeparator(),
				commTool.execute(defaultWorkingDirectory, null));
	}

	@Test
	public void execute_2OptionsPositiveTest_Outout() {
		String[] argument = { "-c", "-d", "testFile5.txt", "testFile4.txt" };
		commTool = new CommTool(argument);
		assertEquals(
				"aaa" + System.lineSeparator() + "ccc" + System.lineSeparator()
				+ "aab" + System.lineSeparator() + "ccc"
				+ System.lineSeparator() + "aaa"
				+ System.lineSeparator() + "\tzzz"
				+ System.lineSeparator() + "\tccc"
				+ System.lineSeparator() + "\taaa"
				+ System.lineSeparator() + "\tbbb"
				+ System.lineSeparator(),
				commTool.execute(defaultWorkingDirectory, null));
	}
}
