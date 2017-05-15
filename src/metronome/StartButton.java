package metronome;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StartButton extends JPanel implements StartToggle {

	private Model aModel;
	private JButton aButton = new JButton();
	
	public StartButton(Model pModel) {
		aModel = pModel;
		aButton.setAction(new ToggleAction(aModel));

		aModel.addStartToggle(this);
		setButtonText();
		add(aButton);
	}
	
	@Override
	public void togglePlayback() {
		aModel.togglePlayback();
	}

	private void setButtonText() {
		if (aModel.getRunState()) {
			aButton.setText("Stop");
		} else {
			aButton.setText("Start");
		}
	}
	
	public void newState() {
		setButtonText();
	}
}
