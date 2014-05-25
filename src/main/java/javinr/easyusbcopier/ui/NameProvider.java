package javinr.easyusbcopier.ui;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Manages the names that will recieve the files.
 * @author javinr
 *
 */
public class NameProvider {

	/**
	 * the directory where the files are being copied
	 */
	private File destDir;
	/**
	 * Indicates if files are renamed or not. If <code>true</code> files
	 * are renamed, if <code>false</code> not.
	 */
	private boolean rename;
	/**
	 * unique number to rename files
	 */
	private int index = 1;
	/**
	 * unique number formatter
	 */
	private DecimalFormat format;

	/**
	 * Constructor for objects of type <code>NameProvider</code>.
	 * @param destDir the directory where the files are being copied
	 * @param rename indicates if files are renamed or not
	 */
	public NameProvider(File destDir, boolean rename) {
		this.destDir = destDir;
		this.rename = rename;
		// TODO Recibir el total de archivos y hacer que el formato añada los 0s necesarios
		format = new DecimalFormat("0");
	}

	/**
	 * Returns a name for the file. If <code>rename</code> is set to 
	 * <code>false</code> it returns the previous name of the file.
	 * Otherwise is return a new name with the following format:
	 * <Destination_directory_name>_<unique_number>
	 * @param oldName previous name of the file
	 * @return a name for the file
	 */
	public String nextName(String oldName) {
		if (rename) {
			return destDir.getName() + "_" + format.format(index++);
		} else {
			return oldName;
		}
	}

}
