package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;

public class OurUNIQToolTest {

	private IUniqTool uniqtool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		uniqtool = null;
	}

	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		uniqtool = new UniqTool(new String[]{"hello"});
		uniqtool.execute(null, null);
		assertEquals(-1, uniqtool.getStatusCode());
	}
	
	/**
	 * Test expected behavior
	 * Get help file
	 */
	@Test
	public void getHelpTest() {
		uniqtool = new UniqTool(new String[]{"hello"});
		String result = uniqtool.getHelp();
		assertTrue(result.contains("uniq"));
	}

	/**
	 * Test expected behavior
	 * Check unique statement within a file
	 */
	@Test
	public void executeGetUniqueTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTest\ntest";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{file.getAbsolutePath()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals(result, "Hello World\r\nhello World\r\nTest\r\ntest");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Check unique when no options is given
	 */
	@Test
	public void executeNoOptionTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTest\ntest";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{file.getAbsolutePath()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals(result, " \nHello World\nhello World\nTest\ntest");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute command with directory instead of file
	 */
	@Test
	public void executeFolderTest() throws IOException{
		File dir = Files.createTempDirectory("tempdir").toFile();
		uniqtool = new UniqTool(dir.getPath().split(" "));
		String result = uniqtool.execute(dir.getParentFile(), null);
		assertEquals(result,"Error: FILE is not found");
		assertEquals(uniqtool.getStatusCode(),1);
		Files.delete(dir.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Contains multiple same option with different num and not case sensitivity
	 */
	@Test
	public void executeGetUniqueSkipNumMultipleSameOptionIgnoreCaseTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTEST\ntest\njest\nBEST";
		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{"-f","2","-f","3","-f","1",file.getAbsolutePath()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals(result,"Hello World\r\nTEST");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Contains multiple same option with different num
	 */
	@Test
	public void executeGetUniqueSkipNumMultipleSameOptionTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTest\ntest\njest\nBEST";
		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{"-f","2","-f","3","-f","1",file.getAbsolutePath()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals(result.compareTo("Hello World\r\nTest"), 0);
		Files.delete(file.toPath());
	}
}