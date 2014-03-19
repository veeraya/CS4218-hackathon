/**
 * Assumption(s) Made: 
 * The Copy command only supports the following functions:
 * 
 * 1. Copy file1 destination file2
 * 2. Copy file1 into directory
 * 3. Copy multiple files into directory
 *
 * These functions are tested in the test cases below.
 * 
 */
package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;

public class CopyToolTest {
	// Variables
	private ICopyTool copyTool;
	private File workingDir;
	private File sourceDir;
	private File destDir;

	@Before
	public void setUp() throws Exception {
		this.copyTool = new CopyTool(null);
		this.workingDir = new File(System.getProperty("user.dir"));
		this.sourceDir = createDir("sourceDir");
		this.destDir = createDir("testDir");
	}

	@After
	public void tearDown() throws Exception {
		this.copyTool = null;

		this.removeFiles(this.sourceDir);
		this.removeFiles(this.destDir);
		Files.delete(this.sourceDir.toPath());
		Files.delete((this.destDir.toPath()));

		this.workingDir = null;
		this.sourceDir = null;
		this.destDir = null;
		
		File file = new File("copiedFolder");
		if (file.exists()) {
			deleteFolder(file);
		}
	}

	// Black Box Positive Testing
	@Test
	public void copy_FileToFileTest() throws IOException {

		File from = File
				.createTempFile("newFileToCopy", ".txt", this.sourceDir);
		File to = new File(this.destDir.toString() + "/newFileCopied.txt");

		boolean fileCopied = this.copyTool.copy(from, to);

		assertTrue(fileCopied == true);
		assertEquals(this.copyTool.getStatusCode(), 0);
	}

	@Test
	public void copy_FileToDirectoryTest() throws IOException {

		File from = File
				.createTempFile("newFileToCopy", ".txt", this.sourceDir);
		File directory = createDir("copiedFolder");

		boolean fileCopied = this.copyTool.copy(from, directory);

		assertTrue(fileCopied == true);
		assertEquals(this.copyTool.getStatusCode(), 0);

	}

	@Test
	public void execute_FileToFileTest() throws IOException {

		File source = File.createTempFile("newFileToCopy", ".txt",
				this.sourceDir);

		String[] args = { source.toString(),
				this.destDir.toString() + "/newFileToFile.txt" };

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);
		assertEquals(this.copyTool.getStatusCode(), 0);
		assertEquals(returnMessage, new String());

	}

	@Test
	public void execute_SingleFileToDirectoryTest() throws IOException {

		File source = File.createTempFile("newFileToCopy", ".txt",
				this.sourceDir);

		String[] args = { source.toString(), this.destDir.toString() };

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), 0);
		assertEquals(returnMessage, new String());

	}

	@Test
	public void execute_MultipleFilesToDirectoryTest() throws IOException {

		File aFile = null;

		String[] args = new String[5];

		for (int i = 0; i < args.length - 1; i++) {
			aFile = File.createTempFile("newFileToCopy_" + i + "_", ".txt",
					this.sourceDir);
			args[i] = aFile.toString();
		}

		args[args.length - 1] = this.destDir.toString();

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), 0);
		assertEquals(returnMessage, new String());

	}

	// Black Box Negative Testing

	@Test
	public void copy_FileNotFoundTest() throws IOException {

		File from = new File(this.sourceDir.toString() + "/fileNotFound.txt");
		File to = new File(this.destDir.toString());

		boolean fileCopied = this.copyTool.copy(from, to);

		assertFalse(fileCopied == true);
		assertEquals(this.copyTool.getStatusCode(), 1);
	}

	@Test
	public void copy_FileAlreadyExistsTest() throws IOException {

		File from = new File(this.sourceDir.toString()
				+ "/fileAlreadyExistsSource.txt");
		File to = new File(this.destDir.toString()
				+ "/fileAlreadyExistsCopied.txt");

		from.createNewFile();
		to.createNewFile();

		boolean fileCopied = this.copyTool.copy(from, to);

		assertFalse(fileCopied == true);
		assertEquals(this.copyTool.getStatusCode(), 2);

	}

	@Test
	public void execute_NoArgumentsTest() throws IOException {

		String[] args = {};

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), -1);
		assertEquals(returnMessage, "copy: missing file operand");

	}

	@Test
	public void execute_SingleArgumentTest() throws IOException {

		File source = File.createTempFile("newFile", ".txt", this.sourceDir);

		String[] args = { source.toString() };

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), -2);
		assertEquals(returnMessage,
				"copy: missing destination file operand after '" + args[0]
						+ "'");

	}

	@Test
	public void execute_SameArgumentsTest() throws IOException {

		File source = File.createTempFile("newFile", ".txt", this.sourceDir);

		String[] args = { source.toString(), source.toString() };

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), -3);
		assertEquals(returnMessage, "copy: '" + args[0] + "' and '" + args[1]
				+ "' are the same file");

	}

	// Black Box Negative Testing

	@Test
	public void execute_FileNotFoundTest() throws IOException {

		String[] args = { this.sourceDir.toString() + "\\fileNotFound.txt",
				this.destDir.toString() };

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), 1);
		assertEquals(returnMessage,
				"copy: 'fileNotFound.txt': No such file or directory");

	}

	@Test
	public void execute_FileAlreadyExistsTest() throws IOException {

		File source = File.createTempFile("newFileToCopy", ".txt",
				this.sourceDir);
		File dest = new File(this.destDir.toString()
				+ "\\fileAlreadyExists.txt");
		dest.createNewFile();

		String destinationPath = dest.getPath().toString().replace("\\", "/");

		String[] args = { source.toString(), destinationPath };

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), 2);
		assertEquals(returnMessage,
				"copy: 'fileAlreadyExists.txt': File already exists");

	}

	@Test
	public void execute_MultipleFilesToDirectory_FileNotFoundTest()
			throws IOException {

		String[] args = new String[5];

		args[0] = File.createTempFile("newFileToCopy-" + 0 + "-", ".txt",
				this.sourceDir).toString();
		args[1] = this.sourceDir.toString() + "\\fileNotFound1.txt";
		args[2] = File.createTempFile("newFileToCopy-" + 2 + "-", ".txt",
				this.sourceDir).toString();
		args[3] = this.sourceDir.toString() + "\\fileNotFound2.txt";
		args[4] = this.destDir.toString();

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(
				"copy: 'fileNotFound1.txt': No such file or directory" + System.lineSeparator() + System.lineSeparator() + "copy: 'fileNotFound2.txt': No such file or directory",
				returnMessage);

	}

	@Test
	public void execute_MultipleFilesToNonDirectoryTest() throws IOException {

		File aFile = null;

		String[] args = new String[5];

		for (int i = 0; i < args.length - 1; i++) {
			aFile = File.createTempFile("newFileToCopy_" + i + "_", ".txt",
					this.sourceDir);
			args[i] = aFile.toString();
		}

		args[args.length - 1] = this.sourceDir.toString() + "/NotADirectory";

		this.copyTool = new CopyTool(args);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), -4);
		assertEquals(returnMessage, "copy: target '" + args[args.length - 1]
				+ "' is not a directory");

	}

	// Helper Functions
	private void removeFiles(File directory) throws IOException {

		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				removeFiles(f);
			}

			Files.delete(f.toPath());
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
