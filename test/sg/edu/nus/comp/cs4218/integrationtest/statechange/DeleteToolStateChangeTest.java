package sg.edu.nus.comp.cs4218.integrationtest.statechange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;

public class DeleteToolStateChangeTest {

	private File file1, file2, dir;
	private String userDir;
	
	@Before
	public void setUpOnce() throws Exception {
		
		// create files with content
		createFile1();
		createFile2();
		
		// create directory
		dir = new File("dir");
		dir.mkdir();
		
		userDir = System.getProperty("user.dir");
	}
	
	private void createFile1() throws Exception {
		BufferedWriter bw;
		String content;
		
		file1 = new File("file1");
		file1.createNewFile();
	
		content = "this is file1 used for testing"+System.lineSeparator();
		content += "there are 5 lines in this file" + System.lineSeparator();
		content += "this is the 3rd line" + System.lineSeparator();
		content += "this is the 4th line" + System.lineSeparator();
		content += "this is the 5th line";
		
		bw = new BufferedWriter(new FileWriter(file1));
		bw.write(content);
		bw.close();
	}
	
	private void createFile2() throws Exception {
		BufferedWriter bw;
		String content;
		
		file2 = new File("file2");
		file2.createNewFile();
		content = "this is file2 used for testing"+System.lineSeparator();
		content += "testing testing 1 2 3"+System.lineSeparator();
		content += "	???	"+System.lineSeparator()+System.lineSeparator();
		
		bw = new BufferedWriter(new FileWriter(file2));
		bw.write(content);
		bw.close();
	}

	@After
	public void tearDown() throws Exception {
		file1.delete();
		file2.delete();
		dir.delete();
		System.setProperty("user.dir", userDir);
	}
	
	
	
	@Test
	public void stateChange_LsDeleteLs_GetStatusCode0() {
		String commandline1, commandline2, commandline3, output;
		Vector<String> results;
		
		commandline1 = "ls";
		commandline2 = "delete file1 file2";
		commandline3 = "ls";
		
		// 1. check that ls initially contains file1 and file2
		results = Shell.shellTestExecution(commandline1);
		assertEquals(0, Integer.parseInt(results.get(1)));

		output = results.get(0);
		assertTrue(output.contains("file1"));
		assertTrue(output.contains("file2"));
		
		// 2. delete file1 and file2
		// 3. check if ls output contained the files deleted
		results = Shell.shellTestExecution(commandline2, commandline3);
		assertEquals(0, Integer.parseInt(results.get(1)));
		assertEquals(0, Integer.parseInt(results.get(2)));
		
		output = results.get(0);
		assertFalse(output.contains("file1"));
		assertFalse(output.contains("file2"));
		
		// recreate the files that have been deleted
		try {
			createFile1();
			createFile2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void stateChange_LsDeleteGrep_GetStatusCode0() {
		String commandline1, commandline2, commandline3, output;
		Vector<String> results;
		
		commandline1 = "ls";
		commandline2 = "delete file1";
		commandline3 = "grep test file1";
		
		// 1. check that ls initially contains file1
		results = Shell.shellTestExecution(commandline1);
		assertEquals(0, Integer.parseInt(results.get(1)));
		
		output = results.get(0);
		assertTrue(output.contains("file1"));
		
		// 2. delete file1
		// 3. execute grep for file1 which has been deleted
		results = Shell.shellTestExecution(commandline2, commandline3);
		assertEquals(0, Integer.parseInt(results.get(1)));
		assertEquals(GrepTool.ERR_CODE_IO, Integer.parseInt(results.get(2)));
		
		output = results.get(0);
		assertFalse(output.contains("file1"));
		
		// recreate file1
		try {
			createFile1();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void stateChange_CdDeleteCd_GetStatusCode0() {
		String commandline1, commandline2, commandline3;
		Vector<String> results;
		
		commandline1 = "cd dir";
		commandline2 = "delete dir";
		commandline3 = "cd dir";
		
		// 1. check that it is able to cd to directory "dir"
		results = Shell.shellTestExecution(commandline1);
		assertEquals(0, Integer.parseInt(results.get(1)));
		// restore original system property
		System.setProperty("user.dir", userDir);
		
		// 2. delete dir
		// 3. check that cd to directory "dir" will fail
		results = Shell.shellTestExecution(commandline2, commandline3);
		assertEquals(0, Integer.parseInt(results.get(1)));
		assertEquals(CdTool.DIRECTORY_ERROR_CODE, Integer.parseInt(results.get(2)));
		
		// recreate dir
		dir = new File("dir");
		dir.mkdir();
	}
	
}
