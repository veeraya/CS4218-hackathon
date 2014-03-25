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
	 * Test expected behavior
	 * Help method
	 */
	@Test
	public void testGetHelpTest() {
		sorttool = new SortTool(new String[]{});
		String result = sorttool.getHelp();
		assertTrue(result.startsWith("sort : sort lines of text files"));
		assertTrue(result.endsWith("-help : Brief information about supported options"));
		assertTrue(result.contains("FILE - Name of the file"));
		assertTrue(result.contains("-c : Check whether the given file is already sorted,"));
	}

	/**
	 * Test expected behavior
	 * Sort arrangement of string
	 * @throws IOException 
	 */
	@Test
	public void sortFileTestArrangementTest() throws IOException {
		sorttool = new SortTool(new String[]{});
		String temp = "test\nTest\nTEst\nTESt\nTEST\ntEST\nteST\ntesT\n";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.sortFile(f1.getAbsolutePath());
		assertEquals(result, "TEST\r\nTESt\r\nTEst\r\nTest\r\ntEST\r\nteST\r\ntesT\r\ntest");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement of string with duplicate of the same statement
	 * @throws IOException 
	 */
	@Test
	public void sortFileTestDuplicateTest() throws IOException{
		sorttool = new SortTool(new String[]{});
		String temp = "test\nTEST\ntest\napple\nApple\n";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.sortFile(f1.getAbsolutePath());
		assertEquals(result, "Apple\r\nTEST\r\napple\r\ntest\r\ntest");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement based on length of string
	 * @throws IOException 
	 */
	@Test
	public void sortFileLengthTest() throws IOException{
		sorttool = new SortTool(new String[]{});
		String temp = "bananas\nbanana\nBanana";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.sortFile(f1.getAbsolutePath());
		assertEquals(result, "Banana\r\nbanana\r\nbananas");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement based on length of string
	 * @throws IOException 
	 */
	@Test
	public void sortFileLengthCharTest() throws IOException{
		sorttool = new SortTool(new String[]{});
		String temp = "bacc\nabc\nhello\nbac\nbaad\nBAcc";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.sortFile(f1.getAbsolutePath());
		assertEquals(result, "BAcc\r\nabc\r\nbaad\r\nbac\r\nbacc\r\nhello");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement of string and special character
	 * @throws IOException 
	 */
	@Test
	public void sortFileTestSpecialCharacterTest() throws IOException{
		sorttool = new SortTool(new String[]{});
		String temp = "test\nTEST\n!!!\n@.@\n死\n";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.sortFile(f1.getAbsolutePath());
		assertEquals(result, "!!!\r\n@.@\r\nTEST\r\ntest\r\n死");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement of string and blank statement
	 * @throws IOException 
	 */
	@Test
	public void sortFileTestBlankLinesTest() throws IOException{
		sorttool = new SortTool(new String[]{});
		String temp = "test\n\n\nTEST\n";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.sortFile(f1.getAbsolutePath());
		assertEquals(result, "\r\n\r\nTEST\r\ntest");
	}

	/**
	 * Test expected behavior
	 * Sort arrangement of sorted string
	 * @throws IOException 
	 */
	@Test
	public void sortFileTestSortedAlreadyTest() throws IOException{
		sorttool = new SortTool(new String[]{});
		String temp = "TEST\ntest";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.sortFile(f1.getAbsolutePath());
		assertEquals(result, "TEST\r\ntest");
	}
	
	/**
	 * Test expected behavior
	 * Check if sorted of a sorted string
	 * @throws IOException 
	 */
	@Test
	public void checkIfSortedTestSortedAlreadyTest() throws IOException {
		sorttool = new SortTool(new String[]{});
		String temp = "TESt\ntest";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.checkIfSorted(f1.getAbsolutePath());
		assertEquals(result, "");
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
	 * Execute help option
	 */
	@Test
	public void executeHelpTest() {
		sorttool = new SortTool(new String[]{"-help", "text.txt"});
		String result = sorttool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertTrue(result.startsWith("NAME\n\nsort - sort lines of text files\n\n"));
		assertTrue(result.endsWith("-help\tBrief information about supported options\n"));
		assertTrue(result.contains("DESCRIPTION\n\nWrite sorted concatenation of all FILE(s)"));
		assertTrue(result.contains("-c\tCheck whether the given file is already sorted, if it"));
		assertEquals(0, sorttool.getStatusCode());
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
	 * Test error handling
	 * Missing parameter for file name
	 */
	@Test
	public void executeNoFileTest() throws IOException {
		sorttool = new SortTool(new String[]{"-c"});
		sorttool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertEquals(2, sorttool.getStatusCode());
	}
	
	/**
	 * Test expected behavior
	 * Execute one file
	 */
	@Test
	public void executeTestOneFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "hello world\nHello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{file.getAbsolutePath()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "Hello world\r\napple\r\nhello world");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute empty file
	 */
	@Test
	public void executeTestEmptyFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		sorttool = new SortTool(new String[]{file.getAbsolutePath()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "");
		Files.delete(file.toPath());
	}

	/**
	 * Test expected behavior
	 * Execute 1 file multiple times
	 */
	@Test
	public void executeTestMultipleSameFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "hello world\nHello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{file.getAbsolutePath(),file.getAbsolutePath(),file.getAbsolutePath()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "Hello world\nHello world\nHello world\napple\napple\napple\nhello world\nhello world\nhello world\n");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute multiple unique files
	 */
	@Test
	public void executeTestMultipleDiffFileTest() throws IOException {
		File file1 = File.createTempFile("tempFile1", ".tmp");
		File directory = new File(System.getProperty("user.home"));
		File file2 = Files.createFile(new File(directory, "tempFile2.tmp").toPath()).toFile();
		directory = Files.createTempDirectory("tempFolder").toFile();
		File file3 = Files.createFile(new File(directory, "tempFile3.tmp").toPath()).toFile();
		
		String input = "hello world\nHello world\napple\n";
		Files.write(file1.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		input = "orange\nOrange\nApple";
		Files.write(file2.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		input = "banana\nBanana\nApple";
		Files.write(file3.toPath(), input.getBytes(), StandardOpenOption.CREATE);

		String arg = file3.getAbsolutePath() +
				" " + file2.getAbsolutePath() + " " + file1.getAbsolutePath();
		sorttool = new SortTool(arg.split(" "));
		String result = sorttool.execute(file1.getParentFile(), null);		
		assertEquals(result, "Apple\nApple\nBanana\nHello world\nOrange\napple\nbanana\nhello world\norange\n");
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(file3.toPath());
		Files.delete(directory.toPath());
	}
	
	/**
	 * Test error handling
	 * Execute a valid and invalid file
	 */
	@Test
	public void executeValidAndInvalidFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "hello world\nHello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{file.getAbsolutePath(),"invalidfile.tmp"});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(sorttool.getStatusCode(),4);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute if sorted option for a sorted file
	 */
	@Test
	public void executeTestOneFileCheckIfSorted1Test() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "Hello world\napple\nhello world\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{"-c",file.getAbsolutePath()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "");
		Files.delete(file.toPath());
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
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(sorttool.getStatusCode(), 4);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test error handling
	 * Execute if sorted option for multiple files
	 */
	@Test
	public void executeTestMultipleFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "apple\nHello world\nhello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{"-c",file.getAbsolutePath(), file.getAbsolutePath()});
		String result = sorttool.execute(file.getParentFile(), null);
		String expected = "Error: extra argument \""+ file.getName() + "\" not allowed with -c.\n";
		assertEquals(result, expected);
		assertEquals(4, sorttool.getStatusCode());
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute if sorted option for empty file
	 */
	@Test
	public void executeTestEmptyFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("temp File", ".tmp");
		sorttool = new SortTool(new String[]{"-c",file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test error handling
	 * Execute if sorted option for invalid file
	 */
	@Test
	public void executeTestInvalidFileCheckIfSortedTest() {
		sorttool = new SortTool(new String[]{"-c","invalid file.tmp"});
		String result = sorttool.execute(new File(System.getProperty("user.home")), null);
		assertEquals(sorttool.getStatusCode(),4);
	}
	
	/**
	 * Test error handling
	 * Execute invalid option
	 */
	@Test
	public void executeTestInvalidOptionTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		sorttool = new SortTool(new String[]{"-c","-d",file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		String expected = "Error: comm: unrecognized option '-d'\nTry 'comm -help' for more information.\n";
		assertEquals(result, expected);
		assertEquals(sorttool.getStatusCode(),1);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test error handling
	 * Execute with no argument
	 */
	@Test
	public void executeTestEmptyArgumentTest() {
		sorttool = new SortTool(new String[]{});
		String result = sorttool.execute(new File(System.getProperty("user.home")), null);
		assertEquals(result, "Error: Missing parameter for OPTION FILE");
		assertEquals(sorttool.getStatusCode(),1);
	}
	
	/**
	 * Test error handling
	 * Execute with a directory instead of a file
	 */
	@Test
	public void executeFolderTest() throws IOException{
		File dir = Files.createTempDirectory("tempdir").toFile();
		sorttool = new SortTool(dir.getAbsolutePath().split(" "));
		String result = sorttool.execute(dir.getParentFile(), null);
		assertEquals(sorttool.getStatusCode(),4);
		Files.delete(dir.toPath());
	}
}
