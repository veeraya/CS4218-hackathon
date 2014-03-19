package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;

public class OurMOVEToolTest {

	private IMoveTool movetool;	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		movetool = null;
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
		assertTrue(!tempFile.exists());
		assertTrue(movedFile.exists());
		Files.delete(movedFile.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * Moving of a non-existing file to a directory
	 */
	@Test
	public void moveInvalidFileTest() throws IOException{
		movetool = new MoveTool(new String[]{});
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = null;
		assertFalse(movetool.move(tempFile, tempFolder));
		Files.delete(tempFolder.toPath());
	}

	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		movetool = new MoveTool(new String[]{});
		String result = movetool.execute(null, null);
		assertEquals(1, movetool.getStatusCode());
		assertEquals("Error: Cannot find working directory", result);
	}
	
	/**
	 * Test expected behavior
	 * Move file into sub-directory of the same parent folder
	 */
	@Test
	public void executeMoveToSubDirTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		String tempName = tempFile.getName() + " " + tempFolder.getName();
		movetool = new MoveTool(tempName.split(" "));
		String msg = movetool.execute(tempFile.getParentFile(), null);
		File movedFile = new File(tempFolder, tempFile.getName());
		assertTrue(movedFile.exists());
		assertTrue(!tempFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(movetool.getStatusCode(),0);
		Files.delete(movedFile.toPath());
		Files.delete(tempFolder.toPath());
	}

	/**
	 * Test expected behavior
	 * Moving file to directory using absolute path
	 */
	@Test
	public void executeMoveToOtherDirTest() throws IOException {
		//Test expected behavior
		//Create a tmp file and copied to root directory
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File dirName = new File(System.getProperty("user.home"));
		movetool = new MoveTool(new String[]{tempFile.getName(), dirName.getAbsolutePath()});
		String msg = movetool.execute(tempFile.getParentFile(), tempFile.getName() + " " + dirName);
		File movedFile = new File(dirName, tempFile.getName());
		assertTrue(movedFile.exists());
		assertTrue(!tempFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(movetool.getStatusCode(),0);
		Files.delete(movedFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Moving file into folder.
	 * Both file name and folder name contains space
	 */
	@Test
	public void executeContainsSpaceTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File parentDir = new File(System.getProperty("user.home"));
		Path fromDirPath = Files.createDirectories(new File(parentDir, "From Dir").toPath());
		Path toDirPath = Files.createDirectories(new File(parentDir, "To Dir").toPath());
		Path tempFileFrom = Files.createFile(new File(fromDirPath.toFile(), "temp File.tmp").toPath());
		String arg = tempFileFrom + " " + toDirPath;
		movetool = new MoveTool(arg.split(" "));
		String msg = movetool.execute(tempWorkingDir, null);
		File movedFile = new File(toDirPath.toFile(), "temp File.tmp");
		assertTrue(movedFile.exists());
		assertTrue(!tempFileFrom.toFile().exists());
		assertTrue(msg.equals(""));
		assertEquals(movetool.getStatusCode(),0);
		Files.delete(movedFile.toPath());
		Files.delete(toDirPath);
		Files.delete(fromDirPath);
	}
	
	/**
	 * Test expected behavior
	 * Move a file in a folder to the parent directory of the folder
	 */
	@Test
	public void executeMoveToParentDir() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createFile(new File(tempFolder, "tempFile.tmp").toPath()).toFile();
		movetool = new MoveTool(new String[]{tempFile.getName(),".."});
		String msg = movetool.execute(tempFile.getParentFile(), null);
		File movedFile = new File(tempFolder.getParentFile(), tempFile.getName());
		assertTrue(movedFile.exists());
		assertTrue(!tempFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(movetool.getStatusCode(),0);
		Files.delete(movedFile.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * No parameter being entered
	 */
	@Test
	public void executeNoParameterTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		movetool = new MoveTool(new String[]{});
		String msg = movetool.execute(tempWorkingDir, null);
		assertEquals(movetool.getStatusCode(),1);
		assertTrue(msg.equals("Error: Missing parameter for SOURCE DEST"));
	}
	
	/**
	 * Test error handling
	 * Missing parameter for [DEST]
	 */
	@Test
	public void executeOneParameterTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		movetool = new MoveTool(new String[]{tempFile.getPath()});
		String msg = movetool.execute(tempFile.getParentFile(), null);
		assertEquals(movetool.getStatusCode(),1);
		assertTrue(msg.equals("Error: Missing parameter for DEST"));
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test error handling
	 * Extra parameter found argument
	 */
	@Test
	public void executeExtraParameterTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File parentDir = new File(System.getProperty("user.home"));
		Path fromDirPath = Files.createDirectories(new File(parentDir, "From Dir").toPath());
		Path toDirPath = Files.createDirectories(new File(parentDir, "To Dir").toPath());
		Path tempFileFrom = Files.createFile(new File(fromDirPath.toFile(), "temp File.tmp").toPath());
		String arg = tempFileFrom + " " + toDirPath + " ExtraParamter";
		movetool = new MoveTool(arg.split(" "));
		String msg = movetool.execute(tempWorkingDir, null);
		assertTrue(msg.equals("Error: Extra parameter found in DEST"));
		assertEquals(movetool.getStatusCode(),1);
		assertTrue(tempFileFrom.toFile().exists());
		File movedFile = new File(toDirPath.toString() + "\\" + tempFileFrom.getFileName().toString());
		assertTrue(!movedFile.exists());
		Files.delete(toDirPath);
		Files.delete(tempFileFrom);
		Files.delete(fromDirPath);
	}
	
	/**
	 * Test error handling
	 * File for source does not exist.
	 */
	@Test
	public void executeInvalidSourceNameTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		movetool = new MoveTool(new String[]{"tempFile.tmp", tempFolder.getPath()});
		String msg = movetool.execute(tempFile.getParentFile(), null);
		assertTrue(msg.equals("Error: SOURCE file not found"));
		assertEquals(movetool.getStatusCode(),1);
		Files.delete(tempFolder.toPath());
		Files.delete(tempFile.toPath());
	}

	/**
	 * Test error handling
	 * Directory for destination does not exist.
	 */
	@Test
	public void executeInvalidDestNameTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		movetool = new MoveTool(new String[]{tempFile.getName(), "Invalid Dest Folder"});
		String msg = movetool.execute(tempFile.getParentFile(), null);
		assertTrue(msg.equals("Error: DEST directory not found"));
		assertEquals(movetool.getStatusCode(),1);
		Files.delete(tempFile.toPath());
	}

