package sg.edu.nus.comp.cs4218.integrationtest.statechange;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;

public class CopyToolStateChangeTest {

	// Static Variables
	private static File workingDir;
	private static File tempDir;
	private static File input;
	private static String inputStr;
	private static String workingDirPath;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		workingDir = new File(System.getProperty("user.dir"));
		tempDir = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "tempDir")
						.toPath()).toString());

		input = new File(tempDir.toString() + File.separator + "input");
		input.createNewFile();

		inputStr = "0123456789012345678901234567890123456789"
				+ System.lineSeparator()
				+ "0123456789012345678901234567890123456789"
				+ System.lineSeparator()
				+ "0123456789 0123456789 0123456789 0123456789";

		Files.write(input.toPath(), inputStr.getBytes(),
				StandardOpenOption.APPEND);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		Files.delete(input.toPath());
		Files.delete(tempDir.toPath());
		workingDir = null;

	}

	@Before
	public void setUp() throws Exception {
		workingDirPath = System.getProperty("user.dir");
	}

	@After
	public void tearDown() throws Exception {
		Files.delete(new File(tempDir.toString() + File.separator + "inputNew")
				.toPath());
		// reset the user.dir property to original state prior to the test case
		System.setProperty("user.dir", workingDirPath);
	}

	// Positive Testing

	@Test
	public void cdCopyLsWithPiping_FilesListed() {

		final String complexCmdStr = "cd \"" + tempDir.toString() + "\"" 
				+ "|copy \"" + input.toString() + "\" \"" + tempDir.toString()
				+ File.separator + "inputNew\"" + "|ls";

		final Vector<String> finalResult = Shell.shellTestExecution(complexCmdStr);

		assertTrue(finalResult.get(0).contains("inputNew"));

	}

	@Test
	public void cdCopyLsWithSequentialExecution_FilesListed() {

		final String cdCmdStr = "cd \"" + tempDir.toString() + "\"";
		final String copyCmdStr = "copy \"" + input.toString() + "\" \""
				+ tempDir.toString() + File.separator + "inputNew\"";
		final String lsCmdStr = "ls";

		final Vector<String> finalResult = Shell.shellTestExecution(cdCmdStr,
				copyCmdStr, lsCmdStr);
		
		
		assertTrue(finalResult.get(0).contains("inputNew"));

	}

	// Negative Testing

	@Test
	public void cdCopyCdMoveWithPiping_FileNotFound() {

		final String complexCmdStr = "cd " + tempDir.getName()
				+ "|copy \"" + input.toString() + "\" \"" + tempDir.toString()
				+ File.separator + "inputNew\""
				+ "|cd ..|move inputNew inputNew2";

		final Vector<String> finalResult = Shell.shellTestExecution(complexCmdStr);

		assertTrue(finalResult.get(0).contains(
				"move: 'inputNew': No such file or directory"));

	}

	@Test
	public void cdCopyCdMoveWithSequentialExecution_FileNotFound() {

		final String cdCmdStr = "cd " + tempDir.getName();
		final String copyCmdStr = "copy \"" + input.toString() + "\" \""
				+ tempDir.toString() + File.separator + "inputNew\"";
		final String cdCmdRootStr = "cd ..";
		final String moveCmdStr = "move inputNew inputNew2";

		final Vector<String> finalResult = Shell.shellTestExecution(cdCmdStr,
				copyCmdStr, cdCmdRootStr, moveCmdStr);

		assertTrue(finalResult.get(0).contains(
				"move: 'inputNew': No such file or directory"));

	}

}
