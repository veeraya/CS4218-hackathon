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
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;

public class UniqPasteIntegrationTest {

	private String content1 = 
			"word1 word2 word3" + NEW_LINE +
			"dog1 cat2 squirrel3" + NEW_LINE +
			"dog1 cat2 squirrel3" + NEW_LINE +
			"sakizaki+ pizicato2 dog3";
	
	private String content2 = 
			"mystere mani" + NEW_LINE +
			"saltimbanco" + NEW_LINE +
			"zumanity zed allegria freetempo";
	
	private static final String FILE_PATH_INPUT_1 = "__test__input__1__";
	private static final String FILE_PATH_INPUT_2 = "__test__input__2__";
	private static final String FILE_PATH_OUTPUT = "__test__output__";
	private static final String FILE_PATH_NOT_EXIST = "__file__not__exists__";
	
	private static final String NEW_LINE = System.lineSeparator();
	
	@Before
	public void setUp() throws Exception {
		deleteFile(FILE_PATH_NOT_EXIST);
		deleteFile(FILE_PATH_OUTPUT);
		createFile(FILE_PATH_INPUT_1, content1);
		createFile(FILE_PATH_INPUT_2, content2);
	}

	@After
	public void tearDown() throws Exception {
		deleteFile(FILE_PATH_INPUT_1);
		deleteFile(FILE_PATH_INPUT_2);
		deleteFile(FILE_PATH_OUTPUT);
		deleteFile(FILE_PATH_NOT_EXIST);
	}

	/**
	 * Positive test
	 * Pass the output of uniq to paste
	 * All with default valid args
	 */
	@Test
	public void UniqWithPasteTest() {
		String[] uniqArgs = {FILE_PATH_INPUT_1, FILE_PATH_OUTPUT};
		IUniqTool uniqTool = new UniqTool(uniqArgs);
		uniqTool.execute(new File(""), "");
		
		String[] pasteArgs = {FILE_PATH_OUTPUT, FILE_PATH_INPUT_2};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), "");
		
		String expected = 
				"word1 word2 word3\tmystere mani" + NEW_LINE +
				"dog1 cat2 squirrel3\tsaltimbanco" + NEW_LINE +
				"sakizaki+ pizicato2 dog3\tzumanity zed allegria freetempo";
		assertEquals(expected, pasteOutput);
	}
	
	/**
	 * Negative test
	 * Pass the output of uniq to paste
	 * Uniq reads from non existing file
	 */
	@Test
	public void UniqWithPasteNonExistingFileTest() {
		String[] uniqArgs = {FILE_PATH_NOT_EXIST, FILE_PATH_OUTPUT};
		IUniqTool uniqTool = new UniqTool(uniqArgs);
		String uniqOutput = uniqTool.execute(new File(""), "");
		
		String[] pasteArgs = {FILE_PATH_OUTPUT, FILE_PATH_INPUT_2};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		pasteTool.execute(new File(""), "");
		
		String expected = String.format(
				UniqTool.ERR_MSG_FILE_NOT_FOUND, FILE_PATH_NOT_EXIST);
		assertEquals(expected, uniqOutput);
	}
	
	/**
	 * Positive test
	 * Pass the output of paste to uniq
	 * All with default valid args
	 */
	@Test
	public void PasteWithUniqTest() {
		String[] pasteArgs = {FILE_PATH_INPUT_1, FILE_PATH_INPUT_2};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), "");
		
		IUniqTool uniqTool = new UniqTool();
		String uniqOutput = uniqTool.execute(new File(""), pasteOutput);
		
		String expected = 
				"word1 word2 word3\tmystere mani" + NEW_LINE +
				"dog1 cat2 squirrel3\tsaltimbanco" + NEW_LINE +
				"dog1 cat2 squirrel3\tzumanity zed allegria freetempo" + NEW_LINE +
				"sakizaki+ pizicato2 dog3";
		assertEquals(expected, uniqOutput);
	}
	
	/**
	 * Negative test
	 * Pass the output of paste to uniq
	 * Paste reads from non existing file
	 */
	@Test
	public void PasteWithUniqNonExistingFileTest() {
		String[] pasteArgs = {FILE_PATH_INPUT_1, FILE_PATH_NOT_EXIST};
		IPasteTool pasteTool = new PasteTool(pasteArgs);
		String pasteOutput = pasteTool.execute(new File(""), "");
		
		IUniqTool uniqTool = new UniqTool();
		String uniqOutput = uniqTool.execute(new File(""), pasteOutput);
		
		String expected = String.format(
				PasteTool.ERR_MSG_FILE_NOT_EXIST, FILE_PATH_NOT_EXIST);
		assertEquals(expected, uniqOutput);
	}
	
	/**
	 * Helper method to create temporary file
	 * @param path
	 * @param content1
	 */
	public void createFile(String path, String content1) {
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path), "UTF-8"));
			out.write(content1);
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
