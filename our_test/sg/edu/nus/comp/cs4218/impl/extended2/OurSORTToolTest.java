package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;

public class OurSORTToolTest {
	
	private ISortTool sorttool;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		sorttool = null;
	}

	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		sorttool = new SortTool(new String[]{"hello"});
		String result = sorttool.execute(null, null);
		assertEquals(1, sorttool.getStatusCode());
		assertEquals("Error: Cannot find working directory", result);
	}
	
	/**
	 * Test expected behavior
	 * Check if sorted of a unsorted string
	 * @throws IOException 
	 */
	@Test
	public void checkIfSortedTestUnsortedTest() throws IOException {
		sorttool = new SortTool(new String[]{});
		String temp = "TEST\ntest\nTEST";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.checkIfSorted(f1.getAbsolutePath());
		assertTrue(result.endsWith("2: disorder: test"));
	}
	
	/**
	 * Test expected behavior
	 * Execute if sorted option for a file using its name
	 */
	@Test
	public void executeTestOneFileCheckIfSorted2Test() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "apple\nHello world\nhello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{"-c",file.getName()});
		sorttool.execute(file.getParentFile(), null);
		assertEquals(sorttool.getStatusCode(), 0);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Reading from filename containing space
	 */
	@Test
	public void executeFileSpaceTest() throws IOException {
		File file = File.createTempFile("temp File", ".tmp");
		sorttool = new SortTool(new String[]{"-c",file.getAbsolutePath()});
		sorttool.execute(file.getParentFile(), null);
		assertEquals(sorttool.getStatusCode(),0);
		Files.delete(file.toPath());
	}

	/**
	 * Test error handling
	 * Execute with a directory instead of a file
	 */
	@Test
	public void executeFolderTest() throws IOException{
		File dir = Files.createTempDirectory("tempdir").toFile();
		sorttool = new SortTool(dir.getAbsolutePath().split(" "));
		sorttool.execute(dir.getParentFile(), null);
		assertEquals(sorttool.getStatusCode(),4);
		Files.delete(dir.toPath());
	}
}