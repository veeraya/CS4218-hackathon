package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;

public class OurDELETEToolTest {

	private IDeleteTool deletetool;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		deletetool = null;
	}

	/**
	 * Test expected behavior
	 * Delete a file
	 */
	@Test
	public void deleteTest() throws IOException {
		deletetool = new DeleteTool(new String[]{});
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		assertTrue(tempFile.exists());
		assertTrue(deletetool.delete(tempFile));
		assertTrue(!tempFile.exists());
	}
	
	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		deletetool = new DeleteTool(new String[]{});
		String result = deletetool.execute(null, null);
		assertEquals(1, deletetool.getStatusCode());
		assertEquals("Error: Cannot find working directory", result);
	}
	
	/**
	 * Test error handling
	 * Delete file that does not exist
	 */
	@Test
	public void deleteInvalidFileTest() throws IOException {
		File tempFile = new File(System.getProperty("java.io.tmpdir"), "temp File.tmp");
		deletetool = new DeleteTool(new String[]{});
		assertFalse(deletetool.delete(tempFile));
	}
	
	/**
	 * Test expected behavior
	 * Delete a file in working directory
	 */
	@Test
	public void executeDeleteChildFileTest() throws IOException {
		File tempFile = Files.createTempFile("tempFile",".tmp").toFile();
		deletetool = new DeleteTool(new String[]{tempFile.getName()});
		String msg = deletetool.execute(tempFile.getParentFile(), null);
		assertTrue(!tempFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(deletetool.getStatusCode(),0);
	}

	/**
	 * Test expected behavior
	 * Delete a file in other directory using absolute path
	 */
	@Test
	public void executeDeleteFileOtherDirTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File parentDir = new File(System.getProperty("user.home"));
		File tempFile = Files.createFile(new File(parentDir, "temp File.tmp").toPath()).toFile();
		deletetool = new DeleteTool(tempFile.getAbsolutePath().split(" "));
		String msg = deletetool.execute(tempWorkingDir, null);
		assertTrue(!tempFile.exists());
		assertTrue(msg.equals(""));
		assertEquals(deletetool.getStatusCode(),0);
	}

	/**
	 * Test error handling
	 * No parameter in the argument
	 */
	@Test
	public void executeDeleteNoParameterTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		deletetool = new DeleteTool(new String[]{});
		String msg = deletetool.execute(tempWorkingDir, null);
		assertTrue(msg.equals("Error: Missing parameter for file"));
		assertEquals(deletetool.getStatusCode(),1);
	}
	
	/**
	 * Test error handling
	 * Extra parameter in the argument
	 */
	@Test
	public void executeDeleteExtraParameterTest() throws IOException {
		File tempFile = Files.createTempFile("temp File", ".tmp").toFile();
		String arg = tempFile.getName() + " ExtraParamter";
		deletetool = new DeleteTool(arg.split(" "));
		String msg = deletetool.execute(tempFile.getParentFile(), null);
		assertTrue(msg.equals("Error: Extra parameter found"));
		assertEquals(deletetool.getStatusCode(),1);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test error handling
	 * Deleting a file that does not exists
	 */
	@Test
	public void executeDeleteNoFileTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		deletetool = new DeleteTool(new String[]{"temp", "File.tmp"});
		String msg = deletetool.execute(tempWorkingDir, null);
		assertTrue(msg.equals("Error: File not found"));
		assertEquals(deletetool.getStatusCode(),1);
	}

	/**
	 * Test error handling
	 * Deleting a directory
	 */
	@Test
	public void executeDeleteNotFileTest() throws IOException {
		//Test error-handling 5
		File tempDir = Files.createTempDirectory("temp Dir.tmp").toFile();
		deletetool = new DeleteTool(tempDir.getName().split(" "));
		String msg = deletetool.execute(tempDir.getParentFile(), null);
		assertTrue(msg.equals("Error: Object to be deleted is not a file"));
		assertEquals(deletetool.getStatusCode(),1);
		Files.delete(tempDir.toPath());
	}
}