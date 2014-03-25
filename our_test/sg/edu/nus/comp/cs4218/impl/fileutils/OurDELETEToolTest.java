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
	 * Delete a file in working directory
	 */
	@Test
	public void executeDeleteFileByNameTest() throws IOException {
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
}