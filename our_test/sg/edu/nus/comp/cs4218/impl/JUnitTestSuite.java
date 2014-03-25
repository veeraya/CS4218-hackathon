package sg.edu.nus.comp.cs4218.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sg.edu.nus.comp.cs4218.impl.fileutils.*;
import sg.edu.nus.comp.cs4218.impl.extended1.*;
import sg.edu.nus.comp.cs4218.impl.extended2.*;

/**
 *Test suite to run all unit and integration test
 */
@RunWith(Suite.class)
@SuiteClasses({OurShellTest.class,OurCATToolTest.class, OurCOPYToolTest.class, OurDELETEToolTest.class, 
	OurMOVEToolTest.class, OurCOMMToolTest.class,
	OurCUTToolTest.class, OurPASTEToolTest.class, OurSORTToolTest.class, OurUNIQToolTest.class, OurWCToolTest.class,
	OurPIPINGToolTest.class, OurGREPToolTest.class, ShellIntegrationTest.class, 
	ShellMoreIntegrationTest.class, ShellFunctionalIntegrationTest.class, ShellErrorIntegrationTest.class})
public class JUnitTestSuite {
 
}