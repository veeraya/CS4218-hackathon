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
		assertTrue(result.startsWith("NAME\n\nsort - sort lines of text files\n\n"));
		assertTrue(result.endsWith("-help\tBrief information about supported options\n"));
		assertTrue(result.contains("DESCRIPTION\n\nWrite sorted concatenation of all FILE(s)"));
		assertTrue(result.contains("-c\tCheck whether the given file is already sorted, if it"));
	}

	/**
	 * Test expected behavior
	 * Sort arrangement of string
	 */
	@Test
	public void sortFileTestArrangementTest() {
		sorttool = new SortTool(new String[]{});
		String input = "test\nTest\nTEst\nTESt\nTEST\ntEST\nteST\ntesT\n";
		String result = sorttool.sortFile(input);
		assertEquals(result, "TEST\nTESt\nTEst\nTest\ntEST\nteST\ntesT\ntest\n");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement of string with duplicate of the same statement
	 */
	@Test
	public void sortFileTestDuplicateTest(){
		sorttool = new SortTool(new String[]{});
		String input = "test\nTEST\ntest\napple\nApple\n";
		String result = sorttool.sortFile(input);
		assertEquals(result, "Apple\nTEST\napple\ntest\ntest\n");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement based on length of string
	 */
	@Test
	public void sortFileLengthTest(){
		sorttool = new SortTool(new String[]{});
		String input = "bananas\nbanana\nBanana";
		String result = sorttool.sortFile(input);
		assertEquals(result, "Banana\nbanana\nbananas\n");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement based on length of string
	 */
	@Test
	public void sortFileLengthCharTest(){
		sorttool = new SortTool(new String[]{});
		String input = "bacc\nabc\nhello\nbac\nbaad\nBAcc";
		String result = sorttool.sortFile(input);
		assertEquals(result, "BAcc\nabc\nbaad\nbac\nbacc\nhello\n");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement of string and special character
	 */
	@Test
	public void sortFileTestSpecialCharacterTest(){
		sorttool = new SortTool(new String[]{});
		String input = "test\nTEST\n!!!\n@.@\n死\n";
		String result = sorttool.sortFile(input);
		assertEquals(result, "!!!\n@.@\nTEST\ntest\n死\n");
	}
	
	/**
	 * Test expected behavior
	 * Sort arrangement of string and blank statement
	 */
	@Test
	public void sortFileTestBlankLinesTest(){
		sorttool = new SortTool(new String[]{});
		String input = "test\n\n\nTEST\n";
		String result = sorttool.sortFile(input);
		assertEquals(result, "\n\nTEST\ntest\n");
	}

	/**
	 * Test expected behavior
	 * Sort arrangement of sorted string
	 */
	@Test
	public void sortFileTestSortedAlreadyTest(){
		sorttool = new SortTool(new String[]{});
		String input = "TEST\ntest";
		String result = sorttool.sortFile(input);
		assertEquals(result, "TEST\ntest\n");
	}
	
	/**
	 * Test expected behavior
	 * Check if sorted of a sorted string
	 */
	@Test
	public void checkIfSortedTestSortedAlreadyTest() {
		sorttool = new SortTool(new String[]{});
		String input = "TESt\ntest";
		String result = sorttool.checkIfSorted(input);
		assertEquals(result, "");
	}
	
	/**
	 * Test expected behavior
	 * Check if sorted of a unsorted string
	 */
	@Test
	public void checkIfSortedTestUnsortedTest() {
		sorttool = new SortTool(new String[]{});
		String input = "TEST\ntest\nTEST";
		String result = sorttool.checkIfSorted(input);
		assertEquals(result, "2: disorder:test\n");
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
		sorttool = new SortTool(new String[]{});
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
		String result = sorttool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertEquals(result, "Error: Missing parameter for FILE");
		assertEquals(1, sorttool.getStatusCode());
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
		sorttool = new SortTool(new String[]{file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "Hello world\napple\nhello world\n");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute empty file
	 */
	@Test
	public void executeTestEmptyFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		sorttool = new SortTool(new String[]{file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "\n");
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
		sorttool = new SortTool(new String[]{file.getName(),file.getName(),file.getName()});
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
		File file1 = File.createTempFile("tempFile 1", ".tmp");
		File directory = new File(System.getProperty("user.home"));
		File file2 = Files.createFile(new File(directory, "tempFile 2.tmp").toPath()).toFile();
		directory = Files.createTempDirectory("temp Folder").toFile();
		File file3 = Files.createFile(new File(directory, "tempFile 3.tmp").toPath()).toFile();
		
		String input = "hello world\nHello world\napple\n";
		Files.write(file1.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		input = "orange\nOrange\nApple";
		Files.write(file2.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		input = "banana\nBanana\nApple";
		Files.write(file3.toPath(), input.getBytes(), StandardOpenOption.CREATE);

		String arg = directory.getName() + "\\" + file3.getName() +
				" " + file2.getAbsolutePath() + " " + file1.getName();
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
		sorttool = new SortTool(new String[]{file.getName(),"invalidfile.tmp"});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "Error: FILE is not found");
		assertEquals(sorttool.getStatusCode(),1);
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
		sorttool = new SortTool(new String[]{"-c",file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(result, "");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute if sorted option for an unsorted file
	 */
	@Test
	public void executeTestOneFileCheckIfSorted2Test() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "apple\nHello world\nhello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{"-c",file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		String expected = "sort: " + file.getName() + " 1: disorder:apple\n";
		assertEquals(result, expected);
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
		sorttool = new SortTool(new String[]{"-c",file.getName(), file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		String expected = "Error: extra argument \""+ file.getName() + "\" not allowed with -c.\n";
		assertEquals(result, expected);
		assertEquals(1, sorttool.getStatusCode());
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute if sorted option for empty file
	 */
	@Test
	public void executeTestEmptyFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
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
		sorttool = new SortTool(new String[]{"-c","invalidfile.tmp"});
		String result = sorttool.execute(new File(System.getProperty("user.home")), null);
		assertEquals(result, "Error: FILE is not found");
		assertEquals(sorttool.getStatusCode(),1);
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
		File dir = Files.createTempDirectory("temp dir").toFile();
		sorttool = new SortTool(dir.getName().split(" "));
		String result = sorttool.execute(dir.getParentFile(), null);
		assertEquals(result,"Error: FILE 01 is not a file");
		assertEquals(sorttool.getStatusCode(),1);
		Files.delete(dir.toPath());
	}
}