	/**
	 * Test error handling
	 * Source file is a directory instead of a file
	 */
	@Test
	public void executeSourceIsDirTest() throws IOException {
		File tempFolder1 = Files.createTempDirectory("tempFolder").toFile();
		File tempFolder2 = Files.createTempDirectory("tempFolder").toFile();
		movetool = new MoveTool(new String[]{tempFolder1.getName(),tempFolder2.getName()});
		String msg = movetool.execute(tempFolder1.getParentFile(), null);
		assertTrue(msg.equals("Error: SOURCE is not a file"));
		assertEquals(movetool.getStatusCode(),1);
		Files.delete(tempFolder1.toPath());
		Files.delete(tempFolder2.toPath());
	}
	

	/**
	 * Test error handling
	 * Destination is a file instead of a directory
	 */
	@Test
	public void executeDestIsFileTest() throws IOException {
		File tempFile1 = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempFile2 = Files.createTempFile("tempFile", ".tmp").toFile();
		movetool = new MoveTool(new String[]{tempFile1.getName(),tempFile2.getName()});
		String msg = movetool.execute(tempFile1.getParentFile(), null);
		assertTrue(msg.equals("Error: DEST is not a directory"));
		assertEquals(movetool.getStatusCode(),1);
		Files.delete(tempFile1.toPath());
		Files.delete(tempFile2.toPath());
	}
	
	/**
	 * Test error handling
	 * File already existed before moving to directory
	 */
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
		String msg = movetool.execute(tempFile.getParentFile(), null);
		assertTrue(msg.equals("Error: A file with the same name exists in the directory"));
		assertEquals(movetool.getStatusCode(),1);
		assertTrue(tempFile.exists());
		Files.delete(tempFile.toPath());
		Files.delete(copiedFile.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test expected behavior
	 * File is being moved to the same directory
	 */
	@Test
	public void executeFileSameLocationTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createFile(new File(tempFolder, "tempFile.tmp").toPath()).toFile();
		movetool = new MoveTool(new String[]{"tempFile.tmp", "..\\"+tempFolder.getName()});
		movetool.execute(tempFile.getParentFile(), null);
		assertTrue(new File(tempFolder, "tempFile.tmp").exists());
		Files.delete(tempFile.toPath());
		Files.delete(tempFolder.toPath());
	}
}