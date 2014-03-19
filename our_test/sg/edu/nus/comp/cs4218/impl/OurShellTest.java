package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.extended1.*;
import sg.edu.nus.comp.cs4218.impl.extended2.*;
import sg.edu.nus.comp.cs4218.impl.fileutils.*;

public class OurShellTest {

	private Shell shell;

	@Before
	public void before(){
		shell = new Shell();
	}

	@After
	public void after(){
		shell = null;
	}

	/**
	 * Test error handling
	 * Empty command
	 */
	@Test
	public void parseEmptyCommandTest(){
		ITool result = shell.parse("");
		assertEquals(result, null);
	}
	
	/**
	 * Test error handling
	 * User type in ctrl z when no command is execute
	 */
	@Test
	public void parseCtrlZTest(){
		ITool result = shell.parse("Ctrl + z");
		assertEquals(result, null);
	}
	
	/**
	 * Test error handling
	 * User type invalid command
	 */
	@Test
	public void parseInvalidCommandTest(){
		ITool result = shell.parse("Hello world");
		assertEquals(result, null);
	}
	
	/**
	 * Test expected behavior
	 * executing cd command
	 */
	@Test
	public void parseFileUtil1CommandTest(){
		ITool result = shell.parse("cd C:\\");
		assertTrue(result instanceof CdTool);
	}
	
	/**
	 * Test expected behavior
	 * executing cat command
	 */
	@Test
	public void parseFileUtil2CommandTest(){
		ITool result = shell.parse("cat -");
		assertTrue(result instanceof CatTool);
	}
	
	/**
	 * Test expected behavior
	 * executing copy command
	 */
	@Test
	public void parseFileUtil3CommandTest(){
		ITool result = shell.parse("copy some stuff");
		assertTrue(result instanceof CopyTool);
	}
	
	/**
	 * Test expected behavior
	 * executing commands with pipe
	 */
	@Test
	public void parseExtended1CommandTest(){
		ITool result = shell.parse("some where | no where");
		assertTrue(result instanceof PipingTool);
	}

	/**
	 * Test expected behavior
	 * executing comm command
	 */
	@Test
	public void parseExtended2aCommandTest(){
		ITool result = shell.parse("comm this file with that file");
		assertTrue(result instanceof CommTool);
	}

	/**
	 * Test expected behavior
	 * executing paste command
	 */
	@Test
	public void parseExtended2bCommandTest(){
		ITool result = shell.parse("paste me or cut me");
		assertTrue(result instanceof PasteTool);
	}
	
	/**
	 * Test expected behavior
	 * executing wc command
	 */
	@Test
	public void parseExtended2cCommandTest(){
		ITool result = shell.parse("wc is counting");
		assertTrue(result instanceof WcTool);
	}
	
	/**
	 * Test error handling
	 * executing command containing a cluster of pipe without spaces
	 */
	@Test
	public void parseMultiplePipeTest(){
		ITool result = shell.parse("echo Hello world |||| wc");
		assertEquals(result, null);
	}
	
	/**
	 * Test expected behavior
	 * runnable object created
	 */
	@Test
	public void executeTest(){
		ITool tool = shell.parse("pwd");
		Runnable run = shell.execute(tool);
		assertTrue(run.toString().contains("sg.edu.nus.comp.cs4218.impl.Shell"));
	}
	
	@Test
	public void stopTest(){
		ITool tool = shell.parse("ls");
		Runnable run = shell.execute(tool);
		shell.stop(run);
		assertTrue(run.toString().contains("sg.edu.nus.comp.cs4218.impl.Shell"));
	}
}
