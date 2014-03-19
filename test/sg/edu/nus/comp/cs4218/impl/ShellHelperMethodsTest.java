package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class ShellHelperMethodsTest {
	Shell shell;
	String sep = File.separator; // to make testing OS independent
	String[] results;
	Vector<ITool> tools;
	
	@Before
	public void setUp() throws Exception {
		shell = new Shell();
	}

	@After
	public void tearDown() throws Exception {
		shell = null;
		results = null;
		tools = null;
	}
	
	@Test
	public void getBasicCommandArgs_PwdNoArgument_ReturnNull() {
		results = shell.getBasicCommandArgs("pwd", " pwd ");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_PwdWithArgument_ReturnNull() {
		results = shell.getBasicCommandArgs("pwd", " pwd abcdef");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_CdNoArgument_Return1EmptyArgument() {
		// cd
		results = shell.getBasicCommandArgs("cd", "cd 	");
		assertEquals(1, results.length);
		assertEquals("", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_CdDirectoryWindows_ReturnDirectoryArgument() {
		// cd directory in Windows OS format
		results = shell.getBasicCommandArgs("cd", "cd \"D:\\Dropbox\\NUS\\Year 3\\Semester 2\\CS4218 - Software Testing & Debugging\\Project\\CS4218\\Shell\\tempDir\\input\"");
		assertEquals(1, results.length);
		assertEquals("D:\\Dropbox\\NUS\\Year 3\\Semester 2\\CS4218 - Software Testing & Debugging\\Project\\CS4218\\Shell\\tempDir\\input", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_CdDirectory_ReturnDirectoryArgument() {
		// cd directory
		results = shell.getBasicCommandArgs("cd", "cd \"/Users\"");
		assertEquals(1, results.length);
		assertEquals("/Users", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_CdDash_ReturnDashArgument() {
		// cd  -
		results = shell.getBasicCommandArgs("cd", "cd - ");
		assertEquals(1, results.length);
		assertEquals("-", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_CdDoubleDots_ReturnDashArgument() {
		// cd  -
		results = shell.getBasicCommandArgs("cd", "cd .. ");
		assertEquals(1, results.length);
		assertEquals("..", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_CdFile_ReturnNull() {
		// cd file (invalid)
		results = shell.getBasicCommandArgs("cd", "cd dir1"+sep+"file1.txt ");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_CdDirectoryDirectory_ReturnNull() {
		// cd directory directory (invalid)
		results = shell.getBasicCommandArgs("cd", "cd ."+sep+"dir 1		dir2");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_CdFileDirectory_ReturnNull() {
		// cd file directory (invalid)
		results = shell.getBasicCommandArgs("cd", "cd dir1"+sep+"file1.txt  \"dir2"+sep+"dir  3\"");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_CdDirectoryFile_ReturnNull() {
		// cd directory file (invalid)
		results = shell.getBasicCommandArgs("cd", "cd	\"dir 2\"  ."+sep+"dir1"+sep+"file1.txt");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_CopyFileFile_ReturnArguments() {
		// copy file1 file2
		results = shell.getBasicCommandArgs("copy", "copy 	"+sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"file.txt		dir4"+sep+"dir5"+sep+"file1.txt");
		assertEquals(2, results.length);
		assertEquals(sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"file.txt", results[0]);
		assertEquals("dir4"+sep+"dir5"+sep+"file1.txt", results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_CopyDirectoryDirectory_ReturnArguments() {
		// copy directory1 directory2
		results = shell.getBasicCommandArgs("copy", "copy 	"+sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"		dir4"+sep+"dir5"+sep);
		assertEquals(2, results.length);
		assertEquals(sep+"dir"+sep+"dir2"+sep+"dir3"+sep, results[0]);
		assertEquals("dir4"+sep+"dir5"+sep, results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_CopyFileDirectory_ReturnArguments() {
		// copy file directory
		results = shell.getBasicCommandArgs("copy", "copy 	\"~"+sep+"dir"+sep+"file 1.txt\"		\"dir4"+sep+"dir 5"+sep+"\"");
		assertEquals(2, results.length);
		assertEquals("~"+sep+"dir"+sep+"file 1.txt", results[0]);
		assertEquals("dir4"+sep+"dir 5"+sep, results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_CopyMultipleFilesDirectory_ReturnArguments() {
		// copy file1 file2 file3 directory
		results = shell.getBasicCommandArgs("copy", "copy 	file.tar.gz		dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2.txt  ~"+sep+"dir1"+sep+"dir2"+sep);
		assertEquals(4, results.length);
		assertEquals("file.tar.gz", results[0]);
		assertEquals("dir4"+sep+"dir5"+sep+"file1.txt", results[1]);
		assertEquals("."+sep+"dir4"+sep+"dir5"+sep+"file2.txt", results[2]);
		assertEquals("~"+sep+"dir1"+sep+"dir2"+sep, results[3]);
	}
	
	/**
	 * Assumption: Shell cannot distinguish a valid file name or directory name
	 * since file names can be without extensions like .txt
	 * Hence, this is left to the tool to check if it is an existing file or directory
	 */
	@Test
	public void getBasicCommandArgs_CopyDirectoryFile_ReturnArguments() {
		// copy directory file (assumptions made)
		results = shell.getBasicCommandArgs("copy", "copy \"~"+sep+"dir 123\"	file1.txt");
		assertEquals(2, results.length);
		assertEquals("~"+sep+"dir 123", results[0]);
		assertEquals("file1.txt", results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_CopyMultipleFiles_ReturnCopyTool() {
		// copy file1 file2 file3 (assumptions made)
		results = shell.getBasicCommandArgs("copy", "copy  file.tar.gz	dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2");
		assertEquals(3, results.length);
		assertEquals("file.tar.gz", results[0]);
		assertEquals("dir4"+sep+"dir5"+sep+"file1.txt", results[1]);
		assertEquals("."+sep+"dir4"+sep+"dir5"+sep+"file2", results[2]);
	}
	
	@Test
	public void getBasicCommandArgs_CopyMultipleFiles_ReturnNull() {
		// copy file1 file2 file3 (invalid)
		results = shell.getBasicCommandArgs("copy", "copy  file.tar.gz	dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2.txt");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_CopyFile_ReturnNull() {
		// copy file (invalid)
		results = shell.getBasicCommandArgs("copy", "copy dir"+sep+"file");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_CopyNoArguments_ReturnNull() {
		// copy (invalid)
		results = shell.getBasicCommandArgs("copy", "copy");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_MoveFileFile_ReturnArguments() {
		// move file1 file2
		results = shell.getBasicCommandArgs("move", "move 	"+sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"file.txt		dir4"+sep+"dir5"+sep+"file1.txt");
		assertEquals(2, results.length);
		assertEquals(sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"file.txt", results[0]);
		assertEquals("dir4"+sep+"dir5"+sep+"file1.txt", results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_MoveDirectoryDirectory_ReturnArguments() {
		// move directory1 directory2
		results = shell.getBasicCommandArgs("move", "move 	"+sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"		dir4"+sep+"dir5"+sep);
		assertEquals(2, results.length);
		assertEquals(sep+"dir"+sep+"dir2"+sep+"dir3"+sep, results[0]);
		assertEquals("dir4"+sep+"dir5"+sep, results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_MoveFileDirectory_ReturnArguments() {
		// move file directory
		results = shell.getBasicCommandArgs("move", "move 	\"~"+sep+"dir"+sep+"file 1.txt\"		\"dir4"+sep+"dir 5"+sep+"\"");
		assertEquals(2, results.length);
		assertEquals("~"+sep+"dir"+sep+"file 1.txt", results[0]);
		assertEquals("dir4"+sep+"dir 5"+sep, results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_MoveMultipleFilesDirectory_ReturnArguments() {
		// move file1 file2 file3 directory
		results = shell.getBasicCommandArgs("move", "move 	file.tar.gz		dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2.txt  ~"+sep+"dir1"+sep+"dir2"+sep);
		assertEquals(4, results.length);
		assertEquals("file.tar.gz", results[0]);
		assertEquals("dir4"+sep+"dir5"+sep+"file1.txt", results[1]);
		assertEquals("."+sep+"dir4"+sep+"dir5"+sep+"file2.txt", results[2]);
		assertEquals("~"+sep+"dir1"+sep+"dir2"+sep, results[3]);
	}
	
	/**
	 * Assumption: Shell cannot distinguish a valid file name or directory name
	 * since file names can be without extensions like .txt
	 * Hence, this is left to the tool to check if it is an existing file or directory
	 */
	@Test
	public void getBasicCommandArgs_MoveDirectoryFile_ReturnNull() {
		// move directory file (assumptions made)
		results = shell.getBasicCommandArgs("move", "move \"~"+sep+"dir 123\"	file1.txt");
		assertEquals(2, results.length);
		assertEquals("~"+sep+"dir 123", results[0]);
		assertEquals("file1.txt", results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_MoveMultipleFiles_ReturnCopyTool() {
		// move file1 file2 file3 (assumptions made)
		results = shell.getBasicCommandArgs("move", "move  file.tar.gz	dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2");
		assertEquals(3, results.length);
		assertEquals("file.tar.gz", results[0]);
		assertEquals("dir4"+sep+"dir5"+sep+"file1.txt", results[1]);
		assertEquals("."+sep+"dir4"+sep+"dir5"+sep+"file2", results[2]);
	}
	
	@Test
	public void getBasicCommandArgs_MoveMultipleFiles_ReturnNull() {
		// move file1 file2 file3 (invalid)
		results = shell.getBasicCommandArgs("move", "move  file.tar.gz	dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2.txt");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_MoveFile_ReturnNull() {
		// move file (invalid)
		results = shell.getBasicCommandArgs("move", "move dir"+sep+"file");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_MoveNoArguments_ReturnNull() {
		// move (invalid)
		results = shell.getBasicCommandArgs("move", "move");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_DeleteFile_ReturnArgument() {
		// delete file
		results = shell.getBasicCommandArgs("delete", "delete dir1d"+sep+"dir2dd"+sep+"main_1.java");
		assertEquals(1, results.length);
		assertEquals("dir1d"+sep+"dir2dd"+sep+"main_1.java", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_DeleteDirectory_ReturnArgument() {
		// delete directory
		results = shell.getBasicCommandArgs("delete", "delete abc"+sep+"def"+sep);
		assertEquals(1, results.length);
		assertEquals("abc"+sep+"def"+sep, results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_DeleteFileDirectory_ReturnArguments() {
		// delete file directory
		results = shell.getBasicCommandArgs("delete", "delete  test.java abc"+sep+"def"+sep+"");
		assertEquals(2, results.length);
		assertEquals("test.java", results[0]);
		assertEquals("abc"+sep+"def"+sep, results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_DeleteDirectoryFile_ReturnArguments() {
		// delete directory file
		results = shell.getBasicCommandArgs("delete", "delete abc"+sep+"def"+sep+" test.java");
		assertEquals(2, results.length);
		assertEquals("abc"+sep+"def"+sep, results[0]);
		assertEquals("test.java", results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_DeleteNoArgument_ReturnNull() {
		// delete (invalid)
		results = shell.getBasicCommandArgs("delete", "delete ");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_Ls_Return1EmptyArgument() {
		// ls
		results = shell.getBasicCommandArgs("ls", "ls   ");
		assertEquals(1, results.length);
		assertEquals("", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_LsFile_ReturnArgument() {
		// ls file
		results = shell.getBasicCommandArgs("ls", "ls ~"+sep+"d1"+sep+"d2"+sep+"main123.xml");
		assertEquals(1, results.length);
		assertEquals("~"+sep+"d1"+sep+"d2"+sep+"main123.xml", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_LsDirectory_ReturnArgument() {
		// ls directory
		results = shell.getBasicCommandArgs("ls", "ls ."+sep+"abc"+sep+"def"+sep);
		assertEquals(1, results.length);
		assertEquals("."+sep+"abc"+sep+"def"+sep, results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_LsDash_ReturnNull() {
		// ls - (invalid)
		results = shell.getBasicCommandArgs("ls", "ls -");
		assertNull(results);
	}
	
	@Test
	public void getBasicCommandArgs_CatFileFile_ReturnArgument() {
		// cat file1 file2
		results = shell.getBasicCommandArgs("cat", "cat  	"+sep+"dir"+sep+"dir2"+sep+sep+"f_i_l_e.py  \"dir1"+sep+"dir2"+sep+"my file.txt\"");
		assertEquals(2, results.length);
		assertEquals(""+sep+"dir"+sep+"dir2"+sep+sep+"f_i_l_e.py", results[0]);
		assertEquals("dir1"+sep+"dir2"+sep+"my file.txt", results[1]);
	}
	
	@Test
	public void getBasicCommandArgs_CatDash_ReturnArgument() {
		// cat -
		results = shell.getBasicCommandArgs("cat", "cat  -");
		assertEquals(1, results.length);
		assertEquals("-", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_CatDirectory_ReturnArgument() {
		// cat directory
		results = shell.getBasicCommandArgs("cat", "cat	 directory");
		assertEquals(1, results.length);
		assertEquals("directory", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_CatMutipleFilesDirectories_ReturnArguments() {
		// cat directory1 file1 directory2 file2
		results = shell.getBasicCommandArgs("cat", "cat	 directory1 file1.txt directory2 file2.txt");
		assertEquals(4, results.length);
		assertEquals("directory1", results[0]);
		assertEquals("file1.txt", results[1]);
		assertEquals("directory2", results[2]);
		assertEquals("file2.txt", results[3]);
	}
	
	/**
	 * Assumption: if there is no "-" or filename argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void getBasicCommandArgs_CatNoArgument_ReturnArgument() {
		results = shell.getBasicCommandArgs("cat", "cat ");
		assertEquals(1, results.length);
		assertEquals("-", results[0]);
	}
	
	
	@Test
	public void getBasicCommandArgs_CatMultipleDashes_ReturnNull() {
		// cat - file1 - file2 -
		results = shell.getBasicCommandArgs("cat", "cat  - \"file1.txt\" - file2.txt -");
		assertNull(results);
	}
		
	@Test
	public void getBasicCommandArgs_EchoString_ReturnArgument() {
		results = shell.getBasicCommandArgs("echo", "echo abc de34 [][] '''");
		assertEquals(1, results.length);
		assertEquals("abc de34 [][] '''", results[0]);
	}
	
	@Test
	public void getBasicCommandArgs_EchoNoArgument_Return1EmptyArgument() {
		results = shell.getBasicCommandArgs("echo", "echo ");
		assertEquals(1, results.length);
		assertEquals("", results[0]);
	}
	
	@Test
	public void getGrepArgs_OptionPatternFile_ReturnArguments() {
		// grep option pattern file
		results = shell.getGrepArgs("grep -A 5 \"testing 123\" test.txt");
		assertEquals(4, results.length);
		assertEquals("-A", results[0]);
		assertEquals("5", results[1]);
		assertEquals("testing 123", results[2]);
		assertEquals("test.txt", results[3]);
	}
	
	@Test
	public void getGrepArgs_OptionPatternMultipleFiles_ReturnArguments() {
		// grep option pattern file1 file2
		results = shell.getGrepArgs("grep -A 5 \"testing 123\" test1.txt test2");
		assertEquals(5, results.length);
		assertEquals("-A", results[0]);
		assertEquals("5", results[1]);
		assertEquals("testing 123", results[2]);
		assertEquals("test1.txt", results[3]);
		assertEquals("test2", results[4]);
	}
	
	@Test
	public void getGrepArgs_PatternFile_ReturnArguments() {
		// grep pattern file
		results = shell.getGrepArgs("grep	\" a?^\\\". \"	\"~"+sep+"dir 1"+sep+"dir 2"+sep+"test.txt\"");
		assertEquals(2, results.length);
		assertEquals(" a?^\\\". ", results[0]);
		assertEquals("~"+sep+"dir 1"+sep+"dir 2"+sep+"test.txt", results[1]);
	}
	
	@Test
	public void getGrepArgs_PatternMultipleFiles_ReturnArguments() {
		// grep pattern file1 file2
		results = shell.getGrepArgs("grep pattern123 file1 file2.mp3");
		assertEquals(3, results.length);
		assertEquals("pattern123", results[0]);
		assertEquals("file1", results[1]);
		assertEquals("file2.mp3", results[2]);
	}
	
	@Test
	public void getGrepArgs_OptionPatternDash_ReturnArguments() {
		// grep option pattern -
		results = shell.getGrepArgs("grep   -o    \"(*){4}\"   -");
		assertEquals(3, results.length);
		assertEquals("-o", results[0]);
		assertEquals("(*){4}", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getGrepArgs_OptionWithNumPatternFile_ReturnArguments() {
		// grep option num pattern file
		results = shell.getGrepArgs("grep -B 21 \"pattern_*\" file");
		assertEquals(4, results.length);
		assertEquals("-B", results[0]);
		assertEquals("21", results[1]);
		assertEquals("pattern_*", results[2]);
		assertEquals("file", results[3]);
	}
	
	@Test
	public void getGrepArgs_PatternDash_ReturnArguments() {
		// grep pattern -
		results = shell.getGrepArgs("grep \"pattern xyz [a-zA-Z]?\" -");
		assertEquals(2, results.length);
		assertEquals("pattern xyz [a-zA-Z]?", results[0]);
		assertEquals("-", results[1]);
	}
	
	/**
	 * Assumption: if there is no "-" or filename as last argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void getGrepArgs_Pattern_ReturnArguments() {
		// grep pattern
		results = shell.getGrepArgs("grep	\"pattern [0-9]* \\\"\"");
		assertEquals(2, results.length);
		assertEquals("pattern [0-9]* \\\"", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getGrepArgs_PatternNoQuotes_ReturnArguments() {
		// grep pattern
		results = shell.getGrepArgs("grep	pattern ");
		assertEquals(2, results.length);
		assertEquals("pattern", results[0]);
		assertEquals("-", results[1]);
	}
	
	
	@Test
	public void getGrepArgs_EmptyPattern_ReturnArguments() {
		// grep pattern in quotes
		results = shell.getGrepArgs("grep \"\"");
		assertEquals(2, results.length);
		assertEquals("", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getGrepArgs_OptionHelp_ReturnArgument() {	
		// grep -help
		results = shell.getGrepArgs("grep	-help");
		assertEquals(1, results.length);
		assertEquals("-help", results[0]);
	}
	
	/**
	 * Assumption: Pattern does not need to be in quotes. So the file name is treated as a pattern 
	 * and assumes a standard input is expected, which adds an additional "-" in its argument
	 */
	@Test
	public void getGrepArgs_File_ReturnArgument() {	
		// grep file (assumptions made)
		results = shell.getGrepArgs("grep	."+sep+"this"+sep+"is"+sep+"a"+sep+"test.txt");
		assertEquals(2, results.length);
		assertEquals("."+sep+"this"+sep+"is"+sep+"a"+sep+"test.txt", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getGrepArgs_Dash_ReturnArgument() {	
		// grep - (assumptions made)
		results = shell.getGrepArgs("grep -");
		assertEquals(2, results.length);
		assertEquals("-", results[0]);
		assertEquals("-", results[1]);
	}
	
	/**
	 * Assumption: pattern must always be declared in front of file 
	 * but options can be anywhere in the command
	 */
	@Test
	public void getGrepArgs_PatternFileOption_ReturnNull() {
		// grep pattern file option (assumptions made)
		results = shell.getGrepArgs("grep	\"pattern+\" file.txt -v");
		assertEquals(3, results.length);
		assertEquals("-v", results[0]);
		assertEquals("pattern+", results[1]);
		assertEquals("file.txt", results[2]);
	}
	
	@Test
	public void getGrepArgs_PatternOption_ReturnArguments() {
		results = shell.getGrepArgs("grep	\"pattern+\" -v");
		assertEquals(3, results.length);
		assertEquals("-v", results[0]);
		assertEquals("pattern+", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getGrepArgs_MultipleOptionsPatternFile_ReturnNull() {
		// grep option1 option2 option3 pattern file
		results = shell.getGrepArgs("grep -c -o \"testing 123\" test.txt");
		assertNull(results);
	}
	
	@Test
	public void getGrepArgs_OptionNoNumPatternDash_ReturnNull() {
		// grep option(no num) pattern - (invalid)
		results = shell.getGrepArgs("grep -C \"pattern 1 2 3\" file");
		assertNull(results);
	}
	
	@Test
	public void getGrepArgs_Option_ReturnNull() {	
		// grep option (invalid)
		results = shell.getGrepArgs("grep	-A");
		assertNull(results);
	}
	
	@Test
	public void getGrepArgs_NoArguments_ReturnNull() {	
		// grep (invalid)
		results = shell.getGrepArgs("	grep ");
		assertNull(results);
	}
	
	@Test
	public void getPipeTools_TwoCommands_ReturnTools() {
		// cd | pwd
		tools = shell.getPipeTools("cd \"D:"+sep+sep+"dir 1"+sep+"dir 1 2 3\"|pwd");
		assertEquals(2, tools.size());
		assertTrue(tools.get(0) instanceof CdTool);
		assertTrue(tools.get(1) instanceof PWDTool);
	}
	
	@Test
	public void getPipeTools_FourCommands_ReturnTools() {
		// ls | cat | echo | grep
		tools = shell.getPipeTools("ls \"dir 1"+sep+"dir2\"|cat \"directory 1\" | echo abcdefg| grep -c \"(\\\\s)*\"");
		assertEquals(4, tools.size());
		assertTrue(tools.get(0) instanceof LsTool);
		assertTrue(tools.get(1) instanceof CatTool);
		assertTrue(tools.get(2) instanceof EchoTool);
		assertTrue(tools.get(3) instanceof GrepTool);
		
	}
	
	@Test
	public void getPipeTools_CommandsWithStdin_ReturnTools() {
		// cat (with stdin) | grep (with stdin) | echo
		tools = shell.getPipeTools("cat file.txt - | grep -c \"abc\" | echo testing 1 2 3 ");
		assertEquals(3, tools.size());
		assertTrue(tools.get(0) instanceof CatTool);
		assertTrue(tools.get(1) instanceof GrepTool);
		assertTrue(tools.get(2) instanceof EchoTool);
	}
	
	@Test
	public void getPipeTools_PipeAtStartOfCommand_ReturnNull() {
		tools = shell.getPipeTools("   | copy file1.a file2.a directory | ls ");
		assertNull(tools);
	}
	
	@Test
	public void getPipeTools_PipeAtEndOfCommand_ReturnNull() {
		tools = shell.getPipeTools("copy file1.a file2.a directory | ls | ");
		assertNull(tools);
	}
	
	@Test
	public void getPipeTools_SingleCommand_ReturnNull() {
		tools = shell.getPipeTools("delete \"directory"+sep+"file 1.pl\"");
		assertNull(tools);
	}
	
	@Test
	public void getTextUtilArgs_WcNoArguments_ReturnArguments() {
		results = shell.getTextUtilArgs("wc", "wc");
		assertEquals(1, results.length);
		assertEquals("-", results[0]);
	}
	
	/**
	 * Assumption: if there is no "-" or filename as last argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void getTextUtilArgs_WcOption_ReturnArguments() {
		results = shell.getTextUtilArgs("wc", "wc -m");
		assertEquals(2, results.length);
		assertEquals("-m", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_WcMultipleOptions_ReturnArguments() {
		results = shell.getTextUtilArgs("wc", "wc -l -w");
		assertEquals(3, results.length);
		assertEquals("-l", results[0]);
		assertEquals("-w", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_WcMultipleOptionsWithStdin_ReturnArguments() {
		results = shell.getTextUtilArgs("wc", "wc -w -help -");
		assertEquals(3, results.length);
		assertEquals("-w", results[0]);
		assertEquals("-help", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_WcFile_ReturnArguments() {
		results = shell.getTextUtilArgs("wc", "wc file");
		assertEquals(1, results.length);
		assertEquals("file", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_WcMultipleFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("wc", "wc file1.txt file2");
		assertEquals(2, results.length);
		assertEquals("file1.txt", results[0]);
		assertEquals("file2", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_WcMultipleOptionsAndFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("wc", "wc -w -l file1.txt file2");
		assertEquals(4, results.length);
		assertEquals("-w", results[0]);
		assertEquals("-l", results[1]);
		assertEquals("file1.txt", results[2]);
		assertEquals("file2", results[3]);
	}
	
	@Test
	public void getTextUtilArgs_WcInvalidOption_ReturnNull() {
		results = shell.getTextUtilArgs("wc", "wc -l -s");
		assertNull(results);
	}
	
	/**
	 * Assumption: options must always be declared before files or stdin ("-")
	 */
	@Test
	public void getTextUtilArgs_WcFilesOptions_ReturnNull() {
		results = shell.getTextUtilArgs("wc", "wc file1 file2 -l -w");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_WcEmptyCommandLine_ReturnNull() {
		results = shell.getTextUtilArgs("wc", "");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_SortOptionHelp_ReturnArgument() {
		results = shell.getTextUtilArgs("sort", "sort -help");
		assertEquals(1, results.length);
		assertEquals("-help", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_SortFile_ReturnArguments() {
		results = shell.getTextUtilArgs("sort", "sort file.txt");
		assertEquals(1, results.length);
		assertEquals("file.txt", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_SortMultipleFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("sort", "sort file.txt file2");
		assertEquals(2, results.length);
		assertEquals("file.txt", results[0]);
		assertEquals("file2", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_SortOptionFile_ReturnArguments() {
		results = shell.getTextUtilArgs("sort", "sort -c file");
		assertEquals(2, results.length);
		assertEquals("-c", results[0]);
		assertEquals("file", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_SortOptionMultipleFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("sort", "sort -c file.txt file2");
		assertEquals(3, results.length);
		assertEquals("-c", results[0]);
		assertEquals("file.txt", results[1]);
		assertEquals("file2", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_SortMultipleOptionsAndFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("sort", "sort -c -help file1 file2");
		assertEquals(4, results.length);
		assertEquals("-c", results[0]);
		assertEquals("-help", results[1]);
		assertEquals("file1", results[2]);
		assertEquals("file2", results[3]);
	}
	
	@Test
	public void getTextUtilArgs_SortNoArguments_ReturnNull() {
		results = shell.getTextUtilArgs("sort", "sort");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_SortStdin_ReturnNull() {
		results = shell.getTextUtilArgs("sort", "sort -");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_SortOption_ReturnNull() {
		results = shell.getTextUtilArgs("sort", "sort -c");
		assertNull(results);
	}
	
	/**
	 * Assumption: option must always be declared before files
	 */
	@Test
	public void getTextUtilArgs_SortFileOption_ReturnNull() {
		results = shell.getTextUtilArgs("sort", "sort file.txt -c");
		assertNull(results);
	}

	@Test
	public void getTextUtilArgs_SortInvalidOptionFile_ReturnNull() {
		results = shell.getTextUtilArgs("sort", "sort -d file.txt");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_CommNoOptionFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("comm", "comm file.txt file2");
		assertEquals(2, results.length);
		assertEquals("file.txt", results[0]);
		assertEquals("file2", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_CommOptionCFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("comm", "comm -c file.txt file2.txt");
		assertEquals(3, results.length);
		assertEquals("-c", results[0]);
		assertEquals("file.txt", results[1]);
		assertEquals("file2.txt", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_CommOptionDFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("comm", "comm -d file.txt file2.txt");
		assertEquals(3, results.length);
		assertEquals("-d", results[0]);
		assertEquals("file.txt", results[1]);
		assertEquals("file2.txt", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_CommOptionHelpFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("comm", "comm -help file.txt file2.txt");
		assertEquals(3, results.length);
		assertEquals("-help", results[0]);
		assertEquals("file.txt", results[1]);
		assertEquals("file2.txt", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_CommOptionHelp_ReturnArgument() {
		results = shell.getTextUtilArgs("comm", "comm -help");
		assertEquals(1, results.length);
		assertEquals("-help", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_CommOptionDHelpFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("comm", "comm -d -help file.txt file2");
		assertEquals(4, results.length);
		assertEquals("-d", results[0]);
		assertEquals("-help", results[1]);
		assertEquals("file.txt", results[2]);
		assertEquals("file2", results[3]);
	}
	
	@Test
	public void getTextUtilArgs_CommOptionCHelpNoFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("comm", "comm -c -help");
		assertEquals(2, results.length);
		assertEquals("-c", results[0]);
		assertEquals("-help", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_CommOptionCDFiles_ReturnNull() {
		results = shell.getTextUtilArgs("comm", "comm -c -d file.txt file2");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_Comm1File_ReturnNull() {
		results = shell.getTextUtilArgs("comm", "comm file.txt");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_CommNoArguments_ReturnNull() {
		results = shell.getTextUtilArgs("comm", "comm");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_CommStdin_ReturnNull() {
		results = shell.getTextUtilArgs("comm", "comm -");
		assertNull(results);
	}
	
	/**
	 * Assumption: options must always be declared before files
	 */
	@Test
	public void getTextUtilArgs_CommFilesOption_ReturnNull() {
		results = shell.getTextUtilArgs("comm", "comm file1 file2 -c");
		assertNull(results);
	}
	
	/**
	 * Assumption: if there is no "-" or filename as last argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void getTextUtilArgs_PasteOption_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste -s");
		assertEquals(2, results.length);
		assertEquals("-s", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_PasteStdin_ReturnArgument() {
		results = shell.getTextUtilArgs("paste", "paste -");
		assertEquals(1, results.length);
		assertEquals("-", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_PasteFile_ReturnArgument() {
		results = shell.getTextUtilArgs("paste", "paste file1");
		assertEquals(1, results.length);
		assertEquals("file1", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_PasteMultipleFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste file1 file2");
		assertEquals(2, results.length);
		assertEquals("file1", results[0]);
		assertEquals("file2", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_PasteOptionHelp_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste -help");
		assertEquals(2, results.length);
		assertEquals("-help", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_PasteOptionSHelpStdin_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste -s -help -");
		assertEquals(3, results.length);
		assertEquals("-s", results[0]);
		assertEquals("-help", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_PasteOptionHelpFile_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste -help file");
		assertEquals(2, results.length);
		assertEquals("-help", results[0]);
		assertEquals("file", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_PasteOptionDNoDelim_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste -d");
		assertEquals(2, results.length);
		assertEquals("-d", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_PasteOptionDDelimStdin_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste -d -abc- -");
		assertEquals(3, results.length);
		assertEquals("-d", results[0]);
		assertEquals("-abc-", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_PasteOptionSFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste -s file file2");
		assertEquals(3, results.length);
		assertEquals("-s", results[0]);
		assertEquals("file", results[1]);
		assertEquals("file2", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_PasteOptionSDFile_ReturnArguments() {
		results = shell.getTextUtilArgs("paste", "paste -s -d ( file.py");
		assertEquals(4, results.length);
		assertEquals("-s", results[0]);
		assertEquals("-d", results[1]);
		assertEquals("(", results[2]);
		assertEquals("file.py", results[3]);
	}
	
	@Test
	public void getTextUtilArgs_PasteInvalidOption_ReturnNull() {
		results = shell.getTextUtilArgs("paste", "paste -c file");
		assertNull(results);
	}
	
	
	/**
	 * Assumption: options must always be declared before files or stdin ("-")
	 */
	@Test
	public void getTextUtilArgs_PasteFileOption_ReturnNull() {
		results = shell.getTextUtilArgs("paste", "paste file -s");
		assertNull(results);
	}
	
	/**
	 * Assumption: if there is no "-" or filename as last argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void getTextUtilArgs_UniqOption_ReturnArguments() {
		results = shell.getTextUtilArgs("uniq", "uniq -i");
		assertEquals(2, results.length);
		assertEquals("-i", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_UniqNoArguments_ReturnArgument() {
		results = shell.getTextUtilArgs("uniq", "uniq");
		assertEquals(1, results.length);
		assertEquals("-", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_UniqStdin_ReturnArgument() {
		results = shell.getTextUtilArgs("uniq", "uniq -");
		assertEquals(1, results.length);
		assertEquals("-", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_UniqFile_ReturnArgument() {
		results = shell.getTextUtilArgs("uniq", "uniq file");
		assertEquals(1, results.length);
		assertEquals("file", results[0]);
	}
	
	@Test
	public void getTextUtilArgs_UniqMultipleFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("uniq", "uniq file1 file2");
		assertEquals(2, results.length);
		assertEquals("file1", results[0]);
		assertEquals("file2", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_UniqOptionHelp_ReturnArguments() {
		results = shell.getTextUtilArgs("uniq", "uniq -help");
		assertEquals(2, results.length);
		assertEquals("-help", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_UniqOptionIHelpFile_ReturnArguments() {
		results = shell.getTextUtilArgs("uniq", "uniq -i -help file");
		assertEquals(3, results.length);
		assertEquals("-i", results[0]);
		assertEquals("-help", results[1]);
		assertEquals("file", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_UniqOptionIStdin_ReturnArguments() {
		results = shell.getTextUtilArgs("uniq", "uniq -i -");
		assertEquals(2, results.length);
		assertEquals("-i", results[0]);
		assertEquals("-", results[1]);
	}
	
	@Test
	public void getTextUtilArgs_UniqOptionFNum_ReturnArguments() {
		results = shell.getTextUtilArgs("uniq", "uniq -f 100");
		assertEquals(3, results.length);
		assertEquals("-f", results[0]);
		assertEquals("100", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_UniqOptionFNumFile_ReturnArguments() {
		results = shell.getTextUtilArgs("uniq", "uniq -f 100 file");
		assertEquals(3, results.length);
		assertEquals("-f", results[0]);
		assertEquals("100", results[1]);
		assertEquals("file", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_UniqOptionFNoNumFile_ReturnNull() {
		results = shell.getTextUtilArgs("uniq", "uniq -f file1");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_UniqOptionINum_ReturnNull() {
		results = shell.getTextUtilArgs("uniq", "uniq -i 7");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_UniqInvalidOption_ReturnNull() {
		results = shell.getTextUtilArgs("uniq", "uniq -d file");
		assertNull(results);
	}
	
	/**
	 * Assumption: options must always be declared before files or stdin ("-")
	 */
	@Test
	public void getTextUtilArgs_UniqFileOption_ReturnNull() {
		results = shell.getTextUtilArgs("uniq", "uniq file -f 2-10,19");
		assertNull(results);
	}
	
	/**
	 * Assumption: if there is no "-" or filename as last argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void getTextUtilArgs_CutOptionFList_ReturnArguments() {
		results = shell.getTextUtilArgs("cut", "cut -f 1-3,6,-10");
		assertEquals(3, results.length);
		assertEquals("-f", results[0]);
		assertEquals("1-3,6,-10", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_CutOptionCStdin_ReturnArguments() {
		results = shell.getTextUtilArgs("cut", "cut -c 8 -");
		assertEquals(3, results.length);
		assertEquals("-c", results[0]);
		assertEquals("8", results[1]);
		assertEquals("-", results[2]);
	}
	
	@Test
	public void getTextUtilArgs_CutOptionFDFile_ReturnArguments() {
		results = shell.getTextUtilArgs("cut", "cut -f -10,3 - 7,20 -d \"+  +\" file");
		assertEquals(5, results.length);
		assertEquals("-f", results[0]);
		assertEquals("-10,3 - 7,20", results[1]);
		assertEquals("-d", results[2]);
		assertEquals("\"+  +\"", results[3]);
		assertEquals("file", results[4]);
	}
	
	@Test
	public void getTextUtilArgs_CutOptionCMultipleFiles_ReturnArguments() {
		results = shell.getTextUtilArgs("cut", "cut -c 1-3, 2- file.txt file2");
		assertEquals(4, results.length);
		assertEquals("-c", results[0]);
		assertEquals("1-3, 2-", results[1]);
		assertEquals("file.txt", results[2]);
		assertEquals("file2", results[3]);
	}
	
	/**
	 * Assumption: the delimiter for -d can be any string of characters
	 * In this test case, "file" is taken as the delimiter for -d thus a standard input is expected
	 */
	@Test
	public void getTextUtilArgs_CutOptionFDWithoutDelim_ReturnArguments() {
		results = shell.getTextUtilArgs("cut", "cut -f 2 -d file");
		assertEquals(5, results.length);
		assertEquals("-f", results[0]);
		assertEquals("2", results[1]);
		assertEquals("-d", results[2]);
		assertEquals("file", results[3]);
		assertEquals("-", results[4]);
	}
	
	@Test
	public void getTextUtilArgs_CutOptionCNoList_ReturnNull() {
		results = shell.getTextUtilArgs("cut", "cut -c");
		assertNull(results);
	}
	
	/**
	 * Assumption: the cut command requires -f option if -d option is used
	 */
	@Test
	public void getTextUtilArgs_CutOptionDWithoutOptionF_ReturnNull() {
		results = shell.getTextUtilArgs("cut", "cut -d abc file");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_CutNoArguments_ReturnNull() {
		results = shell.getTextUtilArgs("cut", "cut");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_CutNoOption_ReturnNull() {
		results = shell.getTextUtilArgs("cut", "cut file");
		assertNull(results);
	}
	
	@Test
	public void getTextUtilArgs_CutInvalidOption_ReturnNull() {
		results = shell.getTextUtilArgs("cut", "cut -a file");
		assertNull(results);
	}
	
	/**
	 * Assumption: options must always be declared before files or stdin ("-")
	 */
	@Test
	public void getTextUtilArgs_CutFileOption_ReturnNull() {
		results = shell.getTextUtilArgs("cut", "cut file -c 1-10");
		assertNull(results);
	}
}
