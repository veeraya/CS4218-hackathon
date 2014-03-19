package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;

public class DeleteToolTest {
	
	IDeleteTool tool;
	
	@Before
	public void setUp() throws Exception {
		String[] args = new String[] {"testdir"};
		tool = new DeleteTool(args);
	}

	@After
	public void tearDown() throws Exception {
		tool = null;
	}

	@Test
	public void delete_File_ReturnStatusCode0() {
		
		File testFile = new File("testfile.txt");
		try {
			testFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean result = tool.delete(testFile);
		assertTrue(result);
		assertEquals(0, tool.getStatusCode());
	}
	
	@Test
	public void delete_Directory_ReturnStatusCode0() {
		
		File testDir = new File("testdir");
		testDir.mkdir();
		
		boolean result = tool.delete(testDir);
		assertTrue(result);
		assertEquals(0, tool.getStatusCode());
	}
	
	@Test
	public void delete_DirectoryNotExist_ReturnStatusCode2() {
		File file = new File("testdir");
		boolean result = tool.delete(file);
		assertFalse(result);
		assertEquals(DeleteTool.STATUS_CODE_INVALID_FILE_OR_DIRECTORY, tool.getStatusCode());
	}
	
	@Test
	public void delete_NotEmptyDirectory_ReturnStatusCode3() {
		File testDir = new File("testdir");
		testDir.mkdir();
		
		File testFile = new File("testdir" + File.separator+ "testfile.txt");
		try {
			testFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean result = tool.delete(testDir);
		assertFalse(result);
		assertEquals(DeleteTool.STATUS_CODE_FAILED_TO_DELETE, tool.getStatusCode());
		
		testFile.delete();
		testDir.delete();
	}
	
	@Test
	public void execute_DeleteDirectory_GetStatusCode0() {
		File testDir = new File("testdir");
		testDir.mkdir();
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertEquals("", result);
		assertEquals(0, tool.getStatusCode());
		assertFalse(testDir.exists());
	}
	
	@Test
	public void execute_DeleteMultipleDirectories_GetStatusCode0() {
		String[] args = new String[] {"testdir1", "testdir2", "testdir3"};
		tool = new DeleteTool(args);
		
		File testDir1 = new File("testdir1");
		testDir1.mkdir();
		File testDir2 = new File("testdir2");
		testDir2.mkdir();
		File testDir3 = new File("testdir3");
		testDir3.mkdir();
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertEquals("", result);
		assertEquals(0, tool.getStatusCode());
		assertFalse(testDir1.exists());
		assertFalse(testDir2.exists());
		assertFalse(testDir3.exists());
	}
	
	@Test
	public void execute_DeleteFile_GetStatusCode0() {
		String[] args = new String[] {"_testfile.txt"};
		tool = new DeleteTool(args);
		
		File testFile = new File("_testfile.txt");
		try {
			testFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertEquals("", result);
		assertEquals(0, tool.getStatusCode());
		assertFalse(testFile.exists());
	}
	
	@Test
	public void execute_DeleteMultipleFiles_GetStatusCode0() {
		String[] args = new String[] {"_testfile1.txt", "_testfile2.txt", "_testfile3.txt"};
		tool = new DeleteTool(args);
		
		File testFile1 = new File("_testfile1.txt");
		File testFile2 = new File("_testfile2.txt");
		File testFile3 = new File("_testfile3.txt");
		try {
			testFile1.createNewFile();
			testFile2.createNewFile();
			testFile3.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertEquals("", result);
		assertEquals(0, tool.getStatusCode());
		assertFalse(testFile1.exists());
		assertFalse(testFile2.exists());
		assertFalse(testFile3.exists());
	}
	
	@Test
	public void execute_DeleteMultipleFilesDirectories_GetStatusCode0() {
		String[] args = new String[] {"_testfile1.txt", "testDir1", "_testfile2.txt", "testDir2"};
		tool = new DeleteTool(args);
		
		File testDir1 = new File("testdir1");
		testDir1.mkdir();
		File testDir2 = new File("testdir2");
		testDir2.mkdir();
		
		File testFile1 = new File("_testfile1.txt");
		File testFile2 = new File("_testfile2.txt");
		try {
			testFile1.createNewFile();
			testFile2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertEquals("", result);
		assertEquals(0, tool.getStatusCode());
		assertFalse(testFile1.exists());
		assertFalse(testFile2.exists());
		assertFalse(testDir1.exists());
		assertFalse(testDir2.exists());
	}
	
	@Test
	public void execute_DeleteDirectoryNotExist_GetStatusCode2() {
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertTrue(!result.isEmpty());
		assertEquals(DeleteTool.STATUS_CODE_INVALID_FILE_OR_DIRECTORY, tool.getStatusCode());
	}
	
	@Test
	public void execute_DeleteFirstDirNotExist_GetStatusCode2() {
		String[] args = new String[] {"testdir1","testdir2"};
		tool = new DeleteTool(args);
		
		File testDir2 = new File("testdir2");
		testDir2.mkdir();
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertTrue(!result.isEmpty());
		assertEquals(DeleteTool.STATUS_CODE_INVALID_FILE_OR_DIRECTORY, tool.getStatusCode());
		assertTrue(testDir2.exists());
	}
	
	@Test
	public void execute_DeleteSecondDirNotExist_GetStatusCode2() {
		String[] args = new String[] {"testdir1","testdir2"};
		tool = new DeleteTool(args);
		
		File testDir1 = new File("testdir1");
		testDir1.mkdir();
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertTrue(!result.isEmpty());
		assertEquals(DeleteTool.STATUS_CODE_INVALID_FILE_OR_DIRECTORY, tool.getStatusCode());
		assertTrue(!testDir1.exists());
	}
	
	@Test
	public void execute_DeleteFileNotExist_GetStatusCode2() {
		String[] args = new String[] {"testfile.txt"};
		tool = new DeleteTool(args);
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertTrue(!result.isEmpty());
		assertEquals(DeleteTool.STATUS_CODE_INVALID_FILE_OR_DIRECTORY, tool.getStatusCode());
	}
	
	@Test
	public void execute_DeleteFirstFileNotExist_GetStatusCode2() {
		String[] args = new String[] {"_testfile1.txt","_testfile2.txt"};
		tool = new DeleteTool(args);
		
		File testFile2 = new File("_testfile2.txt");
		try {
			testFile2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertTrue(!result.isEmpty());
		assertEquals(DeleteTool.STATUS_CODE_INVALID_FILE_OR_DIRECTORY, tool.getStatusCode());
		assertTrue(testFile2.exists());
	}
	
	@Test
	public void execute_DeleteSecondFileNotExist_GetStatusCode2() {
		String[] args = new String[] {"_testfile1.txt","_testfile2.txt"};
		tool = new DeleteTool(args);
		
		File testFile1 = new File("_testfile1.txt");
		try {
			testFile1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertTrue(!result.isEmpty());
		assertEquals(DeleteTool.STATUS_CODE_INVALID_FILE_OR_DIRECTORY, tool.getStatusCode());
		assertTrue(!testFile1.exists());
	}
	
	@Test
	public void execute_NotEmptyDirectory_GetStatusCode3() {
		File testDir = new File("testdir");
		testDir.mkdir();
		
		File testFile = new File("testdir" + File.separator+ "testfile.txt");
		try {
			testFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File workingDir = new File(System.getProperty("user.dir"));
		String result = tool.execute(workingDir, null);
		assertTrue(!result.isEmpty());
		assertEquals(DeleteTool.STATUS_CODE_FAILED_TO_DELETE, tool.getStatusCode());
		
		testFile.delete();
		testDir.delete();
	}
}
