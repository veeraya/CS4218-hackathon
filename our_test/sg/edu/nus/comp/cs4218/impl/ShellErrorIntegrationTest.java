package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * This integration testing is about error handling of the Shell
 */
public class ShellErrorIntegrationTest {

	private Shell shell;
	private File wd;
	private ITool tool;
	
	@Before
	public void setUp() throws Exception {
		wd = new File(System.getProperty("java.io.tmpdir"));
		shell = new Shell();
	}

	@After
	public void tearDown() throws Exception {
		shell = null;
		wd = null;
		tool = null;
	}

	/**
	 * Executing PWD | DELETE
	 * Error with DELETE
	 */
	@Test
	public void pwdDeleteTest() {
		String commandline = "pwd | delete";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: Missing parameter for file\n", result);
	}
	
	/**
	 * Executing ECHO | COPY
	 * Error with COPY
	 */
	@Test
	public void echoCopyTest() {
		String commandline = "echo test.txt | copy hello.txt world.txt";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: SOURCE file not found\n", result);
	}
	
	/**
	 * Executing CD | LS
	 * Error with CD
	 */
	@Test
	public void cdLsTest() {
		String commandline = "cd Z:\\ | ls";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertTrue(result.startsWith("Error: Directory not found\n"));
		assertFalse(result.endsWith("Error: Directory not found\n"));
	}
	
	/**
	 * Executing DELETE | LS
	 * Error with DELETE
	 */
	@Test
	public void deleteLsTest() throws IOException {
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String commandline = "delete " + file.getName() + "extra | ls";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertTrue(result.startsWith("Error: File not found\n"));
		assertTrue(result.contains(file.getName()));
		Files.delete(file.toPath());
	}
	
	/**
	 * Executing ECHO | WC
	 * Error with WC
	 */
	@Test
	public void echoWcTest() throws IOException {
		String commandline = "echo hello world | wc -a -";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: Invalid option(s)\n", result);
	}
	
	/**
	 * Executing ECHO | UNIQ
	 * Error with UNIQ
	 */
	@Test
	public void echoUniqTest() throws IOException {
		String commandline = "echo hello world | uniq -a -";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: Invalid option\n", result);
	}
	
	/**
	 * Executing ECHO | COMM
	 * Error with COMM
	 */
	@Test
	public void echoCommTest() throws IOException {
		File f1 = Files.createTempFile("tempFile", ".tmp").toFile();
		String commandline = "echo hello world | comm -a " + f1.getName() + " -";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: FILE 2 is not found\n", result);
		Files.delete(f1.toPath());
	}
	
	/**
	 * Executing ECHO | GREP
	 * Error with GREP
	 */
	@Test
	public void echoGrepTest() throws IOException {
		String commandline = "echo hello world | grep -d -";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: invalid option(s).\n", result);
	}

	/**
	 * Executing ECHO | CUT
	 * Error with CUT
	 */
	@Test
	public void echoCutTest() throws IOException {
		String commandline = "echo hello world | cut -z -";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: invalid option(s).\n", result);
	}
	
	/**
	 * Executing ECHO | PASTE
	 * Error with PASTE
	 */
	@Test
	public void echoPasteTest() throws IOException {
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String commandline = "echo hello world | paste -z -d , - " + file.getName();
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: invalid option(s).\n", result);
	}
	
	/**
	 * Executing MOVE | COPY
	 * Error with MOVE and COPY
	 */
	@Test
	public void moveCopyTest() throws IOException {
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String commandline = "move " + file.getName() + " " + file.getName() + " | copy hello.txt world.txt";
		tool = shell.parse(commandline);
		String result = tool.execute(wd, null);
		assertEquals("Error: DEST is not a directory\nError: SOURCE file not found\n", result);
		file.delete();
	}
}
