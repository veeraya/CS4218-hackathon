package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class PipingToolTest {
	PipingTool pipingTool;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		pipingTool = null;
	}
	
	@Test
	public void pipe_StdoutCat_GetStatusCode0() {
		ITool to = new CatTool(new String[]{"-"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(to);
		
		pipingTool = new PipingTool(tools);
		pipingTool.pipe("test test test", to);
		assertEquals(0, pipingTool.getStatusCode());
	}
	
	@Test
	public void pipe_EmptyStdoutCd_GetStatusCode0() {
		ITool to = new CdTool(new String[]{System.getProperty("user.dir")});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(to);
		
		pipingTool = new PipingTool(tools);
		pipingTool.pipe("", to);
		assertEquals(0, pipingTool.getStatusCode());
	}

	@Test
	public void pipe_StdoutGrepFail_GetStatusCode0() {
		ITool to = new GrepTool(new String[]{"-A", "2", "pattern", "-"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(to);
		
		pipingTool = new PipingTool(tools);
		pipingTool.pipe("test grep pattern stdin test", to);
		assertEquals(0, pipingTool.getStatusCode());
	}
	
	@Test
	public void pipe_EmptyStdoutDeleteFail_GetStatusCode1() {
		ITool to = new DeleteTool(new String[]{"invalid directory"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(to);
		
		pipingTool = new PipingTool(tools);
		pipingTool.pipe("", to);
		assertEquals(1, pipingTool.getStatusCode());
	}

	@Test
	public void pipe_DeleteLs_GetStatusCode0() {
		// delete | ls
		File dir = new File("testdirectory");
		dir.mkdir();
		
		ITool from = new DeleteTool (new String[]{"testdirectory"});
		ITool to = new LsTool(new String[]{""});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(from);
		tools.add(to);
		
		pipingTool = new PipingTool(tools);
		pipingTool.pipe(from, to);
		assertFalse(dir.exists());
		assertEquals(0, pipingTool.getStatusCode());
	}
	
	@Test
	public void pipe_CdPwd_GetStatusCode0() {
		// cd | pwd
		ITool from = new CdTool(new String[]{System.getProperty("user.dir")});
		ITool to = new PWDTool();
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(from);
		tools.add(to);
		
		pipingTool = new PipingTool(tools);
		pipingTool.pipe(from, to);
		assertEquals(0, pipingTool.getStatusCode());
	}
	
	@Test
	public void pipe_EchoCatWithStdin_GetStatusCode0() {
		// echo | cat (with stdin)
		ITool from = new EchoTool(new String[]{System.getProperty("user.dir")});
		ITool to = new CatTool(new String[]{"-"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(from);
		tools.add(to);
		
		pipingTool = new PipingTool(tools);
		pipingTool.pipe(from, to);
		assertEquals(0, pipingTool.getStatusCode());
	}
	
	@Test
	public void pipe_LsMoveFail_GetStatusCode1() {
		// ls | move (fail)
		ITool from = new LsTool(new String[]{""});
		ITool to = new MoveTool(new String[]{"file1.txt", "file2.txt", "directory1"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(from);
		tools.add(to);
		
		pipingTool = new PipingTool(tools);
		pipingTool.pipe(from, to);
		assertEquals(1, pipingTool.getStatusCode());
	}
	
	@Test
	public void execute_2Commands_GetStatusCode0() {
		// cd | pwd
		ITool tool1 = new CdTool(new String[]{System.getProperty("user.dir")});
		ITool tool2 = new PWDTool();
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(tool1);
		tools.add(tool2);
		
		pipingTool = new PipingTool(tools);
		pipingTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, pipingTool.getStatusCode());
	}
	
	@Test
	public void execute_3CommandsWithStdin_GetStatusCode0() {
		// cat (with stdin) | grep | echo
		ITool tool1 = new CatTool(new String[]{"-"});
		ITool tool2 = new GrepTool(new String[]{"-c", "abc", "-"});
		ITool tool3 = new EchoTool(new String[]{"testing 1 2 3"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(tool1);
		tools.add(tool2);
		tools.add(tool3);
		
		pipingTool = new PipingTool(tools);
		pipingTool.execute(new File(System.getProperty("user.dir")), "xyz");
		assertEquals(0, pipingTool.getStatusCode());
	}
	
	@Test
	public void execute_1CommandFail_GetStatusCode1() {
		// copy (fail) | echo
		ITool tool1 = new CopyTool(new String[]{"abc.txt","def.txt"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(tool1);
		
		pipingTool = new PipingTool(tools);
		pipingTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(1, pipingTool.getStatusCode());
	}
	
	@Test
	public void execute_2CommandsFail_GetStatusCode1() {
		// copy (fail) | echo
		ITool tool1 = new CopyTool(new String[]{"abc.txt","def.txt"});
		ITool tool2 = new EchoTool(new String[]{"testing 1 2 3"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(tool1);
		tools.add(tool2);
		
		pipingTool = new PipingTool(tools);
		pipingTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(1, pipingTool.getStatusCode());
	}
	
	@Test
	public void execute_3CommandsFail_GetStatusCode1() {
		// echo | cd (fail) | pwd
		ITool tool1 = new EchoTool(new String[]{"testing 1 2 3"});
		ITool tool2 = new CopyTool(new String[]{"invalid directory"});
		ITool tool3 = new PWDTool();
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(tool1);
		tools.add(tool2);
		tools.add(tool3);
		
		pipingTool = new PipingTool(tools);
		pipingTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(1, pipingTool.getStatusCode());
	}
	
}
