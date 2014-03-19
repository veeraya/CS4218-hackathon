package sg.edu.nus.comp.cs4218.integrationtest.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

public class GrepCutToolIntegrationTest {
	// Constants
	private static final String PATTERN_STR = "12345";
	private static final String DELIM = "','";
	private static final String FIELDS = "1-5";
	private static final String OPTION_D = "-d";
	private static final String OPTION_F = "-f";

	// Static Variables
	private static File workingDir;
	private static File tempDir;
	private static File input;
	private static String inputStr;

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		File file = new File("tempDir");
		if (file.exists()) {
			if (file.isDirectory()) {
				deleteFolder(file);
			} else {
				file.delete();
			}
		}

		workingDir = new File(System.getProperty("user.dir"));

		/* Creation of directories and files for testing purposes */
		tempDir = new File(Files.createDirectory(
				new File(workingDir.toString() + "\\tempDir").toPath())
				.toString());

		input = new File(tempDir.toString() + "/input");
		input.createNewFile();

		/* Writing of content into files */
		inputStr = "2646092288,4311234592,9690455268,2529595359,5428495892,1284064174,"
				+ System.lineSeparator()
				+ "2396530712,1928897215,6617707331,9252085938,8288890579"
				+ System.lineSeparator()
				+ "354273143,8622722423,7875251808,1275094909,4342409207,7724706832,"
				+ System.lineSeparator()
				+ "3840505297,6167492113,812345852,7917456693,9685436941"
				+ System.lineSeparator()
				+ "6072698664,3129084872,1885554904,4766850783,6781773406,7400763971,4402990716,1076157683,13714252,3264203801,3931545764"
				+ System.lineSeparator()
				+ "9998098015,8379065321,5809855665,9431101122,5703518445,7761383473,4065081664,"
				+ System.lineSeparator()
				+ "7421754539,9628953405,6199633573,3525398541"
				+ System.lineSeparator()
				+ "4137833917,3245739227,4161696366,9526775555,4147765123,6474557823,4312645125,1664679466,9384882899,9182060858,7917820634"
				+ System.lineSeparator()
				+ "1456650293,7674132533,4487276538,4105352452,6032135581,702497468,9630068581,7605132856,9889164183,2752505206,2109406576"
				+ System.lineSeparator()
				+ "7799396634,7112951700,8832315456,5674123459,3652797141,5246522001,2501089074,4716429507,8161287444,"
				+ System.lineSeparator()
				+ "4081530852,8628286170,"
				+ System.lineSeparator()
				+ "5793419374,7644655677,44121234517,3766357937,56175406,7490576563,1328960661,8867706901,6652920543,7341174000,6802973399"
				+ System.lineSeparator()
				+ "3318463660,4634073461,8552676395,1630444444,7535170784,4931913464,5401932805,9318021915,7823892344,5154018347,1550721589"
				+ System.lineSeparator()
				+ "5438032620,9063391621,2126743467,2431269592,6358404029,678493960,2626673380,1106413442,2944742588,3865548624,2500001731"
				+ System.lineSeparator()
				+ "1397645548,3294602769,5851234594,1803558677,5609275305,2619218872,7165947858,8421436585,9949704287,5047946138,2352184155"
				+ System.lineSeparator()
				+ "2027002862,951100469,6591163134,7214348766,"
				+ System.lineSeparator()
				+ "8729041505,340031095,6527274301,6536091455,7171411504,1406330173,2847226666"
				+ System.lineSeparator()
				+ "658250281,3274663482,3471902506,1294209658,2233879820,9625846095,3562706327,8453130552,6727123450,6693155860,5855527439"
				+ System.lineSeparator()
				+ "553011716,3048967804,2921711580,1085529069,2366102244,9117948676,7346184597,5804511837,171995590,2237115688,9430627918";

		Files.write(input.toPath(), inputStr.getBytes(),
				StandardOpenOption.APPEND);

	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		/* Delete all temporary testing files */
		Files.delete(input.toPath());
		Files.delete(tempDir.toPath());

		/* Setting all file objects to null */
		workingDir = null;
		tempDir = null;
		input = null;

