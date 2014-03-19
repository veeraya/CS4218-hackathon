/**
 * Assumption(s) Made: 
 * The Move command only supports the following functions:
 * 
 * 1. Move file1 to file2
 * 2. Move directory dir1 to dir2
 * 3. Move multiple files into directory
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

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;

public class MoveToolTest {
	// Variables
	private IMoveTool moveTool;
	private File workingDir;
	private File sourceDir;
	private File destDir;

	@Before
	public void setUp() throws Exception {
		this.moveTool = new MoveTool(null);
		this.workingDir = new File(System.getProperty("user.dir"));
		this.sourceDir = createDir("_source_dir");
		this.destDir = createDir("_dest_dir");
	}

	@After
	public void tearDown() throws Exception {
		this.moveTool = null;

		this.removeFiles(this.sourceDir);
		this.removeFiles(this.destDir);
		this.sourceDir.delete();
		this.destDir.delete();

		this.workingDir = null;
		this.sourceDir = null;
		this.destDir = null;
	}

	// Black Box Positive Testing

	@Test
	public void move_renameFileTest() throws IOException {

		File from = File.createTempFile("newMoveFile", ".txt", this.sourceDir);
		File to = new File(this.sourceDir.toString() + "/renamedMovedFile.txt");

		boolean fileMoved = this.moveTool.move(from, to);

		assertTrue(fileMoved == true);
		assertEquals(this.moveTool.getStatusCode(), 0);

	}

	@Test
	public void move_renameDirectoryTest() throws IOException {

		File from = createDir("_source_dir");
		File to = new File("renamedDir");

		boolean fileMoved = this.moveTool.move(from, to);

		assertTrue(fileMoved == true);
		assertEquals(0, this.moveTool.getStatusCode());

		to.delete();

	}

	@Test
	public void execute_renameFileTest() throws IOException {

		File from = File.createTempFile("newMoveFile", ".txt", this.sourceDir);
		File to = new File(this.sourceDir.toString() + "/renamedMovedFile.txt");

		String[] args = { from.toString(), to.toString() };

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), 0);
		assertEquals(returnMessage, new String());

		to.delete();
	}

	@Test
	public void execute_renameDirectoryTest() throws IOException {

		File from = createDir("_source_dir");
		File to = new File("renamedDir");

		String[] args = { from.toString(), to.toString() };

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), 0);
		assertEquals(returnMessage, new String());

	}

	@Test
	public void execute_FilesToDirectoryTest() throws IOException {

		File aFile = null;

		String[] args = new String[5];

		for (int i = 0; i < args.length - 1; i++) {
			aFile = File.createTempFile("newFileToMove_" + i + "_", ".txt",
					this.sourceDir);
			args[i] = aFile.toString();
		}

		args[args.length - 1] = this.destDir.toString();

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), 0);
		assertEquals(returnMessage, new String());

	}

	// Black Box Negative Testing

	@Test
	public void execute_NoArgumentsTest() throws IOException {

		String[] args = {};

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), -1);
		assertEquals(returnMessage, "move: missing file operand");

	}

	@Test
	public void execute_SingleArgumentTest() throws IOException {

		File from = File.createTempFile("newMoveFile", ".txt", this.sourceDir);

		String[] args = { from.toString() };

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), -2);
		assertEquals(returnMessage,
				"move: missing destination file operand after '" + args[0]
						+ "'");

	}

	@Test
	public void execute_renameFile_FileNotFoundTest() throws IOException {

		File from = new File(this.sourceDir.toString() + "/fileNotFound.txt");
		File to = new File(this.sourceDir.toString() + "/renamedMovedFile.txt");

		String[] args = { from.toString(), to.toString() };

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), 1);
		assertEquals(returnMessage, "move: '" + from.getName()
				+ "': No such file or directory");

	}

	@Test
	public void execute_renameDirectory_FileNotFoundTest() throws IOException {

		File from = new File(this.sourceDir.toString() + "\\dirNotFound");
		File to = new File("renamedDir");

		String[] args = { from.toString(), to.toString() };

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), 1);
		assertEquals(returnMessage, "move: '" + from.getName()
				+ "': No such file or directory");

	}

	@Test
	public void execute_FilesToNonDirectoryTest() throws IOException {

		File aFile = null;

		String[] args = new String[5];

		for (int i = 0; i < args.length - 1; i++) {
			aFile = File.createTempFile("newFileToMove_" + i + "_", ".txt",
					this.sourceDir);
			args[i] = aFile.toString();
		}

		args[args.length - 1] = this.destDir.toString() + "/notADir";

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), -3);
		assertEquals(returnMessage, "move: target '" + args[args.length - 1]
				+ "' is not a directory");

	}

	@Test
	public void execute_MultipleFilesToDirectory_FileNotFoundTest()
			throws IOException {

		String[] args = new String[5];

		args[0] = File.createTempFile("newFileToMove-" + 0 + "-", ".txt",
				this.sourceDir).toString();
		args[1] = this.sourceDir.toString() + "\\fileNotFound1.txt";
		args[2] = File.createTempFile("newFileToMove-" + 2 + "-", ".txt",
				this.sourceDir).toString();
		args[3] = this.sourceDir.toString() + "\\fileNotFound2.txt";
		args[4] = this.destDir.toString();

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(this.workingDir, null);

		assertEquals("move: 'fileNotFound1.txt': No such file or directory"
				+ System.lineSeparator() + System.lineSeparator()
				+ "move: 'fileNotFound2.txt': No such file or directory",
				returnMessage);
	}

	// Helper Functions
	private void removeFiles(File directory) throws IOException {

		if (directory.listFiles() != null) {
			for (File f : directory.listFiles()) {
				if (f.isDirectory()) {
					removeFiles(f);
				}

				f.delete();
			}
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
