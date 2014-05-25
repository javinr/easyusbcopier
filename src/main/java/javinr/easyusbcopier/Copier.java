/*
 * 
 */
package javinr.easyusbcopier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javinr.easyusbcopier.ui.NameProvider;

import org.apache.commons.io.CopyUtils;

/**
 * Copier is the class that manages the copy of a colection of files to
 * a directory, renaming them if necessary.
 * @author javinr
 *
 */
public class Copier {
	/**
	 * the files that will be copied
	 */
	private ArrayList<File> sourceFiles = new ArrayList<File>();

	/**
	 * the destination directory 
	 */
	private File destDir;

	/**
	 * Specifies if files will be renamed before copy. If true files will 
	 * be renamed to the destination directory name plus a number id.  
	 */
	private boolean rename = false;

	/**
	 *  
	 */
	private ArrayList<CopierListener> listeners = new ArrayList<CopierListener>();

	/**
	 * Sole constructor.
	 */
	public Copier() {
		super();
	}

	/**
	 * Copies the source files to the destination directory.
	 * @return the number of files copied
	 */
	public int copy() {
		if ((sourceFiles != null) && (destDir != null)) {
			int copied = 0;
			NameProvider names = new NameProvider(destDir, rename);
			for (File file : sourceFiles) {
				if (copyFile(names, file)) {
					copied++;
				}
			}
			return copied;
		} else {
			return 0;
		}
	}

	/**
	 * Copies one file to the destination directory. Copies one file to the
	 * directory specified by <a href="#destDir">destDir</a>. It copies the
	 * file with the name provided by names parameter. Listeners are notified
	 * of this copy.
	 * 
	 * @param names the provider of the file's name
	 * @param file the file to copy
	 * @return <code>true</code> if the file is copied, <code>false</code>
	 * otherwise
	 * @see NameProvider
	 */
	protected boolean copyFile(NameProvider names, File file) {
		File outFile = new File(destDir, names.nextName(file.getName()));
		try {
			CopyUtils.copy(new FileInputStream(file), new FileOutputStream(outFile));
			fireEvent(new CopierEvent());
			return true;
		} catch (FileNotFoundException ex) {
			// TODO Treat FileNotFoundException
			ex.printStackTrace();
			return false;
		} catch (IOException ex) {
			// TODO Treat IOException
			ex.printStackTrace();
			return false;
		}
	}

	protected void fireEvent(CopierEvent event) {
		for (CopierListener listener : listeners) {
			listener.copierActionPerformed(event);
		}
	}

	public void setDestDir(File destDir) {
		if (destDir == null) {
			throw new NullPointerException();
		}
		if (!destDir.isDirectory()) {
			throw new IllegalArgumentException();
		}
		this.destDir = destDir;
	}

	public boolean isRename() {
		return rename;
	}

	public void setRename(boolean rename) {
		this.rename = rename;
	}

	public boolean addListener(CopierListener o) {
		return listeners.add(o);
	}

	public boolean removeListener(Object o) {
		return listeners.remove(o);
	}

	public boolean addSourceFile(File o) {
		return sourceFiles.add(o);
	}

	public boolean removeSourceFile(Object o) {
		return sourceFiles.remove(o);
	}

	public void clear() {
		sourceFiles.clear();
	}

	public int sizeOfSourceFiles() {
		return sourceFiles.size();
	}

}
