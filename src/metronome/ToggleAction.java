package metronome;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

@SuppressWarnings("serial")
public class ToggleAction extends AbstractAction implements Action {

	private Model aModel;
	
	public ToggleAction(Model pModel) {
		aModel = pModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		aModel.togglePlayback();
	}

}
