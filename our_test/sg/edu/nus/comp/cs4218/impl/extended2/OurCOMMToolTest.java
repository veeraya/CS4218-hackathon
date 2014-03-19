package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;

public class OurCOMMToolTest {

	private ICommTool commtool;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		commtool = null;
	}

	/**
	 * Test expected behavior
	 * Help method
	 */
	@Test
	public void testGetHelp() {
		commtool = new CommTool(new String[]{});
		String result = commtool.getHelp();
		assertTrue(result.startsWith("NAME\n\ncomm - compare two sorted files line by line\n\n"));
		assertTrue(result.endsWith("-help\tBrief information about supported options\n"));
		assertTrue(result.contains("FILE1\tName of the file 1\nFILE2\tName of the file 2\n\n"));
		assertTrue(result.contains("no options, produce three-column output. Col"));
	}

	/**
	 * Test expected behavior
	 * Compare between two string that is not equal in length
	 */
	@Test
	public void compareFilesUnequalListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "hello world\nthis is a test\nfile\nabc";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\t\t\thello world\ncomm: file 2 is not in sorted order\n\t\tfinal\nthis is a test\ncomm: file 1 is not in sorted order\nfile\nabc\n";
		String result = commtool.compareFiles(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	/**
	 * Test expected behavior
	 * Compare between two string. One of the string is sorted
	 */
	@Test
	public void compareFilesCheckSortStatusUnequalListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "hello world\nthis is a test\nfile\nabc";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\t\t\thello world\ncomm: file 2 is not in sorted order\n";
		String result = commtool.compareFilesCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	/**
	 * Test expected behavior
	 * Compare two string without checking sort status
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusUnequalListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "hello world\nthis is a test\nfile\nabc";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\t\t\thello world\n\t\tfinal\nthis is a test\nfile\nabc\n";
		String result = commtool.compareFilesDoNotCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	/**
	 * Test expected behavior
	 * Compare two string. One string is empty.
	 */
	@Test
	public void compareFilesOneEmptyListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\thello world\ncomm: file 2 is not in sorted order\n\t\tfinal\n";
		String result = commtool.compareFiles(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}


	/**
	 * Test expected behavior
	 * Compare two string check sort status. One string is empty.
	 */
	@Test
	public void compareFilesCheckSortStatusOneEmptyListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\thello world\ncomm: file 2 is not in sorted order\n";
		String result = commtool.compareFilesCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}


	/**
	 * Test expected behavior
	 * Compare two string without checking sort status. one string is empty
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusOneEmptyListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\thello world\n\t\tfinal\n";
		String result = commtool.compareFilesDoNotCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}
	

	/**
	 * Test expected behavior
	 * Compare two string and both string are empty
	 */
	@Test
	public void compareFilesTwoEmptyListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "";
		String input2 = "";
		String expected = "";
		String result = commtool.compareFiles(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	/**
	 * Test expected behavior
	 * Compare two string checking sort status. Both string are empty
	 */
	@Test
	public void compareFilesCheckSortStatusTwoEmptyListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "";
		String input2 = "";
		String expected = "";
		String result = commtool.compareFilesCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	/**
	 * Test expected behavior
	 * Compare two string without checking sort status. Both string are empty
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusTwoEmptyListTest() {
		commtool = new CommTool(new String[]{});
		String input1 = "";
		String input2 = "";
		String expected = "";
		String result = commtool.compareFilesDoNotCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		commtool = new CommTool(new String[]{});
		String result = commtool.execute(null, null);
		assertEquals(1, commtool.getStatusCode());
		assertEquals("Error: Cannot find working directory", result);
	}

	/**
	 * Test error handling
	 * No argument in parameter
	 */
	@Test
	public void executeNoArgsTest() {
		commtool = new CommTool(new String[]{});
		String result = commtool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals(1, commtool.getStatusCode());
		assertEquals("Error: Missing parameter for FILE 1 FILE 2", result);
	}

	/**
	 * Test error handling
	 * Incompleted argument
	 */
	@Test
	public void executeIncompleteArgsTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		commtool = new CommTool(new String[]{file.getName()});
		String result = commtool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals(1, commtool.getStatusCode());
		assertEquals( "Error: Missing parameter for FILE 2", result);
		Files.delete(file.toPath());
	}

	/**
	 * Test expected behavior
	 * Executing command with no option
	 */
	@Test
	public void executeNoOptionTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "\t\tbaby\n\t\t\t\thello world\nthis is a test\ncomm: file 1 is not in sorted order\nfile\n";
		commtool = new CommTool(new String[]{file1.getName(), file2.getName()});
		String result = commtool.execute(tempFolder, null);
		assertEquals(temp.compareTo(result), 0);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}

	/**
	 * Test expected behavior
	 * Execute comm with a option
	 */
	@Test
	public void executeOneOptionTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "\t\tbaby\n\t\t\t\thello world\nthis is a test\nfile\n";
		commtool = new CommTool(new String[]{"-d", file1.getName(),file2.getName()});
		String result = commtool.execute(tempFolder, null);
		assertEquals(temp.compareTo(result), 0);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}

	/**
	 * Test expected behavior
	 * Execute comm with two option
	 */
	@Test
	public void executeTwoOptionTest() throws IOException {
		//Test expected behavior
		//Create tmp folder with 2 files. Execute comm with two option
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "\t\tbaby\n\t\t\t\thello world\nthis is a test\ncomm: file 1 is not in sorted order\n";
		commtool = new CommTool(new String[]{"-d","-c", file1.getName(),file2.getName()});
		String result = commtool.execute(tempFolder, null);
		assertEquals(temp.compareTo(result), 0);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}


	/**
	 * Test expected behavior
	 * Execute help option
	 */
	@Test
	public void executeOptionHelpTest() {
		//Test expected behavior
		//only have help in the option, no file names
		commtool = new CommTool(new String[]{"-help"});
		String result = commtool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertTrue(result.startsWith("NAME\n\ncomm - compare two sorted files line by line"));
		assertTrue(result.endsWith("Brief information about supported options\n"));
		assertTrue(result.contains("no options, produce three-column output."));
		assertTrue(result.contains("file 1\nFILE2\tName of the file 2"));
	}

	/**
	 * Test expected behavior
	 * Multiple options including help
	 */
	@Test
	public void executeOptionsIncludeHelpTest() throws IOException {
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		commtool = new CommTool(new String[]{"-d","-c","-help", file1.getName(),file2.getName()});
		String result = commtool.execute(tempFolder, null);
		assertTrue(result.startsWith("NAME\n\ncomm - compare two sorted files line by line"));
		assertTrue(result.endsWith("Brief information about supported options\n"));
		assertTrue(result.contains("no options, produce three-column output."));
		assertTrue(result.contains("file 1\nFILE2\tName of the file 2"));	
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}

	/**
	 * Test expected behavior
	 * One file does not have any content
	 */
	@Test
	public void executeExmptyFileTest() throws IOException {
		//Test expected behavior
		//One of the file does not contains any string
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		commtool = new CommTool(new String[]{"-d","-c", file1.getName(),file2.getName()});
		String result = commtool.execute(tempFolder, null);
		temp = "hello world\nthis is a test\ncomm: file 1 is not in sorted order\n";
		assertTrue(result.equals(temp));
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * Argument contains invalid option
	 */
	@Test
	public void executeInvalidOptionTest() throws IOException {
		//Error-handling
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		commtool = new CommTool(new String[]{"-d","-i","-c", file1.getName(),file2.getName()});
		String result = commtool.execute(tempFolder, null);
		temp = "Error: comm: unrecognized option '-i'\nTry 'comm -help' for more information.\n";
		assertEquals(commtool.getStatusCode(),1);
		assertTrue(result.equals(temp));
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * Argument contains invalid file
	 */
	@Test
	public void executeInvalidFileName1Test() throws IOException {
		//Error-handling
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "-r tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String args = "-d -c " + file1.getName() + " " + file2.getName();
		commtool = new CommTool(args.split(" "));
		String result = commtool.execute(tempFolder, null);
		temp = "Error: FILE 1 is not found";
		assertTrue(result.equals(temp));
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * Invalid file name for second parameter
	 */
	@Test
	public void executeInvalidFileName2Test() throws IOException {
		//Error-handling
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String args = "-d -c " + file1.getName() + " " + file2.getName() + "extra";
		commtool = new CommTool(args.split(" "));
		String result = commtool.execute(tempFolder, null);
		temp = "Error: FILE 2 is not found";
		assertTrue(result.equals(temp));
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test error handling
	 * Extra parameter in filename
	 */
	@Test
	public void executeInvalidFileName2ExtraParaTest() throws IOException {
		//Error-handling
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String args = "-d -c " + file1.getName() + " " + file2.getName() + " extra";
		commtool = new CommTool(args.split(" "));
		String result = commtool.execute(tempFolder, null);
		temp = "Error: Extra parameter found in FILE 2";
		assertTrue(result.equals(temp));
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Comparing two files with no option and both files are the same file
	 */
	@Test
	public void executeSameFileNoOptionTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		commtool = new CommTool(new String[]{file.getName(),file.getName()});
		String result = commtool.execute(file.getParentFile(), null);
		String expected = "\t\t\t\thello world\n\t\t\t\tthis is a test\n\t\t\t\tfile\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Comparing two files with no check and both files are the same file
	 */
	@Test
	public void executeSameFileNoCheckTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String arg =  "-d " + file.getName() + " " + file.getName();
		commtool = new CommTool(arg.split(" "));
		String result = commtool.execute(file.getParentFile(),null);
		String expected = "\t\t\t\thello world\n\t\t\t\tthis is a test\n\t\t\t\tfile\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Comparing two files with check and both files are the same file
	 */
	@Test
	public void executeSameFileCheckTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String arg = "-c " + file.getName() + " " + file.getName();
		commtool = new CommTool(arg.split(" "));
		String result = commtool.execute(file.getParentFile(), null);
		String expected = "\t\t\t\thello world\n\t\t\t\tthis is a test\ncomm: file 1 is not in sorted order\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test error handling
	 * Comparing one directory and one file
	 */
	@Test
	public void executeFolder1Test() throws IOException{
		File dir = Files.createTempDirectory("temp dir").toFile();
		String arg = "-c " + dir.getName() + " " + dir.getName();
		commtool = new CommTool(arg.split(" "));
		String result = commtool.execute(dir.getParentFile(), null);
		assertEquals(result,"Error: FILE 1 is not a file");
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(dir.toPath());
	}
	
	/**
	 * Test error handling
	 * Comparing one file and one directory
	 */
	@Test
	public void executeFolder2Test() throws IOException{
		File dir = Files.createTempDirectory("temp dir").toFile();
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String arg = "-c " + file.getAbsolutePath() + " " + dir.getName();
		commtool = new CommTool(arg.split(" "));
		String result = commtool.execute(dir.getParentFile(), null);
		assertEquals(result,"Error: FILE 2 is not a file");
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(dir.toPath());
		Files.delete(file.toPath());
	}
}