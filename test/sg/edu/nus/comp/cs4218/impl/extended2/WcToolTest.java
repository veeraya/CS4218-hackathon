package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;

public class WcToolTest {
	private IWcTool wcTool;
	private File file1, file2, dir;
	private String helpContent;

	@Corrected
	//Change the "\n" to System.lineSeparator()
	@Before
	public void setUp() throws Exception {
		BufferedWriter bw;
		String content;

		// create files with content
		file1 = new File("text1");
		file1.createNewFile();
		content = "this file contains 2 newlines, 17 words and 99 characters"+System.lineSeparator();
		content += "this is text1" + System.lineSeparator();
		content += "created only for wctooltest";
		bw = new BufferedWriter(new FileWriter(file1));
		bw.write(content);
		bw.close();

		file2 = new File("text2");
		file2.createNewFile();
		content = "this file contains 4 newlines, 16 words and 88 characters"+System.lineSeparator();
		content += "testing testing 1 2 3"+System.lineSeparator();
		content += " file!"+System.lineSeparator()+System.lineSeparator();
		bw = new BufferedWriter(new FileWriter(file2));
		bw.write(content);
		bw.close();

		// create directory
		dir = new File("dir");
		dir.mkdir();

		// initialize actual content in help file for wc -help option
		helpContent = new String(Files.readAllBytes(new File("help_files/wc_help").toPath())).trim();

	}

	@Before
	public void before() {
		wcTool = new WcTool(null);
	}

	@After
	public void after() {
		wcTool = null;
	}

	@After
	public void tearDown() throws Exception {
		file1.delete();
		file2.delete();
		dir.delete();
		helpContent = null;
	}


	/**
	 * @Corrected
	 * changed test method name getCharacterCountTest() 
	 * and separated each assertion into a test case which are:
	 * getCharacterCount_StringWithNoNewLine_ReturnCount()
	 * getCharacterCount_StringWithNewLine_ReturnCount()
	 * getCharacterCount_EmptyString_Return0() 
	 */
	@Test
	public void getCharacterCount_StringWithNoNewLine_ReturnCount() {
		String input = "Test haha";
		assertEquals("9", wcTool.getCharacterCount(input));
	}

	@Corrected
	//Change the output to be O.S independent and the "\n" to System.lineSeparator()
	@Test
	public void getCharacterCount_StringWithNewLine_ReturnCount() {
		String input = "Test haha" + System.lineSeparator();
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			assertEquals("11", wcTool.getCharacterCount(input));
		}
		else{
			assertEquals("10", wcTool.getCharacterCount(input));
		}
	}

	@Test
	public void getCharacterCount_EmptyString_Return0() {
		String input = "";
		assertEquals("0", wcTool.getCharacterCount(input));
	}

	/**
	 * @Corrected
	 * changed test method name getCharacterCountForNullTest() 
	 */
	@Test
	public void getCharacterCount_Null_Return0() {
		String input = null;
		assertEquals("0", wcTool.getCharacterCount(input));
	}

	/**
	 * @Corrected
	 * changed test method name getWordCountTest() 
	 * and separated each assertion into a test case which are:
	 * getWordCount_EmptyString_Return0()
	 * getWordCount_StringWithNumber_ReturnCount()
	 * getWordCount_StringWithNewLine_ReturnCount() 
	 * getWordCount_SingleWord_Return1()
	 */
	@Test
	public void getWordCount_EmptyString_Return0() {
		String input = "";
		assertEquals("0", wcTool.getWordCount(input));
	}

	@Test
	public void getWordCount_StringWithNumber_ReturnCount() {
		String input = " Test 4 3 "; // combined with number
		assertEquals("3", wcTool.getWordCount(input));
	}

	@Test
	public void getWordCount_StringWithNewLine_ReturnCount() {
		String input = System.lineSeparator() + " Test 4 3 " + System.lineSeparator();
		assertEquals("3", wcTool.getWordCount(input));
	}

	@Test
	public void getWordCount_SingleWord_Return1() {
		String input = "Test";
		assertEquals("1", wcTool.getWordCount(input));
	}

	/**
	 * @Corrected
	 * changed test method name getWordCountForNullTest() 
	 */
	@Test
	public void getWordCount_Null_Return0() {
		String input = null;
		assertEquals("0", wcTool.getWordCount(input));
	}

	/**
	 * @Corrected
	 * changed test method name getNewLineCountTest() 
	 * and separated each assertion into a test case which are:
	 * getNewLineCount_EmptyString_Return0()
	 * getNewLineCount_StringWithNoNewLine_ReturnCount()
	 * getNewLineCount_StringWithNewLine_ReturnCount() 
	 * 
	 * the input4 ("\nTest\n \r\n \r") in the original test case 
	 * is removed since newline is OS dependent and it asserted for 
	 * 5 counts of newline which is incorrect.
	 */
	@Test
	public void getNewLineCount_EmptyString_Return0() {
		String input = "";// empty string
		assertEquals("0", wcTool.getNewLineCount(input));
	}

	@Test
	public void getNewLineCount_StringWithNoNewLine_ReturnCount() {
		String input = "Test haha";
		assertEquals("0", wcTool.getNewLineCount(input));
	}

	@Test
	public void getNewLineCount_StringWithNewLine_ReturnCount() {
		String input = "Test haha"+System.lineSeparator()+System.lineSeparator();
		assertEquals("2", wcTool.getNewLineCount(input));
	}

	/**
	 * @Corrected
	 * changed test method name getNewLineCountForNullTest()
	 */
	@Test
	public void getNewLineCount_Null_Return0() {
		String input = null;
		assertEquals("0", wcTool.getNewLineCount(input));
	}

	/**
	 * @Corrected
	 * changed test method name getHelpTest()
	 * changed original test case's assertion as there is no such class called MessageHelper
	 */
	@Test
	public void getHelp_NormalTest_ReturnHelpContent() {
		assertEquals(helpContent, wcTool.getHelp());
	}
}
