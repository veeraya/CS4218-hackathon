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
		assertEquals("C:\\",result);
	}
	
	/**
	 * Test error handling
	 * Executing cd and pwd
	 */
	@Test
	public void executePipeErrorCdPwdCommandTest(){
		vectors.add(shell.parse("cd ZA:\\"));
		vectors.add(shell.parse("copy hello.txt world"));
		pipingTool = new PipingTool(vectors);
		File workingDir = new File(System.getProperty("java.io.tmpdir"));
		String result = pipingTool.execute(workingDir, "");
		assertTrue(result.contains("ZA"));
		assertTrue(result.contains("hello.txt"));
	}
}