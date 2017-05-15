package metronome;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SliderPanel extends JPanel implements Observer, ChangeListener {

	private JSlider aSlider = new JSlider();
	private Model aModel;
	
	public SliderPanel(Model pModel)
	{
		aModel = pModel;
		aModel.addObserver(this);
		aSlider.setMinimum(aModel.getMinTempo());
		aSlider.setMaximum(aModel.getMaxTempo());
		aSlider.setPaintTicks(true);
		aSlider.setMajorTickSpacing(30);
		aSlider.setSnapToTicks(false);
		aSlider.setValue(aModel.getTempo());
		aSlider.addChangeListener(this);
		add(aSlider);
	}
	
	@Override
	public void newTempo() {
		aSlider.setValue(aModel.getTempo());
	}

	public void stateChanged(ChangeEvent e) {
		aModel.setTempo(aSlider.getValue());
	}
}
