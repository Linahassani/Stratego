package server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;

/**
 * GUI for server with logging and info displaying capabilities 
 * 
 * @author Anders Qvist
 */

@SuppressWarnings("serial")
public class ServerUI extends JPanel implements ActionListener, ItemListener {
	private JButton btnall = new JButton("Full log");
	private JButton btnclear = new JButton("Clear");
	private JButton btnshow = new JButton("Show timebased log");
	private SpinnerDateModel model1 = new SpinnerDateModel();
	private SpinnerDateModel model2 = new SpinnerDateModel();
	private JSpinner spinner1;
	private JSpinner spinner2;
	private JSpinner.DateEditor editor1;
	private JSpinner.DateEditor editor2;
	private DateFormatter format1;
	private DateFormatter format2;
	private JTextArea area = new JTextArea();
	private JScrollPane scroll;
	private JCheckBox exceptions = new JCheckBox("Show only exceptions");
	private JCheckBox network = new JCheckBox("Show only network traffic");
	private Controller controller;
	private String filter = null;

	
	/**
	 * Constructor that takes a Controller instance. Creates the UI.
	 * 
	 * @param controller
	 */
	public ServerUI(Controller controller) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		this.controller = controller;
		spinner1 = new JSpinner(model1);
		spinner2 = new JSpinner(model2);
		scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		btnclear.setPreferredSize(new Dimension(150, 30));
		btnshow.setPreferredSize(new Dimension(150, 30));
		btnall.setPreferredSize(new Dimension(150, 30));
		scroll.setPreferredSize(new Dimension(680, 400));
		editor1 = new JSpinner.DateEditor(spinner1, "yyyy:MM:dd:HH:mm:ss");
		editor2 = new JSpinner.DateEditor(spinner2, "yyyy:MM:dd:HH:mm:ss");
		format1 = (DateFormatter) editor1.getTextField().getFormatter();
		format2 = (DateFormatter) editor2.getTextField().getFormatter();
		format1.setAllowsInvalid(false);
		format1.setOverwriteMode(true);
		format2.setAllowsInvalid(false);
		format2.setOverwriteMode(true);
		spinner1.setEditor(editor1);
		spinner2.setEditor(editor2);
		spinner1.setPreferredSize(new Dimension(150, 30));
		spinner2.setPreferredSize(new Dimension(150, 30));
		add(scroll);
		add(btnshow);
		add(btnclear);
		add(spinner1);
		add(spinner2);
		add(btnall);
		add(exceptions);
		add(network);
		NoneSelectedButtonGroup checkboxes = new NoneSelectedButtonGroup();
		checkboxes.add(exceptions);
		checkboxes.add(network);
		btnshow.addActionListener(this);
		btnclear.addActionListener(this);
		btnall.addActionListener(this);
		exceptions.addItemListener(this);
		network.addItemListener(this);
		JFrame frame = new JFrame();
		frame.setSize(700, 540);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this, null);
		frame.setVisible(true);
	}
	
	/**
	 * Custom buttongroup to allow user to have none selected
	 */

	private class NoneSelectedButtonGroup extends ButtonGroup {
		@Override
		
		public void setSelected(ButtonModel model, boolean selected) {
			if (selected) {
				super.setSelected(model, selected);
				
			} else {
				clearSelection();
			}
		}
	}

	public boolean exceptionsSelected() {
		return exceptions.isSelected();
	}

	public boolean networkSelected() {
		return network.isSelected();
	}
	
	/**
	 * ItemListener on the checkboxes. Deselects boxes if user deselects them, otherwise sets the filter to use
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == network) {
			
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				network.setSelected(false);
				
			} else {
				network.setSelected(true);
				filter = "ip :";
			}
			
		} else if (source == exceptions) {
			
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				exceptions.setSelected(false);
				
			} else {
				exceptions.setSelected(true);
				filter = "Exception";
			}
		}
	}
	
	public void setIPtoConnect(String ip) {
		area.setText(ip);
	}

	/**
	 * ActionListener on the buttons. Calls methods in the controller and sets the
	 * text in the text area.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnshow) {
			area.setText(controller.getLog(filter, model1.getDate(), model2.getDate()));
		}
		
		if (e.getSource() == btnclear) {
			area.setText("");
		}
		
		if (e.getSource() == btnall) {
			area.setText(controller.getLog(filter));
		}
	}

//	public static void main(String[] args) {
//		Controller controller = new Controller();
//		ServerUI ui = new ServerUI(controller);
//	}
}
