package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mew
 * 
 */
public class OurWCToolTest {

	private WcTool wctool;
	private String testString = "The quick brown fox jumps over the lazy dog.\n";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		wctool = null;
	}
	
	/**
	 * Test expected behavior
	 * Get number of character count from a string
	 */
	@Test 
	public void getCharacterCountTest(){
		wctool = new WcTool(new String[]{});
		String count = wctool.getCharacterCount(testString);
		assertEquals(count,"45");
	}
	
	/**
	 * Test expected behavior
	 * Get number of word count from a string
	 */
	@Test 
	public void getWordCountTest(){
		wctool = new WcTool(new String[]{});
		String count = wctool.getWordCount(testString);
		assertEquals(count,"9");
	}
	
	/**
	 * Test expected behavior
	 * Get number of new line count from a string
	 */
	@Test 
	public void getNewLineCountTest(){
		wctool = new WcTool(new String[]{});
		String count = wctool.getNewLineCount(testString);
		assertEquals(count,"1");
	}
	
	/**
	 * Test expected behavior
	 * Get number of character count from a string by parsing -m as an option in the command
	 */
	@Test 
	public void executeCharacterOptionTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{"-m",tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("45\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get number of word count from a string by parsing -w as an option in the command
	 */
	@Test 
	public void executeWordOptionTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{"-w",tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("9\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get number of new line count from a string by parsing -l as an option in the command
	 */
	@Test 
	public void executeNewLineOptionTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{"-l",tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get number of newline and word count from a string by parsing -l and -w as options in the command
	 */
	@Test 
	public void executeTwoOptionsTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{"-l","-w",tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get number of newline, word and character count from a string when there's no option
	 */
	@Test 
	public void executeNoOptionsTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t45\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get number of newline, word and character count from a file when there's no option
	 */
	@Test 
	public void executeFileNameTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{tempFile.getName()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t45\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get number of newline, word and character count from two files when there's no option
	 */
	@Test 
	public void executeTwoFilesTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile1T", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile2 = Files.createTempFile("tempFile2T", ".tmp").toFile();
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		Files.write(tempFile2.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{tempFile.getName(),tempFile2.getName()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t45\t"+tempFile.getName()+"\n1\t9\t45\t"+tempFile2.getName(), msg);
		Files.delete(tempFile.toPath());
		Files.delete(tempFile2.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get number of newline and word count from two files by parsing -l and -w as options in the command
	 */
	@Test 
	public void executeTwoFilesWithOptionsTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile1T", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile2 = Files.createTempFile("tempFile2T", ".tmp").toFile();
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		Files.write(tempFile2.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{"-l","-w",tempFile.getName(),tempFile2.getPath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t"+tempFile.getName()+"\n1\t9\t"+tempFile2.getName(), msg);
		Files.delete(tempFile.toPath());
		Files.delete(tempFile2.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get number of newline, word and character count from a file that contains a space when there's no option
	 */
	@Test 
	public void executeContainsSpaceTest() throws IOException{
		File tempFile = Files.createTempFile("temp File", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(tempFile.getName().split(" "));
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t45\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Get help for wc command
	 */
	@Test
    public void getHelpTest() {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
        wctool = new WcTool(new String[]{"-help"});
        String helpText = wctool.getHelp();	
        assertEquals(helpText, wctool.execute(tempWorkingDir, null));
        wctool = new WcTool(new String[]{"-help", "abcde"});
        assertEquals(helpText, wctool.execute(tempWorkingDir, null));
        wctool = new WcTool(new String[]{"-d", ".","-s","-help","abcde"});
        assertEquals(helpText, wctool.execute(tempWorkingDir, null));
    }
	
	/**
	 * Test expected behavior
	 * Get error message when there's an invalid source name
	 */
	@Test
	public void executeInvalidSourceNameTest() throws IOException {
		File tempFile = Files.createTempFile("temp File", ".tmp").toFile();
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		wctool = new WcTool(new String[]{"tempFile.tmp" , "test"});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("Error: SOURCE file not found", msg);
		assertTrue(wctool.getStatusCode() != 0);
		Files.delete(tempFile.toPath());
	}


}