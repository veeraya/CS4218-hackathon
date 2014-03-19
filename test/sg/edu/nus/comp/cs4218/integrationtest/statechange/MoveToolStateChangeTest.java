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

public class MoveToolStateChangeTest {

	// Static Variables
	private static File workingDir;
	private static File tempDir;
	private static File input;
	private static String inputStr;
	private static String workingDirPath;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File temp = new File("tempDir");
		if (temp.exists()) {
			deleteFolder(temp);
		}
		
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

		if (input.exists()) {
			Files.delete(input.toPath());
			Files.delete(tempDir.toPath());
		}

		File f = new File(workingDir.toString() + File.separator + "inputNew");
		if (f.exists()) {
			f.delete();
		}
		workingDir = null;

	}

	@Before
	public void setUp() throws Exception {
		workingDirPath = System.getProperty("user.dir");
	}

	@After
	public void tearDown() throws Exception {
		
		input.createNewFile();
		Files.write(input.toPath(), inputStr.getBytes(),
				StandardOpenOption.APPEND);
		
		// reset the user.dir property to original state prior to the test case
		System.setProperty("user.dir", workingDirPath);
	}

	// Positive Testing
	@Test
	public void cdMoveLsWithPiping_FilesListed() {

		final String cdCmdStr = "cd \"" + tempDir.toString() + "\"";

		final String moveCmdStr = "move \"" + input.toString() + "\" " + "\""
				+ workingDir.toString() + File.separator + "inputNew\"";

		final String lsCmdStr = "ls";

		final String complexCmdStr = cdCmdStr + "|" + moveCmdStr + "|"
				+ lsCmdStr;

		final Vector<String> finalResult = Shell.shellTestExecution(complexCmdStr);
		assertTrue(finalResult.get(0).contains("input"));
		
		// remove inputNew
		File file = new File(workingDir.toString() + File.separator + "inputNew");
		file.delete();

	}
	

	@Test
	public void cdMoveLsWithSequentialExecution_FilesListed() {

		final String cdCmdStr = "cd \"" + tempDir.toString() + "\"";

		final String moveCmdStr = "move \"" + input.toString() + "\" " + "\""
				+ workingDir.toString() + File.separator + "inputNew\"";

		final String lsCmdStr = "ls";

		final Vector<String> finalResult = Shell.shellTestExecution(cdCmdStr, moveCmdStr, lsCmdStr);
		assertTrue(finalResult.get(0).contains(""));
		
		// remove inputNew
		File file = new File(workingDir.toString() + File.separator + "inputNew");
		if (file.exists()) {
			file.delete();
		}
	}


	// Negative Testing

	@Test
	public void lsMoveDeleteWithPiping_FileNotFound() {

		final String lsCmdStr = "ls";
		final String moveCmdStr = "move \"" + input.toString() + "\" " + "\""
				+ workingDir.toString() + File.separator + "inputNew\"";
		final String deleteCmdStr = "delete " + tempDir.getName()
				+ File.separator + "inputNew";

		final String complexCmdStr = lsCmdStr + "|" + moveCmdStr + "|"
				+ deleteCmdStr;

		final Vector<String> finalResult = Shell.shellTestExecution(complexCmdStr);

		final String expectedMessage = "delete: tempDir"+File.separator+"inputNew: No such file or directory";

		assertTrue(finalResult.get(0).contains(expectedMessage));

	}

	@Test
	public void lsMoveDeleteWithSequentialExecution_FileNotFound() {

		final String lsCmdStr = "ls";
		final String moveCmdStr = "move \"" + input.toString() + "\" " + "\""
				+ workingDir.toString() + File.separator + "inputNew\"";
		final String deleteCmdStr = "delete " + tempDir.getName()
				+ File.separator + "inputNew";

		final Vector<String> finalResult = Shell.shellTestExecution(lsCmdStr,
				moveCmdStr, deleteCmdStr);

		final String expectedMessage = "delete: tempDir"+File.separator+"inputNew: No such file or directory";
		assertTrue(finalResult.get(0).contains(expectedMessage));

	}
	
	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}
}
