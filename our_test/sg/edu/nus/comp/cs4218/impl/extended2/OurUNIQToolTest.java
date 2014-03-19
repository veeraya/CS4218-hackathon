package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;

public class OurUNIQToolTest {

	private IUniqTool uniqtool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		uniqtool = null;
	}

	/**
	 * Test expected behavior
	 * Help method
	 */
	@Test
	public void testGetHelpTest() {
		uniqtool = new UniqTool(new String[]{});
		String result = uniqtool.getHelp();
		assertTrue(result.startsWith("NAME\n\nuniq - report or omit repeated lines\n"));
		assertTrue(result.endsWith("-help\tBrief information about supported options\n"));
		assertTrue(result.contains("-f NUM\tSkips NUM fields on each line before checking for uniqueness"));
		assertTrue(result.contains("separated from each other by at least one space or"));
	}

	/**
	 * Test expected behavior
	 * String only has one statement
	 */
	@Test
	public void getUniqueOneLineTest() {
		uniqtool = new UniqTool(new String[]{});
		String input = "hello world\n";
		String result = uniqtool.getUnique(true, input);
		assertEquals(result.compareTo("hello world\n\n"),0);
		result = uniqtool.getUnique(false, input);
		assertEquals(result.compareTo("hello world\n\n"), 0);
	}

	/**
	 * Test expected behavior
	 * String has same alphabet but different case
	 */
	@Test
	public void getUniqueDifferentCaseTest() {
		uniqtool = new UniqTool(new String[]{});
		String input = "hello world\nHELLo WOrld";
		String result = uniqtool.getUnique(true, input);
		assertEquals(result.compareTo("hello world\nHELLo WOrld\n"), 0);
		result = uniqtool.getUnique(false, input);
		assertEquals(result.compareTo("hello world\n"), 0);
	}

	/**
	 * Test expected behavior
	 * String has multiple lines with different case
	 */
	@Test
	public void getUniqueManySameLineTest(){
		uniqtool = new UniqTool(new String[]{});
		String input = "test1\nhello\nhello\nhello\nHello\ntest1";
		String result = uniqtool.getUnique(true, input);
		assertEquals(result.compareTo("test1\nhello\nHello\ntest1\n"), 0);
		result = uniqtool.getUnique(false, input);
		assertEquals(result.compareTo("test1\nhello\ntest1\n"), 0);
	}

	/**
	 * Test expected behavior
	 * String contains special character
	 */
	@Test
	public void getUniqueSpecialCharacterTest(){
		uniqtool = new UniqTool(new String[]{});
		String input = "test1\nhello\nHello\n好";
		String result = uniqtool.getUnique(true, input);
		assertEquals(result.compareTo("test1\nhello\nHello\n好\n"), 0);
		result = uniqtool.getUnique(false, input);
		assertEquals(result.compareTo("test1\nhello\n好\n"), 0);
	}

	/**
	 * Test expected behavior
	 * String has one statement for the option of skipping the first character
	 */
	@Test
	public void getUniqueSkipNumOneLineTest(){
		uniqtool = new UniqTool(new String[]{});
		String input = "hello world\n";
		String result = uniqtool.getUniqueSkipNum(1,true, input);
		assertEquals(result.compareTo("hello world\n"),0);
		result = uniqtool.getUniqueSkipNum(12,false, input);
		assertEquals(result.compareTo("hello world\n"), 0);
	}

	/**
	 * Test expected behavior
	 * String has same alphabet but different case
	 * Skipping the first 5 character and ignoring case
	 */
	@Test
	public void getUniqueSkipNumDifferentCaseTest(){
		uniqtool = new UniqTool(new String[]{});
		String input = "hello world\nhello world\nHELLo WorlD";
		String result = uniqtool.getUniqueSkipNum(5,true, input);
		assertEquals(result.compareTo("hello world\nHELLo WorlD\n"),0);
		result = uniqtool.getUniqueSkipNum(5,false, input);
		assertEquals(result.compareTo("hello world\n"), 0);
	}

	/**
	 * Test expected behavior
	 * String has same alphabet but different case
	 * Checking and ignoring case sensitivity.
	 */
	@Test
	public void getUniqueSkipNumManySameLineTest() {
		uniqtool = new UniqTool(new String[]{});
		String input = "test1\nhello\nhello\nhello\nHello\ntest1";
		String result = uniqtool.getUniqueSkipNum(2,true, input);
		assertEquals(result.compareTo("test1\nhello\ntest1\n"), 0);
		result = uniqtool.getUniqueSkipNum(2,false, input);
		assertEquals(result.compareTo("test1\nhello\ntest1\n"), 0);
	}

	/**
	 * Test expected behavior
	 * Skipping num of character.
	 * String contains special character
	 */
	@Test
	public void getUniqueSkipNumSpecialCharacterTest(){
		uniqtool = new UniqTool(new String[]{});
		String input = "test1\nhello\nHello\n�?�烧包\n豆沙包";
		String result = uniqtool.getUniqueSkipNum(2,false, input);
		assertEquals(result.compareTo("test1\nhello\n�?�烧包\n"), 0);
		result = uniqtool.getUniqueSkipNum(2,true, input);
		assertEquals(result.compareTo("test1\nhello\n�?�烧包\n"), 0);
	}

	/**
	 * Test expected behavior
	 * Skip 0 character
	 */
	@Test
	public void getUniqueSkipNumMinNumTest() {
		uniqtool = new UniqTool(new String[]{});
		String input = "test1\nhello\nHello\n�?�烧包\n豆沙包";
		String result = uniqtool.getUniqueSkipNum(0,false, input);
		assertEquals(result.compareTo("test1\nhello\n�?�烧包\n豆沙包\n"), 0);
		result = uniqtool.getUniqueSkipNum(0,true, input);
		assertEquals(result.compareTo("test1\nhello\nHello\n�?�烧包\n豆沙包\n"), 0);
	}

	/**
	 * Test expected behavior
	 * Skip 20 character and statement has less than 10 character
	 */
	@Test
	public void getUniqueSkipNumMaxNumTest(){
		uniqtool = new UniqTool(new String[]{});
		String input ="test1\nhello\nHello\n�?�烧包\n豆沙包";
		String result = uniqtool.getUniqueSkipNum(20,false, input);
		assertEquals(result.compareTo("test1\n"), 0);
		result = uniqtool.getUniqueSkipNum(20,true, input);
		assertEquals(result.compareTo("test1\n"), 0);
	}
	
	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		uniqtool = new UniqTool(new String[]{});
		String result = uniqtool.execute(null, null);
		assertEquals(1, uniqtool.getStatusCode());
		assertEquals("Error: Cannot find working directory", result);
	}
	
	/**
	 * Test expected behavior
	 * Execute with help option
	 */
	@Test
	public void executeHelpTest(){
		uniqtool = new UniqTool(new String[]{"-help", "text.txt"});
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertTrue(result.startsWith("NAME\n\nuniq - report or omit repeated lines\n"));
		assertTrue(result.endsWith("-help\tBrief information about supported options\n"));
		assertTrue(result.contains("-f NUM\tSkips NUM fields on each line before checking for uniqueness"));
		assertTrue(result.contains("separated from each other by at least one space or"));
		assertEquals(0, uniqtool.getStatusCode());
	}

	/**
	 * Test error handling
	 * Missing num for -f option
	 */
	@Test
	public void executeNonNumTest(){
		uniqtool = new UniqTool(new String[]{"-i","-f","a","text.txt"});
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertEquals(result.compareTo("Error: NUM has to be a positive number"), 0);
		assertEquals(uniqtool.getStatusCode(),1);
	}

	/**
	 * Test error handling
	 * Neagtive num for -f option
	 */
	@Test
	public void executeNegNumTest(){
		uniqtool = new UniqTool(new String[]{"-i","-f","-2","text.txt"});
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertEquals(result.compareTo("Error: NUM has to be a positive number"), 0);
		assertEquals(uniqtool.getStatusCode(),1);
	}
	
	/**
	 * Test expected behavior
	 * Contains multiple same option with different num
	 */
	@Test
	public void executeGetUniqueSkipNumMultipleSameOptionTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTest\ntest\njest\nBEST";
		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{"-f","2","-f","3","-f","1",file.getName()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals(result.compareTo("Hello World\nTest\nBEST\n"), 0);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Contains multiple same option with different num and case sensitivity
	 */
	@Test
	public void executeGetUniqueSkipNumMultipleSameOptionIgnoreCaseTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTEST\ntest\njest\nBEST";
		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{"-f","2","-i","-f","3","-f","1",file.getName()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals(result,"Hello World\nTEST\n");
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Check unique statement within a file
	 */
	@Test
	public void executeGetUniqueTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTest\ntest";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{file.getName()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals(result.compareTo("Hello World\nhello World\nTest\ntest\n"), 0);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Ignore case sensitivity of all statement within a file
	 */
	@Test
	public void executeGetUniqueIgnoreCaseTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTEST\ntest";
		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{"-i",file.getName()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals(result.compareTo("Hello World\nTEST\n"), 0);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Empty file
	 */
	@Test
	public void executeEmptyFileTest() throws IOException {
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		uniqtool = new UniqTool(new String[]{"-f","2","-i","-f","3","-f","1",file.getName()});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals("\n", result);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test error handling
	 * No argument for the command
	 */
	@Test
	public void executeNoArgTest() throws IOException {
		uniqtool = new UniqTool(new String[]{});
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertEquals("Error: Missing parameter for FILE", result);
		assertEquals(1, uniqtool.getStatusCode());
	}
	
	/**
	 * Test error handling
	 * Invalid option included
	 */
	@Test
	public void executeInvalidOptionTest() throws IOException {
		uniqtool = new UniqTool(new String[]{"-f","2","-i","-f","3","-f","1","-a","temp.txt"});
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertEquals("Error: Invalid option", result);
		assertEquals(uniqtool.getStatusCode(),1);
	}
	
	/**
	 * Test error handling
	 * File does not exist
	 */
	@Test
	public void executeInvalidFileNameTest() {
		uniqtool = new UniqTool(new String[]{"-f","2","-i","-f","3","-f","1","temp.txt"});
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertEquals("Error: FILE is not found", result);
		assertEquals(uniqtool.getStatusCode(),1);
	}
	
	/**
	 * Test expected behavior
	 * Execute command with input file and stdin with options
	 */
	@Test
	public void executeFileStdinWithOptionTest() throws IOException {
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTest\ntest";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{"-i",file.getName(), "-"});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals("Hello World\nTest\n", result);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute command with input file and stdin with out options
	 */
	@Test
	public void executeFileStdinNoOptionTest() throws IOException {
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTest\ntest";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		uniqtool = new UniqTool(new String[]{file.getName(),"-"});
		String result = uniqtool.execute(file.getParentFile(),null);
		assertEquals("Hello World\nhello World\nTest\ntest\n", result);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Execute command with stdin
	 */
	@Test
	public void executeStdinWithOptionTest() throws IOException {
		uniqtool = new UniqTool(new String[]{"-i", "-"});
		uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),null);
		assertEquals(2, uniqtool.getStatusCode());
	}
	
	/**
	 * Test expected behavior
	 * Execute command with stdin ignore case sensitivity
	 */
	@Test
	public void executeStdinParseTest() throws IOException {
		uniqtool = new UniqTool(new String[]{"-i", "-"});
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")), "hello world\nHello World");
		assertEquals("hello world\n", result);
	}
	
	/**
	 * Test error handling
	 * Execute command with stdin ignore case sensitivity and negative num
	 */
	@Test
	public void executeStdinNegNumTest() throws IOException {
		uniqtool = new UniqTool(new String[]{"-i", "-f", "-1", "-"});
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")), "hello world\nHello World");
		assertEquals("Error: NUM has to be a positive number", result);
		assertEquals(1, uniqtool.getStatusCode());
	}
	
	/**
	 * Test expected behavior
	 * Execute command with directory instead of file
	 */
	@Test
	public void executeFolderTest() throws IOException{
		File dir = Files.createTempDirectory("temp dir").toFile();
		uniqtool = new UniqTool(dir.getPath().split(" "));
		String result = uniqtool.execute(dir.getParentFile(), null);
		assertEquals(result,"Error: FILE is not found");
		assertEquals(uniqtool.getStatusCode(),1);
		Files.delete(dir.toPath());
	}
}