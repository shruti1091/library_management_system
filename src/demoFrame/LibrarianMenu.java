package demoFrame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LibrarianMenu extends JFrame {

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
					LibrarianMenu frame = new LibrarianMenu();
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
	public LibrarianMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 512, 460);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Register new Patron");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewPatron.main(new String[]{});
				//dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton.setBounds(145, 66, 205, 35);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Issue Book");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookIssueForm.main(new String[]{});
				//dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(145, 126, 205, 35);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Return Book");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BookReturn.main(new String[] {});
				//dispose();
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_2.setBounds(145, 188, 205, 35);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("Librarian Menu");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(194, 28, 128, 20);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton_3 = new JButton("Logout");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				home.main(new String[] {});
				dispose();
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_3.setBounds(145, 360, 205, 35);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Add Book");
		btnNewButton_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//add book form
				addBookForm.main(new String[] {});
				//dispose();
			}
		});
		btnNewButton_4.setBounds(145, 250, 205, 35);
		contentPane.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Generate Report");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ReportGenerator.main(new String[] {});
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_5.setBounds(145, 314, 205, 35);
		contentPane.add(btnNewButton_5);
	}
}
