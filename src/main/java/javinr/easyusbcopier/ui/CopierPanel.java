package javinr.easyusbcopier.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import javinr.easyusbcopier.Copier;
import javinr.easyusbcopier.CopierEvent;
import javinr.easyusbcopier.CopierListener;

public class CopierPanel extends JPanel implements CopierListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2023161048719167223L;
	private JProgressBar progressBar;
	private JCheckBox renameCheck;
	private JTextField toText;
	private LinkedList<File> files = new LinkedList<File>();
	private JTextField fromText;
	private int copied;
	private JList sourceList;
	/**
	 * 
	 */
	public CopierPanel() {
		super();
		setLayout(new BorderLayout());
		createContentPane();
	}

	/**
	 * @return
	 */
	private void createContentPane() {
		JLabel label;
	
		JPanel formPane = new JPanel();
		formPane.setLayout(new BoxLayout(formPane, BoxLayout.PAGE_AXIS));
	
		JPanel pane = new JPanel(new FlowLayout());
		label = new JLabel(
						Messages.getString("CopierUI.SOURCE_LABEL") + Messages.getString("CopierUI.LABEL_SEPARATOR")); //$NON-NLS-1$ //$NON-NLS-2$
		// fromText = new JTextField(20);
		// label.setLabelFor(fromText);
		sourceList = new JList();
		JScrollPane scrollSource = new JScrollPane(sourceList,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.add(label);
		// pane.add(fromText);
		pane.add(scrollSource);
		pane.add(createSourceButtonPane());
		formPane.add(pane);
	
		pane = new JPanel(new FlowLayout());
		label = new JLabel(Messages.getString("CopierUI.TO_LABEL") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		toText = new JTextField(20);
		label.setLabelFor(toText);
		pane.add(label);
		pane.add(toText);
		JButton browseButton = new JButton(new AbstractAction("Browse") { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;
	
							public void actionPerformed(ActionEvent e) {
								performToBrowse();
							}
						});
		pane.add(browseButton);
		formPane.add(pane);
	
		pane = new JPanel(new FlowLayout());
		label = new JLabel(Messages.getString("CopierUI.RENAME_LABEL")); //$NON-NLS-1$
		renameCheck = new JCheckBox();
		label.setLabelFor(renameCheck);
		pane.add(renameCheck);
		pane.add(label);
		formPane.add(pane);
	
		// Add progress bar
		int totalFiles = 100;
		JPanel progressPane = new JPanel();
		progressPane.setLayout(new BoxLayout(progressPane, BoxLayout.LINE_AXIS));
		progressPane.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(5, 5, 5, 5),
						BorderFactory.createLoweredBevelBorder()));
		progressBar = new JProgressBar(0, totalFiles);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressPane.add(progressBar);
		formPane.add(progressPane);
	
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		JButton cancelButton = new JButton(new AbstractAction(
						Messages.getString("CopierUI.OK_BUTTON")) { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;
	
							public void actionPerformed(ActionEvent e) {
								performOK();
							}
						});
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton setButton = new JButton(new AbstractAction(
						Messages.getString("CopierUI.CANCEL_BUTTON")) { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;
	
							public void actionPerformed(ActionEvent e) {
								performCancel();
							}
						});
		buttonPane.add(setButton);
	
		add(formPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
	
	}

	/**
	 * @return
	 */
	private JPanel createSourceButtonPane() {
		JPanel sourceButtonPane = new JPanel();
		sourceButtonPane.setLayout(new BoxLayout(sourceButtonPane,
						BoxLayout.PAGE_AXIS));
		JButton sourceButton = new JButton(new AbstractAction(
						Messages.getString("CopierUI.ADD_BUTTON")) { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;
	
							public void actionPerformed(ActionEvent e) {
								performSouceAdd();
							}
						});
		sourceButtonPane.add(sourceButton);
		sourceButton = new JButton(new AbstractAction(
						Messages.getString("CopierUI.REMOVE_BUTTON")) { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;
	
							public void actionPerformed(ActionEvent e) {
								performSouceRemove();
							}
						});
		sourceButtonPane.add(sourceButton);
		sourceButton = new JButton(new AbstractAction(
						Messages.getString("CopierUI.REMOVE_ALL_BUTTON")) { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;
	
							public void actionPerformed(ActionEvent e) {
								performSouceRemoveAll();
							}
						});
		sourceButtonPane.add(sourceButton);
		return sourceButtonPane;
	}

	protected void performSouceRemoveAll() {
		files.clear();
		sourceList.setListData(files.toArray());
	}

	protected void performSouceRemove() {
		Object[] filesToRemove = sourceList.getSelectedValues();
		files.removeAll(Arrays.asList(filesToRemove));
		sourceList.setListData(files.toArray());
	}

	/**
	 * 
	 */
	protected void performSouceAdd() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		int res = chooser.showOpenDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			File[] selected = chooser.getSelectedFiles();
			if (selected.length > 0) {
				files.addAll(Arrays.asList(selected));
				sourceList.setListData(files.toArray());
				// StringBuffer selectedText = new StringBuffer(
				// selected.length * 16);
				// for (File file : selected) {
				// selectedText.append(file.getAbsolutePath());
				// selectedText.append(";"); //$NON-NLS-1$
				// }
				// selectedText.deleteCharAt(selectedText.length() - 1);
				// fromText.setText(selectedText.toString());
			}
		}
	}

	/**
	 * 
	 */
	protected void performToBrowse() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		int res = chooser.showOpenDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();
			if (selected != null) {
				toText.setText(selected.getAbsolutePath());
			}
		}
	}

	/**
	 * 
	 */
	protected void performCancel() {
		System.exit(0);
	}

	/**
	 * 
	 */
	protected void performOK() {
		Copier copier = new Copier();
		copier.addListener(this);
		setSourceFiles(copier, fromText.getText());
		copier.setDestDir(new File(toText.getText()));
		copier.setRename(renameCheck.isSelected());
		copied = 0;
		progressBar.setMaximum(copier.sizeOfSourceFiles());
		progressBar.setValue(copied);
		progressBar.setString("" + copied
						+ Messages.getString("CopierUI.FILES_COPIED_MESSAGE"));
		int totalCopied = copier.copy();
		JOptionPane.showMessageDialog(
						null,
						""				+ totalCopied + Messages.getString("CopierUI.FILES_COPIED_MSG")); //$NON-NLS-1$ //$NON-NLS-2$
		progressBar.setValue(0);
		progressBar.setString("");
	}

	public void setSourceFiles(Copier copier, String sourceFiles) {
		StringTokenizer tokenizer = new StringTokenizer(sourceFiles,
						File.pathSeparator);
		while (tokenizer.hasMoreTokens()) {
			copier.addSourceFile(new File(tokenizer.nextToken()));
		}
	}

	public void copierActionPerformed(CopierEvent e) {
		progressBar.setValue(++copied);
		progressBar.setString("" + copied + " "
						+ Messages.getString("CopierUI.FILES_COPIED_MSG")); //$NON-NLS-1$ //NON-NLS-2$ //NON-NLS-3$
	}

}
