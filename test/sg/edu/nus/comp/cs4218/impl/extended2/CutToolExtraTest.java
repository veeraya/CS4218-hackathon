package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;

public class CutToolExtraTest {
	// Static Variables
	private static ICutTool cutTool;
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
		workingDir = new File(System.getProperty("user.dir"));

		/* Creation of directories and files for testing purposes */
		tempDir = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "tempDir")
						.toPath()).toString());
		inputNoDelimiter = new File(tempDir.toString() + File.separator
				+ "inputNoDelimiter");
		inputNoDelimiter.createNewFile();

		inputNoDelimiterTwo = new File(tempDir.toString() + File.separator
				+ "inputNoDelimiterTwo");
		inputNoDelimiterTwo.createNewFile();

		inputWithDelimiter = new File(tempDir.toString() + File.separator
				+ "inputWithDelimiter");
		inputWithDelimiter.createNewFile();

		inputWithDelimiterTwo = new File(tempDir.toString() + File.separator
				+ "inputWithDelimiterTwo");
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
	}

	@After
	public void tearDown() {
		cutTool = null;
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

	// Team Rocket's Black Box Positive Test Cases
	@Test
	public void getHelp_HelpInformationReturned() throws IOException {

		final String expectedMessage = new String(Files.readAllBytes(new File(
				"help_files" + File.separator + "cut_help").toPath()));
		assertEquals(expectedMessage, cutTool.getHelp());
	}

	@Test
	public void cutSpecifiedCharactersOneCharacterOnly_OneCharacterReturned() {

		final String list = "1";
		final String expectedMessage = "0" + System.lineSeparator() + "1";
		final String returnMessage = cutTool.cutSpecfiedCharacters(list,
				inputNoDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutSpecifiedCharactersMultipleSingleCharacters_MultipleCharactersReturned() {

		final String list = "1,3,5,7,9";
		final String expectedMessage = "02468" + System.lineSeparator()
				+ "13579";
		final String returnMessage = cutTool.cutSpecfiedCharacters(list,
				inputNoDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutSpecifiedCharactersMultipleRangedSingleCharacters_MultipleCharactersReturned() {

		final String list = "1-3,7-20,3-5,6,21,19";
		final String expectedMessage = "012345678901234567890"
				+ System.lineSeparator() + "123456789 0123456789";
		final String returnMessage = cutTool.cutSpecfiedCharacters(list,
				inputNoDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutSpecifiedCharactersEndRange_MultipleCharactersReturned() {

		final String list = "-3,-6,-5";
		final String expectedMessage = "012345" + System.lineSeparator()
				+ "123456";
		final String returnMessage = cutTool.cutSpecfiedCharacters(list,
				inputNoDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutSpecifiedCharactersStartRange_MultipleCharactersReturned() {

		final String list = "3-,6-,5-";
		final String expectedMessage = "2345678901234567890"
				+ System.lineSeparator() + "3456789 0123456789";

		final String returnMessage = cutTool.cutSpecfiedCharacters(list,
				inputNoDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutSpecifiedCharactersWithDelimiterSingleFieldOpDtoOpF_SingleFieldReturned() {

		final String delim = ";";
		final String fields = "1";

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		final String returnMessage = cutTool
				.cutSpecifiedCharactersUseDelimiter(fields, delim,
						inputWithDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutSpecifiedCharactersWithDelimiterMultipleFieldOpDtoOpF_MultipleFieldsReturned() {

		final String delim = ";";
		final String fields = "1,2-3";

		final String expectedMessage = "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789 0123456789;0123456789";

		final String returnMessage = cutTool
				.cutSpecifiedCharactersUseDelimiter(fields, delim,
						inputWithDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutSpecifiedCharactersWithDelimiterMultipleFieldEndRange_MultipleFieldsReturned() {

		final String delim = ";";
		final String fields = "-1";

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		final String returnMessage = cutTool
				.cutSpecifiedCharactersUseDelimiter(fields, delim,
						inputWithDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());
	}

	@Test
	public void cutSpecifiedCharactersWithDelimiterMultipleFieldStartRange_MultipleFieldsReturned() {

		final String delim = ";";
		final String fields = "3-";

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789";

		final String returnMessage = cutTool
				.cutSpecifiedCharactersUseDelimiter(fields, delim,
						inputWithDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());
	}

	@Test
	public void cutSpecifiedCharactersWithDelimiterOpF_OriginalInputReturned() {

		final String fields = "1-3";

		final String expectedMessage = "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789 0123456789;0123456789";

		final String returnMessage = cutTool
				.cutSpecifiedCharactersUseDelimiter(fields, "",
						inputWithDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_getHelp_HelpInformationReturned() throws IOException {

		final String[] args = { "-help" };
		cutTool = new CutTool(args);

		final String expectedMessage = new String(Files.readAllBytes(new File(
				"help_files/cut_help").toPath()));
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
	}

	@Test
	public void execute_getHelpWithOtherOptions_HelpInformationReturned()
			throws IOException {

		final String[] args = { "-help", "-c", "1-10",
				inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = new String(Files.readAllBytes(new File(
				"help_files/cut_help").toPath()));
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
	}

	@Test
	public void execute_cutSpecifiedCharactersOneCharacterOnly_OneCharacterReturned() {

		final String[] args = { "-c", "1", inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0" + System.lineSeparator() + "1";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersMultipleSingleCharacters_MultipleCharactersReturned() {

		final String[] args = { "-c", "1,3,5,7,9", inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "02468" + System.lineSeparator()
				+ "13579";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersMultipleRangedCharacters_MultipleCharactersReturned() {

		final String[] args = { "-c", "1-3,3-5,5-7,7-100",
				inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "012345678901234567890"
				+ System.lineSeparator() + "123456789 0123456789";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersMultipleRangedSingleCharacters_MultipleCharactersReturned() {

		final String[] args = { "-c", "1-3,7-20,3-5,6,21,19",
				inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "012345678901234567890"
				+ System.lineSeparator() + "123456789 0123456789";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersEndRange_MultipleCharactersReturned() {

		final String[] args = { "-c", "-3,-6,-5", inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "012345" + System.lineSeparator()
				+ "123456";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersStartRange_MultipleCharactersReturned() {

		final String[] args = { "-c", "3-,6-,5-", inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "2345678901234567890"
				+ System.lineSeparator() + "3456789 0123456789";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterSingleFieldOpDtoOpF_SingleFieldReturned() {

		final String[] args = { "-d", "';'", "-f", "1",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterSingleFieldOpFtoOpD_SingleFieldReturned() {

		final String[] args = { "-f", "1", "-d", "';'",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterMultipleFieldOpDtoOpF_MultipleFieldsReturned() {

		final String[] args = { "-d", "';'", "-f", "1,2-3",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789 0123456789;0123456789";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterMultipleFieldOpFtoOpD_MultipleFieldsReturned() {

		final String[] args = { "-f", "1,2-3", "-d", "';'",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789 0123456789;0123456789";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterMultipleFieldEndRange_MultipleFieldsReturned() {

		final String[] args = { "-f", "-1", "-d", "\";\"",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterMultipleFieldStartRange_MultipleFieldsReturned() {

		final String[] args = { "-f", "3-", "-d", "';'",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterOpF_OriginalInputReturned() {

		final String[] args = { "-f", "1-3", inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789 0123456789;0123456789";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersMultipleFiles_MultipleLinesReturned() {

		final String[] args = { "-c", "7-8,1-5,10",
				inputNoDelimiter.toString(), inputNoDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "01234679" + System.lineSeparator()
				+ "1234578" + System.lineSeparator() + "1234578 "
				+ System.lineSeparator() + "01234679";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterMultipleFiles_MultipleLinesReturned() {

		final String[] args = { "-f", "1-2", "-d", "';'",
				inputWithDelimiter.toString(), inputWithDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789;0123456789"
				+ System.lineSeparator() + "0123456789;0123456789"
				+ System.lineSeparator() + "0123456789 0123456789;0123456789"
				+ System.lineSeparator() + "9876543210;9876543210"
				+ System.lineSeparator() + "9876543210;9876543210"
				+ System.lineSeparator() + "9876543210;9876543210";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithStandardInput_CharactersReturned() {

		final String[] args = { "-c", "1", "-"};
		cutTool = new CutTool(args);

		final String expectedMessage = "0" + System.lineSeparator() + "1";

		final String returnMessage = cutTool.execute(workingDir,
				inputNoDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersUseDelimiterWithStandardInput_CharactersReturned() {

		final String[] args = { "-f", "1", "-d", "';'", "-"};
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		final String returnMessage = cutTool.execute(workingDir,
				inputWithDelimiterStr);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	// Team Rocket's Black Box Negative Test Cases

	@Test
	public void execute_cutSpecifiedCharactersWithStartIndexZeroRange_ErrorMessageReturned() {

		final String[] args = { "-c", "0-1", inputNoDelimiter.toString(),
				inputNoDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithEndIndexZeroRange_ErrorMessageReturned() {

		final String[] args = { "-c", "1-0", inputNoDelimiter.toString(),
				inputNoDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithIndexZeroNoRange_ErrorMessageReturned() {

		final String[] args = { "-c", "0", inputNoDelimiter.toString(),
				inputNoDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithEndIndexZero_ErrorMessageReturned() {

		final String[] args = { "-c", "-0", inputNoDelimiter.toString(),
				inputNoDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithStartIndexZero_ErrorMessageReturned() {

		final String[] args = { "-c", "0-", inputNoDelimiter.toString(),
				inputNoDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterWithIndexZeroNoRange_ErrorMessageReturned() {

		final String[] args = { "-f", "0", "-d", "';'",
				inputWithDelimiter.toString(), inputWithDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterWithStartIndexZeroRange_ErrorMessageReturned() {

		final String[] args = { "-f", "0-1", "-d", "';'",
				inputWithDelimiter.toString(), inputWithDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterWithEndIndexZeroRange_ErrorMessageReturned() {

		final String[] args = { "-f", "1-0", "-d", "';'",
				inputWithDelimiter.toString(), inputWithDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterWithEndIndexZero_ErrorMessageReturned() {

		final String[] args = { "-f", "-0", "-d", "';'",
				inputWithDelimiter.toString(), inputWithDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterWithStartIndexZero_ErrorMessageReturned() {

		final String[] args = { "-f", "0-", "-d", "';'",
				inputWithDelimiter.toString(), inputWithDelimiterTwo.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersFileDoesNotExist_ErrorMessageReturned() {

		final String[] args = { "-c", "1-5", inputNoDelimiter.toString(),
				"fileDoesNotExists.txt" };
		cutTool = new CutTool(args);

		final String expectedMessage = "01234" + System.lineSeparator()
				+ "12345" + System.lineSeparator()
				+ "cut: fileDoesNotExists.txt: No such file or directory";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-2, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterFileDoesNotExist_ErrorMessageReturned() {

		final String[] args = { "-d", "';'", "-f", "1",
				inputWithDelimiter.toString(), "fileDoesNotExists.txt" };
		cutTool = new CutTool(args);

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789" + System.lineSeparator()
				+ "cut: fileDoesNotExists.txt: No such file or directory";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-2, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersRangeEndingWithComma_ErrorMessageReturned() {

		final String[] args = { "-c", "1,", inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterRangeEndingWithComma_ErrorMessageReturned() {

		final String[] args = { "-d", "';'", "-f", "1,",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersRangeStartingWithComma_ErrorMessageReturned() {

		final String[] args = { "-c", ",1", inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterRangeStartingWithComma_ErrorMessageReturned() {

		final String[] args = { "-d", "';'", "-f", ",1",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: fields and positions are numbered from 1"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-1, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersInsufficientArguments_ErrorMessageReturned() {

		final String[] args = { "-c" };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: option requires an argument -- 'c'"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-4, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithNoArguments_ErrorMessageReturned() {

		final String[] args = {};
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: you must specify a list of bytes, characters, or fields"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-4, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersWithOneDashOnly_ErrorMessageReturned() {

		final String[] args = { "-" };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: you must specify a list of bytes, characters, or fields"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-4, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithMoreThanOneDelimiter_ErrorMessageReturned() {

		final String[] args = { "-f", "1", "-d", "';  '",
				inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: the delimiter must be a single character"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-5, cutTool.getStatusCode());
	}

	@Test
	public void execute_cutSpecifiedCharactersWithDelimiterNoDelimiter_ErrorMessageReturned() {

		final String[] args = { "-f", "1", "-d", inputWithDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: option requires an argument -- 'd'"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";

		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-4, cutTool.getStatusCode());

	}

	@Test
	public void execute_cutSpecifiedCharactersInvalidOption_ErrorMessageReturned() {

		final String[] args = { "-g", "1", inputNoDelimiter.toString() };
		cutTool = new CutTool(args);

		final String expectedMessage = "cut: invalid option -- 'g'"
				+ System.lineSeparator()
				+ "Try 'cut -help' for more information.";
		final String returnMessage = cutTool.execute(workingDir, null);

		assertEquals(expectedMessage, returnMessage);
		assertEquals(-3, cutTool.getStatusCode());

	}

}
