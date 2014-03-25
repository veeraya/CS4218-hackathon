/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;

public class OurCUTToolTest {
    
    private ICutTool cuttool; 
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        cuttool = null;
    }
    
	@Test
    public void cutSpecfiedCharDecreasingRangeTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        String expectedResult = "Error: invalid decreasing range";
        assertEquals(expectedResult, cuttool.cutSpecifiedCharactersUseDelimiter("8-6", ":", "alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu"));
    }

    @Test
    public void cutSpecfiedCharNotNumberRangeTest() throws IOException {
        cuttool = new CutTool(new String[]{});
        String expectedResult = "Error: invalid list argument for -c";
        assertEquals(expectedResult, cuttool.cutSpecifiedCharactersUseDelimiter("abc", ":",  "alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu"));
    }

  
}
