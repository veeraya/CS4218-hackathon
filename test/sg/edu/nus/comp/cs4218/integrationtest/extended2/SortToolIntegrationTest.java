package sg.edu.nus.comp.cs4218.integrationtest.extended2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SortTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

public class SortToolIntegrationTest {
	private static final String USER_DIRECTORY = "user.dir";
	private static File defaultWorkingDirectory;

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		defaultWorkingDirectory = new File(System.getProperty(USER_DIRECTORY));
	}

	private void createNewTestFile(String fileName, String content)
			throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(content);
		writer.close();
		writer = null;
		System.gc();
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	private void deleteTestFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}

	@Before
	public void setUp() throws IOException {
		/*
		 * sortfile1 content aaabbb aab aab bbbascaca bba cca ccc
		 */
		createNewTestFile("sortfile1",
				"aaabbb" + System.lineSeparator() + "aab"
						+ System.lineSeparator() + "aab"
						+ System.lineSeparator() + "bbbascaca"
						+ System.lineSeparator() + "bba"
						+ System.lineSeparator() + "cca"
						+ System.lineSeparator() + "ccc");

		/*
		 * sortfile2 content aaa bbb
		 */
		createNewTestFile("sortfile2", "aaa" + System.lineSeparator() + "bbb");

		/*
		 * testfile content aaa aaabbb
		 */
		createNewTestFile("testfile", "aaa" + System.lineSeparator() + "aaabbb");
	}

	@After
	public void tearDown() {
		deleteTestFile("sortfile1");
		deleteTestFile("sortfile2");
		deleteTestFile("testfile");
	}

	@Test
	public void positive_SortThenGrep_Output() {
		String[] sortArguments = { "sortfile1", "sortfile2" };
		ITool sortTool = new SortTool(sortArguments);
		String intermediateResult = sortTool.execute(defaultWorkingDirectory,
				null);

		String[] grepArguments = { "aaa", "-" };
		ITool grepTool = new GrepTool(grepArguments);
		assertEquals(
				"aaa" + System.lineSeparator() + "aaabbb"
						+ System.lineSeparator(),
				grepTool.execute(defaultWorkingDirectory, intermediateResult));
	}
	
	@Test
	public void negative_SortErrorMessageGrep_Empty() {
		String[] sortArguments = { "sortfile1", "notExist" };
		ITool sortTool = new SortTool(sortArguments);
		String intermediateResult = sortTool.execute(defaultWorkingDirectory,
				null);

		String[] grepArguments = { "aaa", "-" };
		ITool grepTool = new GrepTool(grepArguments);
		assertEquals("",
				grepTool.execute(defaultWorkingDirectory, intermediateResult));
	}

	@Test
	public void SortThenCat_Output() {
		String[] sortArguments = { "sortfile1", "sortfile2" };
		ITool sortTool = new SortTool(sortArguments);
		String intermediateResult = sortTool.execute(defaultWorkingDirectory,
				null);

		String[] catArguments = {"-"};
		ITool catTool = new CatTool(catArguments);
		assertEquals(intermediateResult,
				catTool.execute(defaultWorkingDirectory, intermediateResult));
	}
	
	@Test
	public void SortThenEcho_Output(){
		String[] sortArguments = { "sortfile1"};
		ITool sortTool = new SortTool(sortArguments);
		String intermediateResult = sortTool.execute(defaultWorkingDirectory, null);

		String[] echoArguments = {intermediateResult};
		ITool echoTool = new EchoTool(echoArguments);
		assertEquals("aaabbb" + System.lineSeparator(),
				echoTool.execute(defaultWorkingDirectory, null));
	}
}
