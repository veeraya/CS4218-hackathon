package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;

/**
 * @author Mew
 * 
 */
public class OurCATToolTest {

	private ICatTool cattool;
	private File tempFolder;
	private File tempFile;
	private StringBuilder testStr;

	@Before
	public void setUp() throws Exception {
		tempFolder = Files.createTempDirectory("tempFolder").toFile();
		tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		testStr = new StringBuilder();
		testStr.append("This is a test file for CATTool class\n");
		testStr.append("It should read until this line.");
		Files.write(tempFile.toPath(), testStr.toString().getBytes(),  StandardOpenOption.CREATE);
	}

	@After
	public void tearDown() throws Exception {
		cattool = null;
		Files.delete(tempFolder.toPath());
		Files.delete(tempFile.toPath());
	}
	
    private String readFile(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
      }

	@Test
	public void getStringForFileTest() throws IOException {
		cattool = new CatTool(new String[]{});
		assertEquals(cattool.getStringForFile(tempFile), testStr.toString());
	}

	@Test
	public void executeNoArgumentTest() {
		// Test error-handling 1
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		cattool = new CatTool(new String[]{});
		String msg = cattool.execute(tempWorkingDir, null);
		assertTrue(cattool.getStatusCode() != 0);
		assertEquals("Error: Missing parameter",msg);
	}
	
	//works fine with manual testing
	@Test
	public void executeOneFileTest() {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		cattool = new CatTool(new String[]{tempFile.getName()});
		String msg = cattool.execute(tempWorkingDir, null);
		assertTrue(cattool.getStatusCode() == 0);
		assertEquals(msg , testStr.toString());
	}
	
	//works fine with manual testing
	@Test
	public void executeTwoFilesTest() throws IOException {
		File tempFile2 = Files.createTempFile("tempFile2", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile2.toPath(), testStr.toString().getBytes(),  StandardOpenOption.CREATE);
		cattool = new CatTool(new String[]{tempFile.getName(),tempFile2.getName()});
		String msg = cattool.execute(tempWorkingDir, null);
		assertTrue(cattool.getStatusCode() == 0);
		assertEquals(msg , testStr.toString()+testStr.toString());
	}
	
	//cat tempFile.tmp > tempFile2.tmp
	@Test
	public void executeFromFileToOutputFileTest() throws IOException{
		File tempFile2 = Files.createTempFile("tempFile2", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		cattool = new CatTool(new String[]{tempFile.getName(),">",tempFile2.getName()});
		cattool.execute(tempWorkingDir, null);
		assertTrue(cattool.getStatusCode() == 0);
		assertTrue(tempFile2.exists());
		assertEquals(readFile(tempFile)+"\r\n",readFile(tempFile2));
		Files.delete(tempFile2.toPath());
	}
	
	@Test
	public void executeFromFileToNonExistedFileTest() throws IOException{
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		cattool = new CatTool(new String[]{tempFile.getName(), ">", "newFile.tmp"});
		cattool.execute(tempWorkingDir, null);
		File newTempFile = new File(tempWorkingDir.getAbsolutePath()+"\\newFile.tmp");
		assertTrue(cattool.getStatusCode() == 0);
		assertTrue(newTempFile.exists());
		assertEquals(readFile(tempFile)+"\r\n",readFile(newTempFile));
		Files.delete(newTempFile.toPath());
	}
	
	@Test
	public void executeInvalidSourceNameTest() throws IOException {
		cattool = new CatTool(new String[]{"tempFile.tmp", tempFolder.getName()});
		String msg = cattool.execute(tempFile.getParentFile(), null);
		assertEquals("Error: SOURCE file not found", msg);
		assertTrue(cattool.getStatusCode() != 0);
	}
	
	@Test
	public void executeContainsSpaceTest() throws IOException {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFileSpace = Files.createTempFile("tempFile Space", ".tmp").toFile();
		Files.write(tempFileSpace .toPath(), testStr.toString().getBytes(),  StandardOpenOption.CREATE);
		cattool = new CatTool(new String[]{tempFileSpace.getName()});
		String msg = cattool.execute(tempWorkingDir, null);
		assertTrue(cattool.getStatusCode() == 0);
		assertEquals(msg , testStr.toString());
		Files.delete(tempFileSpace.toPath());
	}
}