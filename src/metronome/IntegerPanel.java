package metronome;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class IntegerPanel extends JPanel implements Observer, ActionListener {

	private Model aModel;
	private JTextField aText = new JTextField(3);
	
	public IntegerPanel(Model pModel) {
		aModel = pModel;
		aModel.addObserver(this);
		aText.setText(new Integer(aModel.getTempo()).toString());
		aText.addActionListener(this);
		add(aText);
		add(new JLabel("BPM"));
	}
	
	@Override
	public void newTempo() {
		aText.setText(new Integer(aModel.getTempo()).toString());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (Integer.parseInt(aText.getText()) < aModel.getMinTempo()) aModel.setTempo(aModel.getMinTempo());
		else if (Integer.parseInt(aText.getText()) > aModel.getMaxTempo()) aModel.setTempo(aModel.getMaxTempo());
		else aModel.setTempo(Integer.parseInt(aText.getText()));
	}
}