		/* Setting all file object Strings to null */
		inputStr = null;
	}

	@Test
	public void positive_GrepThenCut_Output() {

		final String[] grepArgs = { PATTERN_STR, input.toString() };
		ITool grepTool = new GrepTool(grepArgs);

		String grepOutput = grepTool.execute(workingDir, null);

		final String[] cutArgs = { OPTION_D, DELIM, OPTION_F, FIELDS };
		ITool cutTool = new CutTool(cutArgs);

		final String cutOutput = cutTool.execute(workingDir, grepOutput);

		final String expectedMessage = "2646092288,4311234592,9690455268,2529595359,5428495892"
				+ System.lineSeparator()
				+ "3840505297,6167492113,812345852,7917456693,9685436941"
				+ System.lineSeparator()
				+ "7799396634,7112951700,8832315456,5674123459,3652797141"
				+ System.lineSeparator()
				+ "5793419374,7644655677,44121234517,3766357937,56175406"
				+ System.lineSeparator()
				+ "1397645548,3294602769,5851234594,1803558677,5609275305"
				+ System.lineSeparator()
				+ "658250281,3274663482,3471902506,1294209658,2233879820";

		assertEquals(expectedMessage, cutOutput);

	}

	@Test
	public void positive_CutThenCat_Output() {

		final String[] cutArgs = { OPTION_D, DELIM, OPTION_F, FIELDS,
				input.toString() };
		ITool cutTool = new CutTool(cutArgs);

		final String cutOutput = cutTool.execute(workingDir, null);

		final String[] catArgs = { "-" };
		ITool catTool = new CatTool(catArgs);

		final String catOutput = catTool.execute(workingDir, cutOutput);

		final String expectedMessage = "2646092288,4311234592,9690455268,2529595359,5428495892"
				+ System.lineSeparator()
				+ "2396530712,1928897215,6617707331,9252085938,8288890579"
				+ System.lineSeparator()
				+ "354273143,8622722423,7875251808,1275094909,4342409207"
				+ System.lineSeparator()
				+ "3840505297,6167492113,812345852,7917456693,9685436941"
				+ System.lineSeparator()
				+ "6072698664,3129084872,1885554904,4766850783,6781773406"
				+ System.lineSeparator()
				+ "9998098015,8379065321,5809855665,9431101122,5703518445"
				+ System.lineSeparator()
				+ "7421754539,9628953405,6199633573,3525398541"
				+ System.lineSeparator()
				+ "4137833917,3245739227,4161696366,9526775555,4147765123"
				+ System.lineSeparator()
				+ "1456650293,7674132533,4487276538,4105352452,6032135581"
				+ System.lineSeparator()
				+ "7799396634,7112951700,8832315456,5674123459,3652797141"
				+ System.lineSeparator()
				+ "4081530852,8628286170"
				+ System.lineSeparator()
				+ "5793419374,7644655677,44121234517,3766357937,56175406"
				+ System.lineSeparator()
				+ "3318463660,4634073461,8552676395,1630444444,7535170784"
				+ System.lineSeparator()
				+ "5438032620,9063391621,2126743467,2431269592,6358404029"
				+ System.lineSeparator()
				+ "1397645548,3294602769,5851234594,1803558677,5609275305"
				+ System.lineSeparator()
				+ "2027002862,951100469,6591163134,7214348766"
				+ System.lineSeparator()
				+ "8729041505,340031095,6527274301,6536091455,7171411504"
				+ System.lineSeparator()
				+ "658250281,3274663482,3471902506,1294209658,2233879820"
				+ System.lineSeparator()
				+ "553011716,3048967804,2921711580,1085529069,2366102244";

		assertEquals(expectedMessage, catOutput);

	}

	@Test
	public void positive_CutThenEcho_Output() {

		final String[] cutArgs = { OPTION_D, DELIM, OPTION_F, FIELDS,
				input.toString() };
		ITool cutTool = new CutTool(cutArgs);

		final String cutOutput = cutTool.execute(workingDir, null);

		final String[] echoArgs = { cutOutput };
		ITool echoTool = new EchoTool(echoArgs);

		final String echoOutput = echoTool.execute(workingDir, null);

		final String expectedMessage = "2646092288,4311234592,9690455268,2529595359,5428495892"
				+ System.lineSeparator();

		assertEquals(expectedMessage, echoOutput);

	}

	@Test
	public void negative_GrepThenCut_ErrorMessageReturned() {

		final String[] grepArgs = { PATTERN_STR, "fileNotExist" };
		ITool grepTool = new GrepTool(grepArgs);

		final String grepOutput = grepTool.execute(workingDir, null);

		final String[] cutArgs = { OPTION_D, DELIM, OPTION_F, FIELDS };
		ITool cutTool = new CutTool(cutArgs);

		final String cutOutput = cutTool.execute(workingDir, grepOutput);

		final String expectedMessage = "Error: Reading or writing problem. Please retry and check that file path is correct";

		assertEquals(expectedMessage, cutOutput);

	}

	@Test
	public void negative_CutThenCat_ErrorMessageReturned() {

		final String[] cutArgs = { OPTION_D, DELIM, OPTION_F, FIELDS,
				"fileNotExist" };
		ITool cutTool = new CutTool(cutArgs);

		final String cutOutput = cutTool.execute(workingDir, null);

		final String[] catArgs = { "-" };
		ITool catTool = new CatTool(catArgs);

		final String catOutput = catTool.execute(workingDir, cutOutput);

		final String expectedMessage = "cut: fileNotExist: No such file or directory";

		assertEquals(expectedMessage, catOutput);

	}

	@Test
	public void negative_CutThenEcho_ErrorMessageReturned() {

		final String[] cutArgs = { OPTION_D, DELIM, OPTION_F, FIELDS,
				"fileNotExist" };
		ITool cutTool = new CutTool(cutArgs);

		final String cutOutput = cutTool.execute(workingDir, null);

		final String[] echoArgs = { cutOutput };
		ITool echoTool = new EchoTool(echoArgs);

		final String echoOutput = echoTool.execute(workingDir, null);

		final String expectedMessage = "cut: fileNotExist: No such file or directory"
				+ System.lineSeparator();

		assertEquals(expectedMessage, echoOutput);

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
