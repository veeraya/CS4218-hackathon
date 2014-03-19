package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;

public class OurPWDToolTest {
	private IPwdTool pwdtool; 
	
	@Before
	public void before(){
		pwdtool = new PWDTool();
	}

    @After
	public void after(){
		pwdtool = null;
	}
	
    /**
     * Test expected behavior
     * Test if method is able to return the path of a directory
     */
	@Test
	public void getStringForDirectoryTest() throws IOException {
		String existsDirString = File.createTempFile("exists", "tmp").getParent();
		File existsDir = new File(existsDirString);
		String dirString = pwdtool.getStringForDirectory(existsDir);
		assertTrue(dirString.equals(existsDirString));
		assertEquals(pwdtool.getStatusCode(), 0);
    }

	/**
	 * Test error-handling 1
	 * Reference of non-existing file
	 */
	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException { 
		File notExistsDir = new File("notexists");
        pwdtool.getStringForDirectory(notExistsDir);
		assertTrue(pwdtool.getStatusCode()!= 0);
    }
		
	/**
	 * Test error-handling 1
	 * Reference of file that is null
	 */
	@Test
	public void getStringForNullDirectoryTest() throws IOException { 
		pwdtool.getStringForDirectory(null);
		assertTrue(pwdtool.getStatusCode()!= 0);
	}
}