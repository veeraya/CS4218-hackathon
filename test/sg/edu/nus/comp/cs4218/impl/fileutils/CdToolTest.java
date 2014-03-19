/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;

public class CdToolTest {

	private final String HOME_DIRECTORY = "user.home";
	private final String USER_DIRECTORY = "user.dir";
	private ICdTool cdTool;
	private String defaultWorkingDirectory;
	private File workingDirectory;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		defaultWorkingDirectory = System.getProperty(USER_DIRECTORY);
		
		cdTool= new CdTool(null);
		workingDirectory = new File(defaultWorkingDirectory);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		cdTool = null;
		workingDirectory = null;
		System.setProperty(USER_DIRECTORY, defaultWorkingDirectory);
	}

	@Test
	public void changeDirectory_validAbsoluteDirectory_CorrectFileDirectoryReturned() {
		File directory = new File(defaultWorkingDirectory);
		File[] currentDirectoryFiles = directory.listFiles();
		File resturnStatement = cdTool.changeDirectory(currentDirectoryFiles[0]
				.getAbsolutePath());

		assertEquals(currentDirectoryFiles[0].getAbsolutePath(),
				resturnStatement.getAbsolutePath());
	}

	@Test
	public void changeDirectory_invalidAbsoluteDirectory_NullObjectReturned(){
		File directory = new File(HOME_DIRECTORY);
		final File resturnStatement = cdTool.changeDirectory(directory
				.getAbsolutePath() + File.separator + "i7v3l1d");

		assertNull(resturnStatement);
	}

	@Test
	public void changeDirectory_nullObject_NullObjectReturned(){
		final File resturnStatement = cdTool.changeDirectory(null);

		assertNull(resturnStatement);
	}

	@Test
	public void changeDirectory_sameDirectory_CorrectDirectoryReturned(){
		final File resturnStatement = cdTool.changeDirectory(defaultWorkingDirectory);

		assertEquals(System.getProperty(USER_DIRECTORY),
				resturnStatement.getAbsolutePath());
	}

	@Test
	public void changeDirectory_emptyString_HomeDirectoryReturn(){
		final File correctDirectory = new File ("");
		final File resturnStatement = cdTool.changeDirectory("");

		assertEquals(correctDirectory.getAbsolutePath(),
				resturnStatement.getAbsolutePath());
	}

	@Test
	public void changeDirectory_validNavigateFromHomeDirectory_CorrectRelativeDirectoryReturned(){
		//Valid for all major O.S distribution
		final String newDirectory = "~" + File.separator + "Downloads";
		cdTool.changeDirectory(newDirectory);

		assertEquals(System.getProperty(HOME_DIRECTORY) + File.separator
				+ "Downloads", System.getProperty(USER_DIRECTORY));
	}

	@Test
	public void changeDirectory_invalidNavigateFromHomeDirectory_NullObjectReturned(){
		final String newDirectory = "~" + File.separator + "Down";
		final File resturnStatement = cdTool.changeDirectory(newDirectory);

		assertNull(resturnStatement);
	}

	@Test
	public void changeDirectory_validRelativeDirectory_CorrectDirectoryReturned(){
		final String newDirectory = "src";
		final File correctFileDirectory = new File (newDirectory);
		final File returnStatement = cdTool.changeDirectory(newDirectory);
		assertEquals(correctFileDirectory.getAbsolutePath(),
				returnStatement.getAbsolutePath());
	}

	@Test
	public void changeDirectory_invalidRelativeDirectory_NullObjectReturned(){
		final String newDirectory = "testing";
		final File resturnStatement = cdTool.changeDirectory(newDirectory);

		assertNull(resturnStatement);
	}

	@Test
	public void changeDirectory_doubleDot_ParentDirectoryReturned(){
		final String newDirectory = "..";
		final File resturnStatement = cdTool.changeDirectory(newDirectory);

		assertEquals(workingDirectory.getParent(),
				resturnStatement.getAbsolutePath());
	}

	@Test
	public void execute_validAbsoluteDirectory_StatusCodeZero() {
		File directory = new File(defaultWorkingDirectory);
		File[] currentDirectoryFiles = directory.listFiles();

		final String[] argument = { currentDirectoryFiles[0].getAbsolutePath() }; 

		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(currentDirectoryFiles[0].getAbsolutePath(),
				System.getProperty(USER_DIRECTORY));
	}

	@Test
	public void execute_invalidAbsoluteDirectory_StatusCodeNonZero() {
		File directory = new File(HOME_DIRECTORY);
		final String[] argument = { directory.getAbsolutePath()
				+ File.separator + "i7v3l1d" };
		cdTool = new CdTool(argument);

		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertNotEquals(0, cdTool.getStatusCode());
	}

	@Test
	public void execute_sameDirectory_StatusCodeZero(){
		final String newDirectory = defaultWorkingDirectory;
		final String[] argument = {newDirectory}; 
		cdTool = new CdTool(argument);

		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(defaultWorkingDirectory, System.getProperty(USER_DIRECTORY)); 
	}

	@Test
	public void execute_emptyString_StatusCodeZero(){
		final String[] argument = {""};
		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(System.getProperty(HOME_DIRECTORY), System.getProperty(USER_DIRECTORY));
	}

	@Test
	public void execute_validNavigateFromHomeDirectory_StatusCodeZero(){
		//Works for major O.S distribution
		final String newDirectory = "~" + File.separator + "Downloads";
		final String[] argument = {newDirectory}; 

		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(System.getProperty(HOME_DIRECTORY) + File.separator
				+ "Downloads", System.getProperty(USER_DIRECTORY));
	}
	
	@Test
	public void execute_invalidNavigateFromHomeDirectory_StatusCodeNonZero(){
		final String newDirectory = "~/Down";
		final String[] argument = {newDirectory}; 
		cdTool = new CdTool(argument);

		cdTool.execute(workingDirectory, null);

		assertNotEquals(0, cdTool.getStatusCode());
	}

	@Test 
	public void execute_ValidNavigateBack_StatusCodeZero(){
		String[] argument = {"src"};
		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);
		
		argument[0] = "-";
		cdTool = new CdTool(argument);
		String result = cdTool.execute(workingDirectory, null);
		
		assertEquals(0, cdTool.getStatusCode());
		assertEquals("", result);
	}
	
	@Test
	public void execute_InvalidNavigateBack_StatusCodeNonZero(){
		String[] argument = {"-"};
		
		cdTool = new CdTool(argument, true);
		cdTool.execute(workingDirectory, null);
		assertNotEquals(0, cdTool.getStatusCode());
	}
	
	@Test
	public void execute_NullArgument_StatusCodeNonZero(){
		cdTool = new CdTool(null);
		cdTool.execute(workingDirectory, null);
		assertNotEquals(0, cdTool.getStatusCode());
	}
}