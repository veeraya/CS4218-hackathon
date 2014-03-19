package sg.edu.nus.comp.cs4218.integrationtest;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;

public class InteractionTest {

	private File file1, file2, emptyFile, dir;
	private String userDir;
	
	@Before
	public void setUp() throws Exception {
		
		// create files with content
		createFile1();
		createFile2();
		createEmptyFile();
		
		// create directory
		dir = new File("dir");
		dir.mkdir();
		
		userDir = System.getProperty("user.dir");
	}
	
	private void createFile1() throws Exception {
		BufferedWriter bw;
		String content;
		
		file1 = new File("file1");
		file1.createNewFile();
	
		content = "this is file1 used for testing"+System.lineSeparator();
		content += "there are 5 lines in this file" + System.lineSeparator();
		content += "this is the 3rd line" + System.lineSeparator();
		content += "this is the 4th line" + System.lineSeparator();
		content += "this is the 5th line";
		
		bw = new BufferedWriter(new FileWriter(file1));
		bw.write(content);
		bw.close();
	}
	
	private void createFile2() throws Exception {
		BufferedWriter bw;
		String content;
		
		file2 = new File("file2");
		file2.createNewFile();
		content = "this is file2 used for testing"+System.lineSeparator();
		content += "testing testing 1 2 3"+System.lineSeparator();
		content += "	???	"+System.lineSeparator()+System.lineSeparator();
		
		bw = new BufferedWriter(new FileWriter(file2));
		bw.write(content);
		bw.close();
	}
	
	private void createEmptyFile() throws Exception {
		emptyFile = new File("emptyFile");
		emptyFile.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		file1.delete();
		file2.delete();
		emptyFile.delete();
		dir.delete();
	}
	
	@After
	public void after() {
		System.setProperty("user.dir",userDir);
	}
	
	/**
	 * Chains of interactions where piping of commands that gives an output and/or takes in standard input
	 * 
	 * grep, cut, paste, uniq, wc, cat are tested with its output pipe to a command that requires a
	 * standard input as well as taking in a standard input of another command's output.
	 * 
	 * pwd, echo, ls, sort, comm are tested with its output pipe to a command that requires a 
	 * standard input.
	 */
	
	@Test
	public void integrateWithPipe_GrepFilesUniqWcStdin_GetStatusCode0() {
		// grep files
		// uniq, wc with standard input
		
		String commandline = "grep -A 5 test file1 file2 | "
						   + "uniq - | "
						   + "wc -l ";
		
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_CutFilesGrepPasteWcStdin_GetStatusCode0() {
		// cut files
		// grep, paste, wc with standard input
		
		String commandline = "cut -c 1-10,15-20 file1 file2 | "
						   + "grep -A 5 test - | "
						   + "paste -d \t | "
						   + "wc -m -";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_PasteFilesCutGrepStdin_GetStatusCode0() {
		// paste files
		// cut, grep with standard input
		
		String commandline = "paste -s file1 file2 |"
						   + "cut -c 1-10,15-20 -|"
						   + "grep -C 3 test ";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_UniqFilesCatWcStdin_GetStatusCode0() {
		// uniq files
		// wc, cat with standard input
		
		String commandline = "uniq -f 1 file1 file2 | wc -m - | cat -";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_WcFilesGrepCatStdin_GetStatusCode0() {
		// wc files
		// grep, cat with standard input
		
		String commandline = "wc emptyFile file2 | "
						   + "grep file1 - |"
						   + "cat";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_CatFileUniqGrepWcStdin_GetStatusCode0() {
		// cat file
		// uniq, grep, wc standard input
		
		String commandline = "cat file1 | uniq -i | grep \"a\" - | wc -l -";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_PwdOutputCatCutPasteStdin_GetStatusCode0() {
		// pwd output
		// cat, cut, paste standard input
		
		String commandline = "pwd| cat | cut -c 1,2,3,4,10-13 - | paste -d =+!";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_EchoOutputCutWcUniqStdin_GetStatusCode0() {
		// echo output
		// cut, wc, uniq standard input
		
		String commandline = "echo \"testing 1 2 3\" | cut -c 1-6,10 | wc -m | uniq -f 3 ";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_LsOutputCatCutStdin_GetStatusCode0() {
		// ls output
		// cat, cut standard input
		
		String commandline = "ls file1 | cat - | cut -c 3-5";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_SortFileUniqCutStdin_GetStatusCode0() {
		// sort file
		// uniq, cut standard input
		
		String commandline = "sort file1 file2 | uniq -f 1 | cut -d \"*\" -f 9,2-5,4";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_CommFilesCatPasteStdin_GetStatusCode0() {
		// comm files
		// cat, paste standard input
		
		String commandline = "comm -c file1 file2 | cat - | paste -s ";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(0, Integer.parseInt(results.elementAt(1)));
	}
	
	/**
	 * Negative scenarios where one utility returns an error
	 * The error does not include the error when parsing a command line by the shell
	 * (such as invalid option format for a particular command type).
	 * Instead, the error is usually whereby a file does not exist 
	 * (i.e. it is a valid command but there is no such file that can be read from)
	 */
	@Test
	public void integrateWithPipe_GrepError_GetStatusCode1() {
		// grep file does not exist
		String commandline = "grep -A 3 abc file | cat - | paste -s ";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_CutError_GetStatusCode1() {
		// cut file does not exist
		String commandline = "cat emptyFile | cut -c 15-20 file | paste -s ";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_PasteError_GetStatusCode1() {
		// paste file does not exist
		String commandline = "cut -c 1-10,15-20 file1 file2 | "
				   + "grep -A 5 test - | "
				   + "paste file | "
				   + "wc -m -";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_UniqError_GetStatusCode1() {
		// uniq file does not exist
		String commandline = "cat file1 | uniq -f 12 file | grep \"a\" - | wc -l -";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_WcError_GetStatusCode1() {
		// wc standard input expected
		String commandline = "wc -l | cat file1 | uniq -i | grep \"a\" -";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));

	}
	
	@Test
	public void integrateWithPipe_CatError_GetStatusCode1() {
		// cat file does not exist
		String commandline = "wc emptyFile file2 | "
				   + "grep file1 - |"
				   + "cat file";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));
	}
	
	@Test
	public void integrateWithPipe_LsError_GetStatusCode1() {
		// ls invalid directory
		String commandline = "ls invalidDir | cat - | cut -c 3-5";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));

	}
	
	@Test
	public void integrateWithPipe_SortError_GetStatusCode1() {
		// sort file does not exist
		file2.setReadable(false);
		String commandline = "sort file1 file | uniq -f 1 | cut -d + -f 9,2-5,4";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));

		file2.setReadable(true);
	}
	
	@Test
	public void integrateWithPipe_CommError_GetStatusCode1() {
		// comm file does not exist
		String commandline = "comm -c file file2 | cat - | paste -s ";
	
		Vector<String> results = Shell.shellTestExecution(commandline);
		assertEquals(1, Integer.parseInt(results.elementAt(1)));
	}
}
