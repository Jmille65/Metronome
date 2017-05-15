package metronome;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class Model {
	private Thread aPlayerThread;
	protected int aTempo;
	private ArrayList<Observer> aObservers = new ArrayList<Observer>();
	private ArrayList<StartToggle> aToggle = new ArrayList<StartToggle>();
	private File aFile;
	protected boolean aRunning = false;
	private int aMinTempo;
	private int aMaxTempo;
	private Clip aClip;
	private Preferences aPrefs;
	
	private static final String FILE_PREF = "file_pref";
	private static final String TEMPO_PREF = "tempo_pref";
	
	private void notifyObservers() {
		for (Observer p : aObservers) {
			p.newTempo();
		}
	}
	
	public void addObserver(Observer pObserver) {
		aObservers.add(pObserver);
	}
	
	public int getTempo() {
		return aTempo;
	}
	
	public void setTempo(int pNewTempo) {
		aTempo = pNewTempo;
		notifyObservers();
		aPrefs.putInt(TEMPO_PREF, pNewTempo);
	}
	
	public Model() {
		
		aPrefs = Preferences.userNodeForPackage(Model.class);
		aTempo = aPrefs.getInt(TEMPO_PREF, 90);
		String fileStringPath = aPrefs.get(FILE_PREF, System.getProperty("user.dir") + "/src/metronome/SoundFiles/4d.wav");

		aMinTempo = 30;
		aMaxTempo = 240;
		aFile = new File(fileStringPath);
		try {
			aClip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		loadFile(aFile);
	}

	public void loadFile(File pFile) {
		aFile = pFile;

		try {
			AudioInputStream pInputStream = AudioSystem.getAudioInputStream(aFile);
			aClip.open(pInputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		aPrefs.put(FILE_PREF, aFile.getAbsolutePath());
	}
	
	public void closeFile() {
		aClip.close();
	}
	
	public void startPlayback() {
		aPlayerThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					
					while (aRunning == true) {

						aClip.start();

						Thread.sleep(60000/aTempo);
						aClip.stop();
						aClip.setFramePosition(0);
					}
				} catch (Exception e) {}	
			}
			
		});
		aRunning = true;
		notifyStartToggles();
		aPlayerThread.start();
	}

	public void stopPlayback() {
		if (aPlayerThread.isAlive()) aPlayerThread.interrupt();
		aRunning = false;
		notifyStartToggles();
	}

	public int getMinTempo() {
		return aMinTempo;
	}

	public void setMinTempo(int aMinTempo) {
		this.aMinTempo = aMinTempo;
	}

	public int getMaxTempo() {
		return aMaxTempo;
	}

	public void setMaxTempo(int aMaxTempo) {
		this.aMaxTempo = aMaxTempo;
	}

	public void addStartToggle(StartToggle pStartToggle) {
		aToggle.add(pStartToggle);
	}
	
	public void togglePlayback() {
		if (aRunning) {
			stopPlayback();
		} else {
			startPlayback();
		}
	}
	
	private void notifyStartToggles() {
		for (StartToggle t : aToggle) {
			t.newState();
		}
	}
	
	public boolean getRunState() {
		return aRunning;
	}
}
