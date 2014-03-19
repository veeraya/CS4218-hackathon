package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CutToolHelperMethodsTest {
	// Non-Static Variables
	private CutTool cutTool;
	private List<String> files;

	// Static Variables
	private static File workingDir;
	private static File tempDir;
	private static File inputNoDelimiter;
	private static File inputNoDelimiterTwo;
	private static File inputWithDelimiter;
	private static File inputWithDelimiterTwo;
	private static String inputNoDelimiterStr;
	private static String inputNoDelimiterTwoStr;
	private static String inputWithDelimiterStr;
	private static String inputWithDelimiterTwoStr;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File temp = new File("tempDir");
		if (temp.exists()) {
			deleteFolder(temp);
		}
		
		workingDir = new File(System.getProperty("user.dir"));

		/* Creation of directories and files for testing purposes */
		tempDir = new File(Files.createDirectory(
				new File(workingDir.toString() + "\\tempDir").toPath())
				.toString());
		inputNoDelimiter = new File(tempDir.toString() + "/inputNoDelimiter");
		inputNoDelimiter.createNewFile();

		inputNoDelimiterTwo = new File(tempDir.toString()
				+ "/inputNoDelimiterTwo");
		inputNoDelimiterTwo.createNewFile();

		inputWithDelimiter = new File(tempDir.toString()
				+ "/inputWithDelimiter");
		inputWithDelimiter.createNewFile();

		inputWithDelimiterTwo = new File(tempDir.toString()
				+ "/inputWithDelimiterTwo");
		inputWithDelimiterTwo.createNewFile();

		/* Writing of content into files */
		inputNoDelimiterStr = "012345678901234567890" + System.lineSeparator()
				+ "123456789 0123456789";

		inputNoDelimiterTwoStr = "123456789 0123456789"
				+ System.lineSeparator() + "012345678901234567890";

		inputWithDelimiterStr = "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789 0123456789;0123456789";

		inputWithDelimiterTwoStr = "9876543210;9876543210;9876543210"
				+ System.lineSeparator() + "9876543210;9876543210;9876543210"
				+ System.lineSeparator() + "9876543210;9876543210;9876543210";

		Files.write(inputNoDelimiter.toPath(), inputNoDelimiterStr.getBytes(),
				StandardOpenOption.APPEND);
		Files.write(inputNoDelimiterTwo.toPath(),
				inputNoDelimiterTwoStr.getBytes(), StandardOpenOption.APPEND);
		Files.write(inputWithDelimiter.toPath(),
				inputWithDelimiterStr.getBytes(), StandardOpenOption.APPEND);
		Files.write(inputWithDelimiterTwo.toPath(),
				inputWithDelimiterTwoStr.getBytes(), StandardOpenOption.APPEND);

	}

	@Before
	public void setUp() {
		cutTool = new CutTool(null);
		files = new ArrayList<String>();
	}

	@After
	public void tearDown() {
		cutTool = null;
		files.clear();
		files = null;
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		/* Delete all temporary testing files */
		Files.delete(inputNoDelimiter.toPath());
		Files.delete(inputWithDelimiter.toPath());
		Files.delete(inputNoDelimiterTwo.toPath());
		Files.delete(inputWithDelimiterTwo.toPath());
		Files.delete(tempDir.toPath());

		/* Setting all file objects to null */
		workingDir = null;
		tempDir = null;
		inputNoDelimiter = null;
		inputNoDelimiterTwo = null;
		inputWithDelimiter = null;
		inputWithDelimiterTwo = null;

		/* Setting all file object Strings to null */
		inputNoDelimiterStr = null;
		inputNoDelimiterTwoStr = null;
		inputWithDelimiterStr = null;
		inputWithDelimiterTwoStr = null;
	}

	// Black Box Positive Testing

	@Test
	public void cutFromFiles_SingleFile_CharactersReturned() throws IOException {

		final String list = "1-2";
		files.add(inputNoDelimiter.toString());

		final String expectedMessage = "01" + System.lineSeparator() + "12";
		final String returnMessage = cutTool.cutFromFiles(list, files,
				workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutFromFiles_MultipleFiles_CharactersReturned()
			throws IOException {

		final String list = "1-2";
		files.add(inputNoDelimiter.toString());
		files.add(inputNoDelimiterTwo.toString());

		final String expectedMessage = "01" + System.lineSeparator() + "12"
				+ System.lineSeparator() + "12" + System.lineSeparator() + "01";

		final String returnMessage = cutTool.cutFromFiles(list, files,
				workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutFromFiles_SingleFileWithStdin_CharactersReturned()
			throws IOException {

		final String list = "1-2";
		final String expectedMessage = "01" + System.lineSeparator() + "12";
		final String returnMessage = cutTool.cutFromFiles(list, files,
				workingDir, inputNoDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutFromFilesWithDelimiter_SingleFile_CharactersReturned()
			throws IOException {

		final String fields = "1";
		final String delim = ";";
		files.add(inputWithDelimiter.toString());

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		final String returnMessage = cutTool.cutFromFilesWithDelimiter(fields,
				delim, files, workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutFromFilesWithDelimiter_MultipleFiles_CharactersReturned()
			throws IOException {

		final String fields = "1";
		final String delim = ";";
		files.add(inputWithDelimiter.toString());
		files.add(inputWithDelimiterTwo.toString());

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789" + System.lineSeparator()
				+ "9876543210" + System.lineSeparator() + "9876543210"
				+ System.lineSeparator() + "9876543210";

		final String returnMessage = cutTool.cutFromFilesWithDelimiter(fields,
				delim, files, workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutFromFilesWithDelimiter_SingleFileWithStdin_CharactersReturned()
			throws IOException {

		final String fields = "1";
		final String delim = ";";

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		final String returnMessage = cutTool.cutFromFilesWithDelimiter(fields,
				delim, files, workingDir, inputWithDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	// Black Box Negative Testing

	@Test
	public void cutFromFiles_SingleFileDoNotExist_ErrorMessageReturned()
			throws IOException {

		final String list = "1-2";
		files.add("filesDoesNotExist");

		final String expectedMessage = "cut: filesDoesNotExist: No such file or directory";
		final String returnMessage = cutTool.cutFromFiles(list, files,
				workingDir, null);

		assertEquals(expectedMessage, returnMessage); 

	}

	@Test
	public void cutFromFiles_MultipleFilesDoNotExist_ErrorMessageReturned()
			throws IOException {

		final String list = "1-2";
		files.add(inputNoDelimiter.toString());
		files.add("fileDoesNotExist");
		files.add(inputNoDelimiterTwo.toString());

		final String expectedMessage = "01" + System.lineSeparator() + "12"
				+ System.lineSeparator()
				+ "cut: fileDoesNotExist: No such file or directory"
				+ System.lineSeparator() + "12" + System.lineSeparator() + "01";

		final String returnMessage = cutTool.cutFromFiles(list, files,
				workingDir, null);

		assertEquals(expectedMessage, returnMessage);

	}

	@Test
	public void cutFromFilesWithDelimiter_SingleFileDoNotExist_ErrorMessageReturned()
			throws IOException {

		final String fields = "1";
		final String delim = ";";
		files.add("fileDoesNotExist");

		final String expectedMessage = "cut: fileDoesNotExist: No such file or directory";
		final String returnMessage = cutTool.cutFromFilesWithDelimiter(fields,
				delim, files, workingDir, null);

		assertEquals(expectedMessage, returnMessage);

	}

	@Test
	public void cutFromFilesWithDelimiter_MultipleFilesDoNotExist_ErrorMessageReturned()
			throws IOException {

		final String fields = "1";
		final String delim = ";";
		files.add(inputWithDelimiter.toString());
		files.add("fileDoesNotExist");
		files.add(inputWithDelimiterTwo.toString());

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789" + System.lineSeparator()
				+ "cut: fileDoesNotExist: No such file or directory"
				+ System.lineSeparator() + "9876543210"
				+ System.lineSeparator() + "9876543210"
				+ System.lineSeparator() + "9876543210";

		final String returnMessage = cutTool.cutFromFilesWithDelimiter(fields,
				delim, files, workingDir, null);

		assertEquals(expectedMessage, returnMessage);

	}
	
	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}
}
