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
		deletetool = new DeleteTool(new String[]{"hello"});
		deletetool.execute(null, null);
		assertEquals(2, deletetool.getStatusCode());
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
		deletetool.execute(tempWorkingDir, null);
		assertTrue(!tempFile.exists());
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
		deletetool.execute(tempWorkingDir, null);
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
		deletetool.execute(tempFile.getParentFile(), null);
		assertEquals(deletetool.getStatusCode(),2);
		Files.delete(tempFile.toPath());
	}
	
	/**
	 * Test error handling
	 * Deleting a file that does not exists
	 */
	@Test
	public void executeDeleteNoFileTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		deletetool = new DeleteTool(new String[]{"temp"});
		deletetool.execute(tempWorkingDir, null);
		assertEquals(deletetool.getStatusCode(),2);
	}

	/**
	 * Test error handling
	 * Deleting a directory
	 */
	@Test
	public void executeDeleteNotFileTest() throws IOException {
		//Test error-handling 5
		File tempDir = Files.createTempDirectory("temp").toFile();
		deletetool = new DeleteTool(tempDir.getAbsolutePath().split(" "));
		deletetool.execute(tempDir.getParentFile(), null);
		assertEquals(deletetool.getStatusCode(),0);
		assertFalse(tempDir.exists());
	}
}