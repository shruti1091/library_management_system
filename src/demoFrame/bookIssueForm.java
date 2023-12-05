package demoFrame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.*;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

public class bookIssueForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					bookIssueForm frame = new bookIssueForm();
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
	public bookIssueForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 522, 424);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Book Issue");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBounds(198, 36, 103, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Enter Patron ID");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(94, 101, 118, 14);
		contentPane.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(250, 100, 143, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		
		JLabel lblNewLabel_2 = new JLabel("Enter ISBN");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setBounds(94, 158, 118, 14);
		contentPane.add(lblNewLabel_2);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(250, 157, 143, 20);
		contentPane.add(textField_1);
		
		JButton btnNewButton = new JButton("Issue");
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("null")
			public void actionPerformed(ActionEvent e) {
				int errorFlag=0;
				issueConstraints constraint=new issueConstraints();
				String patron_no = textField.getText();
				String isbn = textField_1.getText(); 
				String user_type = null;
				int user_type_int = 0; 
				if(constraint.patronIdPresence(patron_no)==0) {
					errorFlag=1;
					JOptionPane.showMessageDialog(contentPane, "Patron id is not present!");
				}
				else if(constraint.patronIdPresence(patron_no)==1) {
					user_type="faculty";
					user_type_int=1;
					
				}
				else if(constraint.patronIdPresence(patron_no)==2) {
					user_type="student";
					user_type_int=2;
					
				}
				if(constraint.patronMaxBooksReached(patron_no,user_type_int)) {
					errorFlag=2;
					JOptionPane.showMessageDialog(contentPane, "Patron has already borrowed maximum books!");
				}
				if(!constraint.isbnPresence(isbn)) {
					errorFlag=3;
					JOptionPane.showMessageDialog(contentPane, "PLease check the ISBN of the book");
				}
				if(!constraint.isbnAvailable(isbn)) {
					errorFlag=4;
					JOptionPane.showMessageDialog(contentPane, "This book has been lent already!");
					
				}
				if(constraint.isbnForLending(isbn)==1) {
					errorFlag=5;
					JOptionPane.showMessageDialog(contentPane, "Book is for library use only!");
				}else if(constraint.isbnForLending(isbn)==2 && !user_type.equals("faculty")) {
					errorFlag=6;
					JOptionPane.showMessageDialog(contentPane, "this book is to be lent only to faculty!");
				}
				else if(constraint.isbnForLending(isbn)==0) {
					//error (what error)
					errorFlag=7;
					JOptionPane.showMessageDialog(contentPane, "The book type doesnt match patron!");
				}
				
				
				
                if(errorFlag==0) {
	                // Get the current system date as the date of issue
	                java.util.Date dateOfIssue = Calendar.getInstance().getTime();
	
	                // Convert java.util.Date to java.sql.Date
	                Date sqlDateOfIssue = new java.sql.Date(dateOfIssue.getTime());
	
	                // Calculate the return date as 15 days from the date of issue
	                Calendar calendar = Calendar.getInstance();
	                calendar.setTime(dateOfIssue);
	                if(user_type_int==1)calendar.add(Calendar.DATE, 90);
	                else calendar.add(Calendar.DATE, 30);
	                java.util.Date returnDate = calendar.getTime();
	
	                // Convert java.util.Date to java.sql.Date for return date
	                Date sqlReturnDate = new java.sql.Date(returnDate.getTime());
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                String formattedDateOfIssue = sdf.format(sqlDateOfIssue);
	
	                // Format the return date as a string (e.g., "yyyy-MM-dd")
	                String formattedReturnDate = sdf.format(sqlReturnDate);
	                try {
	                	Class.forName("com.mysql.cj.jdbc.Driver");
	                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
	                    String sql = "INSERT INTO issue_relation (patron_no, ISBN, date_of_issue, return_date ) VALUES (?, ?, ?, ?)";
	                    PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	                    preparedStatement.setString(1, patron_no);
	                    preparedStatement.setString(2, isbn );
	                    preparedStatement.setString(3, formattedDateOfIssue );
	                    preparedStatement.setNString(4, formattedReturnDate);
	                    preparedStatement.executeUpdate();
	
	                    
	                 
	
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	                try {
	                	Class.forName("com.mysql.cj.jdbc.Driver");
	                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
	                    String sql = "Update LibraryBooks set availability='lent' where ISBN=?";
	                    PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	                    preparedStatement.setString(1, isbn);
	                    preparedStatement.executeUpdate();
	
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	                
	                JOptionPane.showMessageDialog(contentPane, "SUCCESSFULLY ENTERED INTO ISSUE_REATION. BOOK TO BE HANDED OVER TO PATRON!");
	                dispose();
                }
                
                	
                
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton.setBounds(192, 229, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_3 = new JLabel("!!Please check Patron ID and Book taken carefully!!");
		lblNewLabel_3.setForeground(new Color(255, 0, 0));
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(110, 295, 283, 14);
		contentPane.add(lblNewLabel_3);
	}

}
