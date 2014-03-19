package sg.edu.nus.comp.cs4218.integrationtest.extended2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

public class WcToolIntegrationTest {
	
	private File file1, file2, dir;
	private String userDir;

	@Before
	public void setUp() throws Exception {
		// create files with content
		createFile1();
		createFile2();
		
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

	@After
	public void tearDown() throws Exception {
		file1.delete();
		file2.delete();
		dir.delete();
	}

	@Test
	public void structuralIntegration_GrepWc_GetStatusCode0 () {
		
		String [] grepArgs = new String[]{"-A", "2", "testing", "file1"};
		ITool grepTool = new GrepTool(grepArgs);
		String[] wcArgs = new String[]{"-l", "-"};
		ITool wcTool = new WcTool(wcArgs);
		
		String result = grepTool.execute(new File(userDir), null);
		result = wcTool.execute(new File(userDir), result);
		assertEquals(0, grepTool.getStatusCode());
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t3", result);
	}
	
	@Test
	public void structuralIntegration_WcCat_GetStatusCode0 () {
		String[] wcArgs = new String[]{"-m", "-"};
		ITool wcTool = new WcTool(wcArgs);
		String [] catArgs = new String[]{"-"};
		ITool catTool = new CatTool(catArgs);
		
		String result = wcTool.execute(new File(userDir), "this is a standard input");
		result = catTool.execute(new File(userDir), result);
		assertEquals(0, wcTool.getStatusCode());
		assertEquals(0, catTool.getStatusCode());
		assertEquals("\t24", result);
	}
	
	@Test
	public void structuralIntegration_EchoWc_GetStatusCode0 () {
		String [] echoArgs = new String[]{"testing wc and echo together"};
		ITool echoTool = new EchoTool(echoArgs);
		String[] wcArgs = new String[]{"-w", "-"};
		ITool wcTool = new WcTool(wcArgs);
		
		String result = echoTool.execute(new File(userDir), "no stdin");
		result = wcTool.execute(new File(userDir), result);
		assertEquals(0, echoTool.getStatusCode());
		assertEquals(0, wcTool.getStatusCode());
		assertEquals("\t5", result);
	}
	
	@Test
	public void structuralIntegration_WcGrepError_GetStatusCodeNot0 () {
		// wc no standard input when it is expected
		
		String [] wcArgs = new String[]{"-m", "-"};
		ITool wcTool = new WcTool(wcArgs);
		String[] grepArgs = new String[]{"-A", "2", "this", "-"};
		ITool grepTool = new GrepTool(grepArgs);
		
		String result = wcTool.execute(new File(userDir), null);
		assertEquals(WcTool.STATUS_CODE_STDIN_EXPECTED, wcTool.getStatusCode());
		
		result = grepTool.execute(new File(userDir), result);
		assertEquals(0, grepTool.getStatusCode());
	}
	
	@Test
	public void structuralIntegration_CatWcError_GetStatusCodeNot0 () {
		// cat file does not exist
		
		String[] catArgs = new String[]{"fileNotExist"};
		ITool catTool = new CatTool(catArgs);
		String [] wcArgs = new String[]{"-l", "-"};
		ITool wcTool = new WcTool(wcArgs);
		
		String result = catTool.execute(new File(userDir), null);
		assertEquals(CatTool.ERR_CODE_FILE_NOT_FOUND, catTool.getStatusCode());
		
		// error message from cat passed to wc
		result = wcTool.execute(new File(userDir), result);
		assertEquals(0, wcTool.getStatusCode());
	}
	
	@Test
	public void structuralIntegration_WcEchoError_GetStatusCodeNot0 () {
		// wc no standard input when it is expected
		
		String [] wcArgs = new String[]{"-m", "-"};
		ITool wcTool = new WcTool(wcArgs);
		String[] echoArgs = null;
		ITool echoTool = new EchoTool(echoArgs);
		
		String result = wcTool.execute(new File(userDir), null);
		assertEquals(WcTool.STATUS_CODE_STDIN_EXPECTED, wcTool.getStatusCode());
		
		result = echoTool.execute(new File(userDir), result);
		assertEquals(0, echoTool.getStatusCode());
		
	}

}
