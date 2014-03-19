/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * @author pook
 *
 */
public class ShellFunctionalIntegrationTest {
	public TemporaryFolder folder = new TemporaryFolder();
	private File testFile;
	private File testFile2;
	private File testFile3;
	private File testDir;
	private Shell shell;
	private static File workingDir;
	
	@Before
	public void setUp() throws Exception {
		shell = new Shell();
		testDir = new File("testFolder");
		workingDir = new File(testDir.getAbsolutePath());
        testDir.mkdir();
        
        // create new file with text
        testFile = new File(testDir, "test.txt");
        testFile.createNewFile();
        FileWriter fw = new FileWriter(testFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder sb = new StringBuilder();
        sb.append("beforeBeforeBeforeApp\n");
		sb.append("beforeBeforeApp\n");
		sb.append("beforeApp\n");
		sb.append("apple\n");
		sb.append("mid\n");
		sb.append("apple\n");
		sb.append("afterApp\n");
		sb.append("afterAfterApp\n");
		sb.append("afterAfterAfterApp\n");
		sb.append("afterAfterAfterAfterApp\n");
		bw.write(sb.toString());
        bw.close();
        
		testFile2 = new File(testDir, "test2.txt");
        testFile2.createNewFile();
        FileWriter fw2 = new FileWriter(testFile2.getAbsoluteFile());
        BufferedWriter bw2 = new BufferedWriter(fw2);
        StringBuilder sb2 = new StringBuilder();
		sb2.append("banana\n");
		sb2.append("bananas\n");
		bw2.write(sb2.toString());
        bw2.close();
        
        testFile3 = new File(testDir, "test3.txt");
        testFile3.createNewFile();
        FileWriter fw3 = new FileWriter(testFile3.getAbsoluteFile());
        BufferedWriter bw3 = new BufferedWriter(fw3);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("apple\n");
        sb3.append("apple\n");
		sb3.append("banana\n");
		sb3.append("bananas\n");
		bw3.write(sb3.toString());
        bw3.close();
	}

	@After
	public void tearDown() throws Exception {
		// delete all files in directory
        File[] fileList = testDir.listFiles();
        for (int i=0; i<fileList.length; i++){
            File file = fileList[i];
            file.delete();
        }
        // delete directory
        testDir.delete();
        shell = null;
	}

	/**
	 * Below are our 64 test cases which have pairwise coverage.
	 * Parameter for first tool: "CAT", "GREP", "CUT", "PASTE", "UNIQ", "WC", "ECHO", "SORT", "COMM"
	 * Parameter for second tool: "CAT", "GREP", "CUT", "PASTE", "UNIQ", "WC"
	 * Parameter for third tool: "CAT", "GREP", "CUT", "PASTE", "UNIQ", "WC"
	 */
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CAT|CAT|CAT 
	 */
	@Test
	public void catCatCatTest() {
		StringBuilder sb2 = new StringBuilder();
		sb2.append("banana\n");
		sb2.append("bananas\n");
		String expected = sb2.toString() + "\n";
		
		String commandLine = "cat test2.txt | cat - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals(expected, result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CAT|GREP|UNIQ 
	 */
	@Test
	public void catGrepUniqTest() {
		String commandLine = "cat test.txt | grep app - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("apple\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CAT|CUT|GREP 
	 */
	@Test
	public void catCutGrepTest() {
		StringBuilder sb2 = new StringBuilder();
		sb2.append("app" + System.lineSeparator());
		sb2.append("app" + System.lineSeparator());
		sb2.append("aft" + System.lineSeparator());
		sb2.append("aft" + System.lineSeparator());
		sb2.append("aft" + System.lineSeparator());
		sb2.append("aft" + System.lineSeparator());
		String expected = sb2.toString() + "\n";
		
		String commandLine = "cat test.txt | cut -c 1-3 - | grep a -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals(expected, result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CAT|PASTE|GREP 
	 */
	@Test
	public void catPasteGrepTest() {
		String commandLine = "cat test2.txt | paste test2.txt - | grep ban -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tbanana\r\nbananas\tbananas\r\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CAT|UNIQ|WC 
	 */
	@Test
	public void catUniqWcTest() {
		String commandLine = "cat test.txt | uniq - | wc apple";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("10\t10\t130\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CAT|WC|CUT 
	 */
	@Test
	public void catWcCutTest() {
		String commandLine = "cat test.txt | wc - | cut -c 1-3 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("10\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing GREP|GREP|GREP 
	 */
	@Test
	public void grepGrepGrepTest() {
		String commandLine = "grep -B 2 aft test.txt | grep aft - | grep -c aft -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("4\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing GREP|PASTE|CUT 
	 */
	@Test
	public void grepPasteCutTest() {
		String commandLine = "grep -C 2 ban test2.txt | paste test.txt - | cut -f 2 -d '\t' -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\nbananas\n\n\n\n\n\n\n\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing GREP|WC|CAT 
	 */
	@Test
	public void grepWcCatTest() {
		String commandLine = "grep -o apple test.txt | wc - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("2\t2\t14\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CUT|CUT|PASTE 
	 */
	@Test
	public void cutCutPasteTest() {
		String commandLine = "cut -c 2-8 test2.txt | cut -c 1-3 - | paste test2.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tana\nbananas\tana\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CUT|WC|UNIQ 
	 */
	@Test
	public void cutWcUniqTest() {
		String commandLine = "cut -c 1-3 test2.txt | wc - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("2\t2\t7\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing PASTE|PASTE|PASTE 
	 */
	@Test
	public void pastePastePasteTest(){
		String commandLine = "paste -s test2.txt test2.txt | paste test2.txt - | paste - test2.txt ";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tbanana\tbananas\t\tbanana\nbananas\tbanana\tbananas\t\tbananas\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing UNIQ|CUT|PASTE 
	 */
	@Test
	public void uniqCutPasteTest() {
		String commandLine = "uniq test2.txt | cut -c 2-4 - | paste test2.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tana\nbananas\tana\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing WC|CUT|CAT 
	 */
	@Test
	public void wcCutCatTest() {
		String commandLine = "wc test.txt | cut -f 2 -d '\t' - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("10\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing ECHO|PASTE|CAT 
	 */
	@Test
	public void echoPasteCatTest() {
		String commandLine = "echo hello\nworld | paste test2.txt - | cat - ";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\thello\nbananas\tworld\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing SORT|WC|GREP 
	 */
	@Test
	public void sortWcGrepTest() {
		String commandLine = "sort test.txt | wc - | grep 1. -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("10\t10\t130\t\r\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CAT|WC|PASTE 
	 */
	@Test
	public void catWcPasteTest() {
		String commandLine = "cat test2.txt | wc - | paste test2.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\t2\t2\t15\t\nbananas\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing PASTE|PASTE|UNIQ 
	 */
	@Test
	public void pastePasteUniqTest(){
		String commandLine = "paste test2.txt test2.txt | paste test2.txt - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tbanana\tbanana\nbananas\tbananas\tbananas\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing WC|CAT|PASTE 
	 */
	@Test
	public void wcCatPasteTest() {
		String commandLine = "wc test2.txt | cat - | paste test2.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\t2\t2\t15\ttest2.txt\nbananas\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing ECHO|CUT|PASTE 
	 */
	@Test
	public void echoCutPasteTest() {
		String commandLine = "echo helloworld | cut -c 2-5 - | paste test2.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tello\nbananas\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing UNIQ|GREP|GREP 
	 */
	@Test
	public void uniqGrepGrepTest() {
		String commandLine = "uniq test3.txt | grep ban - | grep -o ban -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("ban\r\nban\r\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing GREP|UNIQ|CUT 
	 */
	@Test
	public void grepUniqCutTest() {
		String commandLine = "grep app test3.txt | uniq - | cut -c 2-3 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("pp\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing COMM|WC|UNIQ 
	 */
	@Test
	public void commWcUniqTest() {
		String commandLine = "comm -d test.txt test3.txt | wc - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("14\t15\t165\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing GREP|CAT|GREP 
	 */
	@Test
	public void grepCatGrepTest() {
		String commandLine = "grep bana test3.txt | cat - | grep -A 2 ana -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\r\nbananas\r\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing PASTE|UNIQ|UNIQ 
	 */
	@Test
	public void pasteUniqUniqTest() {
		String commandLine = "paste test2.txt test2.txt | uniq - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tbanana\nbananas\tbananas\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing UNIQ|PASTE|WC 
	 */
	@Test
	public void uniqPasteWcTest() {
		String commandLine = "uniq test3.txt | paste test3.txt - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("4\t7\t48\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing COMM|UNIQ|GREP 
	 */
	@Test
	public void commUniqGrepTest() {
		String commandLine = "comm test3.txt test3.txt | uniq - | grep app -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("\t\t\t\tapple" + System.lineSeparator() + "\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing ECHO|UNIQ|PASTE 
	 */
	@Test
	public void echoUniqPasteTest() {
		String commandLine = "echo hello\nhello\nhelloworld | uniq - | paste test2.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\thello\nbananas\thelloworld\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing GREP|CUT|UNIQ 
	 */
	@Test
	public void grepCutUniqTest() {
		String commandLine = "grep ban test2.txt | cut -c 2-4 - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("ana\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CUT|PASTE|GREP 
	 */
	@Test
	public void cutPasteGrepTest() {
		String commandLine = "cut -c 2-4 test2.txt | paste test2.txt - | grep -o bana -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("bana" + System.lineSeparator() + "bana" + System.lineSeparator() + "\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing SORT|GREP|WC 
	 */
	@Test
	public void sortGrepWcTest() {
		String commandLine = "sort test.txt | grep after - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("4\t4\t70\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing PASTE|CAT|CAT 
	 */
	@Test
	public void pasteCatCatTest() {
		String commandLine = "paste test2.txt test2.txt | cat - | cat - ";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tbanana\nbananas\tbananas\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing SORT|CAT|CAT 
	 */
	@Test
	public void sortCatCatTest() {
		String commandLine = "sort test2.txt | cat - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\nbananas\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing ECHO|WC|CUT 
	 */
	@Test
	public void echoWcCutTest() {
		String commandLine = "echo hello | wc - | cut -c 1-2 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("1\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CUT|GREP|CAT 
	 */
	@Test
	public void cutGrepCatTest() {
		String commandLine = "cut -c 2-4 test2.txt | grep -o na - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("na" + System.lineSeparator() + "na" + System.lineSeparator() + "\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing COMM|GREP|WC 
	 */
	@Test
	public void commGrepWcTest() {
		String commandLine = "comm test2.txt test2.txt | grep -o banan - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("2\t2\t14\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing COMM|CAT|CAT 
	 */
	@Test
	public void commCatCatTest() {
		String commandLine = "comm test2.txt test2.txt | cat - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("\t\t\t\tbanana\n\t\t\t\tbananas\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing PASTE|CUT|CUT 
	 */
	@Test
	public void pasteCutCutTest() {
		String commandLine = "paste test2.txt test2.txt | cut -c 2-10 - | cut -c 3-4 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("an\nan\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing UNIQ|CAT|UNIQ 
	 */
	@Test
	public void uniqCatUniqTest() {
		String commandLine = "uniq test3.txt | cat - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("apple\nbanana\nbananas\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CUT|CUT|WC 
	 */
	@Test
	public void cutCutWcTest() {
		String commandLine = "cut -c 2-10 test2.txt | cut -c 2-3 - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("2\t2\t5\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing UNIQ|UNIQ|CUT 
	 */
	@Test
	public void uniqUniqCutTest() {
		String commandLine = "uniq test3.txt | uniq - | cut -c 2-3 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("pp\nan\nan\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing UNIQ|WC|CAT 
	 */
	@Test
	public void uniqWcCatTest() {
		String commandLine = "uniq test3.txt | wc - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("3\t3\t21\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing COMM|CAT|CUT 
	 */
	@Test
	public void commCatCutTest() {
		String commandLine = "comm test2.txt test2.txt | cat - | cut -c 2-3 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("\t\t\n\t\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing ECHO|GREP|WC 
	 */
	@Test
	public void echoGrepWcTest() {
		String commandLine = "echo helloworld\nhola\nhello | grep hello - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("2\t2\t19\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CUT|UNIQ|CAT 
	 */
	@Test
	public void cutUniqCatTest() {
		String commandLine = "cut -c 2-10 test.txt | uniq - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("eforeBefo\neforeApp\npple\nid\npple\nfterApp\nfterAfter\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing SORT|PASTE|CUT 
	 */
	@Test
	public void sortPasteCutTest() {
		String commandLine = "sort test2.txt | paste - test2.txt - | cut -c 1-10 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tban\nbananas\tba\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CUT|GREP|CUT 
	 */
	@Test
	public void cutGrepCutTest() {
		String commandLine = "cut -c 1-10 test.txt | grep appl - | cut -c 2-3 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("pp\npp\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing COMM|GREP|PASTE 
	 */
	@Test
	public void commGrepPasteTest() {
		String commandLine = "comm test3.txt test3.txt | grep ban - | paste - test3.txt";// | grep ban - | paste - test3.txt
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("\t\t\t\tbanana\tapple\n\t\t\t\tbananas\tapple\n\tbanana\n\tbananas\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing SORT|CUT|PASTE 
	 */
	@Test
	public void sortCutPasteTest() {
		String commandLine = "sort test2.txt | cut -c 2-4 - | paste test2.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tana\nbananas\tana\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing COMM|CUT|_ 
	 */
	@Test
	public void commCutXTest() {
		String commandLine = "comm test2.txt test2.txt | cut -c 1-4 - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("\t\t\t\t\n\t\t\t\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing COMM|PASTE|_ 
	 */
	@Test
	public void commPasteXTest() {
		String commandLine = "comm test2.txt test2.txt | paste test2.txt - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\t\t\t\t\tbanana\nbananas\t\t\t\t\tbananas\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing PASTE|WC|WC 
	 */
	@Test
	public void pasteWcWcTest() {
		String commandLine = "paste test3.txt test2.txt | wc - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("1\t3\t7\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing PASTE|GREP|GREP 
	 */
	@Test
	public void pasteGrepGrepTest() {
		String commandLine = "paste test2.txt test3.txt | grep ban - | grep -o banan -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		String expected = "banan" + System.lineSeparator() + "banan" + System.lineSeparator() + 
						  "banan" + System.lineSeparator() + "banan" + System.lineSeparator() + "\n";
		assertEquals(expected, result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing WC|WC|UNIQ 
	 */
	@Test
	public void wcWcUniqTest() {
		String commandLine = "wc test.txt | wc - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("1\t4\t18\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing WC|PASTE|CUT 
	 */
	@Test
	public void wcPasteCutTest() {
		String commandLine = "wc test.txt | paste test2.txt - | cut -c 2-5 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("anan\nanan\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing ECHO|CAT|WC 
	 */
	@Test
	public void echoCatWcTest() {
		String commandLine = "echo hello | cat - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("1\t1\t5\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing WC|UNIQ|GREP 
	 */
	@Test
	public void wcUniqGrepTest() {
		String commandLine = "wc test2.txt | uniq - | grep 1 -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("2\t2\t15\ttest2.txt" + System.lineSeparator() + "\n" , result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing SORT|UNIQ|UNIQ 
	 */
	@Test
	public void sortUniqUniqTest() {
		String commandLine = "sort test3.txt | uniq - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("apple\nbanana\nbananas\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing GREP|_|PASTE 
	 */
	@Test
	public void grepXPasteTest() {
		String commandLine = "grep ap test3.txt | cat - | paste test2.txt -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("banana\tapple\nbananas\tapple\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing WC|GREP|WC 
	 */
	@Test
	public void wcGrepWcTest() {
		String commandLine = "wc test.txt | grep 1 - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("1\t4\t20\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing ECHO|_|UNIQ 
	 */
	@Test
	public void echoXUniqTest() {
		String commandLine = "echo hello\nhello | cat - | uniq -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("hello\n\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing GREP|_|WC 
	 */
	@Test
	public void grepXWcTest() {
		String commandLine = "grep af. test.txt | cat - | wc -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("4\t4\t70\t\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing CUT|CAT|_ 
	 */
	@Test
	public void cutCatXTest() {
		String commandLine = "cut -c 2-8 test2.txt | cat - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("anana\nananas\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing ECHO|_|GREP 
	 */
	@Test
	public void echoXGrepTest() {
		String commandLine = "echo helloworld\nhola\nhello | cat - | grep he -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("helloworld" + System.lineSeparator() + "hello" + System.lineSeparator() + "\n", result);
	}
	
	/** 
	 * Below are not part of the 64 pairwise test cases. 
	 * These tests have more than 2 pipes.
	 */
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing sort, grep and cut and cat with 3 pipes
	 */
	@Test
	public void sortGrepCutTest() {
		String commandLine = "sort "+testFile.getName()+" | grep a - | cut -c 1 - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("a\na\na\na\na\na\n", result);
	}
	
	/**
	 * Chains of Interactions - Test expected behavior
	 * Executing sort, grep, cut, uniq, wc and cat with 5 pipes
	 */
	@Test
	public void sortGrepCutUniqTest() {
		String commandLine = "sort "+testFile3.getName()+" | grep a - | cut -c 1 - | uniq - | wc - | cat -";
		ITool command = shell.parse(commandLine);
		String result = command.execute(workingDir, null);
		assertEquals("2\t2\t4\t\n", result);
	}

}
