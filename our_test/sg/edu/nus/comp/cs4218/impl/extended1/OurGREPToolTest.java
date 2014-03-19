package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;

public class OurGREPToolTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	private File testFile;
	private File testDir;
	private IGrepTool grepTool;

	@Before
	public void before() throws IOException {
		testDir = new File("testCutFolder");
        testDir.mkdir();
        
        // create new file with text
        testFile = new File(testDir, "test.txt");
        testFile.createNewFile();
        FileWriter fw = new FileWriter(testFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder sb = new StringBuilder();
        sb.append("beforeBeforeBeforeApp" + System.lineSeparator());
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterAfterApp" + System.lineSeparator());
		bw.write(sb.toString());
        bw.close();
	}

	@After
	public void after() {
		grepTool = null;
		
		// delete all files in directory
        File[] fileList = testDir.listFiles();
        for (int i=0; i<fileList.length; i++){
            File file = fileList[i];
            file.delete();
        }
        // delete directory
        testDir.delete();
        // garbage collector swoop in right about here
        grepTool = null;
	}

	@Test
	public void getHelpMethodTest() {
		grepTool = new GrepTool(new String[0]);
		String result = grepTool.getHelp();
		assertEquals("grep [-cov] [-A num] [-B num] [-C num] [pattern] [file ...]" + System.lineSeparator(), result);
	}
	
	@Test
	public void getCountOfMatchingLinesWithEmptyStringTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(0, grepTool.getCountOfMatchingLines("", ""));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void getCountOfMatchingLinesWithNullInputTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(0, grepTool.getCountOfMatchingLines("", null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void getCountOfMatchingLinesWithNullPatternTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(2, grepTool.getCountOfMatchingLines(null, "abc\ndef"));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getCountOfMatchingLinesTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(
				2,
				grepTool.getCountOfMatchingLines("bc",
						String.format("eb\nbc\nbc\n")));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void getCountOfPartiallyMatchingLinesTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(
				3,
				grepTool.getCountOfMatchingLines("b",
						String.format("eb\nbc\nbc\ndef\n")));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getOnlyMatchingLinesWithEmptyStringTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals("", grepTool.getOnlyMatchingLines("", ""));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getOnlyMatchingLinesTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(
				"ab" + System.lineSeparator(),
				grepTool.getOnlyMatchingLines("ab",
						"eb" + System.lineSeparator() + "ab"));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getMatchingLinesWithTrailingContextNegativeNumberTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals("",
				grepTool.getMatchingLinesWithTrailingContext(-1, "abc", ""));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getMatchingLinesWithTrailingContextTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(
				"abc" + System.lineSeparator() + "2" + System.lineSeparator(),
				grepTool.getMatchingLinesWithTrailingContext(
						1,
						"abc",
						"1" + System.lineSeparator() + "abc"
								+ System.lineSeparator() + "2"));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void getMatchingLinesWithAnotherMatchInTrailingContextTest() {
		grepTool = new GrepTool(new String[0]);
		StringBuilder sb = new StringBuilder();
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterApp" + System.lineSeparator());
        String input = sb.toString();
        
        sb = new StringBuilder();
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
        String expected = sb.toString();
        
        assertEquals(expected, grepTool.getMatchingLinesWithTrailingContext(2, "apple", input));
		assertEquals(0, grepTool.getStatusCode());
	}
	

	@Test
	public void getMatchingLinesWithLeadingContextZeroTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals("abc" + System.lineSeparator(),
				grepTool.getMatchingLinesWithLeadingContext(0, "abc", "abc"));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getMatchingLinesWithLeadingContextTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(
				"1" + System.lineSeparator() + "2" + System.lineSeparator()
						+ "abc" + System.lineSeparator(),
				grepTool.getMatchingLinesWithLeadingContext(2, "abc", "1"
						+ System.lineSeparator() + "2" + System.lineSeparator()
						+ "abc" + System.lineSeparator() + "3"));
	}
	
	@Test
	public void getMatchingLinesWithAnotherMatchInLeadingContextTest() {
		grepTool = new GrepTool(new String[0]);
		StringBuilder sb = new StringBuilder();
		sb.append("beforeBeforeBeforeApp" + System.lineSeparator());
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
        String input = sb.toString();
        
        sb = new StringBuilder();
        sb.append("beforeBeforeBeforeApp" + System.lineSeparator());
        sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
        String expected = sb.toString();
        
        assertEquals(expected, grepTool.getMatchingLinesWithLeadingContext(3, "apple", input));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getMatchingLinesWithOutputContextEmptyStringTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals("abc" + System.lineSeparator(),
				grepTool.getMatchingLinesWithOutputContext(1, "", "abc"));
		assertEquals(0, grepTool.getStatusCode());
	}

	
	/* 
	 * @Corrected	previously this test called getMatchingLinesWithLeadingContext instead of getMatchingLinesWithOutputContext
	 * 				and that contradicts the name of the test
	 */
	@Test
	public void getMatchingLinesWithOutputContextTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(
				"1" + System.lineSeparator() + "2" + System.lineSeparator()
						+ "abc" + System.lineSeparator() + "3" + System.lineSeparator(),
				grepTool.getMatchingLinesWithOutputContext(2, "abc", "1"
						+ System.lineSeparator() + "2" + System.lineSeparator()
						+ "abc" + System.lineSeparator() + "3"+ System.lineSeparator()));
	}
	
	@Test
	public void getMatchingLinesWithAnotherMatchInOutputContextTest() {
		grepTool = new GrepTool(new String[0]);
		StringBuilder sb = new StringBuilder();
		sb.append("beforeBeforeBeforeApp" + System.lineSeparator());
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterAfterApp" + System.lineSeparator());
        String input = sb.toString();
        
        sb = new StringBuilder();
        sb.append("beforeBeforeBeforeApp" + System.lineSeparator());
        sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterApp" + System.lineSeparator());
        String expected = sb.toString();
        
        assertEquals(expected, grepTool.getMatchingLinesWithOutputContext(3, "apple", input));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getMatchingLinesOnlyMatchingPartNoMatchingTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals("", grepTool.getMatchingLinesOnlyMatchingPart("d", "abc"));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getMatchingLinesOnlyMatchingPartTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals("a" + System.lineSeparator(),
				grepTool.getMatchingLinesOnlyMatchingPart("a", "abc"));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void getMatchingLinesOnlyMatchingPartMatchedMoreThanOnceTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals("aa" + System.lineSeparator(),
				grepTool.getMatchingLinesOnlyMatchingPart("a", "abcabc"));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getNonMatchingLinesAllMatchingTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals("", grepTool.getNonMatchingLines("abc", "abc"));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getNonMatchingLinesTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(
				"3" + System.lineSeparator(),
				grepTool.getNonMatchingLines("abc",
						"abc" + System.lineSeparator() + "3"));
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getHelpTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(
				grepTool.getHelp(),
				String.format("grep [-cov] [-A num] [-B num] [-C num] [pattern] [file ...]%n"));
		assertEquals(grepTool.getStatusCode(), 0);
	}
	
	/* Positive tests of execute */
	
	@Test
	public void executeGetOnlyMatchingLinesTest(){
		grepTool = new GrepTool(new String[]{"ap*le", "test.txt"});
		StringBuilder sb = new StringBuilder();
        sb.append("apple" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		assertEquals(sb.toString(), grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	
	@Test
	public void executeCountMatchingLinesTest() throws IOException{
        grepTool = new GrepTool(new String[]{"-c", "a..le", "test.txt"});
		assertEquals("2", grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetMatchingLinesOnlyMatchingPartTest() throws IOException{
        grepTool = new GrepTool(new String[]{"-o", "a..le", "test.txt"});
		assertEquals("apple" + System.lineSeparator() + "apple" + System.lineSeparator(), grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeMatchingLinesOnlyMatchingPartMatchedMoreThanOnceTest() throws IOException{
        grepTool = new GrepTool(new String[]{"-o", "Aft", "test.txt"});
		assertEquals("Aft" + System.lineSeparator() + "AftAft" + System.lineSeparator() + "AftAftAft" + System.lineSeparator(), grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeEmptyFileTest() throws IOException{
        // overwrite testFile
        FileWriter fw = new FileWriter(testFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder sb = new StringBuilder();
        sb.append("");
		bw.write(sb.toString());
        bw.close();
        
        grepTool = new GrepTool(new String[]{"-o", "a.", "test.txt"});
		assertEquals("", grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetMatchingLinesWithTrailingContextTest() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		String expected = sb.toString();
		grepTool = new GrepTool(new String[]{"-A", "2", "apples", "test.txt"});
        assertEquals(expected, grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetMatchingLinesWithAnotherMatchInTrailingContextTest() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		String expected = sb.toString();
		
		grepTool = new GrepTool(new String[]{"-A", "2", "apple", "test.txt"});
        assertEquals(expected, grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetMatchingLinesWithLeadingContextTest() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		String expected = sb.toString();
		
		grepTool = new GrepTool(new String[]{"-B", "2", "apple$", "test.txt"});
        assertEquals(expected, grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetMatchingLinesWithAnotherMatchInLeadingContextTest() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		String expected = sb.toString();
		
		grepTool = new GrepTool(new String[]{"-B", "2", "apple", "test.txt"});
        assertEquals(expected, grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetMatchingLinesWithOutputContextTest() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		String expected = sb.toString();
		
		grepTool = new GrepTool(new String[]{"-C", "2", "apple$", "test.txt"});
        assertEquals(expected, grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetMatchingLinesWithAnotherMatchInOutputContextTest() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		String expected = sb.toString();
		
		grepTool = new GrepTool(new String[]{"-C", "2", "apple", "test.txt"});
        assertEquals(expected, grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetNonMatchingLinesTest() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		String expected = sb.toString();
		
        grepTool = new GrepTool(new String[]{"-v", "[A-Z]", "test.txt"});
		assertEquals(expected, grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void executeGetNonMatchingLinesAllMatchedTest() throws IOException{
        grepTool = new GrepTool(new String[]{"-v", ".", "test.txt"});
		assertEquals("", grepTool.execute(testDir, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	/* Negative tests of execute */
	
	@Test
	public void executeNullDirectoryTest(){
		grepTool = new GrepTool(new String[]{"ap*le", "test.txt"});
		assertEquals("Error: Cannot find working directory", grepTool.execute(null, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeInvalidDirectoryTest(){
		grepTool = new GrepTool(new String[]{"ap*le", "test.txt"});
		assertEquals("Error: Cannot find working directory", grepTool.execute(testFile, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeEmptyArgumentTest(){
		String[] strArray = new String[0];
		grepTool = new GrepTool(strArray);
		assertEquals("Error: Missing parameter", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeInvalidArgFormatTest(){
		grepTool = new GrepTool(new String[]{"-A", "a", "apple", "test.txt"});
		assertEquals("Error: invalid number format for argument -A", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeRepeatedOptionTest(){
		grepTool = new GrepTool(new String[]{"-A", "2", "-A", "2", "apple", "test.txt"});
		assertEquals("Error: -A argument can only be specified once", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeInvalidFileNameTest(){
		grepTool = new GrepTool(new String[]{"-A", "2", "apple", "\"test.txt"});
		assertEquals("Error: invalid file name", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeMultipleOptionsTest(){
		grepTool = new GrepTool(new String[]{"-A", "2", "-C", "2", "apple", "test.txt"});
		assertEquals("Error: please specify only 1 option.", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeNegativeNumberATest() throws IOException{
        grepTool = new GrepTool(new String[]{"-A", "-2", "a.", "test.txt"});
		assertEquals("Error: only positive numbers are allowed.", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeNegativeNumberBTest() throws IOException{
        grepTool = new GrepTool(new String[]{"-B", "-2", "a.", "test.txt"});
		assertEquals("Error: only positive numbers are allowed.", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeNegativeNumberCTest() throws IOException{
        grepTool = new GrepTool(new String[]{"-C", "-2", "a.", "test.txt"});
		assertEquals("Error: only positive numbers are allowed.", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeFileNameMissingTest(){
		grepTool = new GrepTool(new String[]{"-A", "2", "apple"});
		assertEquals("Error: you must specify a file name", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	@Test
	public void executeFileDoesNotExistTest(){
		grepTool = new GrepTool(new String[]{"-A", "2", "apple", "test1.txt"});
		assertEquals("Error: file test1.txt doesn't exist", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	


}
