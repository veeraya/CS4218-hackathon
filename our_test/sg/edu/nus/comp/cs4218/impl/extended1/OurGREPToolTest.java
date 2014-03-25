package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;

public class OurGREPToolTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	private File testFile;
	private File testDir;
	private IGrepTool grepTool;

	@Before
	public void before() throws IOException {
		testDir = new File("testCutFolder");
        testDir.mkdir();
        
        // create new file with text
        testFile = new File(testDir, "test.txt");
        testFile.createNewFile();
        FileWriter fw = new FileWriter(testFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder sb = new StringBuilder();
        sb.append("beforeBeforeBeforeApp" + System.lineSeparator());
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterAfterApp" + System.lineSeparator());
		bw.write(sb.toString());
        bw.close();
	}

	@After
	public void after() {
		grepTool = null;
		
		// delete all files in directory
        File[] fileList = testDir.listFiles();
        for (int i=0; i<fileList.length; i++){
            File file = fileList[i];
            file.delete();
        }
        // delete directory
        testDir.delete();
        // garbage collector swoop in right about here
        grepTool = null;
	}

	@Test
	public void executeFileNameMissingTest(){
		grepTool = new GrepTool(new String[]{"-A", "2", "apple"});
		assertEquals("Error: you must specify a file name", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void getCountOfMatchingLinesWithNullInputTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(0, grepTool.getCountOfMatchingLines("", null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void getCountOfMatchingLinesWithNullPatternTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(2, grepTool.getCountOfMatchingLines(null, "abc\ndef"));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeNegativeNumberATest() throws IOException{
        grepTool = new GrepTool(new String[]{"-A", "-2", "a.", testFile.getAbsolutePath()});
		assertEquals("Error: only positive numbers are allowed.", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeNegativeNumberBTest() throws IOException{
        grepTool = new GrepTool(new String[]{"-B", "-2", "a.", testFile.getAbsolutePath()});
		assertEquals("Error: only positive numbers are allowed.", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeNegativeNumberCTest() throws IOException{
        grepTool = new GrepTool(new String[]{"-C", "-2", "a.", testFile.getAbsolutePath()});
		assertEquals("Error: only positive numbers are allowed.", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}


}
