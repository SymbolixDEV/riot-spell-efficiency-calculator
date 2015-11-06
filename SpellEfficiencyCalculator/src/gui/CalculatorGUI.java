package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import calculator.SpellEfficiencyCalculator;
import dto.Static.ChampionSpell;
import main.java.riotapi.RiotApiException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class CalculatorGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField txtAbilityPower;
	private JTextField txtCooldownReduction;
	private JTextField txtAttackPower;
	private JButton btnCalculate;
	private JButton btnReset;
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
	 * Create the application.
	 * @throws RiotApiException 
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
		
		txtAbilityPower = new JTextField();
		txtAbilityPower.setBounds(72, 0, 144, 38);
		txtAbilityPower.setHorizontalAlignment(SwingConstants.CENTER);
		txtAbilityPower.setText("0");
		frame.getContentPane().add(txtAbilityPower);
		txtAbilityPower.setColumns(10);
		txtAbilityPower.addActionListener(this);
		
		JLabel lblNewLabel = new JLabel("Ability Power");
		lblNewLabel.setBounds(228, 0, 144, 38);
		lblNewLabel.setLabelFor(txtAbilityPower);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label_1 = new JLabel("");
		label_1.setBounds(288, 0, 144, 38);
		frame.getContentPane().add(label_1);
		
		txtAttackPower = new JTextField();
		txtAttackPower.setBounds(72, 38, 144, 38);
		txtAttackPower.setHorizontalAlignment(SwingConstants.CENTER);
		txtAttackPower.setText("0");
		txtAttackPower.setToolTipText("");
		frame.getContentPane().add(txtAttackPower);
		txtAttackPower.setColumns(10);
		txtAttackPower.addActionListener(this);
		
		JLabel lblAttackPower = new JLabel("Attack Power");
		lblAttackPower.setBounds(228, 38, 144, 38);
		lblAttackPower.setLabelFor(txtAttackPower);
		frame.getContentPane().add(lblAttackPower);
		
		JLabel label_3 = new JLabel("");
		label_3.setBounds(288, 38, 144, 38);
		frame.getContentPane().add(label_3);
		
		txtCooldownReduction = new JTextField();
		txtCooldownReduction.setBounds(72, 76, 144, 38);
		txtCooldownReduction.setHorizontalAlignment(SwingConstants.CENTER);
		txtCooldownReduction.setText("0");
		frame.getContentPane().add(txtCooldownReduction);
		txtCooldownReduction.setColumns(10);
		txtCooldownReduction.addActionListener(this);
		
		JLabel lblCooldownReduction = new JLabel("Cooldown Reduction (%)");
		lblCooldownReduction.setBounds(228, 76, 144, 38);
		lblCooldownReduction.setLabelFor(txtCooldownReduction);
		frame.getContentPane().add(lblCooldownReduction);
		
		JLabel label_5 = new JLabel("");
		label_5.setBounds(288, 76, 144, 38);
		frame.getContentPane().add(label_5);
		
		btnCalculate = new JButton("Calculate");
		btnCalculate.setBounds(0, 114, 144, 38);
		frame.getContentPane().add(btnCalculate);
		btnCalculate.addActionListener(this);
		
		btnReset = new JButton("Reset");
		btnReset.setBounds(144, 114, 144, 38);
		frame.getContentPane().add(btnReset);
		btnReset.addActionListener(this);
		
		btnClose = new JButton("Close");
		btnClose.setBounds(288, 114, 144, 38);	
		frame.getContentPane().add(btnClose);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		SpellEfficiencyCalculator calculator = null;
		try {
			calculator = new SpellEfficiencyCalculator();
		} catch (RiotApiException f) {
			JOptionPane.showMessageDialog(CalculatorGUI.this, "Error accessing Riot's API.");
			f.printStackTrace();
		}
		
		if (e.getSource() == btnCalculate) {
			
			trycatch:
			try{
				calculator.setAttackPower(Double.parseDouble(txtAttackPower.getText()));
				calculator.setAbilityPower(Double.parseDouble(txtAbilityPower.getText()));
				calculator.setCoolDownReduction((Double.parseDouble(txtCooldownReduction.getText())));
				if(calculator.getAbilityPower() < 0 || calculator.getAttackPower() < 0 || calculator.getCoolDownReduction() < 0){
					JOptionPane.showMessageDialog(CalculatorGUI.this, "Inputs must be positive, real numbers.");
					break trycatch;
				}
				ChampionSpell mostEfficient = calculator.calculateEfficiency();
				JOptionPane.showMessageDialog(CalculatorGUI.this, mostEfficient.getName() + 
						" is most efficient with a DPS of " + calculator.getDPS(mostEfficient));
			} catch (NumberFormatException n) {
				JOptionPane.showMessageDialog(CalculatorGUI.this, "Inputs must be positive, real numbers.");
				n.printStackTrace();
			} catch (IllegalArgumentException i){
				JOptionPane.showMessageDialog(CalculatorGUI.this, "Cooldown reduction must be a positive real number at or below 40.");
				i.printStackTrace();
			}
			
		} else if (e.getSource() == btnReset) {
			txtAbilityPower.setText("0");
			txtAttackPower.setText("0");
			txtCooldownReduction.setText("0");
			calculator.reset();
		} else if (e.getSource() == btnClose) {
			System.exit(0);
		}
	}
}
