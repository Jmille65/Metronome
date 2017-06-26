package MetronomeApplet;

import java.awt.GridLayout;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import metronome.IntegerPanel;
import metronome.Model;
import metronome.SliderPanel;
import metronome.StartButton;
import metronome.TapPanel;
import metronome.ToggleAction;


@SuppressWarnings("serial")
public class Applet extends JApplet {
	
private static final String TOGGLE = "toggle";
	
	public static void main(String[] args) {
		new Applet();
	}
	
	public Applet() {
		
		// Initializing Model and Swing UI
		Model aModel = new Model();
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

		setVisible(true);
	}
}
