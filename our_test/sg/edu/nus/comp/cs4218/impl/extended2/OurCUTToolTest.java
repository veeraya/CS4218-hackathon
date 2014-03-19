/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;

/**
 * @author pook
 *
 */
public class OurCUTToolTest {
    
    private ICutTool cuttool; 
    private File testDir;
    private File testFile;
    
    @Before
    public void setUp() throws Exception {        
        testDir = new File("testCutFolder");
        testDir.mkdir();
        
        // create new file with text
        testFile = new File(testDir, "test.txt");
        testFile.createNewFile();
        FileWriter fw = new FileWriter(testFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder sb = new StringBuilder();
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six:seven\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        bw.write(sb.toString());
        bw.close();
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
        // garbage collector swoop in right about here
        cuttool = null;
    }
    
    @Test
    public void cutSpecfiedCharactersSingleLineTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        String expectedResult = ":bar:ba";
        assertEquals(expectedResult, cuttool.cutSpecfiedCharacters("4-10","foo:bar:baz:qux:quux"));
        expectedResult = ":two:th";
        assertEquals(expectedResult, cuttool.cutSpecfiedCharacters("4-10","one:two:three:four:five:six:seven"));
        expectedResult = "ha:beta";
        assertEquals(expectedResult, cuttool.cutSpecfiedCharacters("4-10","alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu"));
        expectedResult = " quick ";
        assertEquals(expectedResult, cuttool.cutSpecfiedCharacters("4-10","the quick brown fox jumps over the lazy dog"));
    }
    
