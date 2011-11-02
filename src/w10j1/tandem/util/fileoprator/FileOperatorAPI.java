package w10j1.tandem.util.fileoprator;

public interface FileOperatorAPI {

	/**
	 * File initialization.
	 */
	public abstract void createFile();

	/**
	 * File read, read the whole file.
	 * 
	 * @return readStr
	 */
	public abstract String readFile();

	/**
	 * Append to file with newStr
	 * 
	 * @param newStr
	 *            the string to be appended to file
	 */
	public abstract void appendToFile(String newStr);

	/**
	 * Write to file with newStr
	 * 
	 * @param newStr
	 *            the string to be written to file (rewrite)
	 */
	public abstract void writeFile(String newStr);

	/**
	 * Replace the first line with the specified string with a new string
	 * provided.
	 * 
	 * @param oldStr
	 *            content to search
	 * @param newStr
	 *            content to replace
	 */
	public abstract void replaceBy(String oldStr, String newStr);

}