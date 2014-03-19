package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;


public class EchoToolTest {
	private IEchoTool echoTool;
	private File workingDir;

	@Before
	public void setUp() throws Exception {
		workingDir = new File(System.getProperty("user.dir"));
		echoTool = new EchoTool();
	}

	@After
	public void tearDown() throws Exception {
		echoTool = null;
	}
	
	@Test 
	public void echoNullArgument_StatusCodeZero() {
		assertEquals("", echoTool.echo(null));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	@Test 
	public void echoNewLineAtStart_StatusCodeZero() {
		assertEquals(System.lineSeparator(), 
				echoTool.echo(System.lineSeparator() + "word"));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	@Test 
	public void echoNewLineNotAtStart_StatusCodeZero() {
		assertEquals("word" + System.lineSeparator(), 
				echoTool.echo("word" + System.lineSeparator() + "next"));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	
	@Test 
	public void echoWithNullArgument_StatusCodeZero() {
		assertEquals(System.lineSeparator(), echoTool.execute(workingDir, null));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	@Test 
	public void echoWithOneArgument_StatusCodeZero() {
		String[] args = { " Lorem ipsum " };
		echoTool = new EchoTool(args);
		
		assertEquals("Lorem ipsum" + System.lineSeparator(), 
				echoTool.execute(workingDir, null));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	@Test 
	public void echoWithMultipleArguments_StatusCodeZero() {
		String[] args = { " Lorem ipsum ", "  odit tempore debitis " };
		echoTool = new EchoTool(args);
		
		assertEquals("Lorem ipsum   odit tempore debitis" + System.lineSeparator(), 
				echoTool.execute(workingDir, null));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	@Test 
	public void echoWithOnlyNeWLine_StatusCodeZero() {
		String[] args = { System.lineSeparator() };
		echoTool = new EchoTool(args);
		
		assertEquals( System.lineSeparator(), 
				echoTool.execute(workingDir, null));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	@Test 
	public void echoWithStringAfterNewLine_StatusCodeZero() {
		String[] args = { "Lorem ipsum  ", System.lineSeparator(), "consectetur adipisicing" };
		echoTool = new EchoTool(args);
		
		assertEquals("Lorem ipsum" + System.lineSeparator(), 
				echoTool.execute(workingDir, null));
		assertEquals(0, echoTool.getStatusCode());
	}
	
}
