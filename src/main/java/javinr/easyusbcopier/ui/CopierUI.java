/**
 * 
 */
package javinr.easyusbcopier.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author javinr
 * 
 */
public class CopierUI {
	private static CopierUI copierUI = null;

	public static CopierUI getInstance() {
		if (copierUI == null) {
			copierUI = new CopierUI();
		}

		return copierUI;
	}

	private JFrame frame = null;

	private static final long serialVersionUID = 3979272443112207669L;

	protected CopierUI() {
		createGUI();
	}

	private void createGUI() {
		// Create and set up the window.
		if (frame == null) {
			// Make sure we have nice window decorations.
			JFrame.setDefaultLookAndFeelDecorated(true);

			frame = new JFrame(Messages.getString("CopierUI.TITLE")); //$NON-NLS-1$
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.setContentPane(new CopierPanel());
		}
	}

	private static TrayIcon createTrayMenu() {

		String tooltip = Messages.getString("CopierUI.TOOLTIP");
		PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("icon.ico"), tooltip, popup);

		// Create a pop-up menu components
		MenuItem exitItem = new MenuItem(Messages.getString("CopierUI.EXIT_MENU"));
		MenuItem openItem = new MenuItem("CopierUI.OPEN_MENU");
		
		exitItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SystemTray.getSystemTray().remove(trayIcon);
				System.exit(0);
			}
		});
		
		openItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getInstance().restore();
			}
		});
		// Add components to pop-up menu
		popup.add(openItem);
		popup.addSeparator();
		popup.add(exitItem);

		
		
		return trayIcon;
	}

	private static Image createImage(String path) {
		URL imageURL = CopierUI.class.getClassLoader().getResource(path);
        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, Messages.getString("CopierUI.TOOLTIP"))).getImage();
        }
	}

	public static void main(String[] args) {
		createAndShowGUI();
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

	}

	private static void createAndShowGUI() {
		if (!SystemTray.isSupported()){
			System.out.println();
			System.exit(-1);
		}
		SystemTray tray = SystemTray.getSystemTray();
		TrayIcon trayIcon = CopierUI.createTrayMenu();
		try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
	}

	public void minimize() {
		frame.setVisible(false);
	}

	public void restore() {
		frame.pack();
		frame.setVisible(true);
	}

}
