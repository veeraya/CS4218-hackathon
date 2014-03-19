/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;

/**
 * @author pook
 *
 */
public class OurLSToolTest {

	//TODO Always test against the interface! 
	private ILsTool lstool; 
	private File testDir;
	
	
	@Before
	public void before(){
		testDir = new File("testLsFolder");
		testDir.mkdir();
		lstool = new LsTool(new String[0]);
		
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
		lstool = null;
	}
	
	@Test
	public void executeEmptyDirectoryTest() throws IOException {
		//Test ls in empty directory
		//Create a tmp-file and get (existing) parent directory
		assertEquals("", lstool.execute(testDir, ""));
		assertEquals(0, lstool.getStatusCode());
    }
	
	@Test
	public void executeOneFileTest() throws IOException{
		File file = new File(testDir, "test1.txt");
		file.createNewFile();
		assertEquals("test1.txt", lstool.execute(testDir, ""));
		assertEquals(0, lstool.getStatusCode());
	}
	
	@Test
	public void executeFileWithSpace() throws IOException{
		File file = new File(testDir, "this is a test.txt");
		file.createNewFile();
		assertEquals("this is a test.txt", lstool.execute(testDir, ""));
		assertEquals(0, lstool.getStatusCode());
	}
	
	@Test
	public void executeManyFilesTest() throws IOException{
		File file2 = new File(testDir, "test2.txt");
		file2.createNewFile();
		File file1 = new File(testDir, "test1.txt");
		file1.createNewFile();
		File file3 = new File(testDir, "test3.pdf");
		file3.createNewFile();
		File file4 = new File(testDir, "test4.doc");
		file4.createNewFile();
		assertEquals("test1.txt\r\ntest2.txt\r\ntest3.pdf\r\ntest4.doc", lstool.execute(testDir, ""));
		assertEquals(0, lstool.getStatusCode());
	}
	
	@Test
	public void executeOneDirectoryTest() throws IOException{
		File folder = new File(testDir, "folder1");
		folder.mkdir();
		assertEquals("folder1", lstool.execute(testDir, ""));
		assertEquals(0, lstool.getStatusCode());
	}
	
	@Test
	public void executeManyDirecotiresTest() throws IOException{
		File folder1 = new File(testDir, "folder1");
		folder1.mkdir();
		File folder2 = new File(testDir, "folder2");
		folder2.mkdir();
		File folder3 = new File(testDir, "abcFolder");
		folder3.mkdir();
		File folder4 = new File(testDir, "MyFolder");
		folder4.mkdir();
		File folder5 = new File(testDir, "yetAnotherFolder");
		folder5.mkdir();
		assertEquals("abcFolder\r\nfolder1\r\nfolder2\r\nMyFolder\r\nyetAnotherFolder", lstool.execute(testDir, null));
		assertEquals(0, lstool.getStatusCode());
	}
	
	@Test
	public void executeManyFilesAndDirectoriesTest() throws IOException{
		File file2 = new File(testDir, "test2.txt");
		file2.createNewFile();
		File file1 = new File(testDir, "test1.txt");
		file1.createNewFile();
		File file3 = new File(testDir, "test3.pdf");
		file3.createNewFile();
		File file4 = new File(testDir, "test4.doc");
		file4.createNewFile();
		
		File folder1 = new File(testDir, "folder1");
		folder1.mkdir();
		File folder2 = new File(testDir, "folder2");
		folder2.mkdir();
		File folder3 = new File(testDir, "abcFolder");
		folder3.mkdir();
		File folder4 = new File(testDir, "MyFolder");
		folder4.mkdir();
		File folder5 = new File(testDir, "yetAnotherFolder");
		folder5.mkdir();
		assertEquals("abcFolder\r\nfolder1\r\nfolder2\r\nMyFolder\r\ntest1.txt\r\ntest2.txt\r\ntest3.pdf\r\ntest4.doc\r\nyetAnotherFolder", lstool.execute(testDir, ""));
		assertEquals(0, lstool.getStatusCode());
	}
	
	@Test
	public void executeNullWorkingDirectoryTest() throws IOException{
		assertEquals("Error: Cannot find working directory", lstool.execute(null, ""));
		assertEquals(1, lstool.getStatusCode());
	}
	
	@Test
	public void executeNonExistentWorkingDirectoryTest() throws IOException{
		assertEquals("Error: Cannot find working directory", lstool.execute(new File("abc"), ""));
		assertEquals(1, lstool.getStatusCode());
	}
	
	@Test
	public void executeNotAWorkingDirectoryTest() throws IOException{
		File file = new File(testDir, "test2.txt");
		file.createNewFile();
		assertEquals("Error: Cannot find working directory", lstool.execute(file, ""));
		assertEquals(1, lstool.getStatusCode());
	}

}
