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
		assertTrue(result.startsWith("comm : Compares two sorted files line by line. With no options"));
		assertTrue(result.endsWith("-help : Brief information about supported options"));
		assertTrue(result.contains("FILE1 - Name of the file 1"));
		assertTrue(result.contains("-c : check that the input is correctly sorted, even if"));
	}

	/**
	 * Test expected behavior
	 * Compare between two string that is not equal in length
	 * @throws IOException 
	 */
	@Test
	public void compareFilesUnequalListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "hello world\nthis is a test\nfile\nabc";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world\nfinal";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "\tbaby\r\n\t\thello world\r\ncomm: file 2 is not in sorted order\r\n\tfinal\r\nthis is a test\r\ncomm: file 1 is not in sorted order\r\nfile\r\nabc\r\n";
		String result = commtool.compareFiles(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected,result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}

	/**
	 * Test expected behavior
	 * Compare between two string. One of the string is sorted
	 * @throws IOException 
	 */
	@Test
	public void compareFilesCheckSortStatusUnequalListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "hello world\nthis is a test\nfile\nabc";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world\nfinal";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "\tbaby\r\n\t\thello world\r\ncomm: file 2 is not in sorted order\r\n";
		String result = commtool.compareFilesCheckSortStatus(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected, result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}

	/**
	 * Test expected behavior
	 * Compare two string without checking sort status
	 * @throws IOException 
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusUnequalListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "hello world\nthis is a test\nfile\nabc";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world\nfinal";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "\tbaby\r\n\t\thello world\r\n\tfinal\r\nthis is a test\r\nfile\r\nabc\r\n";
		String result = commtool.compareFilesDoNotCheckSortStatus(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected,result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}

	/**
	 * Test expected behavior
	 * Compare two string. One string is empty.
	 * @throws IOException 
	 */
	@Test
	public void compareFilesOneEmptyListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world\nfinal";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "\t\tbaby\n\t\thello world\ncomm: file 2 is not in sorted order\n\t\tfinal\n";
		String result = commtool.compareFiles(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected,result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}


	/**
	 * Test expected behavior
	 * Compare two string check sort status. One string is empty.
	 * @throws IOException 
	 */
	@Test
	public void compareFilesCheckSortStatusOneEmptyListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world\nfinal";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "\t\tbaby\n\t\thello world\ncomm: file 2 is not in sorted order\n";
		String result = commtool.compareFilesCheckSortStatus(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected, result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}


	/**
	 * Test expected behavior
	 * Compare two string without checking sort status. one string is empty
	 * @throws IOException 
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusOneEmptyListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world\nfinal";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "\tbaby\r\n\thello world\r\n\tfinal\r\n";
		String result = commtool.compareFilesDoNotCheckSortStatus(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected, result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}
	

	/**
	 * Test expected behavior
	 * Compare two string and both string are empty
	 * @throws IOException 
	 */
	@Test
	public void compareFilesTwoEmptyListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "";
		String result = commtool.compareFiles(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected, result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}

	/**
	 * Test expected behavior
	 * Compare two string checking sort status. Both string are empty
	 * @throws IOException 
	 */
	@Test
	public void compareFilesCheckSortStatusTwoEmptyListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "";
		String result = commtool.compareFilesCheckSortStatus(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected, result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}

	/**
	 * Test expected behavior
	 * Compare two string without checking sort status. Both string are empty
	 * @throws IOException 
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusTwoEmptyListTest() throws IOException {
		commtool = new CommTool(new String[]{});
		String temp = "";
		File f1 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "";
		File f2 = Files.createTempFile("temp", ".tmp").toFile();
		Files.write(f2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String expected = "";
		String result = commtool.compareFilesDoNotCheckSortStatus(f1.getAbsolutePath(), f2.getAbsolutePath());
		assertEquals(expected, result);
		Files.delete(f1.toPath());
		Files.delete(f2.toPath());
	}

	/**
	 * Test error handling
	 * Invalid working directory
	 */
	@Test
	public void executeInvalidWorkingDirTest() {
		commtool = new CommTool(new String[]{"hello","world"});
		commtool.execute(null, null);
		assertEquals(4, commtool.getStatusCode());
	}

	/**
	 * Test error handling
	 * No argument in parameter
	 */
	@Test
	public void executeNoArgsTest() {
		commtool = new CommTool(new String[]{});
		commtool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals(2, commtool.getStatusCode());
	}

	/**
	 * Test error handling
	 * Incompleted argument
	 */
	@Test
	public void executeIncompleteArgsTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		commtool = new CommTool(new String[]{file.getName()});
		commtool.execute(new File(System.getProperty("java.io.tmpdir")), null);
		assertEquals(2, commtool.getStatusCode());
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
		temp = "\tbaby\r\n\t\thello world\r\nthis is a test\r\ncomm: file 1 is not in sorted order\r\nfile\r\n";
		commtool = new CommTool(new String[]{file1.getAbsolutePath(), file2.getAbsolutePath()});
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
		temp = "\tbaby\r\n\t\thello world\r\nthis is a test\r\nfile\r\n";
		commtool = new CommTool(new String[]{"-d", file1.getAbsolutePath(),file2.getAbsolutePath()});
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
		temp = "\tbaby\r\n\t\thello world\r\nthis is a test\r\ncomm: file 1 is not in sorted order\r\n";
		commtool = new CommTool(new String[]{"-d","-c", file1.getAbsolutePath(),file2.getAbsolutePath()});
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
		assertTrue(result.startsWith("comm : Compares two sorted files line"));
		assertTrue(result.endsWith("-help : Brief information about supported options"));
		assertTrue(result.contains("FILE1 - Name of the file 1"));
		assertTrue(result.contains("-c : check that the input is correctly"));
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
		assertTrue(result.startsWith("comm : Compares two sorted files line"));
		assertTrue(result.endsWith("-help : Brief information about supported options"));
		assertTrue(result.contains("FILE1 - Name of the file 1"));
		assertTrue(result.contains("-c : check that the input is correctly"));
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
		commtool = new CommTool(new String[]{"-d","-c", file1.getAbsolutePath(),file2.getAbsolutePath()});
		String result = commtool.execute(tempFolder, null);
		temp = "hello world\r\nthis is a test\r\ncomm: file 1 is not in sorted order\r\n";
		assertEquals(result, temp);
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
		temp = "comm: unknown option -- i\r\nTry comm '-help' for more information";
		assertEquals(commtool.getStatusCode(),1);
		assertEquals(result, temp);
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
		temp = "comm: unknown option -- r\r\nTry comm '-help' for more information";
		assertEquals(result, temp);
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
		String args = "-d -c " + file1.getAbsolutePath() + " " + file2.getAbsolutePath() + "extra";
		commtool = new CommTool(args.split(" "));
		String result = commtool.execute(tempFolder, null);
		temp = "tempFile2.txtextra: No such file or directory";
		assertTrue(result.contains(temp));
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
		temp = "comm: extra operand 'extra'";
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
		commtool = new CommTool(new String[]{file.getAbsolutePath(),file.getAbsolutePath()});
		String result = commtool.execute(file.getParentFile(), null);
		String expected = "\t\thello world\r\n\t\tthis is a test\r\n\t\tfile\r\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Comparing two files with no check and both files are the same file
	 */
	@Test
	public void executeSameFileNoCheckTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String arg =  "-d " + file.getAbsolutePath() + " " + file.getAbsolutePath();
		commtool = new CommTool(arg.split(" "));
		String result = commtool.execute(file.getParentFile(),null);
		String expected = "\t\thello world\r\n\t\tthis is a test\r\n\t\tfile\r\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test expected behavior
	 * Comparing two files with check and both files are the same file
	 */
	@Test
	public void executeSameFileCheckTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String arg = "-c " + file.getAbsolutePath() + " " + file.getAbsolutePath();
		commtool = new CommTool(arg.split(" "));
		String result = commtool.execute(file.getParentFile(), null);
		String expected = "\t\thello world\r\n\t\tthis is a test\r\ncomm: file 1 is not in sorted order\r\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test error handling
	 * Comparing one directory and one file
	 */
	@Test
	public void executeFolder1Test() throws IOException{
		File dir = Files.createTempDirectory("tempdir").toFile();
		String arg = "-c " + dir.getAbsolutePath() + " " + dir.getAbsolutePath();
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
		File dir = Files.createTempDirectory("tempdir").toFile();
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String arg = "-c " + file.getAbsolutePath() + " " + dir.getAbsolutePath();
		commtool = new CommTool(arg.split(" "));
		String result = commtool.execute(dir.getParentFile(), null);
		assertEquals(result,"Error: FILE 2 is not a file");
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(dir.toPath());
		Files.delete(file.toPath());
	}
}