package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;

public class OurCOMMToolTest {

	private ICommTool commtool;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		commtool = null;
	}

	   /** 
     * Test error handling 
     * Invalid working directory 
     */
    @Test
    public void executeInvalidWorkingDirTest() { 
        commtool = new CommTool(new String[]{"hello","world"}); 
        commtool.execute(null, null); 
        assertEquals(4, commtool.getStatusCode()); 
    } 
    
    /**
     * Test expected behavior
     * Execute comm with a option
     */
    @Test
    public void executeOneOptionTest() throws IOException {
        File tempFolder = Files.createTempDirectory("tempFolder").toFile();
        File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
        File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
        String temp = "hello world\nthis is a test\nfile";
        Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
        temp = "baby\nhello world";
        Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
        temp = "\tbaby\r\n\t\thello world\r\nthis is a test\r\nfile\r\n";
        commtool = new CommTool(new String[]{"-d", file1.getName(),file2.getName()});
        String result = commtool.execute(tempFolder, null);
        assertEquals(temp.compareTo(result), 0);
        Files.delete(file1.toPath());
        Files.delete(file2.toPath());
        Files.delete(tempFolder.toPath());
    }
    
    /**
     * Test expected behavior
     * Execute comm with a option
     */
    @Test
    public void executeFileWithSpaceTest() throws IOException {
        File tempFolder = Files.createTempDirectory("tempFolder").toFile();
        File file1 = Files.createFile(new File(tempFolder, "temp File1.txt").toPath()).toFile();
        File file2 = Files.createFile(new File(tempFolder, "temp File2.txt").toPath()).toFile();
        String temp = "hello world\nthis is a test\nfile";
        Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
        temp = "baby\nhello world";
        Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
        temp = "\tbaby\r\n\t\thello world\r\nthis is a test\r\nfile\r\n";
        commtool = new CommTool(new String[]{"-d", file1.getName(),file2.getName()});
        String result = commtool.execute(tempFolder, null);
        assertEquals(temp.compareTo(result), 0);
        Files.delete(file1.toPath());
        Files.delete(file2.toPath());
        Files.delete(tempFolder.toPath());
    }
    
    /**
	 * Test expected behavior
	 * Comparing two files with no option and both files are the same file
	 */
	@Test
	public void executeSameFileNoOptionTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		commtool = new CommTool(new String[]{file.getAbsolutePath(),file.getAbsolutePath()});
		String result = commtool.execute(file.getParentFile(), null);
		String expected = "\t\thello world\r\n\t\tthis is a test\r\n\t\tfile\r\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	/**
	 * Test error handling
	 * Comparing one directory and one file
	 */
	@Test
	public void executeFolder1Test() throws IOException{
		File dir = Files.createTempDirectory("tempdir").toFile();
		String arg = "-c " + dir.getAbsolutePath() + " " + dir.getAbsolutePath();
		commtool = new CommTool(arg.split(" "));
		String result = commtool.execute(dir.getParentFile(), null);
		assertEquals(result,"Error: FILE 1 is not a file");
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(dir.toPath());
	}
	
	
}