package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CatTool extends ATool implements ICatTool {

	public static final int ERR_CODE_FILE_NOT_FOUND = 1;
	public static final String ERR_MSG_FILE_NOT_FOUND = "Error: cat cannot find %s."
			+ System.lineSeparator()
			+ "Please check that the file name is correct."
			+ System.lineSeparator();

	public static final int ERR_CODE_READ_FAILURE = 2;
	public static final String ERR_MSG_READ_FAILURE = "Error: Cannot read %s."
			+ System.lineSeparator()
			+ "Ensure that the file is not used by other programs and you have proper permission."
			+ System.lineSeparator();

	public CatTool(String[] arguments) {
		super(arguments);
	}

	public CatTool() {
		super(null);
	}

	@Override
	public String getStringForFile(File toRead) {
		StringBuilder fileContent = new StringBuilder();
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(toRead), "UTF8"));
			String line = in.readLine();

			while (line != null) {
				fileContent.append(line + System.lineSeparator());
				line = in.readLine();
			}
			in.close();
			
		} catch (FileNotFoundException e ) {
			setStatusCode(ERR_CODE_FILE_NOT_FOUND);
			return ERR_MSG_FILE_NOT_FOUND;
			
		} catch (IOException e) {
			setStatusCode(ERR_CODE_READ_FAILURE);
			return ERR_MSG_READ_FAILURE;
			
		} 

		return fileContent.toString();
	}

	@Override
	public String execute(File workingDir, String stdin) {
		if (args == null) {
			if (stdin == null) {
				return "";
			} else {
				return stdin;
			}
		}

		StringBuilder sb = new StringBuilder();

		for (String arg : args) {
			if (arg.equals("-")) {
				sb.append(stdin);
			} else {
				File file = new File(arg);
				String fileContent = getStringForFile(file);
				sb.append(fileContent);
			}
		}

		return sb.toString();
	}
}
