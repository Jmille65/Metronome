package metronome;

import java.awt.FileDialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


@SuppressWarnings("serial")
public class Metronome extends JFrame {
	
private static final String TOGGLE = "toggle";
	
	public static void main(String[] args) {
		new Metronome();
	}
	
	public Metronome() {
		
		// Initializing Model and Swing UI
		Model aModel = new Model();
		setTitle("Metronome");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		setLayout(new GridLayout(4, 1));
		
		StartButton s = new StartButton(aModel);
		
		s.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), TOGGLE);
		s.getActionMap().put(TOGGLE, new ToggleAction(aModel));
		
		
		// Building Panels
		add(new SliderPanel(aModel));
		add(new IntegerPanel(aModel));
		add(new TapPanel(aModel));
		add(s);
		
		
		// Building the Menu Bar
		JMenuBar pMenuBar = new JMenuBar();
		setJMenuBar(pMenuBar);
		JMenu pOptionsMenu = new JMenu("Options");
		pMenuBar.add(pOptionsMenu);
		JMenuItem pOpenNewFile = new JMenuItem("New Sound");
		pOptionsMenu.add(pOpenNewFile);
		
		pOpenNewFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fc = new FileDialog(Metronome.this, "Open", FileDialog.LOAD);
				fc.setDirectory(System.getProperty("user.dir") + "/src/metronome/SoundFiles");
				fc.setFilenameFilter(new FilenameFilter() {

					@Override
					public boolean accept(File f, String name) {
						return name.endsWith(".wav");
					}
					
				});
				fc.setVisible(true);
				fc.setAlwaysOnTop(true);
				String returnVal = fc.getDirectory() + fc.getFile();
				if (!returnVal.equals("nullnull")) {
					System.out.println(returnVal);
					if (aModel.getRunState()) {
						aModel.stopPlayback();
						aModel.closeFile();
						aModel.loadFile(new File(returnVal));
						aModel.startPlayback();
					} else {
						aModel.closeFile();
						aModel.loadFile(new File(returnVal));
					}
					
				}
			}
			
		});
		
		pack(); 
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}
}
