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

import sg.edu.nus.comp.cs4218.ITool;

public class ShellMoreIntegrationTest {

	private File testFile;
	private File testFile2;
	private File dummyDir;
	private File dummyFile;
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

		dummyDir = new File(testDir, "dummyFolder");
		dummyDir.mkdir();
		dummyFile = new File(dummyDir, "dummy.txt");
		dummyFile.createNewFile();

	}

	@After
	public void after(){
		// delete all files in directory
		File[] dummyList = dummyDir.listFiles();
		for (int i=0; i<dummyList.length; i++){
			File file = dummyList[i];
			file.delete();
		}

		File[] fileList = testDir.listFiles();
		for (int i=0; i<fileList.length; i++){
			File file = fileList[i];
			file.delete();
		}

		// delete directory
		dummyDir.delete();
		testDir.delete();
		shell = null;
	}

	/**
	 * State of Shell changed - Test expected behavior
	 * Executing cd, ls and grep - locating number of dummy files in testDir sub-directory, dummyDir
	 */
	@Test
	public void cdLsGrepTest() {
		String commandLine = "cd " + dummyDir.getAbsolutePath() + " | ls | grep -c dummy -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("1\n", result);
	}

	/**
	 * State of Shell changed - Test expected behavior
	 * Executing copy, ls and grep - copy dummy file to current directory and check the number of 
	 * dummy files in the current directory
	 */
	@Test
	public void copyLsGrepTest() {
		String commandLine = "copy " + testFile.getName() + " " + dummyDir.getName() + " | ls | grep -c dummy -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("1\n", result);
		//shows that the file test.txt is now in dummy directory
		commandLine = "ls " + dummyDir.getName();
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		assertEquals("dummy.txt" + System.lineSeparator() + "test.txt", result);
		//shows that the file test.txt is also in test directory
		commandLine = "ls " + testDir.getAbsolutePath() + " | grep -c test.txt -";
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		assertEquals("1\n", result);
	}

	/**
	 * State of Shell changed - Test expected behavior
	 * Executing move, ls and grep - copy dummy file to current directory and check the number of 
	 * dummy files in the current directory
	 */
	@Test
	public void moveLsGrepTest() {
		String commandLine = "move " + testFile.getName() + " " + dummyDir.getName() + " | ls | grep -c dummy -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("1\n", result);
		//shows that the file test.txt is now in dummy directory
		commandLine = "ls " + dummyDir.getName();
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		assertEquals("dummy.txt" + System.lineSeparator() + "test.txt", result);
		//shows that the file test.txt is not in test directory
		commandLine = "ls " + testDir.getAbsolutePath() + " | grep -c test.txt -";
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		assertEquals("0\n", result);	
	}

	/**
	 * State of Shell changed - Test expected behavior
	 * Executing move, ls and grep - copy dummy file to current directory and check the number of 
	 * dummy files in the current directory
	 */
	@Test
	public void deleteLsGrepTest() {
		String commandLine = "delete " + testFile.getName() + " | ls | grep -c test.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("0\n", result);	
	}

	/**
	 * State of Shell changed - Test expected behavior
	 * Executing cd, ls and cat - change to a specified directory and save the content of
	 * that directory to a text file
	 */
	@Test
	public void cdLsCatTest() {
		//check that the current directory is testFolder
		String commandLine = "pwd ";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals(workingDir.getAbsolutePath(), result);	
		//execute complex command
		commandLine = "cd " + dummyDir.getName() + " | ls | cat - > files.txt";
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		assertEquals("", result);	
		//check that files.txt is created in dummyFolder by printing out files.txt
		commandLine = "ls " + dummyDir.getName();
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		assertEquals("dummy.txt"+System.lineSeparator() + "files.txt", result);
	}

	/**
	 * State of Shell changed - Test expected behavior
	 * Executing ls, cat and copy -  save the content of
	 * the specified directory to a text file. Copy the file to another location.
	 */
	@Test
	public void lsCatCopyTest() {
		//execute complex command
		String commandLine = "ls | cat - > files.txt | copy files.txt " + dummyDir.getName();
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("", result);	
		//check that files.txt is created in dummyFolder
		commandLine = "ls " + dummyDir.getName();
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		String expected = "dummy.txt" + System.lineSeparator() + "files.txt";
		assertEquals(expected, result);
	}
	
	/**
	 * State of Shell changed - Test expected behavior
	 * Executing ls, cat, copy and delete -  save the content of
	 * the specified directory to a text file. Copy the file to another location and delete it afterwards.
	 */
	@Test
	public void lsCatCopyDeleteTest() {
		//execute complex command
		String commandLine = "ls | cat - > files.txt | copy files.txt " + dummyDir.getName() + " | delete " + dummyDir.getName() + "\\files.txt";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("", result);	
		//check that files.txt is created in dummyFolder
		commandLine = "ls " + dummyDir.getName();
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		String expected = "dummy.txt";
		assertEquals(expected, result);
	}
	
	/**
	 * State of Shell changed - Test expected behavior
	 * Executing ls, cat, move and delete -  save the content of
	 * the specified directory to a text file. Copy the file to another location and delete it afterwards.
	 */
	@Test
	public void lsCatMoveDeleteTest() {
		//execute complex command
		String commandLine = "ls | cat - > files.txt | move files.txt " + dummyDir.getName() + " | delete " + dummyDir.getName() + "\\files.txt";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("", result);	
		//check that files.txt is created in dummyFolder
		commandLine = "ls " + dummyDir.getName();
		command = shell.parse(commandLine);
		result = command.execute(workingDir, null);
		String expected = "dummy.txt";
		assertEquals(expected, result);
	}

}
