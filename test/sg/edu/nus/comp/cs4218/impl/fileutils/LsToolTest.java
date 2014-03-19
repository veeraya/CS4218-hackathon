package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;

public class LsToolTest {
	private static ILsTool lsTool;
	private static String strDefaultDirectory;
	private static File currentWorkingDirectory;
	private static final String DIRECTORY_ERROR_MSG = "No such file or directory";

	@Before
	public void setUp() throws Exception {
		strDefaultDirectory = System.getProperty("user.dir");
		currentWorkingDirectory = new File(strDefaultDirectory);
	}

	@After
	public void tearDown() throws Exception {
		lsTool = null;
		System.setProperty("user.dir", strDefaultDirectory);
		strDefaultDirectory = null;
	}

	@Test
	public void getFiles_CurrentDirectory_DirectoryListMatch() {
		lsTool = new LsTool(null);
		final File[] fileList = currentWorkingDirectory.listFiles();

		final List<File> returnStatement = lsTool.getFiles(currentWorkingDirectory);

		assertEquals(fileList.length, returnStatement.size());

		for (int i = 0; i < returnStatement.size(); i++) {
			assertEquals(fileList[i], returnStatement.get(i));
		}
	}

	@Test
	public void getFiles_EmptyDirectory_EmptyList() {
		lsTool = new LsTool(null);
		final String newFolderName = "newFolder"; 
		new File(newFolderName).mkdir();
		
		final File newFolder = new File(newFolderName);
		final List<File> fileList = lsTool.getFiles(newFolder);

		if(fileList == null){
			fail("fileList is null object");
		}

		assertEquals(0, fileList.size());
		
		if(newFolder.exists()){
			try {
				Files.delete(newFolder.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void getFiles_SingleDirectory_ListOfDirectoryMatched() {
		lsTool = new LsTool(null);
		final File singleFileDirectroy = new File("bin");
		final List<File> fileList = lsTool.getFiles(singleFileDirectroy);

		
		assertEquals(new File("bin").list().length, fileList.size()); 
		assertEquals(singleFileDirectroy.listFiles()[0], fileList.get(0));
	}

	@Test
	public void getFiles_NullDirectory_NullObjectReturned() {
		lsTool = new LsTool(null);
		final List<File> returnStatement = lsTool.getFiles(null);

		assertNull(returnStatement);
	}

	@Test
	public void getFiles_InvalidDirectory_NullObjectReject() {
		lsTool = new LsTool(null);
		final File invalidDirectory = new File("invalid");
		final List<File> returnStatement = lsTool.getFiles(invalidDirectory);

		assertNull(returnStatement);
	}

	@Test
	public void getFiles_MultipleFileNames_ListofDirectoryMatched() {
		lsTool = new LsTool(null);
		final File singleFileDirectory = new File("src" + File.separator + "sg"
				+ File.separator + "edu" + File.separator + "nus"
				+ File.separator + "comp" + File.separator + "cs4218"
				+ File.separator + "extended1");

		final List<File> fileList = lsTool.getFiles(singleFileDirectory);
		
		for(int i = 0; i < fileList.size(); i++){
			assertEquals(singleFileDirectory.listFiles()[i], fileList.get(i));
		}
	}

	@Test
	public void getStringForFiles_NullObject_Error() {
		lsTool = new LsTool(null);
		final String returnStatement = lsTool.getStringForFiles(null);

		assertEquals("", returnStatement);
	}

	@Test
	public void getStringForFiles_EmptyList_EmptyStringReturned() {
		lsTool = new LsTool(null);
		final List<File> emptyList = new ArrayList<File>();
		final String returnStatement = lsTool.getStringForFiles(emptyList);

		assertEquals("", returnStatement);
	}

	@Test
	public void execute_CurrentDirectory_ListOfDirectoryMatched() {
		String[] argument = {""};
		lsTool = new LsTool(argument);
		
		final File[] fileList = currentWorkingDirectory.listFiles();
		Vector<String> fileNames = new Vector<>();
		
		for(int i = 0; i < fileList.length; i++){
			fileNames.add(fileList[i].getName());
		}
		
		String[] expectedFileList = fileNames.toArray(new String[fileNames.size()]);
		
		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);
		final String[] testStrFileList = returnStatement.split(System.lineSeparator());

		assertArrayEquals(expectedFileList, testStrFileList);

		assertEquals(0, lsTool.getStatusCode());
	}
	
	@Test
	public void execute_NullArgument_StatusCodeNonZero(){
		lsTool = new LsTool(null);
		
		assertNotEquals(0, lsTool.execute(currentWorkingDirectory, null));
	}

	@Test
	public void execute_EmptyDirectory_EmptyStringReturned() {
		final String newFolderName = "newFolder"; 

		new File(newFolderName).mkdir();
		
		final String[] arguement = {newFolderName};
		lsTool = new LsTool(arguement);

		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);

		assertEquals("", returnStatement);
		assertEquals(0, lsTool.getStatusCode());
	}

	@Test
	public void execute_InvalidDirectory_ErrorMessageReturned() {
		final String[] arguement = {"invalid"};
		lsTool = new LsTool(arguement);
		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);

		assertEquals(DIRECTORY_ERROR_MSG, returnStatement);
		
		assertNotSame(lsTool.getStatusCode(),0);
	}

	@Test
	public void execute_ListCorrectAbsoluteDirectory_CorrectListofDirectoryReturned(){
		File[] pwdFiles = currentWorkingDirectory.listFiles();
		int counter = 0;
		while(!pwdFiles[counter].isDirectory()){
			counter++;
		}
		
		String[] argument = {pwdFiles[counter].getAbsolutePath()};
		lsTool = new LsTool(argument);

		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);
		File[] files = pwdFiles[counter].listFiles();
		Vector<String> directoryList = new Vector<>();
		for(int i = 0; i< files.length; i++){
			directoryList.add(files[i].getName());
		}
		String[] correctDirectoryList = (String[]) directoryList.toArray(new String[directoryList.size()]);
		Arrays.sort(correctDirectoryList);

		final String[] fileList = returnStatement.split(System.lineSeparator());
		Arrays.sort(fileList);

		assertArrayEquals(correctDirectoryList, fileList);
	}

