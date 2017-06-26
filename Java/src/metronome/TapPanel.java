package metronome;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TapPanel extends JPanel implements ActionListener {

	private JButton aTapButton = new JButton();
	private Model aModel;
	private boolean aTimeSelector = true;
	private long aTimeA;
	private long aTimeB;
	
	public TapPanel(Model pModel) {
		aModel = pModel;
		aTapButton.setText("Tap");
		aTapButton.addActionListener(this);
		add(aTapButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (aTimeSelector) {
			aTimeA = System.currentTimeMillis();
		} else {
			aTimeB = System.currentTimeMillis();
		}
		aTimeSelector = !aTimeSelector;
		int pNewTempo = (int) (60000/Math.abs(aTimeA - aTimeB));
		if (pNewTempo > aModel.getMinTempo() && pNewTempo < aModel.getMaxTempo()) {
			aModel.setTempo(pNewTempo);
		}
	}
}
