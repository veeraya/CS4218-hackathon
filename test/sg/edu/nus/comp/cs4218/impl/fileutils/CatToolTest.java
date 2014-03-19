package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;

public class CatToolTest {
	private ICatTool catTool;
	private static final String NEW_LINE = System.lineSeparator();
	private static final String FILE = "__FILE.txt";
	private static final String FILE_CONTENT = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Earum, quo, optio,"
			+ NEW_LINE + "Earum, quo, optio, quibusdam odit tempore debitis";
	private File workingDir;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		createFile(FILE, FILE_CONTENT);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		deleteFile(FILE);
	}

	@Before
	public void setUp() throws Exception {
		workingDir = new File(System.getProperty("user.dir"));
		catTool = new CatTool();
	}

	@After
	public void tearDown() throws Exception {
		catTool = null;
		workingDir = null;
	}

	@Test
	public void getStringForFile_StatusCodeZero() {
		assertEquals(FILE_CONTENT + NEW_LINE,
				catTool.getStringForFile(new File(FILE)));
		assertEquals(0, catTool.getStatusCode());
	}

	@Test
	public void getStringForFileNotFound_StatusCodeOne() {
		File nonExistingFile = createNonExistingFile();
		assertEquals(CatTool.ERR_MSG_FILE_NOT_FOUND,
				catTool.getStringForFile(nonExistingFile));
		assertEquals(CatTool.ERR_CODE_FILE_NOT_FOUND, catTool.getStatusCode());
	}

	@Test
	public void execute_NoArguments_StatusCodeZero() {
		assertEquals(catTool.execute(workingDir, ""), "");
		assertEquals(0, catTool.getStatusCode());
	}

	@Test
	public void execute_NullArguments_StatusCodeZero() {
		assertEquals(catTool.execute(workingDir, null), "");
		assertEquals(0, catTool.getStatusCode());
	}

	@Test
	public void execute_InputFromStdin_StatusCodeZero() {
		assertEquals(catTool.execute(workingDir, " Lorem ipsum "),
				" Lorem ipsum ");
		assertEquals(0, catTool.getStatusCode());
	}

	@Test
	public void execute_MultipleFiles_StatusCodeZero() {
		String[] args = { "cat_io_files/file1", "cat_io_files/file2" };
		catTool = new CatTool(args);

		assertEquals(
				catTool.execute(workingDir, null),
				"This is content from file 1" + System.lineSeparator()
						+ "----------------------------"
						+ System.lineSeparator()
						+ "This is content from file 2 "
						+ System.lineSeparator());
		assertEquals(0, catTool.getStatusCode());
	}

	@Test
	public void execute_FileAndStdin_StatusCodeZero() {
		String[] args = { "cat_io_files/file1", "-" };
		catTool = new CatTool(args);

		assertEquals("This is content from file 1" + System.lineSeparator()
				+ "Input from stdin",
				catTool.execute(workingDir, "Input from stdin"));
		assertEquals(0, catTool.getStatusCode());
	}

	@Test
	public void execute_MultipleFilesAndStdin_StatusCodeZero() {
		String[] args = { "cat_io_files/file1", "-", "cat_io_files/file2" };
		catTool = new CatTool(args);

		assertEquals("This is content from file 1"
				+ System.lineSeparator()
				+ "Input from stdin"
				+ "----------------------------"
				+ System.lineSeparator()
				+ "This is content from file 2 "
				+ System.lineSeparator(),
				catTool.execute(workingDir, "Input from stdin"));
		assertEquals(0, catTool.getStatusCode());
	}

	// Helper methods
	private File createNonExistingFile() {
		String randomFileName = "__RANDOM_FILE";
		int count = 0;

		File nonExistingFile = new File("__RANDOM_FILE");
		while (nonExistingFile.exists()) {
			nonExistingFile = new File(randomFileName + count++);
		}
		return nonExistingFile;
	}

	protected static void createFile(String pathName, String content) {
		File file = new File(pathName);
		BufferedWriter writer = null;

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(pathName), "utf-8"));
			writer.write(content);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected static void deleteFile(String pathName) {
		File file = new File(pathName);
		if (file.exists()) {
			file.delete();
		}
	}
}