	@Test
	public void execute_ListIncorrectAbsoluteDirectory_ErrorMessageReturned(){
		final String[] arguments = {"/us"};
		lsTool = new LsTool(arguments);

		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);

		assertEquals(returnStatement, DIRECTORY_ERROR_MSG);
	}

	@Test
	public void execute_ListCorrectRelativeDirectory_CorrectListofDirectoryReturned(){
		final String[] arguments = {"src"};
		lsTool = new LsTool(arguments);

		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);
		final String correctDirectoryList = "sg";

		assertEquals(returnStatement, correctDirectoryList);
	}

	@Test
	public void execute_ListInCorrectRelativeDirectory_ErrorMessageReturned(){
		final String[] arguments = {"sr"};
		lsTool = new LsTool(arguments);

		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);

		assertEquals(returnStatement, DIRECTORY_ERROR_MSG);
	}

	@Test
	public void execute_ListCorrectFileName_ListFileName() {
		final String arguementDirectoryList = "src" + File.separator + "sg"
				+ File.separator + "edu" + File.separator + "nus"
				+ File.separator + "comp" + File.separator + "cs4218"
				+ File.separator + "fileutils" + File.separator
				+ "ILsTool.java";

		final String[] arguments = {arguementDirectoryList};

		lsTool = new LsTool(arguments);
		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);

		//		String correctDirectoryList = arguementDirectoryList + System.lineSeparator();

		//FIXME: Need to return absolute path name in this case
		assertEquals("ILsTool.java",returnStatement);
	}

	@Test
	public void execute_ListIncorrectFileName__ErrorMessageReturned(){
		final String[] arguments = {"src.java"};
		lsTool = new LsTool(arguments);

		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);

		assertEquals(DIRECTORY_ERROR_MSG, returnStatement);
	}

	@Test
	public void execute_ListMultipleDirectoryFirstDirCorrect_CorrectListofDirectoryReturned(){
		final String[] arguments = {"src", "test"};
		lsTool = new LsTool(arguments);

		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);
		final String correctDirectoryList = "src:" + System.lineSeparator()
				+ "sg" + System.lineSeparator() + System.lineSeparator() + "test:"
				+ System.lineSeparator() + "sg";

		assertEquals(correctDirectoryList, returnStatement);
	}

	@Test
	public void execute_ListMultipleDirectoryFirstDirIncorrect_ErrorMsgThenCorrectListOfDirectoryReturned(){
		final String[] arguments = {"sr", "test"};
		lsTool = new LsTool(arguments);

		final String returnStatement = lsTool.execute(currentWorkingDirectory, null);
		final String correctDirectoryList = "sr:" + System.lineSeparator()
				+ DIRECTORY_ERROR_MSG + System.lineSeparator()
				+ System.lineSeparator() + "test:" + System.lineSeparator()
				+ "sg";

		assertEquals(correctDirectoryList, returnStatement);
	}
	
	@Test
	public void execute_NullPresentWorkingDirectory_NonZeroStatusCode(){
		final String[] argument = {""};
		lsTool = new LsTool(argument);
		
		lsTool.execute(null, null);
		assertNotEquals(0, lsTool.getStatusCode());
	}
}