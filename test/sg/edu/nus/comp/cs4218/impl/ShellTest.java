package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SortTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class ShellTest {
	Shell shell;
	String sep = File.separator; // to make testing OS independent
	ITool tool;
	
	@Before
	public void setUp() throws Exception {
		shell = new Shell();
	}

	@After
	public void tearDown() throws Exception {
		shell = null;
		tool = null;
	}

	@Test
	public void parse_PwdNoArgument_ReturnPWDTool() {
		tool = shell.parse(" pwd ");
		assertTrue(tool instanceof PWDTool);
	}
	
	@Test
	public void parse_PwdWithArguments_ReturnPWDTool() {
		// arguments will be ignored
		tool = shell.parse(" pwd asfjsjif ajsfijse");
		assertTrue(tool instanceof PWDTool);
	}
	
	@Test
	public void parse_CdNoArgument_ReturnCdTool() {
		// cd
		tool = shell.parse("cd 	");
		assertTrue(tool instanceof CdTool);
	}
	
	@Test
	public void parse_CdDirectory_ReturnCdTool() {
		// cd directory
		// Windows OS directory
		tool = shell.parse("cd   C:"+sep+"dir"+sep+"dir2"+sep+"dir3");
		assertTrue(tool instanceof CdTool);
		
	}
	
	@Test
	public void parse_CdDash_ReturnCdTool() {
		// cd  -
		tool = shell.parse("cd - ");
		assertTrue(tool instanceof CdTool);
	}
	
	@Test
	public void parse_CdDoubleDots_ReturnCdTool() {
		// cd  ..
		tool = shell.parse("cd .. ");
		assertTrue(tool instanceof CdTool);
	}
	
	@Test
	public void parse_CdFile_ReturnNull() {
		// cd file (invalid)
		tool = shell.parse("cd dir1"+sep+"file1.txt ");
		assertNull(tool);
		
	}
	
	@Test
	public void parse_CdDirectoryDirectory_ReturnNull() {
		// cd directory directory (invalid)
		tool = shell.parse("cd ."+sep+"dir 1		dir2");
		assertNull(tool);
	}
	
	@Test
	public void parse_CdFileDirectory_ReturnNull() {
		// cd file directory (invalid)
		tool = shell.parse("cd dir1"+sep+"file1.txt  \"dir2"+sep+"dir  3\"");
		assertNull(tool);
	}
	
	@Test
	public void parse_CdDirectoryFile_ReturnNull() {
		// cd directory file (invalid)
		tool = shell.parse("cd	\"dir 2\"  ."+sep+"dir1"+sep+"file1.txt");
		assertNull(tool);
	}
	
	@Test
	public void parse_LsNoArgument_ReturnLsTool() {
		// ls
		tool = shell.parse("ls");
		assertTrue(tool instanceof LsTool);
	}
	
	@Test
	public void parse_LsFile_ReturnLsTool() {
		// ls file
		tool = shell.parse("ls ~"+sep+"d1"+sep+"d2"+sep+"main123.xml");
		assertTrue(tool instanceof LsTool);
	}
	
	@Test
	public void parse_LsDirectory_ReturnLsTool() {
		// ls directory
		tool = shell.parse("ls ."+sep+"abc"+sep+"def"+sep);
		assertTrue(tool instanceof LsTool);
	}
	
	@Test
	public void parse_LsDash_ReturnNull() {
		// ls - (invalid)
		tool = shell.parse("ls -");
		assertNull(tool);	
	}
	
	@Test
	public void parse_CopyFileFile_ReturnCopyTool() {
		// copy file1 file2
		tool = shell.parse("copy 	"+sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"file.txt		dir4"+sep+"dir5"+sep+"file1.txt");
		assertTrue(tool instanceof CopyTool);
	}
	
	@Test
	public void parse_CopyDirectoryDirectory_ReturnCopyTool() {
		// copy directory1 directory2
		tool = shell.parse("copy 	"+sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"		dir4"+sep+"dir5"+sep);
		assertTrue(tool instanceof CopyTool);
	}
	
	@Test
	public void parse_CopyFileDirectory_ReturnCopyTool() {	
		// copy file directory
		tool = shell.parse("copy 	\"~"+sep+"dir"+sep+"file 1.txt\"		\"dir4"+sep+"dir 5"+sep+"\"");
		assertTrue(tool instanceof CopyTool);
	}
	
	@Test
	public void parse_CopyMultipleFilesDirectory_ReturnCopyTool() {
		// copy file1 file2 file3 directory
		tool = shell.parse("copy 	file.tar.gz		dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2.txt  ~"+sep+"dir1"+sep+"dir2"+sep);
		assertTrue(tool instanceof CopyTool);
	}
	
	/**
	 * Assumption: Shell cannot distinguish a valid file name or directory name
	 * since file names can be without extensions like .txt
	 * Hence, this is left to the tool to check if it is an existing file or directory
	 */
	@Test
	public void parse_CopyDirectoryFile_ReturnCopyTool() {
		// copy directory file (assumptions made)
		tool = shell.parse("copy \"~"+sep+"dir 123\"	file1.txt");
		assertTrue(tool instanceof CopyTool);
	}
	
	@Test
	public void parse_CopyMultipleFiles_ReturnCopyTool() {
		// copy file1 file2 file3 (assumptions made)
		tool = shell.parse("copy  file.tar.gz	dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2");
		assertTrue(tool instanceof CopyTool);
	}
	
	@Test
	public void parse_CopyMultipleFiles_ReturnNull() {
		// copy file1 file2 file3 (invalid)
		tool = shell.parse("copy  file.tar.gz	dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2.txt");
		assertNull(tool);
	}
	
	@Test
	public void parse_CopyNoArguments_ReturnNull() {
		// copy (invalid)
		tool = shell.parse("copy");
		assertNull(tool);
	}
	
	@Test
	public void parse_CopyFile_ReturnNull() {
		// copy file (invalid)
		tool = shell.parse("copy dir"+sep+"file");
		assertNull(tool);
	}
	
	@Test
	public void parse_MoveFileFile_ReturnMoveTool() {
		// move file1 file2
		tool = shell.parse("move 	"+sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"file.txt		dir4"+sep+"dir5"+sep+"file1.txt");
		assertTrue(tool instanceof MoveTool);
	}
	
	@Test
	public void parse_MoveDirectoryDirectory_ReturnMoveTool() {
		// move directory1 directory2
		tool = shell.parse("move 	"+sep+"dir"+sep+"dir2"+sep+"dir3"+sep+"		dir4"+sep+"dir5"+sep);
		assertTrue(tool instanceof MoveTool);
	}
	
	@Test
	public void parse_MoveFileDirectory_ReturnMoveTool() {
		// move file directory
		tool = shell.parse("move 	\"~"+sep+"dir"+sep+"file 1.txt\"		\"dir4"+sep+"dir 5"+sep+"\"");
		assertTrue(tool instanceof MoveTool);
	}
	
	@Test
	public void parse_MoveMultipleFilesDirectory_ReturnMoveTool() {	
		// move file1 file2 file3 directory
		tool = shell.parse("move 	file.tar.gz		dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2.txt  ~"+sep+"dir1"+sep+"dir2"+sep);
		assertTrue(tool instanceof MoveTool);
	}
	
	/**
	 * Assumption: Shell cannot distinguish a valid file name or directory name
	 * since file names can be without extensions like .txt
	 * Hence, this is left to the tool to check if it is an existing file or directory
	 */
	@Test
	public void parse_MoveDirectoryFile_ReturnMoveTool() {
		// move directory file (assumptions made)
		tool = shell.parse("move \"~"+sep+"dir 123\"	file1.txt");
		assertTrue(tool instanceof MoveTool);
	}
	
	@Test
	public void parse_MoveMultipleFiles_ReturnMoveTool() {
		// move file1 file2 file3 (assumptions made)
		tool = shell.parse("move  file.tar.gz	dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2");
		assertTrue(tool instanceof MoveTool);
	}
	
	@Test
	public void parse_MoveMultipleFiles_ReturnNull() {
		// move file1 file2 file3 (invalid)
		tool = shell.parse("move  file.tar.gz	dir4"+sep+"dir5"+sep+"file1.txt  ."+sep+"dir4"+sep+"dir5"+sep+"file2.txt");
		assertNull(tool);
	}
	
	@Test
	public void parse_MoveNoArguments_ReturnNull() {
		// move (invalid)
		tool = shell.parse("move");
		assertNull(tool);
	}
	
	@Test
	public void parse_MoveFile_ReturnNull() {
		// move file (invalid)
		tool = shell.parse("move dir"+sep+"file");
		assertNull(tool);
	}
	
	@Test
	public void parse_DeleteFile_ReturnDeleteTool() {
		// delete file
		tool = shell.parse("delete dir1d"+sep+"dir2dd"+sep+"main_1.java");
		assertTrue(tool instanceof DeleteTool);
	}
	
	@Test
	public void parse_DeleteDirectory_ReturnDeleteTool() {	
		// delete directory
		tool = shell.parse("delete		abc"+sep+"def"+sep);
		assertTrue(tool instanceof DeleteTool);
	}
	
	@Test
	public void parse_DeleteDirectoryFile_ReturnDeleteTool() {
		// delete directory file
		tool = shell.parse("delete abc"+sep+"def"+sep+" test.java");
		assertTrue(tool instanceof DeleteTool);
	}
	
	@Test
	public void parse_DeleteFileDirectory_ReturnDeleteTool() {
		// delete file directory
		tool = shell.parse("delete  test.java abc"+sep+"def"+sep+"");
		assertTrue(tool instanceof DeleteTool);
	}
	
	@Test
	public void parse_DeleteNoArgument_ReturnNull() {
		// delete (invalid)
		tool = shell.parse("delete ");
		assertNull(tool);
	}
	
	@Test
	public void parse_CatFileFile_ReturnCatTool() {
		// cat file1 file2
		tool = shell.parse("cat  	"+sep+"dir"+sep+"dir2"+sep+sep+"f_i_l_e.py  \"dir1"+sep+"dir2"+sep+"my file.txt\"");
		assertTrue(tool instanceof CatTool);
	}
	
	@Test
	public void parse_CatDash_ReturnCatTool() {
		// cat -
		tool = shell.parse("cat  -");
		assertTrue(tool instanceof CatTool);
	}
	
	@Test
	public void parse_CatDirectory_ReturnCatTool() {
		// cat directory
		tool = shell.parse("cat	 directory");
		assertTrue(tool instanceof CatTool);
	}
	
	@Test
	public void parse_CatMutipleFilesDirectories_ReturnCatTool() {
		// cat directory1 file1 directory2 file2
		tool = shell.parse("cat	 directory1 file1.txt directory2 file2.txt");
		assertTrue(tool instanceof CatTool);
	}
	
	/**
	 * Assumption: if there is no "-" or filename argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void parse_CatNoArgument_ReturnCatTool() {
		// cat
		tool = shell.parse("cat");
		assertTrue(tool instanceof CatTool);
	}
	
	@Test
	public void parse_CatMultipleDashes_ReturnNull() {
		// cat - file1 - file2 -
		tool = shell.parse("cat  - \"file1.txt\" - file2.txt -");
		assertNull(tool);
	}
	
	
	@Test
	public void parse_EchoString_ReturnEchoTool() {
		tool = shell.parse("echo abc de34 [][] '''");
		assertTrue(tool instanceof EchoTool);
	}
	
	@Test
	public void parse_EchoNoArgument_ReturnEchoTool() {
		tool = shell.parse("echo ");
		assertTrue(tool instanceof EchoTool);
	}
	
	@Test
	public void parse_GrepOptionPatternFile_ReturnGrepTool() {
		// grep option pattern file
		tool = shell.parse("grep -A 5 \"testing 123\" test.txt");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepOptionPatternMultipleFiles_ReturnGrepTool() {
		// grep option pattern file1 file2
		tool = shell.parse("grep -A 5 \"testing 123\" test1.txt test2");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepPatternFile_ReturnGrepTool() {
		// grep pattern file
		// allows \" in pattern
		tool = shell.parse("grep	\" a?^\\\". \"	\"~"+sep+"dir 1"+sep+"dir 2"+sep+"test.txt\"");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepPatternMultipleFiles_ReturnGrepTool() {
		// grep pattern file1 file2
		tool = shell.parse("grep pattern123 file1 file2.mp3");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepOptionPatternDash_ReturnGrepTool() {
		// grep option pattern -
		tool = shell.parse("grep   -o    \"(*){4}\"   -");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepOptionWithNumPatternFile_ReturnGrepTool() {
		// grep option num pattern file
		tool = shell.parse("grep -B 21 \"pattern_*\" file");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepPatternDash_ReturnGrepTool() {
		// grep pattern -
		tool = shell.parse("grep \"pattern xyz [a-zA-Z]?\" -");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepPattern_ReturnGrepTool() {
		// grep pattern
		tool = shell.parse("grep	\"pattern [0-9]* \\\"\"");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_PatternNoQuotes_ReturnGrepTool() {
		// grep pattern
		tool = shell.parse("grep	pattern ");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepEmptyPattern_ReturnGrepTool() {
		// grep pattern in quotes
		tool = shell.parse("grep \"\"");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepOptionHelp_ReturnGrepTool() {
		// grep -help
		tool = shell.parse("grep	-help");
		assertTrue(tool instanceof GrepTool);
	}
	
	/**
	 * Assumption: Pattern does not need to be in quotes. So the file name is treated as a pattern 
	 * and assumes a standard input is expected, which adds an additional "-" in its argument
	 */
	@Test
	public void parse_GrepFile_ReturnGrepTool() {	
		// grep file (assumptions made)
		tool = shell.parse("grep	."+sep+"this"+sep+"is"+sep+"a"+sep+"test.txt");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepDash_ReturnGrepTool() {	
		// grep - (assumptions made)
		tool = shell.parse("grep -");
		assertTrue(tool instanceof GrepTool);
	}
	
	/**
	 * Assumption: pattern must always be declared in front of file 
	 * but options can be anywhere in the command
	 */
	@Test
	public void parse_GrepPatternFileOption_ReturnGrepTool() {
		// grep pattern file option (assumptions made)
		tool = shell.parse("grep	\"pattern+\" file.txt -v");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepPatternOption_ReturnGrepTool() {
		// grep pattern option
		tool = shell.parse("grep	\"pattern+\" -v");
		assertTrue(tool instanceof GrepTool);
	}
	
	@Test
	public void parse_GrepMultipleOptionsPatternFile_ReturnNull() {
		// grep option1 option2 option3 pattern file (invalid)
		tool = shell.parse("grep -c -o \"testing 123\" test.txt");
		assertNull(tool);
	}
	
	@Test
	public void parse_GrepOptionNoNumPatternDash_ReturnNull() {
		// grep option(no num) pattern - (invalid)
		tool = shell.parse("grep -C \"pattern 1 2 3\" file");
		assertNull(tool);
	}
	
	@Test
	public void parse_GrepOption_ReturnNull() {
		// grep option (invalid)
		tool = shell.parse("grep	-A");
		assertNull(tool);
	}
	
	@Test
	public void parse_GrepNoArguments_ReturnNull() {
		// grep (invalid)
		tool = shell.parse(" grep ");
		assertNull(tool);
	}
	
	@Test
	public void parse_Pipe2Commands_ReturnPipingTool() {
		// cd | pwd
		tool = shell.parse("cd \"D:"+sep+sep+"dir 1"+sep+"dir 1 2 3\"|pwd");
		assertTrue(tool instanceof PipingTool);
	}
	
	@Test
	public void parse_PipeFourCommands_ReturnPipingTool() {
		// ls | cat | echo | grep
		tool = shell.parse("ls \"dir 1"+sep+"dir2\" file.txt |cat directory1 | echo abcdefg| grep -c \"(\\\\s)*\"");
		assertTrue(tool instanceof PipingTool);
	}
	
	@Test
	public void parse_PipeCommandsWithStdin_ReturnTools() {
		// cat (with stdin) | grep (with stdin) | echo
		tool = shell.parse("cat file.txt - | grep -C 3 \"abc\" | echo testing 1 2 3 ");
		assertTrue(tool instanceof PipingTool);
	}
	
	@Test
	public void parse_PipeAtStartOfCommand_ReturnNull() {
		tool = shell.parse("   | copy file1.a file2.a directory | ls ");
		assertNull(tool);
	}
	
	@Test
	public void parse_PipeAtEndOfCommand_ReturnNull() {
		tool = shell.parse("copy file1.a file2.a directory | ls | ");
		assertNull(tool);
	}
	
	@Test
	public void parse_WcNoArguments_ReturnWcTool() {
		tool = shell.parse("wc");
		assertTrue(tool instanceof WcTool);
	}
	
	@Test
	public void parse_WcOption_ReturnWcTool() {
		tool = shell.parse("wc -m");
		assertTrue(tool instanceof WcTool);
	}
	
	@Test
	public void parse_WcMultipleOptions_ReturnWcTool() {
		tool = shell.parse("wc -l -w");
		assertTrue(tool instanceof WcTool);
	}
	
	@Test
	public void parse_WcMultipleOptionsWithStdin_ReturnWcTool() {
		tool = shell.parse("wc -w -help -");
		assertTrue(tool instanceof WcTool);
	}
	
	@Test
	public void parse_WcFile_ReturnWcTool() {
		tool = shell.parse("wc file");
		assertTrue(tool instanceof WcTool);
	}
	
	@Test
	public void parse_WcMultipleFiles_ReturnWcTool() {
		tool = shell.parse("wc file1.txt file2");
		assertTrue(tool instanceof WcTool);
	}
	
	@Test
	public void parse_WcMultipleOptionsAndFiles_ReturnWcTool() {
		tool = shell.parse("wc -w -l file1.txt file2");
		assertTrue(tool instanceof WcTool);
	}
	
	@Test
	public void parse_WcInvalidOption_ReturnNull() {
		tool = shell.parse("wc -l -s");
		assertNull(tool);
	}
	
	/**
	 * Assumption: options must always be declared before files or stdin ("-")
	 */
	@Test
	public void parse_WcFilesOptions_ReturnNull() {
		tool = shell.parse("wc file1 file2 -l -w");
		assertNull(tool);
	}
	
	@Test
	public void parse_SortOptionHelp_ReturnSortTool() {
		tool = shell.parse("sort -help");
		assertTrue(tool instanceof SortTool);
	}
	
	@Test
	public void parse_SortFile_ReturnSortTool() {
		tool = shell.parse("sort file.txt");
		assertTrue(tool instanceof SortTool);
	}
	
	@Test
	public void parse_SortMultipleFiles_ReturnSortTool() {
		tool = shell.parse("sort file.txt file2");
		assertTrue(tool instanceof SortTool);
	}
	
	@Test
	public void parse_SortOptionFile_ReturnSortTool() {
		tool = shell.parse("sort -c file");
		assertTrue(tool instanceof SortTool);
	}
	
	@Test
	public void parse_SortOptionMultipleFiles_ReturnSortTool() {
		tool = shell.parse("sort -c file.txt file2");
		assertTrue(tool instanceof SortTool);
	}
	
	@Test
	public void parse_SortMultipleOptionsAndFiles_ReturnSortTool() {
		tool = shell.parse("sort -c -help file1 file2");
		assertTrue(tool instanceof SortTool);
	}
	
	@Test
	public void parse_SortNoArguments_ReturnNull() {
		tool = shell.parse("sort");
		assertNull(tool);
	}
	
	@Test
	public void parse_SortStdin_ReturnNull() {
		tool = shell.parse("sort -");
		assertNull(tool);
	}
	
	@Test
	public void parse_SortOption_ReturnNull() {
		tool = shell.parse("sort -c");
		assertNull(tool);
	}
	
	/**
	 * Assumption: option must always be declared before files
	 */
	@Test
	public void parse_SortFileOption_ReturnNull() {
		tool = shell.parse("sort file.txt -c");
		assertNull(tool);
	}
	
	@Test
	public void parse_SortInvalidOptionFile_ReturnNull() {
		tool = shell.parse("sort -d file.txt");
		assertNull(tool);
	}
	
	@Test
	public void parse_CommNoOptionFiles_ReturnCommTool() {
		tool = shell.parse("comm file.txt file2");
		assertTrue(tool instanceof CommTool);
	}
	
	@Test
	public void parse_CommOptionCFiles_ReturnCommTool() {
		tool = shell.parse("comm -c file.txt file2.txt");
		assertTrue(tool instanceof CommTool);
	}
	
	@Test
	public void parse_CommOptionDFiles_ReturnCommTool() {
		tool = shell.parse("comm -d file.txt file2.txt");
		assertTrue(tool instanceof CommTool);
	}
	
	@Test
	public void parse_CommOptionHelpFiles_ReturnCommTool() {
		tool = shell.parse("comm -help file.txt file2.txt");
		assertTrue(tool instanceof CommTool);
	}
	
	@Test
	public void parse_CommOptionHelp_ReturnCommTool() {
		tool = shell.parse("comm -help");
		assertTrue(tool instanceof CommTool);
	}
	
	@Test
	public void parse_CommOptionDHelpFiles_ReturnCommTool() {
		tool = shell.parse("comm -d -help file.txt file2");
		assertTrue(tool instanceof CommTool);
	}
	
	@Test
	public void parse_CommOptionCHelpNoFiles_ReturnCommTool() {
		tool = shell.parse("comm -c -help");
		assertTrue(tool instanceof CommTool);
	}
	
	@Test
	public void parse_CommOptionCDFiles_ReturnNull() {
		tool = shell.parse("comm -c -d file.txt file2");
		assertNull(tool);
	}
	
	@Test
	public void parse_Comm1File_ReturnNull() {
		tool = shell.parse("comm file.txt");
		assertNull(tool);
	}
	
	@Test
	public void parse_CommNoArguments_ReturnNull() {
		tool = shell.parse("comm");
		assertNull(tool);
	}
	
	@Test
	public void parse_CommStdin_ReturnNull() {
		tool = shell.parse("comm -");
		assertNull(tool);
	}
	
	/**
	 * Assumption: options must always be declared before files
	 */
	@Test
	public void parse_CommFilesOption_ReturnNull() {
		tool = shell.parse("comm file1 file2 -c");
		assertNull(tool);
	}
	
	/**
	 * Assumption: if there is no "-" or filename as last argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void parse_PasteOption_ReturnPasteTool() {
		tool = shell.parse("paste -s");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteStdin_ReturnPasteTool() {
		tool = shell.parse("paste -");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteFile_ReturnPasteTool() {
		tool = shell.parse("paste file1");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteMultipleFiles_ReturnPasteTool() {
		tool = shell.parse("paste file1 file2");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteOptionHelp_ReturnPasteTool() {
		tool = shell.parse("paste -help");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteOptionSHelpStdin_ReturnPasteTool() {
		tool = shell.parse("paste -s -help -");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteOptionHelpFile_ReturnPasteTool() {
		tool = shell.parse("paste -help file");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteOptionDNoDelim_ReturnPasteTool() {
		tool = shell.parse("paste -d");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteOptionDDelimStdin_ReturnPasteTool() {
		tool = shell.parse("paste -d -abc- -");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteOptionSFiles_ReturnPasteTool() {
		tool = shell.parse("paste -s file file2");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteOptionSDFile_ReturnPasteTool() {
		tool = shell.parse("paste -s -d ( file.py");
		assertTrue(tool instanceof PasteTool);
	}
	
	@Test
	public void parse_PasteInvalidOption_ReturnNull() {
		tool = shell.parse("paste -c file");
		assertNull(tool);
	}
	
	/**
	 * Assumption: options must always be declared before files or stdin ("-")
	 */
	@Test
	public void parse_PasteFileOption_ReturnNull() {
		tool = shell.parse("paste file -s");
		assertNull(tool);
	}
	
	/**
	 * Assumption: if there is no "-" or filename as last argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void parse_UniqOption_ReturnUniqTool() {
		tool = shell.parse("uniq -i");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqNoArguments_ReturnUniqTool() {
		tool = shell.parse("uniq");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqStdin_ReturnUniqTool() {
		tool = shell.parse("uniq -");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqFile_ReturnUniqTool() {
		tool = shell.parse("uniq file");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqMultipleFiles_ReturnUniqTool() {
		tool = shell.parse("uniq file1 file2");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqOptionHelp_ReturnUniqTool() {
		tool = shell.parse("uniq -help");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqOptionIHelpFile_ReturnUniqTool() {
		tool = shell.parse("uniq -i -help file");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqOptionIStdin_ReturnUniqTool() {
		tool = shell.parse("uniq -i -");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqOptionFNum_ReturnUniqTool() {
		tool = shell.parse("uniq -f 100");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqOptionFNumFile_ReturnUniqTool() {
		tool = shell.parse("uniq -f 100 file");
		assertTrue(tool instanceof UniqTool);
	}
	
	@Test
	public void parse_UniqOptionFNoNumFile_ReturnNull() {
		tool = shell.parse("uniq -f file1");
		assertNull(tool);
	}
	
	@Test
	public void parse_UniqOptionINum_ReturnNull() {
		tool = shell.parse("uniq -i 7");
		assertNull(tool);
	}
	
	@Test
	public void parse_UniqInvalidOption_ReturnNull() {
		tool = shell.parse("uniq -d file");
		assertNull(tool);
	}
	
	/**
	 * Assumption: options must always be declared before files or stdin ("-")
	 */
	@Test
	public void parse_UniqFileOption_ReturnNull() {
		tool = shell.parse("uniq file -f 2-10,19");
		assertNull(tool);
	}
	
	/**
	 * Assumption: if there is no "-" or filename as last argument, 
	 * it is by default treated as "-" (standard input)
	 */
	@Test
	public void parse_CutOptionFList_ReturnCutTool() {
		tool = shell.parse("cut -f 1-3,6,-10");
		assertTrue(tool instanceof CutTool);
	}
	
	@Test
	public void parse_CutOptionCStdin_ReturnCutTool() {
		tool = shell.parse("cut -c 8 -");
		assertTrue(tool instanceof CutTool);
	}
	
	@Test
	public void parse_CutOptionFDFile_ReturnCutTool() {
		tool = shell.parse("cut -f -10,3 - 7,20 -d \"+  +\" file");
		assertTrue(tool instanceof CutTool);
	}
	
	@Test
	public void parse_CutOptionCMultipleFiles_ReturnCutTool() {
		tool = shell.parse("cut -c 1-3, 2- file.txt file2");
		assertTrue(tool instanceof CutTool);
	}
	
	/**
	 * Assumption: the delimiter for -d can be any string of characters
	 * In this test case, "file" is taken as the delimiter for -d thus a standard input is expected
	 */
	@Test
	public void parse_CutOptionFDWithoutDelim_ReturnCutTool() {
		tool = shell.parse("cut -f 2 -d file");
		assertTrue(tool instanceof CutTool);
	}
	
	@Test
	public void parse_CutOptionCNoList_ReturnNull() {
		tool = shell.parse("cut -c");
		assertNull(tool);
	}
	
	/**
	 * Assumption: the cut command requires -f option if -d option is used
	 */
	@Test
	public void parse_CutOptionDWithoutOptionF_ReturnNull() {
		tool = shell.parse("cut -d abc file");
		assertNull(tool);
	}
	
	@Test
	public void parse_CutNoArguments_ReturnNull() {
		tool = shell.parse("cut");
		assertNull(tool);
	}
	
	@Test
	public void parse_CutNoOption_ReturnNull() {
		tool = shell.parse("cut file");
		assertNull(tool);
	}
	
	@Test
	public void parse_CutInvalidOption_ReturnNull() {
		tool = shell.parse("cut -a file");
		assertNull(tool);
	}
	
	/**
	 * Assumption: options must always be declared before files or stdin ("-")
	 */
	@Test
	public void parse_CutFileOption_ReturnNull() {
		tool = shell.parse("cut file -c 1-10");
		assertNull(tool);
	}
}
