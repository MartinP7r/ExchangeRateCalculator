package exchangeRateApp;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

public class ExchangeRateGUI extends JFrame {
	
	public static void main(String[] args) {
		RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				
				
				ExchangeRateGUI frame = new ExchangeRateGUI();
				
				
				
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
	private void createWidgets() {
		
	}
	
	private void setupInteractions() {
		//move to other method
		txtEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				if () {
					// find way to make method for this
					try {
						BigDecimal currentRate = null;
						Iterator<DynaBean> rsdcIterator = rsdc.getRows().iterator();
						while(rsdcIterator.hasNext()) {
							DynaBean bean = (DynaBean) rsdcIterator.next();
							if (bean.get("currency_name").equals(comboBox.getSelectedItem())) {
								//selectedRate = (BigDecimal) bean.get("rate").toString();
								currentRate = new BigDecimal(bean.get("rate").toString());
							}
						}
						BigDecimal txtEuroDecimal = new BigDecimal(txtEuro.getText());
						txtOther.setText(currentRate.multiply(txtEuroDecimal).toString());
					} catch (NumberFormatException e1) {
						// continue
						e1.printStackTrace();
						if (txtEuro.getText().isEmpty()) {
							JOptionPane.showMessageDialog(getContentPane(), "Input field (Euro) is empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
						}
						else if (!isNumeric(txtEuro.getText())) {
							JOptionPane.showMessageDialog(getContentPane(), "Input field (Euro) may only contain numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
						}
					}
//				}
			}
		});
	
		comboBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Iterator<DynaBean> rsdcIterator = rsdc.getRows().iterator();
				while(rsdcIterator.hasNext()) {
					DynaBean bean = (DynaBean) rsdcIterator.next();
					if (bean.get("currency_name").equals(comboBox.getSelectedItem())) {
						txtOther.setText(bean.get("rate").toString());
					}
			    }
			}
		});
	
		
	}
	
	private void addWidgets() {
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtEuro;
	private JTextField txtOther;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private String rateOther;
	private JLabel lblDummydate;
	private JLabel lblLastUpdate;
	private JComboBox<String> comboBox;
	private RowSetDynaClass rsdc;
	private JLabel lblSource;
	private JLabel lblEuro;
	private JLabel lblNewLabel;
	private JRadioButton srcEuro;
	private JButton btnReset;
	private JMenuBar menuBar;
	private JMenuItem mntmAbout;
	
	public ExchangeRateGUI() {
		List<EuroExchangeRate> ratesList = new SQLiteTest().getRates();
		
		String[] comboFiller = new String[ratesList.size()];
		int i = 0;
		for (EuroExchangeRate entry : ratesList) {
			comboFiller[i] = entry.currencyName;
			i++;
		}
		
		rateOther = ratesList.get(0).rate;
		
		rsdc = new SQLiteTest().getDyna();
	    	
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(500, 300));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{10, 0, 0, 0, 0, 10, 0};
		gridBagLayout.rowHeights = new int[]{10, 0, 0, 0, 0, 0, 10, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		lblLastUpdate = new JLabel("last update:");
		GridBagConstraints gbc_lblLastUpdate = new GridBagConstraints();
		gbc_lblLastUpdate.anchor = GridBagConstraints.EAST;
		gbc_lblLastUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastUpdate.gridx = 1;
		gbc_lblLastUpdate.gridy = 1;
		getContentPane().add(lblLastUpdate, gbc_lblLastUpdate);
		
		lblDummydate = new JLabel(new SQLiteTest().checkTime()
						.toString());

//		JLabel lblDummydate = new JLabel("dummyDate");		//new SQLiteTest().checkTime().toString()
		GridBagConstraints gbc_lblDummydate = new GridBagConstraints();
		gbc_lblDummydate.anchor = GridBagConstraints.WEST;
		gbc_lblDummydate.insets = new Insets(0, 0, 5, 5);
		gbc_lblDummydate.gridx = 3;
		gbc_lblDummydate.gridy = 1;
		getContentPane().add(lblDummydate, gbc_lblDummydate);
		
		lblSource = new JLabel("Source");
		GridBagConstraints gbc_lblSource = new GridBagConstraints();
		gbc_lblSource.insets = new Insets(0, 0, 5, 5);
		gbc_lblSource.gridx = 4;
		gbc_lblSource.gridy = 1;
		getContentPane().add(lblSource, gbc_lblSource);
		
		lblEuro = new JLabel("Euro:");
		GridBagConstraints gbc_lblEuro = new GridBagConstraints();
		gbc_lblEuro.anchor = GridBagConstraints.EAST;
		gbc_lblEuro.insets = new Insets(0, 0, 5, 5);
		gbc_lblEuro.gridx = 1;
		gbc_lblEuro.gridy = 2;
		getContentPane().add(lblEuro, gbc_lblEuro);
		
		lblNewLabel = new JLabel("€");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 2;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		txtEuro = new JTextField();
		txtEuro.setText("1.00");
//		txtEuro.setMinimumSize(new Dimension(300, 25));
//		txtEuro.setMaximumSize(new Dimension(300, 25));
		GridBagConstraints gbc_txtEuro = new GridBagConstraints();
		gbc_txtEuro.insets = new Insets(0, 0, 5, 5);
		gbc_txtEuro.anchor = GridBagConstraints.WEST;
		gbc_txtEuro.gridx = 3;
		gbc_txtEuro.gridy = 2;
		getContentPane().add(txtEuro, gbc_txtEuro);
		txtEuro.setColumns(10);

		comboBox = new JComboBox<>(comboFiller);
		comboBox.setMaximumSize(comboBox.getPreferredSize());		//helps setting the size to the width of the largest item inside
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.VERTICAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		getContentPane().add(comboBox, gbc_comboBox);

		// needs to go in other thread?
		// at least into method setupInteractions;
		//JComboBox<String> comboBox = null;
		//javax.swing.SwingUtilities.isEventDispatchThread
		//java.awt.EventQueue.isDispatchThread()

							
		srcEuro = new JRadioButton("");
		buttonGroup.add(srcEuro);
		srcEuro.setSelected(true);
		GridBagConstraints gbc_srcEuro = new GridBagConstraints();
		gbc_srcEuro.insets = new Insets(0, 0, 5, 5);
		gbc_srcEuro.gridx = 4;
		gbc_srcEuro.gridy = 2;
		getContentPane().add(srcEuro, gbc_srcEuro);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 3;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		

		txtOther = new JTextField();
		txtOther.setText(rateOther);
		GridBagConstraints gbc_txtOther = new GridBagConstraints();
		gbc_txtOther.insets = new Insets(0, 0, 5, 5);
		gbc_txtOther.anchor = GridBagConstraints.WEST;
		gbc_txtOther.gridx = 3;
		gbc_txtOther.gridy = 3;
		getContentPane().add(txtOther, gbc_txtOther);
		txtOther.setColumns(10);
		
		JButton btnConvert = new JButton("Convert");
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Whaaaat");
			}
		});
		
		JRadioButton srcOther = new JRadioButton("");
		buttonGroup.add(srcOther);
		GridBagConstraints gbc_srcOther = new GridBagConstraints();
		gbc_srcOther.insets = new Insets(0, 0, 5, 5);
		gbc_srcOther.gridx = 4;
		gbc_srcOther.gridy = 3;
		getContentPane().add(srcOther, gbc_srcOther);
		
		btnReset = new JButton("Reset");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.anchor = GridBagConstraints.EAST;
		gbc_btnReset.insets = new Insets(0, 0, 5, 5);
		gbc_btnReset.gridx = 1;
		gbc_btnReset.gridy = 5;
		getContentPane().add(btnReset, gbc_btnReset);
		GridBagConstraints gbc_btnConvert = new GridBagConstraints();
		gbc_btnConvert.insets = new Insets(0, 0, 5, 5);
		gbc_btnConvert.anchor = GridBagConstraints.WEST;
		gbc_btnConvert.gridx = 3;
		gbc_btnConvert.gridy = 5;
		getContentPane().add(btnConvert, gbc_btnConvert);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmAbout.setHorizontalTextPosition(SwingConstants.LEFT);
		mntmAbout.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		mntmAbout.addMenuKeyListener(new MenuKeyListener() {
			public void menuKeyPressed(MenuKeyEvent e) {
			}
			public void menuKeyReleased(MenuKeyEvent e) {
			}
			public void menuKeyTyped(MenuKeyEvent e) {
			}
		});
		
		JMenu mnMenu = new JMenu("menu1");
		menuBar.add(mnMenu);
		
		JMenuItem mntmItem = new JMenuItem("Item1");
		mnMenu.add(mntmItem);
		
		JMenuItem mntmItem_1 = new JMenuItem("Item2");
		mnMenu.add(mntmItem_1);
		menuBar.add(mntmAbout);
		
		//setting up GUI interactions in separate method
		setupInteractions();
	}
	
	// put somewhere, where to whole app can use it?
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
}
