package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

import game.SoundPlayer;
import user.UserSettings;

/**
 * Window for showing and changing the current user settings.
 * @author Henrik Sandström
 */
public class SettingsUI extends JPanel implements ActionListener, MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4007407740535759310L;
	private Viewer viewer;
	private JCheckBox cbMusic, cbAudio, cbFullscreen;
	private JButton btnSave, btnBack;
	private UserSettings userSettings;
	private JSlider slMusic, slEffects;

	public SettingsUI(Viewer viewer) {
		this.viewer = viewer;
		setLayout(new BorderLayout());
		
		add(Common.newTitle("Settings"), BorderLayout.PAGE_START);

		JPanel panel = new JPanel(new GridLayout(5,2));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(0,150,0,150));
		
		panel.add(new JLabel("Use music"));				
		cbMusic = Common.newCheckBox();		
		panel.add(cbMusic);
		
		panel.add(new JLabel("Music volume"));		
		slMusic = new JSlider();	
		slMusic.setOpaque(false);
		slMusic.setMaximum(0);
		slMusic.setMinimum(-40);
		slMusic.setValue(-10);
		slMusic.setPreferredSize(new Dimension(100, 20));
		slMusic.addMouseMotionListener(this);
		panel.add(slMusic);
		
		panel.add(new JLabel("Use audio effects"));
		cbAudio = Common.newCheckBox();
		panel.add(cbAudio);
		
		panel.add(new JLabel("Effects volume"));		
		slEffects = new JSlider();	
		slEffects.setOpaque(false);
		slEffects.setMaximum(0);
		slEffects.setMinimum(-30);
		slEffects.setValue(0);
		slEffects.setPreferredSize(new Dimension(100, 20));
		panel.add(slEffects);
		
		panel.add(new JLabel("Fullscreen"));
		cbFullscreen = Common.newCheckBox();
		panel.add(cbFullscreen);
		
		add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setPreferredSize(new Dimension(600, 100));

		btnSave = Common.newButton("Save settings", this);
		buttonPanel.add(btnSave);
		btnBack = Common.newButton("Back to menu", this);
		buttonPanel.add(btnBack);

		add(buttonPanel,BorderLayout.SOUTH);
		
		userSettings = UserSettings.getInstance();

		initialize();
	}

	/**
	 * Loads and initializes the saved/default user settings.
	 */
	public void initialize() {
		HashMap<String, Integer> temp = userSettings.getUserSettings();
		cbMusic.setSelected(temp.get("music") == 1);
		cbAudio.setSelected(temp.get("audioEffects") == 1);
		slMusic.setValue(temp.get("musicVolume"));
		slEffects.setValue(temp.get("effectsVolume"));
		
		cbFullscreen.setSelected(temp.get("fullscreen") == 1);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnBack) {
			initialize();	// resets unsaved changes
		}else if(e.getSource() == btnSave) {
			HashMap<String, Integer> newUserSettings = new HashMap<String, Integer>();
			newUserSettings.put("music", (cbMusic.isSelected() ? 1 : 0));
			newUserSettings.put("audioEffects", (cbAudio.isSelected() ? 1 : 0));
			newUserSettings.put("musicVolume", slMusic.getValue());
			newUserSettings.put("effectsVolume", slEffects.getValue());
			newUserSettings.put("fullscreen", (cbFullscreen.isSelected() ? 1 : 0));
			userSettings.writeSettings(newUserSettings);
			
		}
		viewer.switchToMenu();
		viewer.settingsChanged();	//unfortunately always a necessity as volume might have been edited
	}
	
	protected void paintComponent(Graphics g) {        
		super.paintComponent(g);       
		Common.paintComponent(g, this, Common.getNormalBackground());     
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SoundPlayer.getInstance().isPlaying()) {
			viewer.volumeDragged(slMusic.getValue());
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// unused
		
	}

}
