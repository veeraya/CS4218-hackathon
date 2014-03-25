package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;

public class OurPASTEToolTest {
	private IPasteTool pastetool; 
	private File testDir;

	@Before
	public void setUp() throws Exception {
		testDir = new File("testPasteFolder");
		testDir.mkdir();
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
	public void pasteRelativeFilePathTest() throws IOException {
		File file = new File(testDir, "test.txt");
		file.createNewFile();
		
		pastetool = new PasteTool(new String[]{"-s", "test.txt", "test.txt"});
		String expectedOutput = "" + System.lineSeparator() + "";
		assertEquals(expectedOutput, pastetool.execute(testDir, null));
		
		file.delete();
		pastetool = null;
	}
	
	@Test
	public void executeNoParamTest(){
		pastetool = new PasteTool(new String[]{});
		assertEquals("Error: Missing parameter", pastetool.execute(testDir, null));
		assertEquals(1, pastetool.getStatusCode());
		pastetool = null;
	}
}
