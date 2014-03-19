package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author pook
 *
 */
public class OurECHOToolTest {
	private EchoTool echotool; 
	
	@Before
	public void before(){
		echotool = new EchoTool(new String[]{});
	}

    @After
	public void after(){
    	echotool = null;
	}
    
	@Test
	public void echoHelloWorldTest() {
		assertEquals("Hello World!", echotool.echo("Hello World!"));
	}
	
	@Test
	public void echoEmptyStringTest() {
		assertEquals("", echotool.echo(""));
	}
	
	@Test
	public void echoNullStringTest() {
		assertEquals("", echotool.echo(null));
	}
	
	@Test
	public void echoNonEnglishStringTest() {
		assertEquals("ปุ๊�?น่ารั�?", echotool.echo("ปุ๊�?น่ารั�?"));
	}

}
