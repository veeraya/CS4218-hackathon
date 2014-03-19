package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;

public class CutToolTest {
	// Variables
	private ICutTool cutTool;
	
	@Before
	public void setUpOnce() throws IOException {
		cutTool = new CutTool(null);
		
	}

	@After
	public void tearDownOnce() throws IOException {
		cutTool = null;
	}

	@Test
	/**
	 * cut specified character with valid range given
	 */
	public void cutSpecfiedCharacters_ListWithInRange_CharactersReturned() {
		String list1 = "1,8-9,3-10";
		String list2 = "8-9,3-6,-1";
		String input1 = "123456789012345";
		String input2 = "123";
		String output11 = "134567890";
		String output12 = "1345689";
		String output21 = "13";
		String output22 = "13";
		assertEquals(output11, cutTool.cutSpecfiedCharacters(list1, input1));
		assertEquals(output12, cutTool.cutSpecfiedCharacters(list2, input1));

		assertEquals(output21, cutTool.cutSpecfiedCharacters(list1, input2));
		assertEquals(output22, cutTool.cutSpecfiedCharacters(list2, input2));
	}

	@Test
	/**
	 * cut specified character with out of range given
	 */
	public void cutSpecfiedCharacters_ListOutOfRange_CharactersReturned() {
		String list1 = "1,8-9,3-16";
		String list2 = "8-19,3-6,-1";
		String input1 = "123456789012345";
		String input2 = "123";
		String output11 = "13456789012345";
		String output12 = "1345689012345";
		String output21 = "13";
		String output22 = "13";
		assertEquals(output11, cutTool.cutSpecfiedCharacters(list1, input1));
		assertEquals(output12, cutTool.cutSpecfiedCharacters(list2, input1));

		assertEquals(output21, cutTool.cutSpecfiedCharacters(list1, input2));
		assertEquals(output22, cutTool.cutSpecfiedCharacters(list2, input2));
	}

	@Test
	/**
	 * cut specified field with delimiter and valid range list
	 */
	public void cutSpecifiedCharactersUseDelimiter_ListWithInRange_CharactersReturned() {
		String list1 = "1,8-9,3-15";
		String list2 = "12-,3-6,-1";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 3 4 5 6 7 8 9 10 11 12 13 14 15";
		String output2 = "1 3 4 5 6 12 13 14 15 16 17";

		assertEquals(output1,
				cutTool.cutSpecifiedCharactersUseDelimiter(list1, " ", input1));
		assertEquals(output2,
				cutTool.cutSpecifiedCharactersUseDelimiter(list2, " ", input1));
	}

	@Test
	/**
	 * cut specified field with delimiter and out of range list
	 */
	public void cutSpecifiedCharactersUseDelimiter_ListOutOfRange_CharactersReturned() {
		String list1 = "1,8-9,3-100";
		String list2 = "12-,3-6,-1";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output2 = "1 3 4 5 6 12 13 14 15 16 17";

		assertEquals(output1,
				cutTool.cutSpecifiedCharactersUseDelimiter(list1, " ", input1));
		assertEquals(output2,
				cutTool.cutSpecifiedCharactersUseDelimiter(list2, " ", input1));
	}

	@Test
	/**
	 * cut specified field with delimiter and list with single values
	 */
	public void cutSpecifiedCharactersUseDelimiter_ListWithSingleNum_CharactersReturned() {
		String list1 = "15,12,14,16,200,29,1";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 12 14 15 16";

		assertEquals(output1,
				cutTool.cutSpecifiedCharactersUseDelimiter(list1, " ", input1));
	}

	@Test
	/**
	 * cut specified field with delimiter and list with single values
	 */
	public void cutSpecifiedCharactersUseDelimiter_ListAllOutofRangeNum_CharactersReturned() {
		String list1 = "200,211,231";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "";
		assertEquals(output1,
				cutTool.cutSpecifiedCharactersUseDelimiter(list1, " ", input1));
	}

	@Test
	/**
	 * cut specified field with delimiter and list with single and out of
	range values
	 */
	public void cutSpecifiedCharactersUseDelimiter_DelimNotExistNum_CharactersReturned() {
		String list1 = "15,12,14,16,200,29,1";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";

		assertEquals(output1,
				cutTool.cutSpecifiedCharactersUseDelimiter(list1, "d", input1));
	}

	@Test
	/**
	 * cut specified field with delimiter and list with single and out of
	range values and tab contain in input
	 */
	public void cutSpecifiedCharactersUseDelimiter_DelimInputContainTab_CharactersReturned() {
		String list1 = "15,12,14,16,200,29,1";
		String input1 = "1 2 	 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 11 13 14 15";

		assertEquals(output1,
				cutTool.cutSpecifiedCharactersUseDelimiter(list1, " ", input1));
	}

}
