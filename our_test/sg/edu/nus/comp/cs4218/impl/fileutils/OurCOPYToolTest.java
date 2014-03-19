package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;

public class OurCOPYToolTest {

	private ICopyTool copytool;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		copytool = null;
	}
	
	/**
	 * Test expected behavior
	 * Copying file to a folder
	 */
	@Test
	public void copyTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{});
		assertTrue(copytool.copy(tempFile, tempFolder));
		File copiedFile = new File(tempFolder, tempFile.getName());
		assertTrue(copiedFile.exists());
		Files.delete(tempFile.toPath());
		Files.delete(copiedFile.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * Copying the same file twice to the same location
	 */
	@Test
	public void copyExistedTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{});
		assertTrue(copytool.copy(tempFile, tempFolder));
		File copiedFile = new File(tempFolder, tempFile.getName());
		assertFalse(copytool.copy(tempFile, tempFolder));
		Files.delete(tempFile.toPath());
		Files.delete(copiedFile.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		copytool = new CopyTool(new String[]{});
		String result = copytool.execute(null, null);
		assertEquals(1, copytool.getStatusCode());
		assertEquals("Error: Cannot find working directory", result);
	}
	
	/**
	 * Test expected behavior
	 * Copy file to sub directory
	 */
	@Test
	public void executeCopyToSubDirTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		String tempName = tempFile.getName() + " " + tempFolder.getName();
		copytool = new CopyTool(tempName.split(" "));
		String msg = copytool.execute(tempFile.getParentFile(), tempName);
		File copiedFile = new File(tempFolder, tempFile.getName());
		assertTrue(copiedFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(copytool.getStatusCode(),0);
		Files.delete(tempFile.toPath());
		Files.delete(copiedFile.toPath());
		Files.delete(tempFolder.toPath());
	}

	/**
	 * Test expected behavior
	 * Copy file to directory using absolute path
	 */
	@Test
	public void executeCopyToOtherDirTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File dirName = new File(System.getProperty("user.home"));
		copytool = new CopyTool(new String[]{tempFile.getName(),dirName.getAbsolutePath()});
		String msg = copytool.execute(tempFile.getParentFile(), null);
		File copiedFile = new File(dirName, tempFile.getName());
		assertTrue(copiedFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(copytool.getStatusCode(),0);
		Files.delete(copiedFile.toPath());
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Copy file to directory
	 * Both file and directory contains space
	 */
	@Test
	public void executeContainsSpaceTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File parentDir = new File(System.getProperty("user.home"));
		Path fromDirPath = Files.createDirectories(new File(parentDir, "From Dir").toPath());
		Path toDirPath = Files.createDirectories(new File(parentDir, "To Dir").toPath());
		Path tempFileFrom = Files.createFile(new File(fromDirPath.toFile(), "temp File.tmp").toPath());
		String arg = tempFileFrom + " " + toDirPath;
		copytool = new CopyTool(arg.split(" "));
		String msg = copytool.execute(tempWorkingDir, null);
		File copiedFile = new File(toDirPath.toFile(), "temp File.tmp");
		assertTrue(copiedFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(copytool.getStatusCode(),0);
		Files.delete(copiedFile.toPath());
		Files.delete(toDirPath);
		Files.delete(tempFileFrom);
		Files.delete(fromDirPath);
	}
	
	/**
	 * Test expected behavior
	 * Copy file from a folder to the folder's parent directory
	 */
	@Test
	public void executeCopyToParentDir() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createFile(new File(tempFolder, "tempFile.tmp").toPath()).toFile();
		copytool = new CopyTool(new String[]{tempFile.getName(),".."});
		String msg = copytool.execute(tempFile.getParentFile(), null);
		File copiedFile = new File(tempFolder.getParentFile(), tempFile.getName());
		assertTrue(copiedFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(copytool.getStatusCode(),0);
		Files.delete(copiedFile.toPath());
		Files.delete(tempFile.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * No parameter in argument
	 */
	@Test
	public void executeNoParameterTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		copytool = new CopyTool(new String[]{});
		String msg = copytool.execute(tempWorkingDir, null);
		assertTrue(copytool.getStatusCode() != 0);
		assertTrue(msg.equals("Error: Missing parameter for SOURCE DEST"));
		assertEquals(copytool.getStatusCode(),1);
	}
	
	/**
	 * Test error handling
	 * Missing parameter in argument
	 */
	@Test
	public void executeOneParameterTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{tempFile.getName()});
		String msg = copytool.execute(tempFile.getParentFile(), null);
		assertTrue(copytool.getStatusCode() != 0);
		assertTrue(msg.equals("Error: Missing parameter for DEST"));
		assertEquals(copytool.getStatusCode(),1);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test error handling
	 * Extra parameter in argument
	 */
	@Test
	public void executeExtraParameterTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File parentDir = new File(System.getProperty("user.home"));
		Path fromDirPath = Files.createDirectories(new File(parentDir, "From Dir").toPath());
		Path toDirPath = Files.createDirectories(new File(parentDir, "To Dir").toPath());
		Path tempFileFrom = Files.createFile(new File(fromDirPath.toFile(), "temp File.tmp").toPath());
		String arg = tempFileFrom + " " + toDirPath + " ExtraParamter";
		copytool = new CopyTool(arg.split(" "));
		String msg = copytool.execute(tempWorkingDir, null);
		assertTrue(msg.equals("Error: Extra parameter found in DEST"));
		assertEquals(copytool.getStatusCode(),1);
		Files.delete(toDirPath);
		Files.delete(tempFileFrom);
		Files.delete(fromDirPath);
	}
	
	/**
	 * Test error handling
	 * Wrong source file name
	 */
	@Test
	public void executeInvalidSourceNameTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{"tempFile.tmp", tempFolder.getName()});
		String msg = copytool.execute(tempFile.getParentFile(), null);
		assertTrue(msg.equals("Error: SOURCE file not found"));
		assertEquals(copytool.getStatusCode(),1);
		Files.delete(tempFolder.toPath());
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test error handling
	 * Invalid destination folder
	 */
	@Test
	public void executeInvalidDestNameTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{tempFile.getName(),  "Invalid Dest Folder"});
		String msg = copytool.execute(tempFile.getParentFile(), null);
		assertTrue(msg.equals("Error: DEST directory not found"));
		assertEquals(copytool.getStatusCode(),1);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test error handling
	 * Source file is a directory instead of file
	 */
	@Test
	public void executeSourceIsDirTest() throws IOException {
		File tempFolder1 = Files.createTempDirectory("tempFolder").toFile();
		File tempFolder2 = Files.createTempDirectory("tempFolder").toFile();
		copytool = new CopyTool(new String[]{tempFolder1.getName(),  tempFolder2.getName()});
		String msg = copytool.execute(tempFolder1.getParentFile(), null);
		assertTrue(msg.equals("Error: SOURCE is not a file"));
		assertEquals(copytool.getStatusCode(),1);
		Files.delete(tempFolder1.toPath());
		Files.delete(tempFolder2.toPath());
	}
	
	/**
	 * Test error handling
	 * Destination file is a file instead of directory
	 */
	@Test
	public void executeDestIsFileTest() throws IOException {
		File tempFile1 = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempFile2 = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{tempFile1.getName(),  tempFile2.getName()});
		String msg = copytool.execute(tempFile1.getParentFile(), null);
		assertTrue(msg.equals("Error: DEST is not a directory"));
		assertEquals(copytool.getStatusCode(),1);
		Files.delete(tempFile1.toPath());
		Files.delete(tempFile2.toPath());
	}
	
	/**
	 * Test error handling
	 * Destination folder contains file with the same name
	 */
	@Test
	public void executeFileExistedBefCopyTest() throws IOException {
		//Test error-handling 8
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{tempFile.getName(),  tempFolder.getName()});
		copytool.execute(tempFile.getParentFile(), null);
		File copiedFile = new File(tempFolder, tempFile.getName());
		assertTrue(copiedFile.exists());
		String msg = copytool.execute(tempFile.getParentFile(), null);
		assertTrue(msg.equals("Error: A file with the same name exists in the directory"));
		assertEquals(copytool.getStatusCode(),1);
		Files.delete(copiedFile.toPath());
		Files.delete(tempFolder.toPath());
		Files.delete(tempFile.toPath());
	}
	
	/*
	 * Test expected behavior
	 * Copy file to the current working director
	 */
	@Test
	public void executeFileSameLocationTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createFile(new File(tempFolder, "tempFile.tmp").toPath()).toFile();
		copytool = new CopyTool(new String[]{"tempFile.tmp", "..\\" + tempFolder.getName()});
		copytool.execute(tempFile.getParentFile(), null);
		assertTrue(new File(tempFolder, "tempFile.tmp").exists());
		Files.delete(tempFile.toPath());
		Files.delete(tempFolder.toPath());
	}
}