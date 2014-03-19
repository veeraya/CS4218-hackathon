/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.ITool;

public class ShellIntegrationTest {

	public TemporaryFolder folder = new TemporaryFolder();
	private File testFile;
	private File testFile2;
	private File testDir;
	private Shell shell;
	private static File workingDir;

	@Before
	public void before() throws IOException{
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
        sb.append("beforeBeforeBeforeApp" + System.lineSeparator());
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterAfterApp" + System.lineSeparator());
		bw.write(sb.toString());
        bw.close();
        
		testFile2 = new File(testDir, "test2.txt");
        testFile2.createNewFile();
        FileWriter fw2 = new FileWriter(testFile2.getAbsoluteFile());
        BufferedWriter bw2 = new BufferedWriter(fw2);
        StringBuilder sb2 = new StringBuilder();
		sb2.append("banana" + System.lineSeparator());
		sb2.append("bananas" + System.lineSeparator());
		bw2.write(sb2.toString());
        bw2.close();
	}

	@After
	public void after(){
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
	 * Structural Integration - Test expected behavior
	 * Executing echo followed by grep
	 */
	@Test()
	public void grepEchoTest() {
		String commandLine = "echo the quick brown fox jumps over the lazy dog | grep -c q..k -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("0\n", result);
	}
	
	/**
	 * Structural Integration - Test expected behavior
	 * Executing cat followed by grep
	 */
	@Test()
	public void grepCatTest() {
		String commandLine = "cat "+testFile.getName()+" | grep -c a..e -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("4\n", result);
	}
	
	/**
	 * Structural Integration - Test expected behavior
	 * Executing grep followed by cut
	 */
	@Test()
	public void grepCutTest() {
		String commandLine = "grep apple "+testFile.getName()+"| cut -c 1 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("a\na\n", result);
	}
	
	/**
	 * Structural Integration - Test expected behavior
	 * Executing grep followed by wc
	 */
	@Test()
	public void grepWcTest() {
		String commandLine = "grep apple "+testFile.getName()+"| wc -l ";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("2\t\n", result);
	}
	
	/**
	 * Structural Integration - Test expected behavior
	 * Executing grep followed by paste
	 */
	@Test()
	public void grepPasteTest() {
		String commandLine = "grep apple "+testFile.getName()+" | paste -d . - "+testFile2.getName();
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("apple.banana\napple.bananas\n", result);
	}
	
	/**
	 * Structural Integration - Test expected behavior
	 * Executing grep followed by uniq
	 */
	@Test()
	public void grepUniqTest() {
		String commandLine = "grep apple "+testFile.getName()+" | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("apple\n", result);
	}
	
	/**
	 * Structural Integration - Test expected behavior
	 * Executing ls followed by grep
	 */
	@Test()
	public void grepLsTest() {
		String commandLine = "ls "+testDir.getAbsolutePath()+" | grep test -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("test.txt" + System.lineSeparator() + "test2.txt" + System.lineSeparator() + "\n", result);
	}
	
	/**
	 * Structural Integration - Test expected behavior
	 * Executing grep followed by sort
	 */
	@Test()
	public void grepSortTest() {
		String commandLine = "sort "+testFile.getName()+" | grep -B 2 mid -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("beforeBeforeApp\r\nbeforeBeforeBeforeApp\r\nmid\r\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing sort, grep and cut with 2 pipes
	 */
	@Test()
	public void sortGrepCutTest() {
		String commandLine = "sort "+testFile.getName()+" | grep a - | cut -c 1 - ";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("a\na\na\na\na\na\n", result);
	}

}
