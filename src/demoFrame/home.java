package demoFrame;

import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;

public class home extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					home frame = new home();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public home() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 496);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(7, 7, 7, 7));

		setContentPane(contentPane);
		
		JButton button_patron = new JButton("Patron");
		button_patron.setBounds(199, 155, 151, 47);
		button_patron.setForeground(new Color(0, 0, 0));
		button_patron.setFont(new Font("Tahoma", Font.PLAIN, 13));
		button_patron.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Search.main(new String[] {});
				dispose();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(button_patron);
		
		JButton button_librarian = new JButton("Librarian");
		button_librarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LibrarianLogin.main(new String[] {});
			}
		});
		button_librarian.setFont(new Font("Tahoma", Font.PLAIN, 13));
		button_librarian.setBounds(199, 236, 151, 47);
		contentPane.add(button_librarian);
		
		JLabel label_continueAs = new JLabel("Continue As...");
		label_continueAs.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_continueAs.setBounds(216, 70, 151, 64);
		contentPane.add(label_continueAs);
		
		JButton btnNewButton = new JButton("Admin");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addLibrarian.main(new String[]{});
				dispose();
			}
		});
		btnNewButton.setBounds(199, 322, 151, 47);
		contentPane.add(btnNewButton);
	}
}
