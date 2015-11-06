package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import calculator.SpellEfficiencyCalculator;
import dto.Static.ChampionSpell;
import exception.NegativeNumberException;
import main.java.riotapi.RiotApiException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

/**
 * Container for the SpellEfficiencyCalculator which has all of the fields needed to calculate
 * efficiency and three options - calculate, reset, and close. Close exits the program, reset
 * sets all of the parameters back to zero, and calculate gives a pop-up box with the most
 * efficient spell given the parameters.
 * 
 * @author Leonard Kerr
 */
public class CalculatorGUI extends JFrame implements ActionListener {

	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Spell Efficiency Calculator";
	/** Text to be displayed on the calculate button. */
	private static final String CALCULATE_TEXT = "Calculate";
	/** Text to be displayed on the reset button. */
	private static final String RESET_TEXT = "Reset";
	/** Text to be displayed on the close button. */
	private static final String CLOSE_TEXT = "Close";
	/** Label to describe the ability power text box. */
	private static final String AP_LABEL = "Ability Power";
	/** Label to describe the attack power text box. */
	private static final String AD_LABEL = "Attack Damage";
	/** Label to describe the cooldown reduction text box. */
	private static final String CDR_LABEL = "Cooldown Reduction (%)";
	/** Frame to contain all of the elements of the GUI */
	private JFrame frame;
	/** A text field that allows the user to modify the ability power */
	private JTextField txtAbilityPower;
	/** A text field that allows the user to modify the cooldown reduction */
	private JTextField txtCooldownReduction;
	/** A text field that allows the user to modify the attack damage */
	private JTextField txtAttackPower;
	/** A button used to calculate the most efficient spells given the parameters */
	private JButton btnCalculate;
	/** A button used to reset the parameters back to their default value */
	private JButton btnReset;
	/** A button used to terminate the program */
	private JButton btnClose;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalculatorGUI window = new CalculatorGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the GUI for the SpellEfficiency Calculator.
	 * @throws RiotApiException whenever information can not be properly retrieved from the Riot API
	 */
	public CalculatorGUI() throws RiotApiException {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle(APP_TITLE);
		
		txtAbilityPower = new JTextField();
		txtAbilityPower.setBounds(72, 0, 144, 38);
		txtAbilityPower.setHorizontalAlignment(SwingConstants.CENTER);
		txtAbilityPower.setText("0");
		txtAbilityPower.setToolTipText(AP_LABEL);
		frame.getContentPane().add(txtAbilityPower);
		txtAbilityPower.setColumns(10);
		txtAbilityPower.addActionListener(this);
		
		JLabel lblNewLabel = new JLabel(AP_LABEL);
		lblNewLabel.setBounds(228, 0, 144, 38);
		lblNewLabel.setLabelFor(txtAbilityPower);
		frame.getContentPane().add(lblNewLabel);
		
		txtAttackPower = new JTextField();
		txtAttackPower.setBounds(72, 38, 144, 38);
		txtAttackPower.setHorizontalAlignment(SwingConstants.CENTER);
		txtAttackPower.setText("0");
		txtAttackPower.setToolTipText(AD_LABEL);
		frame.getContentPane().add(txtAttackPower);
		txtAttackPower.setColumns(10);
		txtAttackPower.addActionListener(this);
		
		JLabel lblAttackPower = new JLabel(AD_LABEL);
		lblAttackPower.setBounds(228, 38, 144, 38);
		lblAttackPower.setLabelFor(txtAttackPower);
		frame.getContentPane().add(lblAttackPower);
		
		
		txtCooldownReduction = new JTextField();
		txtCooldownReduction.setBounds(72, 76, 144, 38);
		txtCooldownReduction.setHorizontalAlignment(SwingConstants.CENTER);
		txtCooldownReduction.setText("0");
		txtCooldownReduction.setToolTipText(CDR_LABEL);
		frame.getContentPane().add(txtCooldownReduction);
		txtCooldownReduction.setColumns(10);
		txtCooldownReduction.addActionListener(this);
		
		JLabel lblCooldownReduction = new JLabel(CDR_LABEL);
		lblCooldownReduction.setBounds(228, 76, 144, 38);
		lblCooldownReduction.setLabelFor(txtCooldownReduction);
		frame.getContentPane().add(lblCooldownReduction);
				
		btnCalculate = new JButton(CALCULATE_TEXT);
		btnCalculate.setBounds(0, 114, 144, 38);
		frame.getContentPane().add(btnCalculate);
		btnCalculate.addActionListener(this);
		
		btnReset = new JButton(RESET_TEXT);
		btnReset.setBounds(144, 114, 144, 38);
		frame.getContentPane().add(btnReset);
		btnReset.addActionListener(this);
		
		btnClose = new JButton(CLOSE_TEXT);
		btnClose.setBounds(288, 114, 144, 38);	
		frame.getContentPane().add(btnClose);
	}
	
	/**
	 * Performs an action based on the given {@link ActionEvent}.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Create a new SpellEfficiencyCalculator
		SpellEfficiencyCalculator calculator = null;
		
		try {
			//Instantiate the SpellEfficiencyCalculator
			calculator = new SpellEfficiencyCalculator();
		} catch (RiotApiException f) {
			JOptionPane.showMessageDialog(CalculatorGUI.this, "Error accessing Riot's API.");
			f.printStackTrace();
		} catch (NegativeNumberException n){
			JOptionPane.showMessageDialog(CalculatorGUI.this, "Error building calculator - Negative parameters.");
			n.printStackTrace();
		}
		
		if (e.getSource() == btnCalculate) {
			try{
				try {
					calculator.setAttackPower(Double.parseDouble(txtAttackPower.getText()));
				} catch (NegativeNumberException f) {
					JOptionPane.showMessageDialog(CalculatorGUI.this, f.getMessage());
					f.printStackTrace();
				}
				try {
					calculator.setAbilityPower(Double.parseDouble(txtAbilityPower.getText()));
				} catch (NegativeNumberException f) {
					JOptionPane.showMessageDialog(CalculatorGUI.this, f.getMessage());
					f.printStackTrace();
				}
				try {
					calculator.setCoolDownReduction((Double.parseDouble(txtCooldownReduction.getText())));
				} catch (IllegalArgumentException f) {
					JOptionPane.showMessageDialog(CalculatorGUI.this, f.getMessage());
				}
				//Calculate the most efficient spell
				ChampionSpell mostEfficient = calculator.calculateEfficiency();
				JOptionPane.showMessageDialog(CalculatorGUI.this, mostEfficient.getName() + 
						" is most efficient with a DPS of " + Math.round(calculator.getDPS(mostEfficient)));
			} catch (NumberFormatException n) {
				JOptionPane.showMessageDialog(CalculatorGUI.this, "Inputs must be positive, real numbers.");
				n.printStackTrace();
			}
		} else if (e.getSource() == btnReset) {
			txtAbilityPower.setText("0");
			txtAttackPower.setText("0");
			txtCooldownReduction.setText("0");
			try {
				calculator.reset();
			} catch (NegativeNumberException e1) {
				JOptionPane.showMessageDialog(CalculatorGUI.this, "Error resetting calculator - Negative paremeters.");
				e1.printStackTrace();
			}
		} else if (e.getSource() == btnClose) {
			System.exit(0);
		}
	}
}
