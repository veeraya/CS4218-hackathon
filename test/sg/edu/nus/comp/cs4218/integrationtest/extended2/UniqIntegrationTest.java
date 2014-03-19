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

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

public class UniqIntegrationTest {
	private String content = 
			"word1 word2 word3" + NEW_LINE +
			"dog1 cat2 squirrel3" + NEW_LINE +
			"dog1 cat2 squirrel3" + NEW_LINE +
			"sakizaki+ pizicato2 dog3";
	
	private static final String FILE_PATH_INPUT = "__test__input__";
	private static final String FILE_PATH_OUTPUT = "__test__output__";
	private static final String FILE_PATH_NOT_EXIST = "__file__not__exists__";
	
	private static final String NEW_LINE = System.lineSeparator();
	
	@Before
	public void setUp() throws Exception {
		deleteFile(FILE_PATH_NOT_EXIST);
		deleteFile(FILE_PATH_OUTPUT);
		createFile(FILE_PATH_INPUT, content);
	}

	@After
	public void tearDown() throws Exception {
		deleteFile(FILE_PATH_INPUT);
		deleteFile(FILE_PATH_OUTPUT);
		deleteFile(FILE_PATH_NOT_EXIST);
	}

	/**
	 * Positive test
	 * Pass the output of grep to uniq
	 * All with default valid args
	 */
	@Test
	public void GrepWithUniqTest() {
		String[] grepArgs = {"dog", FILE_PATH_INPUT};
		IGrepTool grepTool = new GrepTool(grepArgs);
		String grepOutput = grepTool.execute(new File(""), "");
		
		// Grep output will be passed to uniq as a stdin
		String[] uniqArgs = {"-"};
		IUniqTool uniqTool = new UniqTool(uniqArgs);
		String uniqOutput = uniqTool.execute(new File(""), grepOutput);
		
		String expected = 
				"dog1 cat2 squirrel3" + NEW_LINE +
				"sakizaki+ pizicato2 dog3";
		assertEquals(expected, uniqOutput);
	}
	
	/**
	 * Negative test
	 * Pass the output of grep to uniq
	 * Grep reads from non existing file
	 */
	@Test
	public void GrepWithUniqNonExistingFileTest() {
		String[] grepArgs = {"dog", FILE_PATH_NOT_EXIST};
		IGrepTool grepTool = new GrepTool(grepArgs);
		String grepOutput = grepTool.execute(new File(""), "");
		
		// Grep output will be passed to uniq as a stdin
		String[] uniqArgs = {"-"};
		IUniqTool uniqTool = new UniqTool(uniqArgs);
		String uniqOutput = uniqTool.execute(new File(""), grepOutput);
		
		String expected = GrepTool.ERR_MSG_IO.trim();
		assertEquals(expected, uniqOutput);
	}
	
	/**
	 * Positive test
	 * Pass the output of uniq to echo
	 * All with default valid args
	 */
	@Test
	public void UniqWithEchoTest() {
		String[] uniqArgs = {FILE_PATH_INPUT};
		IUniqTool uniqTool = new UniqTool(uniqArgs);
		String uniqOutput = uniqTool.execute(new File(""), "");
		
		String[] echoArgs = {uniqOutput};
		IEchoTool echoTool = new EchoTool(echoArgs);
		String echoOutput = echoTool.execute(new File(""), uniqOutput);
		
		String expected = 
				"word1 word2 word3" + NEW_LINE;
		assertEquals(expected, echoOutput);
	}
	
	/**
	 * Negative test
	 * Pass the output of uniq to echo
	 * Uniq reads from non existing file
	 */
	@Test
	public void UniqWithEchoNonExistingFileTest() {
		String[] uniqArgs = {FILE_PATH_NOT_EXIST};
		IUniqTool uniqTool = new UniqTool(uniqArgs);
		String uniqOutput = uniqTool.execute(new File(""), "");
		
		String[] echoArgs = {uniqOutput};
		IEchoTool echoTool = new EchoTool(echoArgs);
		String echoOutput = echoTool.execute(new File(""), uniqOutput);
		
		String expected = String.format(
				UniqTool.ERR_MSG_FILE_NOT_FOUND, FILE_PATH_NOT_EXIST)
				+ NEW_LINE;
		assertEquals(expected, echoOutput);
	}
	
	/**
	 * Positive test
	 * Pass the output of uniq to echo
	 * All with default valid args
	 */
	@Test
	public void UniqWithCatTest() {
		String[] uniqArgs = {FILE_PATH_INPUT, FILE_PATH_OUTPUT};
		IUniqTool uniqTool = new UniqTool(uniqArgs);
		String uniqOutput = uniqTool.execute(new File(""), "");
		
		String[] catArgs = {FILE_PATH_OUTPUT};
		ICatTool catTool = new CatTool(catArgs);
		String echoOutput = catTool.execute(new File(""), uniqOutput);
		
		String expected = 
				"word1 word2 word3" + NEW_LINE +
				"dog1 cat2 squirrel3" + NEW_LINE +
				"sakizaki+ pizicato2 dog3" + NEW_LINE;
		assertEquals(expected, echoOutput);
	}
	
	/**
	 * Negative test
	 * Pass the output of uniq to echo
	 * Uniq has invalid option
	 */
	@Test
	public void UniqWithCatInvalidOptionTest() {
		String[] uniqArgs = {"-b"};
		IUniqTool uniqTool = new UniqTool(uniqArgs);
		String uniqOutput = uniqTool.execute(new File(""), "");
		
		ICatTool catTool = new CatTool();
		String echoOutput = catTool.execute(new File(""), uniqOutput);
		
		String expected = String.format(
				UniqTool.ERR_MSG_INVALID_OPTION, "-b");
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
