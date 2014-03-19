package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;

public class SortToolExtraTest {
	private ISortTool sortTool;
	private static final String USER_DIRECTORY = "user.dir";
	private static File defaultWorkingDirectory;
	
	final String SORT_HELP = "sort : sort lines of text files"
			+ System.lineSeparator() + "Command Format - sort [OPTIONS] [FILE]"
			+ System.lineSeparator() + "FILE - Name of the file"
			+ System.lineSeparator() + "-c : Check whether the given file is"
			+ " already sorted, if it is not all sorted, print a diagnostic"
			+ " containing the first line that is out of order"
			+ System.lineSeparator()
			+ "-help : Brief information about supported options";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		defaultWorkingDirectory = new File(System.getProperty(USER_DIRECTORY));
		
		// creating testFile of sorted and unsorted order
		File myFile1 = new File("unSortFile.txt");
		myFile1.createNewFile();
		writeFile("unSortFile.txt", "zzz" + System.lineSeparator() + "bbb"
				+ System.lineSeparator() + "aaa" + System.lineSeparator()
				+ "ggg" + System.lineSeparator() + "fff");

		// creating testFile 1/2 with sorted order
		File myFile2 = new File("sortFile.txt");
		myFile2.createNewFile();
		writeFile("sortFile.txt", "aaa" + System.lineSeparator() + "bbb"
				+ System.lineSeparator() + "ccc" + System.lineSeparator()
				+ "ddd" + System.lineSeparator() + "eee");
		
		File emptyFile = new File("emptyFile.txt");
		emptyFile.createNewFile();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File file1 = new File("sortFile.txt");
		if (file1.exists()) {
			file1.delete();
		}

		File file2 = new File("unSortFile.txt");
		if (file2.exists()) {
			file2.delete();
		}
		
		File emptyFile = new File("emptyFile.txt");
		if(emptyFile.exists()){
			emptyFile.delete();
		}
	}

	@Before
	public void setUp() throws Exception {
		sortTool = new SortTool(null);
	}

	@After
	public void tearDown() throws Exception {
		sortTool = null;
	}
	
	public static void writeFile(String fileName, String s) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write(s);
		out.close();
	}

	@Test
	public void checkIfSorted_NullObject_MissingOperand(){
		assertEquals("sort: missing operand" + System.lineSeparator()
				+ "Try 'sort -help' for more information",
				sortTool.checkIfSorted(null));
	}
	
	@Test
	public void sortFile_NullObject_MissingOperand(){
		assertEquals("sort: missing operand" + System.lineSeparator()
				+ "Try 'sort -help' for more information",
				sortTool.sortFile(null));
	}
	
	@Test
	public void execute_1SortedFile_Output(){
		String[] arguments = {"sortFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+"ccc"+System.lineSeparator()+"ddd"+System.lineSeparator()+"eee",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_1UnsortedFile_Output(){
		String[] arguments = {"unSortFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+"fff"+System.lineSeparator()+"ggg"+System.lineSeparator()+"zzz",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_1Unsorted1SortedFile_Output(){
		String[] arguments = {"sortFile.txt", "unSortFile.txt"};
		sortTool = new SortTool(arguments);
		
		assertEquals(
				"aaa" + System.lineSeparator() + "aaa" + System.lineSeparator()
						+ "bbb" + System.lineSeparator() + "bbb"
						+ System.lineSeparator() + "ccc"
						+ System.lineSeparator() + "ddd"
						+ System.lineSeparator() + "eee"
						+ System.lineSeparator() + "fff"
						+ System.lineSeparator() + "ggg"
						+ System.lineSeparator() + "zzz",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_File2NotExist_NoSuchFileError(){
		String[] arguments = {"sortFile.txt", "NotExist.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("sort: stat failed: NotExist.txt: No such file or directory",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_1ValidOption1SortedFile_Output(){
		String[] arguments = {"-c", "sortFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_1ValidOption1UnsortedFile_Output(){
		String[] arguments = {"-c", "unSortFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("sort: unSortFile.txt:2: disorder: bbb",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_1ValidOption2File_ExtraOperand(){
		String[] arguments = {"-c", "unSortFile.txt", "sortFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("sort: extra operand 'sortFile.txt' not allowed with -c",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_2ValidOption1File_Output(){
		String[] arguments = {"-c", "-c", "sortFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_1ValidOption1File2NotExist_ExtraOperand(){
		String[] arguments = {"-c", "sortFile.txt", "NotExist.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("sort: extra operand 'NotExist.txt' not allowed with -c",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_2ValidOption1File1NotExist_ExtraOperand(){
		String[] arguments = {"-c", "-c", "NotExist.txt", "sortFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("sort: extra operand 'sortFile.txt' not allowed with -c",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_HelpOption_Output(){
		String[] arguments = {"-help", "sortFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals(SORT_HELP,
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_HelpWithValidOption_Output(){
		String[] arguments = {"-help", "-c"};
		sortTool = new SortTool(arguments);
		assertEquals(SORT_HELP,
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_HelpWithInvalidOption_Output(){
		String[] arguments = {"-help", "-unknown"};
		sortTool = new SortTool(arguments);
		assertEquals(SORT_HELP,
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_NoOptionEmptyFile_Output(){
		String[] arguments = {"emptyFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	@Test
	public void execute_1ValidOptionEmptyFile_Output(){
		String[] arguments = {"-c", "emptyFile.txt"};
		sortTool = new SortTool(arguments);
		assertEquals("",
				sortTool.execute(defaultWorkingDirectory, null));
	}
	
	
	//This is an unsupported features
	public void execute_UnsortedInputFromStdinNoOption_SortedOutput(){
		String[] argument = {"-"}; 
		
		sortTool = new SortTool(argument);
		
		String result = sortTool.execute(new File("user.dir"),
				"aaa" + System.lineSeparator() + "ccc" + System.lineSeparator()
						+ "bbb" + System.lineSeparator());
		assertEquals(
				"aaa" + System.lineSeparator() + "bbb" + System.lineSeparator()
						+ "ccc" + System.lineSeparator(), result);
	}
}
