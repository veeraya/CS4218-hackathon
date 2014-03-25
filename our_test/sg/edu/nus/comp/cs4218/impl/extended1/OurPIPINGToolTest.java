package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.fileutils.*;
import sg.edu.nus.comp.cs4218.impl.extended2.*;

public class OurPIPINGToolTest {
	private IPipingTool pipingTool;
	private Vector vectors;
	private Shell shell;

	
	@Before
	public void setUp() throws Exception {
		vectors = new Vector();
		shell = new Shell();
	}

	@After
	public void tearDown() throws Exception {
		vectors = null;
		pipingTool = null;
		shell = null;
	}

	/**
	 * Test error handling
	 * file does not exists
	 */
//	@CORRECTED
	@Test
	public void pipeIToolInvalidFileIToolTest() {
		pipingTool = new PipingTool(vectors);
		PWDTool from = new PWDTool();
		GrepTool to = new GrepTool(new String[]{"abc","file1"});
		
		String output = pipingTool.pipe(from, to);
		assertEquals("", output);
	}
	
	/**
	 * Test expected behavior
	 * Executing echo followed by wc
	 */
//	@CORRECTED
	@Test
	public void pipeIToolIToolTest() {
		pipingTool = new PipingTool(vectors);
		EchoTool from = new EchoTool(new String[]{"abc"});
		WcTool to = new WcTool(new String[]{"-"});
		
		String output = pipingTool.pipe(from, to);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals("1\t1\t3\t", output);
	}

	/**
	 * Test exoected result
	 * Input string parse to cat tool
	 */	
	@Test
	public void pipeStringIToolTest() {
		pipingTool = new PipingTool(vectors);
		CatTool catTool = new CatTool(new String[]{"-"});
		
		String input = "abc";
		String result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(input, result);
	}
	
	/**
	 * Test error handling
	 * first command has an invalid file in argument
	 */
	@Test
	public void pipeIToolIToolInvalidFileTest(){
		pipingTool = new PipingTool(vectors);
		
		SortTool from = new SortTool(new String[]{"file.tmp"});
		UniqTool to = new UniqTool(new String[]{"-"});
		String result = pipingTool.pipe(from,to);
		assertEquals("\r",result);
	}
	
	/**
	 * Test expected behavior
	 * Move a file followed by deleting the file
	 */
	@Test
	public void pipeIToolFileTest() throws IOException{
		pipingTool = new PipingTool(vectors);
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		MoveTool from = new MoveTool(new String[]{tempFile.getAbsolutePath(), ".."});
		DeleteTool to = new DeleteTool(new String[]{tempFile.getParentFile().getParentFile().getAbsolutePath() + "\\" + tempFile.getName()});
		
		String result = pipingTool.pipe(from, to);
		assertEquals("",result);
		assertTrue(!tempFile.exists());
		tempFile = new File(tempFile.getParentFile().getParentFile(), tempFile.getName());
		assertTrue(!tempFile.exists());
	}

	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(null, null);
		assertEquals(1, pipingTool.getStatusCode());
		assertEquals("Error: Cannot find working directory", result);
	}
	
	/**
	 * Test error handling
	 * No argument in parameter
	 */
	@Test
	public void executeNoArgTest() {
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals(1, pipingTool.getStatusCode());
		assertEquals("Error: Missing parameter for PROGRAM-1 | PROGRAM-2", result);
	}
	
	/**
	 * Test error handling
	 * Parameter only contains one command
	 */
	@Test
	public void executeOneArgTest() {
		vectors.add(new PWDTool());
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals(1, pipingTool.getStatusCode());
		assertEquals("Error: Missing parameter for PROGRAM-1 | PROGRAM-2", result);
	}
	
	/**
	 * Test error handling
	 * Invalid command in to parameter
	 */
	@Test
	public void executeValidInvalidArgTest() {
		vectors.add(new PWDTool());
		vectors.add(shell.parse("cwd"));
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertTrue(result.contains("Invalid command 'cwd'"));
	}
	
	/**
	 * Test error handling
	 * Invalid file
	 */
	@Test
	public void executePipeInvalidFileTest() {
		String command = "temp.tmp C:\\";
		vectors.add(new CopyTool(command.split(" ")));
		command = "C:\\temp.tmp";
		vectors.add(new  DeleteTool(command.split(" ")));
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertTrue(result.contains("copy"));
		assertTrue(result.contains("delete"));
	}
	
	/**
	 * Test error handling
	 * Invalid command in from tool
	 */
	@Test
	public void executePipeInvalidCommandTest(){
		vectors.add(shell.parse("scout -i hello"));
		vectors.add(shell.parse("uniqx"));
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals("Error: Invalid command 'scout'\n\r\n",result);
	}
	
	/**
	 * Test expected behavior
	 * Executing echo and wc
	 */
	@Test
	public void executePipeEchoWcCommandTest(){
		vectors.add(shell.parse("echo hello world"));
		vectors.add(shell.parse("wc"));
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals("1\t2\t11\t\n",result);
	}
	
	/**
	 * Test expected behavior
	 * Executing echo and cat
	 */
	@Test
	public void executePipeEchoCatCommandTest(){
		vectors.add(shell.parse("echo hello world"));
		vectors.add(shell.parse("cat"));
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals("hello world\n",result);
	}
	
	/**
	 * Test expected behavior
	 * Executing echo and cut
	 */
	@Test
	public void executePipeEchoCutCommandTest(){
		vectors.add(shell.parse("echo foo:bar:baz:qux:quux"));
		vectors.add(shell.parse("cut -c 4-10 -"));
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals(":bar:ba\n",result);
	}
	
	/**
	 * Test expected behavior
	 * Executing echo and paste
	 */
	@Test
	public void executePipeEchoPasteCommandTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		String temp = "Mark Smith\nBobby Brown";
		Files.write(tempFile.toPath(), temp.getBytes(),StandardOpenOption.CREATE);
		vectors.add(shell.parse("echo 555-1234\n555-9876"));
		vectors.add(shell.parse("paste -d . " + tempFile.getName() + " -"));
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals("Mark Smith.555-1234\nBobby Brown.555-9876\n",result);
	}
	
	/**
	 * Test expected behavior
	 * Executing cd and pwd
	 */
	@Test
	public void executePipeCdPwdCommandTest(){
		vectors.add(shell.parse("cd C:\\"));
		vectors.add(shell.parse("pwd"));
		pipingTool = new PipingTool(vectors);
		File workingDir = new File(System.getProperty("java.io.tmpdir"));
		String result = pipingTool.execute(workingDir, null);
		assertEquals("C:\\\n",result);
	}
	
	/**
	 * Test expected behavior
	 * Executing multiple parameter
	 */
	@Test
	public void executePipeEchoWcUnqiCommandTest(){
		String command = "echo hello world| wc | uniq";
		vectors.add(shell.parse("echo hello world"));
		vectors.add(shell.parse("wc"));
		vectors.add(shell.parse("uniq"));
		pipingTool = new PipingTool(vectors);
		String result = pipingTool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals("1\t2\t11\t\n",result);
	}
}