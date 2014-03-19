package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MoveToolHelperMethodsTest {
	// Variables
	private MoveTool moveTool;
	private File workingDir;
	private File tempDir;
	private File newFileNotInWD;
	private File newFileInWD;
	private String[] fileArgs;

	@Before
	public void setUp() throws Exception {
		this.moveTool = new MoveTool(null);
		this.workingDir = new File(System.getProperty("user.dir"));
		this.tempDir = createDir("tempDir");

		this.newFileInWD = new File(this.workingDir.toString()
				+ "/newFileInWorkingDir.txt");
		this.newFileInWD.createNewFile();

		this.newFileNotInWD = new File(this.tempDir.toString()
				+ "/newFileNotInWorkingDir.txt");
		this.newFileNotInWD.createNewFile();

		this.fileArgs = new String[5];

		File tempFile;

		for (int start = 0; start <= this.fileArgs.length - 2; start++) {
			tempFile = new File(Integer.toString(start) + ".txt");
			tempFile.createNewFile();
			fileArgs[start] = tempFile.getName();
		}

		fileArgs[4] = this.tempDir.getName();
	}

	@After
	public void tearDown() throws Exception {
		// Clean up temp files
		for (int i = 0; i < 4; i++) {
			File file = new File(i + ".txt");
			if (file.exists()) {
				file.delete();
			}
		}
		
		this.moveTool = null;
		Files.delete(newFileInWD.toPath());
		removeFiles(tempDir);
		Files.delete(tempDir.toPath());
		this.workingDir = null;
		this.tempDir = null;
	}

	// Black Box Positive Testing

	@Test
	public void getReturnMessage_FileMoved_StatusCodeZero() {

		final String returnMessage = this.moveTool.getReturnMessage(0);

		assertEquals(System.lineSeparator(), returnMessage);

	}

	@Test
	public void getReturnMessage_FileNotFound_StatusCodeOne() {

		final String returnMessage = this.moveTool.getReturnMessage(1);

		assertEquals(
				"move: '': No such file or directory" + System.lineSeparator(),
				returnMessage);

	}

	@Test
	public void getReturnMessage_MissingArguments_StatusCodeMinusOne() {

		final String returnMessage = this.moveTool.getReturnMessage(-1);

		assertEquals("move: missing file operand", returnMessage);

	}

	@Test
	public void getReturnMessage_MissingDestinationFile_StatusCodeMinusTwo() {

		final String[] args = { "1" };
		this.moveTool = new MoveTool(args);

		final String returnMessage = this.moveTool.getReturnMessage(-2);

		assertEquals("move: missing destination file operand after '1'",
				returnMessage);

	}

	@Test
	public void getReturnMessage_NotADirectory_StatusCodeMinusThree() {

		final String[] args = { "1", "3" };
		this.moveTool = new MoveTool(args);

		final String expectedMessage = "move: target '" + args[args.length - 1]
				+ "' is not a directory";
		final String returnMessage = this.moveTool.getReturnMessage(-3);

		assertEquals(expectedMessage, returnMessage);

	}

	@Test
	public void createFileObject_FileInWorkingDirectory() throws IOException {

		final File fileObj = moveTool.createFileObject(newFileInWD.getName(),
				workingDir);

		assertEquals(newFileInWD, fileObj);

	}

	@Test
	public void createFileObject_FileNotInWorkingDirectory() throws IOException {

		final File fileObj = this.moveTool.createFileObject(
				newFileNotInWD.toString(), this.workingDir);

		assertEquals(newFileNotInWD.getAbsolutePath(), fileObj.getAbsolutePath());

	}

	@Test
	public void isDirectory_IsADirectory_ReturnTrue() {

		final boolean isDirectory = moveTool.isDirectory(tempDir.toString());

		assertSame(true, isDirectory);
	}

	@Test
	public void isDirectory_NotADirectory_ReturnTrue() {

		final boolean isDirectory = moveTool.isDirectory(workingDir.toString()
				+ "/notADir");

		assertFalse(isDirectory == true);
	}

	@Test
	public void moveMultipleFiles() throws IOException {

		this.moveTool = new MoveTool(this.fileArgs);

		final String returnMessage = moveTool
				.moveMultipleFiles(this.workingDir);

		assertEquals("", returnMessage.trim());

	}

	// Helper Functions
	private void removeFiles(File directory) throws IOException {
		for (final File aFile : directory.listFiles()) {
			if (aFile.isDirectory()) {
				removeFiles(aFile);
			}

			Files.delete(aFile.toPath());
		}
	}
	
	private File createDir(String path) {
		File dir = new File(path);
		try {
			Files.createDirectories(dir.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dir;
	}
}
