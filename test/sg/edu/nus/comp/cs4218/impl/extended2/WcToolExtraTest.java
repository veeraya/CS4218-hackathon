package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;

public class WcToolExtraTest {

	private IWcTool wcTool;
	private File file1, file2, dir;
	private String stdin, helpContent;
	
	@Before
	public void setUp() throws Exception {
		BufferedWriter bw;
		String content;
		
		// create files with content
		file1 = new File("text1");
		file1.createNewFile();
		content = "this file contains 2 newlines, 17 words and 99 characters"+System.lineSeparator();
		content += "this is text1" + System.lineSeparator();
		content += "created only for wctooltest";
		bw = new BufferedWriter(new FileWriter(file1));
		bw.write(content);
		bw.close();
		
		file2 = new File("text2");
		file2.createNewFile();
		content = "this file contains 4 newlines, 16 words and 88 characters"+System.lineSeparator();
		content += "testing testing 1 2 3"+System.lineSeparator();
		content += " file!"+System.lineSeparator()+System.lineSeparator();
		bw = new BufferedWriter(new FileWriter(file2));
		bw.write(content);
		bw.close();
		
		// create directory
		dir = new File("dir");
		dir.mkdir();
		
		// initialize string to be used as standard input
		stdin = "this is a stdin with 2 newlines , 16 words and 84 characters"+System.lineSeparator();
		stdin += "testing standard input"+System.lineSeparator();
		
		// initialize actual content in help file for wc -help option
		helpContent = new String(Files.readAllBytes(new File("help_files/wc_help").toPath())).trim();
				
	}
	
	@Before
	public void before() {
		wcTool = new WcTool(null);
	}

	@After
	public void after() {
		wcTool = null;
	}
	
	@After
	public void tearDown() throws Exception {
		file1.delete();
		file2.delete();
		dir.delete();
		stdin = null;
		helpContent = null;
	}
	
