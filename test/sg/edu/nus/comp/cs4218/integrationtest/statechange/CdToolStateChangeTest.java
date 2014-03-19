package sg.edu.nus.comp.cs4218.integrationtest.statechange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;

public class CdToolStateChangeTest {
	private static final String USER_DIRECTORY = "user.dir";
	private static File defaultWorkingDirectory = new File(
			System.getProperty(USER_DIRECTORY));

	@Before
	public void setUp() throws IOException {
	}

	@After
	public void tearDown() throws IOException {
		System.setProperty(USER_DIRECTORY,defaultWorkingDirectory.getAbsolutePath());
		try {
			delete(new File("complexTest2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void lsCdls_resultDiffer(){
		String command = "ls";
		Vector<String> controlResult = Shell.shellTestExecution(command);

		command = "ls|cd src|ls";
		Vector<String> testResult = Shell.shellTestExecution(command); 
		System.out.println("\t\t" + testResult.toString());
		assertNotEquals(controlResult.get(0), testResult.get(0));
	}

	@Test
	public void lsMovecd_errorMessage(){
		File theDir = new File("complexTest");

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			theDir.mkdir();  
		}

		String command = "move complexTest complexTest2| cd complexTest";
		Vector<String> testResult = Shell.shellTestExecution(command);
		assertEquals("cd: complexTest: No such file or directory", testResult.get(0));
		
		try {
			delete(new File("complexTest2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
				System.out.println("Directory is deleted : "
						+ file.getAbsolutePath());
			} else {
				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					System.out.println("Directory is deleted : "
							+ file.getAbsolutePath());
				}
			}
		} else {
			// if file, then delete it
			file.delete();
			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}
}