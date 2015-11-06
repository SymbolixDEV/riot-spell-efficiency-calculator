package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTree;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CalculatorGUI {

	private JFrame frame;
	private JTextField txtAbilityPower;
	private JTextField txtCooldownReduction;
	private JTextField txtAttackPower;

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
	 */
	public CalculatorGUI() {
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
		txtAbilityPower.setText("Ability Power");
		txtAbilityPower.setBounds(12, 13, 140, 22);
		frame.getContentPane().add(txtAbilityPower);
		txtAbilityPower.setColumns(10);
		
		txtCooldownReduction = new JTextField();
		txtCooldownReduction.setText("Cooldown Reduction");
		txtCooldownReduction.setBounds(12, 83, 140, 22);
		frame.getContentPane().add(txtCooldownReduction);
		txtCooldownReduction.setColumns(10);
		
		txtAttackPower = new JTextField();
		txtAttackPower.setText("Attack Power");
		txtAttackPower.setToolTipText("");
		txtAttackPower.setBounds(12, 48, 140, 22);
		frame.getContentPane().add(txtAttackPower);
		txtAttackPower.setColumns(10);
		
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnCalculate.setBounds(35, 118, 97, 25);
		frame.getContentPane().add(btnCalculate);
		
		JLabel label = new JLabel("%");
		label.setBounds(164, 86, 56, 16);
		frame.getContentPane().add(label);
	}
}
