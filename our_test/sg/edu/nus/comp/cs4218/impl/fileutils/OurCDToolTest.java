package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;

public class OurCDToolTest {
	
	private ICdTool cdtool;
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
            cdtool = null;
    }


	@Test
	public void changeDirectoryTest() throws IOException {
		//Test expected behavior
		//Create a tmp-file and get (existing) parent directory
		File dir = File.createTempFile("exists", "tmp");
		String dirName = dir.getName();
		String dirParentString = dir.getParent();
		
		cdtool = new CdTool(new String[]{});
		File newDir = cdtool.changeDirectory(dir.getAbsolutePath());
		String newDirString = newDir.getAbsolutePath();
		
		assertTrue(newDirString.equals(dirParentString + "\\" + dirName));
		assertEquals(cdtool.getStatusCode(), 0);
		
    }

}
