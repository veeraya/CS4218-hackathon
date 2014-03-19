package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CommTool extends ATool implements ICommTool {
	private static final String TOOL_NAME = "comm: ";
	private static final String FILE_NOT_SORTED_ERROR = " is not in sorted order";

	private static final String OPTION_ERROR = TOOL_NAME + "unknown option -- ";
	private static final int OPTION_ERROR_CODE = 1;

	private static final String MISSING_OPERAND_ERROR = TOOL_NAME + "missing operand ";
	private static final int MISSING_OPERAND_ERROR_CODE = 2;

	private static final String EXTRA_OPERAND_ERROR = TOOL_NAME + "extra operand ";
	private static final int EXTRA_OPERAND_ERROR_CODE = 3;

	private static final String FILE_NOT_FOUND_ERROR = ": No such file or directory";
	private static final int FILE_NOT_FOUND_ERROR_CODE = 4;

	private static final String SUGGEST_SEE_HELP = "Try comm '-help' for more information";

	private StringBuilder compareFileResult;
	private File file1;
	private File file2;
	private BufferedReader file1Reader = null;
	private BufferedReader file2Reader = null;

	private String file1CurrentLine;
	private String file2CurrentLine;

	private String file1PreviousLine = "";
	private String file2PreviousLine = "";

	public CommTool(final String[] arguments) {
		super(arguments);
		this.compareFileResult = new StringBuilder();
	}

	/**
	 * Compare 2 files line by line.
	 * 
	 * @param input1
	 *            file path to the first file
	 * @param input2
	 *            file path to the second file
	 * 
	 * @return 3-column output. Column 1 contains lines unique to input1, column
	 *         2 contains lines unique to input2, and column 3 contains lines
	 *         common to both files.
	 */
	@Override
	public String compareFiles(final String input1, final String input2) {
		this.file1 = new File(input1);
		this.file2 = new File(input2);

		try {
			file1Reader = new BufferedReader(new FileReader(file1));
		} catch (FileNotFoundException e) {
			file1 = null;
			setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
			return TOOL_NAME + input1 + FILE_NOT_FOUND_ERROR;
		}

		try {
			file2Reader = new BufferedReader(new FileReader(file2));
		} catch (FileNotFoundException e) {
			file2 = null;
			setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
			return TOOL_NAME + input2 + FILE_NOT_FOUND_ERROR;
		}

		boolean isFile1Sorted = true;
		boolean isFile2Sorted = true;

		try {
			this.file1CurrentLine = file1Reader.readLine();
			this.file2CurrentLine = file2Reader.readLine();

			while (file1CurrentLine != null && file2CurrentLine != null) {
				// Checking of sorted files
				isFile1Sorted = isFile1Sorted
						&& checkForSortedFile(this.file1PreviousLine,
								this.file1CurrentLine, 1);
				isFile2Sorted = isFile2Sorted
						&& checkForSortedFile(this.file2PreviousLine,
								this.file2CurrentLine, 2);

				compareCurrentLine();
			}
			readRemainingFileContent(true, true, isFile1Sorted, isFile2Sorted);
			file1Reader.close();
			file2Reader.close();
	} catch (IOException e1) {
		setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
		compareFileResult.setLength(0);
		compareFileResult.append(TOOL_NAME + input1 + FILE_NOT_FOUND_ERROR);
	}

	return compareFileResult.toString();
}

@Override
public String compareFilesCheckSortStatus(final String input1, final String input2) {
	this.file1 = new File(input1);
	this.file2 = new File(input2);

	try {
		file1Reader = new BufferedReader(new FileReader(file1));
	} catch (FileNotFoundException e) {
		setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
		return TOOL_NAME + input1 + FILE_NOT_FOUND_ERROR;
	}

	try {
		file2Reader = new BufferedReader(new FileReader(file2));
	} catch (FileNotFoundException e) {
		try {
			file1Reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
		return TOOL_NAME + input2 + FILE_NOT_FOUND_ERROR;
	}

	try {
		this.file1CurrentLine = file1Reader.readLine();
		this.file2CurrentLine = file2Reader.readLine();

		while (file1CurrentLine != null && file2CurrentLine != null) {
			// Checking of sorted files
			if (!checkForSortedFile(file1PreviousLine, file1CurrentLine, 1)
					|| !checkForSortedFile(file2PreviousLine,
							file2CurrentLine, 2)) {
				file1Reader.close();
				file2Reader.close();
				return this.compareFileResult.toString();
			}

			compareCurrentLine();
		}

		readRemainingFileContent(true, false, true, true);

		file1Reader.close();
		file2Reader.close();

	} catch (IOException e) {
		setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
		return TOOL_NAME + input2 + FILE_NOT_FOUND_ERROR;
	}

	return this.compareFileResult.toString();
}

@Override
public String compareFilesDoNotCheckSortStatus(final String input1, final String input2) {
	this.file1 = new File(input1);
	this.file2 = new File(input2);

	try {
		file1Reader = new BufferedReader(new FileReader(file1));
	} catch (FileNotFoundException e) {
		setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
		return TOOL_NAME + input1 + FILE_NOT_FOUND_ERROR;
	}

	try {
		file2Reader = new BufferedReader(new FileReader(file2));
	} catch (FileNotFoundException e) {
		try {
			file1Reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
		return TOOL_NAME + input2 + FILE_NOT_FOUND_ERROR;
	}

	try {
		this.file1CurrentLine = file1Reader.readLine();
		this.file2CurrentLine = file2Reader.readLine();

		while (file1CurrentLine != null && file2CurrentLine != null) {
			compareCurrentLine();
		}

		readRemainingFileContent(false, false, false, false);

		file1Reader.close();
		file2Reader.close();

	} catch (IOException e1) {
		setStatusCode(FILE_NOT_FOUND_ERROR_CODE);
		return TOOL_NAME + input2 + FILE_NOT_FOUND_ERROR;
	}

	return compareFileResult.toString();
}

protected void compareCurrentLine() throws IOException {
	// Line compare
	if (file1CurrentLine.compareTo(file2CurrentLine) == 0) {
		compareFileResult.append("\t\t" + file1CurrentLine
				+ System.lineSeparator());

		file1PreviousLine = file1CurrentLine;
		file1CurrentLine = file1Reader.readLine();

		file2PreviousLine = file2CurrentLine;
		file2CurrentLine = file2Reader.readLine();
	} else if (file1CurrentLine.compareTo(file2CurrentLine) < 0) {
		compareFileResult.append(file1CurrentLine + System.lineSeparator());

		file1PreviousLine = file1CurrentLine;
		file1CurrentLine = file1Reader.readLine();

	} else if (file1CurrentLine.compareTo(file2CurrentLine) > 0) {
		compareFileResult.append("\t" + file2CurrentLine
				+ System.lineSeparator());

		file2PreviousLine = file2CurrentLine;
		file2CurrentLine = file2Reader.readLine();
	}
}

protected void readRemainingFileContent(final boolean checkIfFileIsSorted,
		final boolean continueIfFileIsNotSorted, boolean isFile1Sorted,
		boolean isFile2Sorted) throws IOException {
	if (file2CurrentLine == null) { // File 1 still has some more content

		while (file1CurrentLine != null) {
			if (checkIfFileIsSorted) {
				isFile1Sorted = isFile1Sorted
						&& checkForSortedFile(this.file1PreviousLine,
								this.file1CurrentLine, 1);
				if (!continueIfFileIsNotSorted && !isFile1Sorted) {
					return;
				}
			}
			compareFileResult.append(file1CurrentLine
					+ System.lineSeparator());
			file1PreviousLine = file1CurrentLine;
			file1CurrentLine = file1Reader.readLine();
		}

	} else if (file1CurrentLine == null) { // File 2 still has some more
		// content

		while (file2CurrentLine != null) {
			if (checkIfFileIsSorted) {
				isFile2Sorted = isFile2Sorted
						&& checkForSortedFile(this.file2PreviousLine,
								this.file2CurrentLine, 2);
				if (!continueIfFileIsNotSorted && !isFile2Sorted) {
					return;
				}
			}
			compareFileResult.append("\t" + file2CurrentLine
					+ System.lineSeparator());
			file2PreviousLine = file2CurrentLine;
			file2CurrentLine = file2Reader.readLine();
		}

	}
}

protected boolean checkForSortedFile(final String previousLine, final String currentLine,
		final int fileNum) {
	if (previousLine.compareTo(currentLine) > 0) {
		compareFileResult.append(TOOL_NAME + "file " + fileNum
				+ FILE_NOT_SORTED_ERROR + System.lineSeparator());
		return false;
	} else {
		return true;
	}
}

@Override
public String getHelp() {
	StringBuilder output = new StringBuilder();
	File file = new File("help_files" + File.separator + "comm_help");
	
	BufferedReader br = null;
	try {
		br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
		   output.append(line + System.lineSeparator());
		}
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	return output.toString().trim();
}

@Override
public String execute(final File workingDir, final String stdin) {
	// Help override all other execution
	for (int i = 0; i < args.length; i++) {
		if ("-help".equals(args[i])) {
			setStatusCode(0);
			return getHelp();
		}
	}

	String comparsionResult = "";

	comparsionResult = checkForInvalidOption();
	if(!"".equals(comparsionResult)){
		return comparsionResult;
	}

	//Assume no more invalid option from this point onwards
	final List<Integer> fileInputPositions = new ArrayList<Integer>();
	String lastOption = null;

	//Check for number of non-option file inputs
	for(int i = 0; i < args.length; i++){
		if(args[i].startsWith("-")){	//Option
			lastOption = args[i];
		}
		else{	//Non-option file input
			fileInputPositions.add(i);
		}
	}

	switch(fileInputPositions.size()){
	case 0:
		setStatusCode(MISSING_OPERAND_ERROR_CODE);
		comparsionResult = MISSING_OPERAND_ERROR + System.lineSeparator()
				+ SUGGEST_SEE_HELP;
		break;

	case 1:
		setStatusCode(MISSING_OPERAND_ERROR_CODE);
		final String onlyFileInput = args[fileInputPositions.get(0)];
		comparsionResult = MISSING_OPERAND_ERROR + "after '"
				+ onlyFileInput + "'" + System.lineSeparator()
				+ SUGGEST_SEE_HELP;
		break;

	case 2:	//Correct one
		final String fileInput1 = args[fileInputPositions.get(0)];
		final String fileInput2 = args[fileInputPositions.get(1)];

		if(lastOption == null){
			comparsionResult = compareFiles(fileInput1, fileInput2);
		}
		else if("-c".equals(lastOption)){
			comparsionResult = compareFilesCheckSortStatus(fileInput1, fileInput2);
		}
		else if("-d".equals(lastOption)){
			comparsionResult = compareFilesDoNotCheckSortStatus(fileInput1, fileInput2);
		}
		break;

	default:
		final String extraFile = args[fileInputPositions.get(2)];
		setStatusCode(EXTRA_OPERAND_ERROR_CODE);
		comparsionResult = EXTRA_OPERAND_ERROR + "'" + extraFile + "'"
				+ System.lineSeparator() + SUGGEST_SEE_HELP;
		break;

	}
	return comparsionResult;
}

protected String checkForInvalidOption(){
	String result = "";
	
	for(int i = 0; i<args.length; i++){
		if (args[i].startsWith("-")) {
			if (!"-c".equals(args[i]) && !"-d".equals(args[i])) {
				setStatusCode(OPTION_ERROR_CODE);
					result = OPTION_ERROR + args[i].substring(1)
							+ System.lineSeparator() + SUGGEST_SEE_HELP;
			}
		}
	}
	return result;
}
}
