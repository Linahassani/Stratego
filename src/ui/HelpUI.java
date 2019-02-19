//package ui;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.SwingConstants;
//import javax.swing.border.EmptyBorder;
//
///**
// * Help/rules UI
// * Easiest to test it when all is connected 
// * @author yun
// *
// */
//
//public class HelpUI extends JPanel implements ActionListener{
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 4971352996518635125L;
//	private Viewer viewer;
//	private JButton btnBack;
//	private String text;
//	private Helplist help;
//
//	public HelpUI(Viewer viewer) {
//		this.viewer = viewer;
//		setLayout(new BorderLayout());
//
//		add(Common.newTitle("Rules of Stratego"),BorderLayout.NORTH);
//
//		JPanel buttonPanel = new JPanel();
//		buttonPanel.setOpaque(false);
//		buttonPanel.setPreferredSize(new Dimension(600, 100));		
//		btnBack = Common.newButton("Back to menu", this);	
//		buttonPanel.add(btnBack);		
//		add(buttonPanel,BorderLayout.SOUTH);
//		help = new Helplist();
//		add(help, BorderLayout.CENTER);
//
//	}
//
//	private class Helplist extends JPanel {		
//
//		public Helplist(){
//			setOpaque(false);
//			setBorder(new EmptyBorder(20,0,0,0));
//			rules();
//		}
//		public void rules(){
//			try (BufferedReader br = new BufferedReader(new FileReader("files/RulesOfStratego"))) {
//				String line = null;
//				while ((line = br.readLine()) != null) {
//					if(line == ".") {
//						line += "\n";
//					}
//					text = new String(line);
//					add(new JLabel(text, SwingConstants.CENTER));
//				}
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//		protected void paintComponent(Graphics g) {	        
//			super.paintComponent(g);	       
//			Common.paintComponent(g, this, Common.getInnerBackground());      
//		}
//	}
//
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		if(e.getSource() == btnBack) {
//			viewer.switchToMenu();
//		} 
//
//	}
//
//	protected void paintComponent(Graphics g) {        
//		super.paintComponent(g);       
//		Common.paintComponent(g, this, Common.getNormalBackground());     
//	}
//
//
//}