	private boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("windows"))
			return true;
		else
			return false;
	}
	
	@Test
	public void execute_StdinOnly_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), stdin);
		assertEquals(0,wcTool.getStatusCode());
		if (isWindows())
			assertEquals("\t"+"2"+"\t"+"16"+"\t"+"86", result);
		else
			assertEquals("\t"+"2"+"\t"+"16"+"\t"+"84", result);
	}
	
	/**
	 * Assumption: if only an option as argument, then we assume an implicit standard input
	 */
	@Test
	public void execute_OneOption_GetStatusCode0() {
		// only one option as the argument
		wcTool = new WcTool(new String[]{"-w"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), stdin);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t16", result);
	}
	
	@Test
	public void execute_MOptionStdin_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-m", "-"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), stdin);
		assertEquals(0, wcTool.getStatusCode());
		if (isWindows())
			assertEquals("\t86", result);
		else
			assertEquals("\t84", result);
	}
	
	@Test
	public void execute_WOptionStdin_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-w", "-"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), stdin);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t16", result);
	}
	
	@Test
	public void execute_LOptionStdin_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-l", "-"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), stdin);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t2", result);
	}
	
	@Test
	public void execute_MultipleOptionsStdin_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-w", "-l", "-"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), stdin);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t2\t16", result);
	}
	
	@Test
	public void execute_OneFile_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"text1"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		if (isWindows())
			assertEquals("\t"+"2"+"\t"+"17"+"\t"+"101 text1", result);
		else
			assertEquals("\t"+"2"+"\t"+"17"+"\t"+"99 text1", result);
	}
	
	@Test
	public void execute_MOptionFile_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-m", "text1"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		if (isWindows())
			assertEquals("\t"+"101 text1", result);
		else
			assertEquals("\t"+"99 text1", result);
	}
	
	@Test
	public void execute_WOptionFile_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-w", "text1"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t"+"17 text1", result);
	}
	
	@Test
	public void execute_LOptionFile_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-l", "text1"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t"+"2 text1", result);
	}
	
	@Test
	public void execute_MultipleOptionsFile_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-m", "-l", "text2"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), stdin);
		assertEquals(0, wcTool.getStatusCode());
		if (isWindows())
			assertEquals("\t4\t92 text2", result);
		else
			assertEquals("\t4\t88 text2", result);
	}
	
	@Test
	public void execute_MultipleFiles_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"text1", "text2"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		if (isWindows())
			assertEquals("\t2\t17\t101 text1"+System.lineSeparator()+"\t4\t16\t92 text2"+System.lineSeparator()+"\t6\t33\t193 total", result);
		else
			assertEquals("\t2\t17\t99 text1"+System.lineSeparator()+"\t4\t16\t88 text2"+System.lineSeparator()+"\t6\t33\t187 total", result);
	}
	
	@Test
	public void execute_MOptionMultipleFiles_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-m", "text1", "text2"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		if (isWindows())
			assertEquals("\t101 text1"+System.lineSeparator()+"\t92 text2"+System.lineSeparator()+"\t193 total", result);
		else
			assertEquals("\t99 text1"+System.lineSeparator()+"\t88 text2"+System.lineSeparator()+"\t187 total", result);
	}
	
	@Test
	public void execute_WOptionMultipleFiles_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-w", "text1", "text2"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t17 text1"+System.lineSeparator()+"\t16 text2"+System.lineSeparator()+"\t33 total", result);
	}
	
	@Test
	public void execute_LOptionMultipleFiles_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-l", "text1", "text2"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t2 text1"+System.lineSeparator()+"\t4 text2"+System.lineSeparator()+"\t6 total", result);
	}
	
	@Test
	public void execute_MultipleOptionsAndFiles_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-m", "-w", "text1", "text2"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		if (isWindows())
			assertEquals("\t17\t101 text1"+System.lineSeparator()+"\t16\t92 text2"+System.lineSeparator()+"\t33\t193 total", result);
		else
			assertEquals("\t17\t99 text1"+System.lineSeparator()+"\t16\t88 text2"+System.lineSeparator()+"\t33\t187 total", result);
	}
	
	@Test
	public void execute_HelpOption_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-help"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals(helpContent, result);
	}
	
	@Test
	public void execute_HelpOptionStdin_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-help", "-"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals(helpContent, result);
	}
	
	@Test
	public void execute_HelpOptionFile_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"-m", "-help", "text2"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals(helpContent, result);
	}
	
	/**
	 * Assumption: Even if the file does not exist, the status code is 0 because the 
	 * command will still execute the other files. 
	 * Hence if it was used in the middle of a piping sequence, the pipe will
	 * still continue on to the next command to execute.
	 */
	@Test
	public void execute_FileNotExist_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"file123", "text2"});
		String expectedOutput;
		if (isWindows())
			expectedOutput = "wc: file123: open: No such file or directory" + System.lineSeparator() +
							 "\t4\t16\t92 text2" + System.lineSeparator() +
							 "\t4\t16\t92 total";
		else
			expectedOutput = "wc: file123: open: No such file or directory" + System.lineSeparator() +
					 "\t4\t16\t88 text2"+ System.lineSeparator() +
					 "\t4\t16\t88 total";
		
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals(expectedOutput, result);
	}
	
	@Test
	public void execute_Directory_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"dir"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("wc: dir: read: Is a directory" + System.lineSeparator(), result);
	}
	
	@Test
	public void execute_NullArguments_GetStatusCode2() {
		wcTool = new WcTool(null);
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(WcTool.STATUS_CODE_INVALID_ARGUMENTS, wcTool.getStatusCode());
		assertEquals("wc: Invalid arguments given", result);
	}

	@Test
	public void execute_EmptyArgument_GetStatusCode2() {
		wcTool = new WcTool(new String[]{""});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(WcTool.STATUS_CODE_INVALID_ARGUMENTS, wcTool.getStatusCode());
		assertEquals("wc: Invalid arguments given", result);
	}
	
	/**
	 * Assumption: Assume that only a "-" preceding multiple valid options such as "wc -lwm file"
	 * is invalid even though it is valid in the actual unix shell. 
	 * Instead, a similar command that will be accepted is "wc -l -w -m file".
	 */
	@Test
	public void execute_DashMultipleValidOptions_GetStatusCode2() {
		wcTool = new WcTool(new String[]{"-lmw", "text1"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(WcTool.STATUS_CODE_INVALID_OPTION, wcTool.getStatusCode());
		assertEquals("wc: illegal option" + System.lineSeparator() + "usage: wc [-clmw] [file ...]", result);
	}
	
	@Test
	public void execute_InvalidOption_GetStatusCode3() {
		wcTool = new WcTool(new String[]{"-a", "text1"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(WcTool.STATUS_CODE_INVALID_OPTION, wcTool.getStatusCode());
		assertEquals("wc: illegal option" + System.lineSeparator() + "usage: wc [-clmw] [file ...]", result);
	}
	
	@Test
	public void execute_NoStdinWhenExpected_GetStatusCode4() {
		wcTool = new WcTool(new String[]{"-l"});
		String result = wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(WcTool.STATUS_CODE_STDIN_EXPECTED, wcTool.getStatusCode());
		assertEquals("wc: A standard input is expected", result);
	}

}
