package firstTest;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingSetTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6599147977712785552L;
	
	private JButton btn01;
	
	public SwingSetTest() {
		setTitle("Titel des Swing-Fensters");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		createWidgets();
		setupInteraction();
		addWidgets();
	}


	private void createWidgets() {
		btn01 = new JButton();
		btn01.setText("Button01");
		btn01.setFont(btn01.getFont().deriveFont(Font.BOLD));	// geht das auch ohne btn01. vor getFont()? -- nope
	}
		
		
	private void setupInteraction() {
		btn01.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button " + e.getActionCommand() + " geklickt!");
			}
		});
	}
	
	
	private void addWidgets() {
		btn01.setBounds(10, 10, 100, 25);
		this.getContentPane().setLayout(null);
		this.getContentPane().add(btn01);
		this.setSize(140, 85);
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				SwingSetTest gui = new SwingSetTest();
				gui.setLocationRelativeTo(null);
				gui.setVisible(true);
				
			}
		});
	}
}
