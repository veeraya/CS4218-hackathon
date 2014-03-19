/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 *
 */
public class LsTool extends ATool implements ILsTool {
	public final static int DIRECTORY_ERROR_CODE = 2;
	public final static String DIRECTORY_ERROR_MSG = "No such file or directory";

	/**
	 * 
	 * @param arguments
	 *            arguments to passed in to execute the command. The type of
	 *            arguments allow are file names and directory (both relative
	 *            and absolute).
	 */
	public LsTool(final String[] arguments) {
		super(arguments);
	}

	/**
	 * Return the list of file in the specified directory if the directory is
	 * valid, <i>null</i> otherwise
	 * 
	 * @param directory
	 *            the file directory the list of file will be retrieved
	 * 
	 * @return the list of file in the specified directory if the directory is
	 *         valid, <i>null</i> otherwise
	 */
	@Override
	public List<File> getFiles(final File directory) {
		ArrayList<File> fileList = new ArrayList<File>();

		//Error checking
		/*
		 * Error 1: Empty directory
		 * Error 2: Non-existing directory
		 */
		if( directory == null ||
				!directory.exists()){			//Invalid file/ directory path
			fileList = null;
		}
		else if (directory.isDirectory()){	//Directory
			final File[] files = directory.listFiles();

			for(int i = 0; i < files.length; i++){
				fileList.add(files[i]);
			}
		}
		else if(directory.isFile()){		//File
			fileList.add(directory);
		}

		return fileList;
	}

	/**
	 * Return the list of file and folder specified by list (including the
	 * hidden files), the list is separated by the single character '|'
	 * 
	 * @param files
	 *            the list of file which the file and folder name to be
	 *            extracted
	 * 
	 * @return the list of file and folder specified by list (including the
	 *         hidden files), the list is separated by the line separator
	 */
	@Override
	public String getStringForFiles(final List<File> files) {
		StringBuilder fileList = new StringBuilder();

		//Error: Null files object
		if(files != null){
			File file;

			for(int i = 0; i < files.size(); i++){
				file = files.get(i);

				if(file.exists()){
					fileList.append(files.get(i).getName());
					fileList.append(System.lineSeparator());
				}
			}
		}
		if(fileList.length() > 0){
			fileList.setLength(fileList.lastIndexOf(System.lineSeparator()));
		}
		return fileList.toString();
	}

	/**
	 * Executes the ls tool with arguments provided in the constructor, the tool
	 * will list the file and folder in the current directory in a one file/
	 * folder per row format
	 * 
	 * @param workingDir
	 *            the current working directory path
	 * 
	 * @param stdin
	 *            the additional input from the stdin
	 * 
	 * @return the list of file and folder in the current directory
	 */
	@Override
	public String execute(final File workingDir, String stdin) {
		String output = null; 

		if(this.args == null){
			setStatusCode(DIRECTORY_ERROR_CODE);
			output = DIRECTORY_ERROR_MSG;
		}
		else if("".equals(this.args[0])){			//List file for present working directory
			if(	 workingDir == null || 
					!workingDir.exists() || 
					!workingDir.isDirectory()){
				setStatusCode(DIRECTORY_ERROR_CODE);
				output = DIRECTORY_ERROR_MSG;
			}
			else{
				output = listDirectory(workingDir);
			}
		}
		else if(this.args.length == 1) {	//Only one file/ directory
			final File directory = new File (this.args[0]);
			output = listDirectory(directory);
		}
		else{								//At least two or more file/ directory
			File directory;
			output = "";

			for(int i =0; i < this.args.length; i++){	
				directory = new File (this.args[i]);

				final String prefixStr = this.args[i].concat(":"+ System.lineSeparator()); 
				final String result = listDirectory(directory);

				if (!"".equals(output)) {
					output = output + System.lineSeparator()
							+ System.lineSeparator();
				}
				if(result.equals(DIRECTORY_ERROR_MSG)){	//Error in listing directory
					/*
					 * FIXME: to fix the behaviour that the order of the error
					 * message needed to be preserved
					 */
					output = prefixStr + DIRECTORY_ERROR_MSG + output;
				}
				else {	//No error in listing directory
					output = output + prefixStr + result;
				}
			}
		}
		return output;
	}

	private String listDirectory(File directory){
		String directoryListing;
		final List<File> filesList = getFiles(directory);

		if(filesList == null){
			setStatusCode(DIRECTORY_ERROR_CODE);
			directoryListing = DIRECTORY_ERROR_MSG;
		}
		else{
			directoryListing = getStringForFiles(filesList);
		}
		return directoryListing;
	}
}