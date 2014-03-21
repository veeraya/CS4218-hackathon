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

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;

public class OurCOPYToolTest {

	private ICopyTool copytool;
	private ArrayList<File> files;
	
	@Before
	public void setUp() throws Exception {
		files = new ArrayList<File>();
	}

	@After
	public void tearDown() throws Exception {
		copytool = null;
		for(int i = 0; i < files.size(); i ++){
			Files.delete(files.get(i).toPath());
		}
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
		files.add(tempFile);
		files.add(copiedFile);
		files.add(tempFolder);
		assertTrue(copiedFile.exists());
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
		files.add(tempFile);
		files.add(copiedFile);
		files.add(tempFolder);
		assertFalse(copytool.copy(tempFile, tempFolder));
	}
	
	/**
	 * Test error handling
	 * Invalid working directory
	 * @throws IOException 
	 */
	@Test
	public void executeInvalidWorkingDirTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{tempFile.getName(),tempFolder.getName()});
		copytool.execute(null, null);files.add(tempFile);
		files.add(tempFile);
		files.add(tempFolder);
		assertEquals(1, copytool.getStatusCode());
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
		copytool.execute(tempFile.getParentFile(), tempName);
		File copiedFile = new File(tempFolder, tempFile.getName());
		assertTrue(copiedFile.exists());
		files.add(tempFile);
		files.add(copiedFile);
		files.add(tempFolder);
		assertEquals(copytool.getStatusCode(),0);
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
		copytool.execute(tempFile.getParentFile(), null);
		File copiedFile = new File(dirName, tempFile.getName());
		assertTrue(copiedFile.exists());
		files.add(copiedFile);
		files.add(tempFile);
		assertEquals(copytool.getStatusCode(),0);
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
		copytool.execute(tempWorkingDir, null);
		File copiedFile = new File(toDirPath.toFile(), "temp File.tmp");
		assertTrue(copiedFile.exists());
		files.add(copiedFile);
		files.add(toDirPath.toFile());
		files.add(tempFileFrom.toFile());
		files.add(fromDirPath.toFile());
		assertEquals(copytool.getStatusCode(),0);
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
		copytool.execute(tempFile.getParentFile(), null);
		File copiedFile = new File(tempFolder.getParentFile(), tempFile.getName());
		assertTrue(copiedFile.exists());
		files.add(copiedFile);
		files.add(tempFile);
		files.add(tempFolder);
		assertEquals(copytool.getStatusCode(),0);
	}
	
	/**
	 * Test error handling
	 * No parameter in argument
	 */
	@Test
	public void executeNoParameterTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		copytool = new CopyTool(new String[]{});
		copytool.execute(tempWorkingDir, null);
		assertEquals(copytool.getStatusCode(),-1);
	}
	
	/**
	 * Test error handling
	 * Missing parameter in argument
	 */
	@Test
	public void executeOneParameterTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{tempFile.getName()});
		copytool.execute(tempFile.getParentFile(), null);
		files.add(tempFile);
		assertEquals(copytool.getStatusCode(),-2);
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
		copytool.execute(tempWorkingDir, null);
		files.add(toDirPath.toFile());
		files.add(tempFileFrom.toFile());
		files.add(fromDirPath.toFile());
		assertEquals(copytool.getStatusCode(),1);
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
		copytool.execute(tempFile.getParentFile(), null);
		files.add(tempFolder);
		files.add(tempFile);
		assertEquals(copytool.getStatusCode(),1);
	}
	
	/**
	 * Test error handling
	 * Invalid destination folder
	 */
	@Test
	public void executeInvalidDestNameTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{tempFile.getName(),  "Invalid Dest Folder"});
		copytool.execute(tempFile.getParentFile(), null);
		files.add(tempFile);
		assertEquals(copytool.getStatusCode(),2);
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
		copytool.execute(tempFolder1.getParentFile(), null);
		files.add(tempFolder1);
		files.add(tempFolder2);
		assertEquals(copytool.getStatusCode(),1);
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
		copytool.execute(tempFile1.getParentFile(), null);
		files.add(tempFile1);
		files.add(tempFile2);
		assertEquals(copytool.getStatusCode(),-4);
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
		copytool.execute(tempFile.getParentFile(), null);
		files.add(copiedFile);
		files.add(tempFolder);
		files.add(tempFile);
		assertEquals(copytool.getStatusCode(),2);
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
		files.add(tempFile);
		files.add(tempFolder);
		assertEquals(copytool.getStatusCode(),0);
	}
}