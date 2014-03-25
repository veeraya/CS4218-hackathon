package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;

public class OurMOVEToolTest {

	private IMoveTool movetool;	
	private ArrayList<File> files;
	@Before
	public void setUp() throws Exception {
		files = new ArrayList<File>();
	}

	@After
	public void tearDown() throws Exception {
		movetool = null;
		for(int i = 0; i < files.size(); i ++){
			Files.delete(files.get(i).toPath());
		}
	}

	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		movetool = new MoveTool(new String[]{"hello","world"});
		movetool.execute(null, null);
		assertEquals(1, movetool.getStatusCode());
	}
	
	/**
	 * Test expected behavior
	 * Moving of file into folder
	 */
	@Test
	public void moveTest() throws IOException {
		movetool = new MoveTool(new String[]{});
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		assertTrue(movetool.move(tempFile, tempFolder));
		File movedFile = new File(tempFolder, tempFile.getName());
		assertFalse(tempFile.exists());
		assertTrue(movedFile.exists());
		files.add(movedFile);
		files.add(tempFolder);
	}

	
	/**
	 * Test error handling
	 * Directory for destination does not exist.
	 */
	@Test
	public void executeInvalidDestNameTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		movetool = new MoveTool(new String[]{tempFile.getName(), "Invalid Dest Folder"});
		movetool.execute(tempFile.getParentFile(), null);
		assertEquals(movetool.getStatusCode(),1);
		files.add(tempFile);
	}
	
	@Test
	public void executeFileExistedBefCopyTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		String tempName = tempFile.getName() + " " + tempFolder.getName();
		File copiedFile = new File(tempFolder.toString() + "\\" + tempFile.getName());
		Files.copy(tempFile.toPath(), copiedFile.toPath());
		assertTrue(copiedFile.exists());
		assertTrue(tempFile.exists());
		movetool = new MoveTool(tempName.split(" "));
		movetool.execute(tempFile.getParentFile(), null);
		assertEquals(movetool.getStatusCode(),1);
		assertTrue(tempFile.exists());
		files.add(tempFile);
		files.add(copiedFile);
		files.add(tempFolder);
	}
	
	/**
	 * Test expected
	 * Directory name contains space
	 */
	@Test
	public void executeDirHasSpaceTest() throws IOException {
		File tempFolder = Files.createTempDirectory("temp Folder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		String tempName = tempFile.getName() + " " + tempFolder.getName();
		File copiedFile = new File(tempFolder.toString() + "\\" + tempFile.getName());
		movetool = new MoveTool(tempName.split(" "));
		movetool.execute(tempFile.getParentFile(), null);
		assertEquals(movetool.getStatusCode(),0);
		assertTrue(tempFile.exists());
		files.add(tempFile);
		files.add(copiedFile);
		files.add(tempFolder);
	}
	
	/**
	 * Test expected
	 * Directory name contains space
	 */
	@Test
	public void executeSecArg() throws IOException {
		File tempFolder = Files.createTempDirectory("temp Folder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		String tempName = tempFile.getName() + " " + tempFolder.getName();
		File copiedFile = new File(tempFolder.toString() + "\\" + tempFile.getName());
		movetool = new MoveTool(tempName.split(" "));
		movetool.execute(tempFile.getParentFile(), null);
		assertEquals(movetool.getStatusCode(),0);
		assertTrue(tempFile.exists());
		files.add(tempFile);
		files.add(copiedFile);
		files.add(tempFolder);
	}
	
	/**
	 * Test expected
	 * File name contains space
	 */
	@Test
	public void executeFileHasSpaceTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("temp File", ".tmp").toFile();
		String tempName = tempFile.getName() + " " + tempFolder.getName();
		File copiedFile = new File(tempFolder.toString() + "\\" + tempFile.getName());
		movetool = new MoveTool(tempName.split(" "));
		movetool.execute(tempFile.getParentFile(), null);
		assertEquals(movetool.getStatusCode(),0);
		assertTrue(tempFile.exists());
		files.add(tempFile);
		files.add(copiedFile);
		files.add(tempFolder);
	}

}