package sg.edu.nus.comp.cs4218.integrationtest.fileutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;

public class CatWithGrepTest {
	private String content = 
			"word1 word2 word3" + NEW_LINE +
			"dog1 cat2 squirrel3" + NEW_LINE +
			"sakizaki+ pizicato2 dog3";
	private static final String FILE_PATH = "__test__input__";
	private static final String NEW_LINE = System.lineSeparator();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		createFile(FILE_PATH, content);
	}

	@After
	public void tearDown() throws Exception {
		deleteFile(FILE_PATH);
	}

	/**
	 * Positive test
	 * Pass the output of cat to grep
	 * All valid and default args
	 */
	@Test
	public void CatGrepTest() {
		String[] catArgs = {FILE_PATH};
		ICatTool catTool = new CatTool(catArgs);
		String catOutput = catTool.execute(new File(""), "");
		
		/**
		 * Note:
		 * Output from cat is passed as stdin (rather than file path)
		 * to grep so that the interface matches.
		 * 
		 * The rest of the test cases follow similar approach.
		 */
		String[] grepArgs = {"dog"};
		IGrepTool grepTool = new GrepTool(grepArgs);
		String grepOutput = grepTool.execute(new File(""), catOutput);
		
		String expected = 
				"dog1 cat2 squirrel3" + NEW_LINE +
				"sakizaki+ pizicato2 dog3" + NEW_LINE;
		
		assertEquals(expected, grepOutput);
	}

	/**
	 * Negative test
	 * Pass the output of cat to grep
	 * File not exist
	 */
	@Test
	public void CatGrepFileNotExistTest() {
		String[] catArgs = {"_NON_EXISTING_FILE_"};
		ICatTool catTool = new CatTool(catArgs);
		String catOutput = catTool.execute(new File(""), "");
		
		String[] grepArgs = {"dog"};
		IGrepTool grepTool = new GrepTool(grepArgs);
		String grepOutput = grepTool.execute(new File(""), catOutput);
		
		String expected = "";
		assertEquals(expected, grepOutput);
	}
	
	/**
	 * Positive test
	 * Pass the output of grep to cat
	 * All valid and default args
	 */
	@Test
	public void GrepCatTest() {
		String[] grepArgs = {"dog", FILE_PATH};
		IGrepTool grepTool = new GrepTool(grepArgs);
		String grepOutput = grepTool.execute(new File(""), "");
		
		String[] catArgs = null;
		ICatTool catTool = new CatTool(catArgs);
		String catOutput = catTool.execute(new File(""), grepOutput);
		
		
		String expected = 
				"dog1 cat2 squirrel3" + NEW_LINE +
				"sakizaki+ pizicato2 dog3" + NEW_LINE;
		
		assertEquals(expected, catOutput);
	}

	/**
	 * Negative test
	 * Pass the output of grep to cat
	 * File not exist
	 */
	@Test
	public void GrepCatFileNotExistTest() {
		String[] grepArgs = {"dog", "_NON_EXISTING_FILE_"};
		IGrepTool grepTool = new GrepTool(grepArgs);
		String grepOutput = grepTool.execute(new File(""), "");
		
		String[] catArgs = null;
		ICatTool catTool = new CatTool(catArgs);
		String catOutput = catTool.execute(new File(""), grepOutput);
		
		String expected = GrepTool.ERR_MSG_IO;
		assertEquals(expected, catOutput);
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
