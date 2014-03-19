package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;

public class OurPASTEToolTest {
	private IPasteTool pastetool; 
	private File testDir;
	private File testFile1;
	private File testFile2;
	private File testFile3;

	@Before
	public void setUp() throws Exception {
		testDir = new File("testPasteFolder");
		testDir.mkdir();

		// create new file with text
		testFile1 = new File(testDir, "names.txt");
		testFile1.createNewFile();
		FileWriter fw = new FileWriter(testFile1.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		StringBuilder sb = new StringBuilder();
		sb.append("Mark Smith\n");
		sb.append("Bobby Brown\n");
		sb.append("Sue Miller\n");
		sb.append("Jenny Igotit");
		bw.write(sb.toString());
		bw.close();

		testFile2 = new File(testDir, "numbers.txt");
		testFile2.createNewFile();
		fw = new FileWriter(testFile2.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		sb = new StringBuilder();
		sb.append("555-1234\n");
		sb.append("555-9876\n");
		sb.append("555-6743\n");
		sb.append("867-5309");
		bw.write(sb.toString());
		bw.close();


		// create new file with text
		testFile3 = new File(testDir, "names short.txt");
		testFile3.createNewFile();
		fw = new FileWriter(testFile3.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		sb = new StringBuilder();
		sb.append("Mark Smith\n");
		sb.append("Bobby Brown\n");
		sb.append("Sue Miller\n");
		bw.write(sb.toString());
		bw.close();
	}

	@After
	public void tearDown() throws Exception {
		// delete all files in directory
		File[] fileList = testDir.listFiles();
		for (int i=0; i<fileList.length; i++){
			File file = fileList[i];
			file.delete();
		}
		// delete directory
		testDir.delete();
		pastetool = null;
	}

	@Test
	public void pasteSerialTest() {
		pastetool = new PasteTool(new String[]{});
		String expectedOutput = "Mark Smith\tBobby Brown\tSue Miller\tJenny Igotit\n555-1234\t555-9876\t555-6743\t867-5309";
		String[] inputArr = {"Mark Smith\nBobby Brown\nSue Miller\nJenny Igotit", "555-1234\n555-9876\n555-6743\n867-5309"};
		assertEquals(expectedOutput, pastetool.pasteSerial(inputArr));
	}

	@Test
	public void pasteNoDelimEqualLinesTest(){
		pastetool = new PasteTool(new String[]{});
		String[] inputArr = {"Mark Smith\nBobby Brown\nSue Miller\nJenny Igotit", "555-1234\n555-9876\n555-6743\n867-5309"};
		String expectedOutput = "Mark Smith\t555-1234\nBobby Brown\t555-9876\nSue Miller\t555-6743\nJenny Igotit\t867-5309";
		assertEquals(expectedOutput, pastetool.pasteUseDelimiter(null,inputArr));
	}

	@Test
	public void pasteNoDelimUnequalLinesTest(){
		pastetool = new PasteTool(new String[]{});
		String[] inputArr = {"Mark Smith\nBobby Brown\nSue Miller", "555-1234\n555-9876\n555-6743\n867-5309"};
		String expectedOutput = "Mark Smith\t555-1234\nBobby Brown\t555-9876\nSue Miller\t555-6743\n\t867-5309";
		assertEquals(expectedOutput, pastetool.pasteUseDelimiter(null,inputArr));
		expectedOutput = "Mark Smith\t555-1234\nBobby Brown\t555-9876\nSue Miller\t555-6743\n\t867-5309";
		assertEquals(expectedOutput, pastetool.pasteUseDelimiter(null,inputArr));
	}

	@Test
	public void pasteUseDelimEqualLinesTest(){
		pastetool = new PasteTool(new String[]{});
		String[] inputArr = {"Mark Smith\nBobby Brown\nSue Miller\nJenny Igotit", "555-1234\n555-9876\n555-6743\n867-5309"};
		String delim = ".,";
		String expectedOutput = "Mark Smith.555-1234\nBobby Brown,555-9876\nSue Miller.555-6743\nJenny Igotit,867-5309";
		assertEquals(expectedOutput, pastetool.pasteUseDelimiter(delim,inputArr));
	}

	@Test
	public void pasteUseDelimUnequalLinesTest(){
		pastetool = new PasteTool(new String[]{});
		String[] inputArr = {"Mark Smith\nBobby Brown\nSue Miller", "555-1234\n555-9876\n555-6743\n867-5309"};
		String delim = ".,";
		String expectedOutput = "Mark Smith.555-1234\nBobby Brown,555-9876\nSue Miller.555-6743\n,867-5309";
		assertEquals(expectedOutput, pastetool.pasteUseDelimiter(delim,inputArr));
		delim = ".";
		expectedOutput = "Mark Smith.555-1234\nBobby Brown.555-9876\nSue Miller.555-6743\n.867-5309";
		assertEquals(expectedOutput, pastetool.pasteUseDelimiter(delim,inputArr));
	}

	@Test
	public void executeBlankParamTest(){
		pastetool = new PasteTool(new String[]{});
		assertEquals("Error: Missing parameter", pastetool.execute(testDir, null));
		assertEquals(1, pastetool.getStatusCode());
	}

	@Test
	public void executeNullParamTest(){
		pastetool = new PasteTool(new String[]{});
		assertEquals("Error: Missing parameter", pastetool.execute(testDir, null));
		assertEquals(1, pastetool.getStatusCode());
	}

	@Test
	public void executeInvalidFileNameTest(){
		pastetool = new PasteTool(new String[]{"numbers.txt", "\"test.txt"});
		assertEquals("Error: invalid file name", pastetool.execute(testDir, null));
		assertEquals(1, pastetool.getStatusCode());
	}

	@Test
	public void executeMissingFilesTest(){
		pastetool = new PasteTool(new String[]{"numbers.txt", "test.txt"});
		assertEquals("Error: file test.txt doesn't exist", pastetool.execute(testDir, null));
		pastetool = new PasteTool(new String[]{"test.txt", "numbers.txt"});
		assertEquals("Error: file test.txt doesn't exist", pastetool.execute(testDir, null));
		pastetool = new PasteTool(new String[]{"test.txt", "test2.txt"});
		assertEquals("Error: file test.txt doesn't exist", pastetool.execute(testDir, null));
		assertEquals(1, pastetool.getStatusCode());
	}

	@Test
	public void executeMissingFileNameTest(){
		pastetool = new PasteTool(new String[]{"-s"});
		assertEquals("Error: you must specify a file name", pastetool.execute(testDir, null));
		assertEquals(1, pastetool.getStatusCode());
	}

	@Test
	public void executePasteSerialTest(){
		pastetool = new PasteTool(new String[]{"-s", "names.txt", "numbers.txt"});
		String expectedOutput = "Mark Smith\tBobby Brown\tSue Miller\tJenny Igotit\n555-1234\t555-9876\t555-6743\t867-5309";
		assertEquals(expectedOutput, pastetool.execute(testDir, null));
	}

	@Test
	public void executePasteUseDelimiterEqualLines(){
		pastetool = new PasteTool(new String[]{"-d", ".,", "\"names.txt\"", "numbers.txt"});
		String expectedOutput = "Mark Smith.555-1234\nBobby Brown,555-9876\nSue Miller.555-6743\nJenny Igotit,867-5309";
		assertEquals(expectedOutput, pastetool.execute(testDir, null));
	}

	@Test
	public void executePasteUseDelimiterUnequalLines(){
		pastetool = new PasteTool(new String[]{"-d", ".,", "\"names", "short.txt\"", "numbers.txt"});
		String expectedOutput = "Mark Smith.555-1234\nBobby Brown,555-9876\nSue Miller.555-6743\n,867-5309";
		assertEquals(expectedOutput, pastetool.execute(testDir,null));
		pastetool = new PasteTool(new String[]{"-d", ".", "\"names", "short.txt\"", "numbers.txt"});
		expectedOutput = "Mark Smith.555-1234\nBobby Brown.555-9876\nSue Miller.555-6743\n.867-5309";
		assertEquals(expectedOutput, pastetool.execute(testDir, null));
	}

	// Note: as stated in forum, "a priority scheme when multiple arguments/options are supplied."
	// In this case, I choose -s to take precedent over -d as there is no interface method to handle both at the same time.
	@Test
	public void executeSerialDelim(){
		pastetool = new PasteTool(new String[]{"-s", "-d", ",", "names.txt", "numbers.txt"});
		String expectedOutput = "Mark Smith\tBobby Brown\tSue Miller\tJenny Igotit\n555-1234\t555-9876\t555-6743\t867-5309";
		assertEquals(expectedOutput, pastetool.execute(testDir, null));
	}

	@Test
	public void executeStdinNull(){
		pastetool = new PasteTool(new String[]{"-s", "-d", ",", "names.txt", "-"});
		String expectedOutput = "Mark Smith\tBobby Brown\tSue Miller\tJenny Igotit\n555-1234\t555-9876\t555-6743\t867-5309";
		String result = pastetool.execute(testDir, null);
		result = pastetool.execute(testDir, "555-1234\n555-9876\n555-6743\n867-5309");
		assertEquals(expectedOutput, result);
	}

	@Test
	public void executeStdin(){
		pastetool = new PasteTool(new String[]{"-s", "-d", ",", "names.txt", "-"});
		String expectedOutput = "Mark Smith\tBobby Brown\tSue Miller\tJenny Igotit\n555-1234\t555-9876\t555-6743\t867-5309";
		String numbers = "555-1234\t555-9876\t555-6743\t867-5309";
		assertEquals(expectedOutput, pastetool.execute(testDir, numbers));
	}

	@Test
	public void getHelpTest() {
		pastetool = new PasteTool(new String[]{"-help"});
		String helpText = pastetool.getHelp();
		assertEquals(helpText, pastetool.execute(testDir, null));
		pastetool = new PasteTool(new String[]{"-help","abcde"});
		assertEquals(helpText, pastetool.execute(testDir, null));
		pastetool = new PasteTool(new String[]{"-d", ".,", "-s", "-help", "abcde"});
		assertEquals(helpText, pastetool.execute(testDir, null));
	}
}
