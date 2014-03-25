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
	 * Test error handling
	 * Invalid working directory
	 * @throws IOException 
	 */
	@Test
	public void executeInvalidWorkingDirTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[]{tempFile.getName(),tempFolder.getName()});
		copytool.execute(null, null);
		assertEquals(1, copytool.getStatusCode());
		Files.delete(tempFile.toPath());
		Files.delete(tempFolder.toPath());
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
		assertEquals(copytool.getStatusCode(),2);
		Files.delete(copiedFile.toPath());
		Files.delete(tempFolder.toPath());
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
		copytool.execute(tempWorkingDir, null);
		File copiedFile = new File(toDirPath.toFile(), "temp File.tmp");
		assertTrue(copiedFile.exists());
		assertEquals(copytool.getStatusCode(),0);
		Files.delete(copiedFile.toPath());
		Files.delete(toDirPath);
		Files.delete(tempFileFrom);
		Files.delete(fromDirPath);
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
		Files.delete(tempFile1.toPath());
		Files.delete(tempFile2.toPath());
		assertEquals(copytool.getStatusCode(),-4);
	}
	
	/**
	 * Test expected behavior
	 * Copy file to directory
	 * File contains space
	 */
	@Test
	public void executeFirstArg() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File parentDir = new File(System.getProperty("user.home"));
		Path fromDirPath = Files.createDirectories(new File(parentDir, "FromDir").toPath());
		Path toDirPath = Files.createDirectories(new File(parentDir, "ToDir").toPath());
		Path tempFileFrom = Files.createFile(new File(fromDirPath.toFile(), "temp File.tmp").toPath());
		String arg = tempFileFrom + " " + toDirPath;
		copytool = new CopyTool(arg.split(" "));
		copytool.execute(tempWorkingDir, null);
		File copiedFile = new File(toDirPath.toFile(), "temp File.tmp");
		assertTrue(copiedFile.exists());
		assertEquals(copytool.getStatusCode(),0);
		Files.delete(copiedFile.toPath());
		Files.delete(toDirPath);
		Files.delete(tempFileFrom);
		Files.delete(fromDirPath);
	}
	
	/**
	 * Test expected behavior
	 * Copy file to directory
	 * Both file and directory contains space
	 */
	@Test
	public void executeFileSpaceTest() throws IOException {
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
		assertEquals(copytool.getStatusCode(),0);
		Files.delete(copiedFile.toPath());
		Files.delete(toDirPath);
		Files.delete(tempFileFrom);
		Files.delete(fromDirPath);
	}
}