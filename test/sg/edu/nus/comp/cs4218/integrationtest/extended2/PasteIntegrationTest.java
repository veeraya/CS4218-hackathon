package sg.edu.nus.comp.cs4218.integrationtest.extended2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

public class PasteIntegrationTest {
	private String content1 = 
			"word1 word2 word3" + NEW_LINE +
			"dog1 cat2 squirrel3" + NEW_LINE +
			"sakizaki+ pizicato2 dog3";
	
	private String content2 = 
			"mystere mani" + NEW_LINE +
			"saltimbanco" + NEW_LINE +
			"zumanity zed allegria freetempo";
	
	private static final String FILE_PATH_1 = "__test__input__1__";
	private static final String FILE_PATH_2 = "__test__input__2__";
	private static final String FILE_PATH_NOT_EXISTS = "__file__not__exists";
	
	private static final String NEW_LINE = System.lineSeparator();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// In case file exists.
		deleteFile(FILE_PATH_NOT_EXISTS);
		
		createFile(FILE_PATH_1, content1);
		createFile(FILE_PATH_2, content2);
	}

	@After
	public void tearDown() throws Exception {
		deleteFile(FILE_PATH_1);
		deleteFile(FILE_PATH_2);
		
		deleteFile(FILE_PATH_NOT_EXISTS);
	}

	/**
	 * Positive test
	 * Pass the output of grep to paste
	 * All with default valid args
	 */
	@Test
	public void GrepWithPasteTest() {
		String[] grepArgs = {"dog", FILE_PATH_1};
		IGrepTool grepTool = new GrepTool(grepArgs);
		String grepOutput = grepTool.execute(new File(""), "");
		
		// Grep output will be passed to paste as a stdin
		String[] pasteArgs = {"-", FILE_PATH_2};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), grepOutput);
		
		String expected = 
				"dog1 cat2 squirrel3\tmystere mani" + NEW_LINE +
				"sakizaki+ pizicato2 dog3\tsaltimbanco" + NEW_LINE +
				"\tzumanity zed allegria freetempo";
		assertEquals(expected, pasteOutput);
	}
	
	/**
	 * Negative test
	 * Pass the output of grep to paste
	 * Grep reads from non existing file
	 */
	@Test
	public void GrepWithPasteNonExistingFileTest() {
		String[] grepArgs = {"dog", FILE_PATH_NOT_EXISTS};
		IGrepTool grepTool = new GrepTool(grepArgs);
		String grepOutput = grepTool.execute(new File(""), "");
		
		// Grep output will be passed to paste as a stdin
		String[] pasteArgs = {"-", FILE_PATH_2};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), grepOutput);
		
		String expected = 
				GrepTool.ERR_MSG_IO.trim() + "\tmystere mani" + NEW_LINE +
				"\tsaltimbanco" + NEW_LINE +
				"\tzumanity zed allegria freetempo";
		assertEquals(expected, pasteOutput);
	}
	
	/**
	 * Positive test
	 * Pass the output of paste to echo
	 * All with default valid args
	 */
	@Test
	public void PasteWithEchoTest() {
		String[] pasteArgs = {FILE_PATH_1, FILE_PATH_2};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), "");
		
		String[] echoArgs = {pasteOutput};
		IEchoTool echoTool = new EchoTool(echoArgs);
		String echoOutput = echoTool.execute(new File(""), pasteOutput);
		
		String expected = 
				"word1 word2 word3\tmystere mani" + NEW_LINE;
		assertEquals(expected, echoOutput);
	}
	
	/**
	 * Negative test
	 * Pass the output of paste to echo
	 * Paste reads from non existing file
	 */
	@Test
	public void PasteWithEchoNonExistingFileTest() {
		String[] pasteArgs = {FILE_PATH_1, FILE_PATH_NOT_EXISTS};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), "");
		
		String[] echoArgs = {pasteOutput};
		IEchoTool echoTool = new EchoTool(echoArgs);
		String echoOutput = echoTool.execute(new File(""), pasteOutput);
		
		String expected = String.format(
				PasteTool.ERR_MSG_FILE_NOT_EXIST, FILE_PATH_NOT_EXISTS)
				+ NEW_LINE;
		assertEquals(expected, echoOutput);
	}
	
	/**
	 * Positive test
	 * Pass the output of paste to echo
	 * All with default valid args
	 */
	@Test
	public void PasteWithCatTest() {
		String[] pasteArgs = {FILE_PATH_1, FILE_PATH_2};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), "");
		
		ICatTool catTool = new CatTool();
		String echoOutput = catTool.execute(new File(""), pasteOutput);
		
		String expected = 
				"word1 word2 word3\tmystere mani" + NEW_LINE +
				"dog1 cat2 squirrel3\tsaltimbanco" + NEW_LINE +
				"sakizaki+ pizicato2 dog3\tzumanity zed allegria freetempo";
		assertEquals(expected, echoOutput);
	}
	
	/**
	 * Negative test
	 * Pass the output of paste to echo
	 * Paste has invalid option
	 */
	@Test
	public void PasteWithCatInvalidOptionTest() {
		String[] pasteArgs = {"-a", FILE_PATH_1, FILE_PATH_2};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), "");
		
		ICatTool catTool = new CatTool();
		String echoOutput = catTool.execute(new File(""), pasteOutput);
		
		String expected = String.format(
				PasteTool.ERR_MSG_INVALID_OPTION, "-a");
		assertEquals(expected, echoOutput);
	}

	/**
	 * Helper method to create temporary file
	 * @param path
	 * @param content
	 */
	public void createFile(String path, String content) {
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path), "UTF-8"));
			out.write(content);
			out.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
