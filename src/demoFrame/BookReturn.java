package demoFrame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.ActionEvent;

public class BookReturn extends JFrame {

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
					BookReturn frame = new BookReturn();
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
	public BookReturn() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 496);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label_heading = new JLabel("Book Return");
		label_heading.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_heading.setBounds(212, 53, 117, 35);
		contentPane.add(label_heading);
		
		JLabel label_isbn = new JLabel("ISBN");
		label_isbn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_isbn.setBounds(131, 175, 62, 27);
		contentPane.add(label_isbn);
		
		final JTextField text_isbn = new JTextField();
		text_isbn.setBounds(239, 173, 255, 35);
		contentPane.add(text_isbn);
		text_isbn.setColumns(10);
		
		JButton button_submit = new JButton("Submit");
		button_submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int errorFlag=0;
				returnConstraints constraint=new returnConstraints();
				String isbn = text_isbn.getText(); 
				int user_type_int = 0;
				String patron_id=null;
				try {
                	Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
                    String sql = "select patron_no from issue_relation where isbn=? and returnedBool=0";
                    PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, isbn);
                    //System.out.println("SQL Query: " + sql);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                    if (resultSet.next()) {
                        patron_id = resultSet.getString("patron_no");  
                       // System.out.println("Fetched patron_id: " + patron_id); // Debugging line
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                }
				if(constraint.isbnPresence(isbn)==0) {
					errorFlag=1;
					JOptionPane.showMessageDialog(null, "ISBN not present in issue_relation");
				}
				else if(constraint.isbnPresence(isbn)==1) {
					user_type_int=1;
				}
				else if(constraint.isbnPresence(isbn)==2) {
					user_type_int=2;
				}
				if(constraint.returnedOnSameDay(isbn)) {
					//error
					errorFlag=1;
					JOptionPane.showMessageDialog(null, "Book can't be returned on same day");
					dispose();
				}
				
				if(errorFlag==0) {
					fineCalculation fine=new fineCalculation();
					int fineAmount=fine.calculateFine(isbn,user_type_int);
					if(fineAmount>0) {
						
						if(!fine.collectFine(isbn,fineAmount)) {
							//can't return book
							errorFlag=1;
							JOptionPane.showMessageDialog(null, "Book can't be retuned when fine is not paid");
						}
						else if(fine.collectFine(isbn, fineAmount)) {
							//return book
							//update fine amount paid
							try {
			                	Class.forName("com.mysql.cj.jdbc.Driver");
			                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
			                    String sql = "update issue_relation set fine_incurred=? where isbn=? and returnedBool=0";
			                    PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			                    preparedStatement.setInt(1, fineAmount);
			                    preparedStatement.setString(2, isbn );
			                    preparedStatement.executeUpdate();
			
			                } catch (Exception e1) {
			                    e1.printStackTrace();
			                }
							errorFlag=0;
							
						}
					}
					else if(fineAmount==0) {
						//update fine amount to 0
						try {
		                	Class.forName("com.mysql.cj.jdbc.Driver");
		                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
		                    String sql = "update issue_relation set fine_incurred=? where isbn=? and returnedBool=0";
		                    PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		                    preparedStatement.setInt(1, 0);
		                    preparedStatement.setString(2, isbn );
		                    preparedStatement.executeUpdate();
		
		                } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
						errorFlag=0;
					}
				}
				if(errorFlag==0) {
					//return book
					// Get the current system date as the date of issue
	                java.util.Date CurrentDate = Calendar.getInstance().getTime();
	
	                // Convert java.util.Date to java.sql.Date
	                Date sqlCurrentDate = new java.sql.Date(CurrentDate.getTime());
	
	                
	
	                // Convert java.util.Date to java.sql.Date for return date
	           
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                String formattedCurrentDate = sdf.format(sqlCurrentDate);
	
	           
	                try {
	                	Class.forName("com.mysql.cj.jdbc.Driver");
	                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
	                    String sql = "update issue_relation set return_date=?, returnedBool=1  where isbn=? and patron_no=? and returnedBool=0";
	                    PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	                    preparedStatement.setString(1, formattedCurrentDate);
	                    preparedStatement.setString(2, isbn );
	                    preparedStatement.setString(3, patron_id );
	                    preparedStatement.executeUpdate();
	                    //System.out.println("returned in issue_relation");
	
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	                try {
	                	Class.forName("com.mysql.cj.jdbc.Driver");
	                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
	                    String sql = "Update LibraryBooks set availability='avl' where ISBN=?";
	                    PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	                    preparedStatement.setString(1, isbn);
	                    preparedStatement.executeUpdate();
	                    //System.out.println("returned in LibraryBooks");
	
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	                JOptionPane.showMessageDialog(null, "book return successful");
	                dispose();
				}
			}
		});
		button_submit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		button_submit.setBounds(212, 290, 117, 41);
		contentPane.add(button_submit);
	}

}