    @Test
    public void cutSpecfiedCharactersMultipleLinesTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append(":bar:ba\n");
        sb.append(":two:th\n");
        sb.append("ha:beta\n");
        sb.append(" quick ");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecfiedCharacters("4-10", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharactersIndexTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append(":\n");
        sb.append(":\n");
        sb.append("h\n");
        sb.append(" ");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecfiedCharacters("4", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharactersStartToCharTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("foo:b\n");
        sb.append("one:t\n");
        sb.append("alpha\n");
        sb.append("the q");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecfiedCharacters("-5", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharactersCharToEndTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("bar:baz:qux:quux\n");
        sb.append("two:three:four:five:six:seven\n");
        sb.append("a:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu\n");
        sb.append("quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecfiedCharacters("5-", readFile(testFile)));
        
    }
    
    @Test
    public void cutSpecfiedCharactersManyRangesTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append(":bar:bax\n");
        sb.append(":two:thfive:s\n");
        sb.append("ha:betalta:ep\n");
        sb.append(" quick  jumps");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecfiedCharacters("4-10,20-25", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharactersOverlappingRangesTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append(":bar:baz:qux\n");
        sb.append(":two:three:f\n");
        sb.append("ha:beta:gamm\n");
        sb.append(" quick brown");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecfiedCharacters("4-10,8-15", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharDelimSingleLineTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        String expectedResult = "";
        assertEquals(expectedResult, cuttool.cutSpecifiedCharactersUseDelimiter("6-8", ":", "foo:bar:baz:qux:quux"));
        expectedResult = "six:seven";
        assertEquals(expectedResult, cuttool.cutSpecifiedCharactersUseDelimiter("6-8", ":", "one:two:three:four:five:six:seven"));
        expectedResult = "zeta:eta:teta";
        assertEquals(expectedResult, cuttool.cutSpecifiedCharactersUseDelimiter("6-8", ":", "alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu"));
        expectedResult = "the quick brown fox jumps over the lazy dog";
        assertEquals(expectedResult, cuttool.cutSpecifiedCharactersUseDelimiter("6-8", ":", "the quick brown fox jumps over the lazy dog"));
    }
    
    @Test
    public void cutSpecfiedCharDelimMultiLinesTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("six:seven\n");
        sb.append("zeta:eta:teta\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecifiedCharactersUseDelimiter("6-8", ":", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharDelimEndWithDelimTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        assertEquals("", cuttool.cutSpecifiedCharactersUseDelimiter("3", ":", "foo:bar:"));
        assertEquals("", cuttool.cutSpecifiedCharactersUseDelimiter("3", ":", "\n"));
    }
    
    @Test
    public void cutSpecfiedCharDelimNewLineTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        assertEquals("", cuttool.cutSpecifiedCharactersUseDelimiter("3", ":", "\n"));
    }
    
    @Test
    public void cutSpecfiedCharDelimIndexTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("six\n");
        sb.append("zeta\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecifiedCharactersUseDelimiter("6", ":", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharDelimNoDelimInInputTest() {
        cuttool = new CutTool(new String[]{});
        String expectedResult = "the quick brown fox jumps over the lazy dog";
        assertEquals(expectedResult, cuttool.cutSpecifiedCharactersUseDelimiter("4-10",":", "the quick brown fox jumps over the lazy dog"));
    }

    
    @Test
    public void cutSpecfiedCharDelimStartToCharTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("foo:bar:baz\n");
        sb.append("one:two:three\n");
        sb.append("alpha:beta:gamma\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecifiedCharactersUseDelimiter("-3", ":", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharDelimCharToEndTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("six:seven\n");
        sb.append("zeta:eta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecifiedCharactersUseDelimiter("6-", ":", readFile(testFile)));
        
    }
    
    @Test
    public void cutSpecfiedCharDelimManyRangesTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("foo:bar:qux:quux\n");
        sb.append("one:two:four:five\n");
        sb.append("alpha:beta:delta:epsilon:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecifiedCharactersUseDelimiter("-2,9-,4-5,8", ":", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharDelimOverlappingRangesTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecifiedCharactersUseDelimiter("-5,9-,4-6,8", ":", readFile(testFile)));
    }
    
    @Test
    public void cutSpecfiedCharDelimEmptyDelimTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        StringBuilder sb = new StringBuilder();
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six:seven\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.cutSpecifiedCharactersUseDelimiter("-5,9-,4-6,8", null, readFile(testFile)));
        assertEquals(expectedOutput, cuttool.cutSpecifiedCharactersUseDelimiter("-5,9-,4-6,8", "", readFile(testFile)));
    }
    
    @Test
    public void executeCRangeTest() throws IOException {
        cuttool = new CutTool(new String[]{"-c", "4-10,20-25", "test.txt"});
        StringBuilder sb = new StringBuilder();
        sb.append(":bar:bax\n");
        sb.append(":two:thfive:s\n");
        sb.append("ha:betalta:ep\n");
        sb.append(" quick  jumps");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.execute(testDir, null));
    }
    
    @Test
    public void executeFRangeWithDTest() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "-d", "\":\"", "test.txt"});
        assertEquals(expectedOutput, cuttool.execute(testDir, null));
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "-d", ":", "test.txt"});
        assertEquals(expectedOutput, cuttool.execute(testDir, null));
    }
    
    @Test
    public void executeFRangeWithoutDTest() throws IOException {
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "test.txt"});
        StringBuilder sb = new StringBuilder();
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six:seven\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        assertEquals(expectedOutput, cuttool.execute(testDir, null));
    }
    
    @Test
    public void executeCAndFRangeTest() throws IOException {
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "-c", "4,5,6", "test.txt"});
        assertEquals("Error: only one type of list may be specified", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
    }
    
    @Test
    public void executeNoRangeListTest() throws IOException {
        cuttool = new CutTool(new String[]{"-d", "\":\"", "test.txt"});
        assertEquals("Error: you must specify a list of bytes, characters, or fields", cuttool.execute(testDir, null));
        cuttool = new CutTool(new String[]{"test.txt"});
        assertEquals("Error: you must specify a list of bytes, characters, or fields", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
    }
    
    @Test
    public void executeCAndDTest() throws IOException {
        cuttool = new CutTool(new String[]{"-c", "4,5,6", "-d", "\":\"", "test.txt"});
        assertEquals("Error: an input delimiter may be specified only when operating on fields", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
    }

    @Test
    public void executeCDFTest() throws IOException {
        cuttool = new CutTool(new String[]{"-c", "4,5,6", "-f", "3,4", "-d", "\":\"", "test.txt"});
        assertEquals("Error: an input delimiter may be specified only when operating on fields", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
    }
    
    @Test
    public void executeInvalidRangeTest() throws IOException {
        cuttool = new CutTool(new String[]{"-c", "5-3,10", "test.txt"});
        assertEquals("Error: invalid decreasing range", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
        cuttool = new CutTool(new String[]{"-c", "abc", "test.txt"});
        assertEquals("Error: invalid list argument for -c", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
        cuttool = new CutTool(new String[]{"-c", "5--3", "test.txt"});
        assertEquals("Error: invalid list argument for -c", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
        cuttool = new CutTool(new String[]{"-f", "5-3,10", "-d", ":", "test.txt"});
        assertEquals("Error: invalid decreasing range", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
        cuttool = new CutTool(new String[]{"-f", "abc", "-d", ":", "test.txt"});
        assertEquals("Error: invalid list argument for -f", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
        cuttool = new CutTool(new String[]{"-f", "5--3", "-d", ":", "test.txt"});
        assertEquals("Error: invalid list argument for -f", cuttool.execute(testDir, null));
        assertEquals(1, cuttool.getStatusCode());
    }
    
    @Test
    public void executeNoFileOptionTest() throws IOException {
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "-d", ":"});
        assertEquals("Error: you must specify a file name", cuttool.execute(testDir, null));
    }
    
    @Test
    public void executeFileDoesNotExistTest() throws IOException {
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "-d", ":", "test1.txt"});
        assertEquals("Error: file test1.txt doesn't exist", cuttool.execute(testDir, null));
    }
    
    @Test
    public void executeManyFilesTest() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(":bar:bax\n");
        sb.append(":two:thfive:s\n");
        sb.append("ha:betalta:ep\n");
        sb.append(" quick  jumps\n");
        sb.append(":bar:bax\n");
        sb.append(":two:thfive:s\n");
        sb.append("ha:betalta:ep\n");
        sb.append(" quick  jumps");
        String expectedOutput = sb.toString();
        cuttool = new CutTool(new String[]{"-c", "4-10,20-25", "test.txt", "test.txt"});
        assertEquals(expectedOutput, cuttool.execute(testDir, null));
        
        sb = new StringBuilder();
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog\n");
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        expectedOutput = sb.toString();
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "-d", "\":\"", "test.txt", "test.txt"});
        assertEquals(expectedOutput, cuttool.execute(testDir, null));
    }
    
    @Test
    public void executeFileWithSpace() throws IOException{
        // create new file with text
        File test = new File(testDir, "test with space.txt");
        test.createNewFile();
        FileWriter fw = new FileWriter(test.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder sb = new StringBuilder();
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six:seven\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        bw.write(sb.toString());
        bw.close();
        
        sb = new StringBuilder();
        sb.append("foo:bar:baz:qux:quux\n");
        sb.append("one:two:three:four:five:six\n");
        sb.append("alpha:beta:gamma:delta:epsilon:zeta:teta:iota:kappa:lambda:mu\n");
        sb.append("the quick brown fox jumps over the lazy dog");
        String expectedOutput = sb.toString();
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "-d", "\":\"", "\"test", "with", "space.txt\""});
        assertEquals(expectedOutput, cuttool.execute(testDir, null));
    }
    
    @Test
    public void executeFileWithSpaceNotExist() throws IOException{
        cuttool = new CutTool(new String[]{"-f", "-5,9-,4-6,8", "-d", ":", "\"test", "wrong", "file", "name.txt\""});
        assertEquals("Error: file test wrong file name.txt doesn't exist", cuttool.execute(testDir, null));
    }
    
    @Test
    public void getHelpTest() {
        cuttool = new CutTool(new String[]{"-help"});
        String helpText = cuttool.getHelp();
        assertEquals(helpText, cuttool.execute(testDir, null));
        cuttool = new CutTool(new String[]{"-help","abcde"});
        assertEquals(helpText, cuttool.execute(testDir, null));
        cuttool = new CutTool(new String[]{"-c", "2,3,4", "-help", "abcde"});
        assertEquals(helpText, cuttool.execute(testDir, null));
    }
    
    
    private String readFile(File file) throws IOException {
      byte[] encoded = Files.readAllBytes(file.toPath());
      return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
    }

}
