package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CopyToolHelperMethodsTest {
	// Variables
	private CopyTool copyTool;
	private File workingDir;
	private File tempDir;
	private File newFileNotInWD;
	private File newFileInWD;
	private String[] fileArgs;

	@Before
	public void setUp() throws Exception {
		this.copyTool = new CopyTool(null);
		this.workingDir = new File(System.getProperty("user.dir"));
		this.tempDir = createDir("tempDir");

		this.newFileInWD = new File(this.workingDir.toString()
				+ File.separator + "newFileInWorkingDir.txt");
		this.newFileInWD.createNewFile();

		this.newFileNotInWD = new File(this.tempDir.toString()
				+ File.separator + "newFileNotInWorkingDir.txt");
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
		this.copyTool = null;
		
		Files.delete(newFileInWD.toPath());
		removeFiles(tempDir);
		deleteFile(tempDir.getAbsolutePath());
		deleteFile(workingDir.toString() + "/0.txt");
		deleteFile(workingDir.toString() + "/1.txt");
		deleteFile(workingDir.toString() + "/2.txt");
		deleteFile(workingDir.toString() + "/3.txt");
		
		this.workingDir = null;
		this.tempDir = null;
	}

	// Black Box Positive Testing

	@Test
	public void getReturnMessage_FileCopied_StatusCodeZero() {

		final String returnMessage = this.copyTool.getReturnMessage(0);

		assertEquals(System.lineSeparator(), returnMessage);

	}

	@Test
	public void getReturnMessage_FileNotFound_StatusCodeOne() {

		final String returnMessage = this.copyTool.getReturnMessage(1);

		assertEquals(
				"copy: '': No such file or directory" + System.lineSeparator(),
				returnMessage);

	}

	@Test
	public void getReturnMessage_FileAlreadyExists_StatusCodeTwo() {

		final String returnMessage = this.copyTool.getReturnMessage(2);

		assertEquals("copy: '': File already exists", returnMessage);

	}

	@Test
	public void getReturnMessage_MissingArguments_StatusCodeMinusOne() {

		final String returnMessage = this.copyTool.getReturnMessage(-1);

		assertEquals("copy: missing file operand", returnMessage);

	}

	@Test
	public void getReturnMessage_MissingDestinationFile_StatusCodeMinusTwo() {

		final String[] args = { "1" };
		this.copyTool = new CopyTool(args);

		final String returnMessage = this.copyTool.getReturnMessage(-2);

		assertEquals("copy: missing destination file operand after '1'",
				returnMessage);

	}

	@Test
	public void getReturnMessage_NotADirectory_StatusCodeMinusThree() {

		final String[] args = { "1", "1" };
		this.copyTool = new CopyTool(args);

		final String expectedMessage = "copy: '" + args[0] + "' and '"
				+ args[1] + "' are the same file";
		final String returnMessage = this.copyTool.getReturnMessage(-3);

		assertEquals(expectedMessage, returnMessage);

	}

	@Test
	public void getReturnMessage_NotADirectory_StatusCodeMinusFour() {

		final String[] args = { "1", "3" };
		this.copyTool = new CopyTool(args);

		final String expectedMessage = "copy: target '" + args[args.length - 1]
				+ "' is not a directory";
		final String returnMessage = this.copyTool.getReturnMessage(-4);

		assertEquals(expectedMessage, returnMessage);

	}

	@Test
	public void createFileObject_FileInWorkingDirectory() throws IOException {

		final File fileObj = copyTool.createFileObject(newFileInWD.getName(),
				workingDir);

		assertEquals(newFileInWD, fileObj);

	}

	@Test
	public void createFileObject_FileNotInWorkingDirectory() throws IOException {

		File fileObj = this.copyTool.createFileObject(
				newFileNotInWD.toString(), this.workingDir);

		assertEquals(newFileNotInWD.getAbsolutePath(), fileObj.getAbsolutePath());

	}

	@Test
	public void isDirectory_IsADirectory_ReturnTrue() {

		final boolean isDirectory = copyTool.isDirectory(tempDir.toString());

		assertSame(true, isDirectory);

	}

	@Test
	public void isDirectory_NotADirectory_ReturnTrue() {

		final boolean isDirectory = copyTool.isDirectory(workingDir.toString()
				+ "/notADir");

		assertNotSame(true, isDirectory);
	}

	@Test
	public void copyMultipleFiles() throws IOException {

		this.copyTool = new CopyTool(this.fileArgs);

		final String returnMessage = copyTool.copyMultipleFiles(workingDir);

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
	
	private void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

}
