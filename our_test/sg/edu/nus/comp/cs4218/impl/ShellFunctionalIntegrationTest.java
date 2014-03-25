/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * @author pook
 *
 */
public class ShellFunctionalIntegrationTest {
	public TemporaryFolder folder = new TemporaryFolder();
	private File testFile;
	private File testFile2;
	private File testFile3;
	private File testDir;
	private Shell shell;
	private static File workingDir;
	
	@Before
	public void setUp() throws Exception {
		shell = new Shell();
		testDir = new File("testFolder");
		workingDir = new File(testDir.getAbsolutePath());
        testDir.mkdir();
        
        // create new file with text
        testFile = new File(testDir, "test.txt");
        testFile.createNewFile();
        FileWriter fw = new FileWriter(testFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder sb = new StringBuilder();
        sb.append("beforeBeforeBeforeApp\n");
		sb.append("beforeBeforeApp\n");
		sb.append("beforeApp\n");
		sb.append("apple\n");
		sb.append("mid\n");
		sb.append("apple\n");
		sb.append("afterApp\n");
		sb.append("afterAfterApp\n");
		sb.append("afterAfterAfterApp\n");
		sb.append("afterAfterAfterAfterApp\n");
		bw.write(sb.toString());
        bw.close();
        
		testFile2 = new File(testDir, "test2.txt");
        testFile2.createNewFile();
        FileWriter fw2 = new FileWriter(testFile2.getAbsoluteFile());
        BufferedWriter bw2 = new BufferedWriter(fw2);
        StringBuilder sb2 = new StringBuilder();
		sb2.append("banana\n");
		sb2.append("bananas\n");
		bw2.write(sb2.toString());
        bw2.close();
        
        testFile3 = new File(testDir, "test3.txt");
        testFile3.createNewFile();
        FileWriter fw3 = new FileWriter(testFile3.getAbsoluteFile());
        BufferedWriter bw3 = new BufferedWriter(fw3);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("apple\n");
        sb3.append("apple\n");
		sb3.append("banana\n");
		sb3.append("bananas\n");
		bw3.write(sb3.toString());
        bw3.close();
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
        shell = null;
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CD | PWD
	 */
	@Test
	public void cdPwdTest() {
		String commandLine = "cd C:\\ | pwd";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("C:\\", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CAT|PASTE|GREP 
	 */
	@Test
	public void catPasteGrepTest() {
		String commandLine = "cat " + testFile2.getAbsolutePath() + " | paste " + testFile2.getAbsolutePath() + " - | grep ban -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tbanana\r\nbananas\tbananas\r\n\n", result);
	}
}
