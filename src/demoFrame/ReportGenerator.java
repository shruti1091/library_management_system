package demoFrame;

import java.awt.BorderLayout;
import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class ReportGenerator extends JFrame {

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
					ReportGenerator frame = new ReportGenerator();
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
	public ReportGenerator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 573, 459);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("GENERATE REPORTS");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(185, 23, 174, 44);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Books Currently Issued");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection con = null;
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        
		        try {
		            Class.forName("com.mysql.cj.jdbc.Driver");
		            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
		            
		            // Modified SQL query with JOIN to get book title
		            String query = "SELECT issue_relation.ISBN, issue_relation.patron_no, issue_relation.date_of_issue, issue_relation.return_date, LibraryBooks.title FROM issue_relation JOIN LibraryBooks ON issue_relation.ISBN = LibraryBooks.ISBN WHERE issue_relation.returnedBool = 0";
		            ps = con.prepareStatement(query);
		            
		            rs = ps.executeQuery();
		            
		            JFrame resultFrame = new JFrame("Books Currently Issued");
		            resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            resultFrame.getContentPane().setLayout(new BorderLayout());
		            
		            JTable table = new JTable();
		            table.setModel(new DefaultTableModel(
		                new Object [][] {},
		                new String [] {"ISBN", "Patron_no", "Date_of_issue", "Return_date", "Title"}
		            ));
		            DefaultTableModel model = (DefaultTableModel) table.getModel();
		            
		            while(rs.next()) {
		                model.addRow(new Object[]{rs.getString("ISBN"), rs.getString("patron_no"), rs.getDate("date_of_issue"), rs.getDate("return_date"), rs.getString("title")});
		            }
		            
		            JScrollPane scrollPane = new JScrollPane(table);
		            resultFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		            resultFrame.setSize(600, 400);
		            resultFrame.setVisible(true);
		            
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        } finally {
		            try {
		                if(rs != null) rs.close();
		                if(ps != null) ps.close();
		                if(con != null) con.close();
		            } catch (SQLException se) {
		                se.printStackTrace();
		            }
		        }
		    

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(154, 77, 247, 25);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Patrons with Overdue Books");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection con = null;
		        PreparedStatement ps = null;
		        ResultSet rs = null;

		        try {
		            Class.forName("com.mysql.cj.jdbc.Driver");
		            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");

		            // SQL query to fetch patrons with overdue books
		            String query = "SELECT patron_no, ISBN, return_date FROM issue_relation WHERE returnedBool = 0 AND return_date < CURDATE()";
		            ps = con.prepareStatement(query);

		            rs = ps.executeQuery();

		            JFrame overdueFrame = new JFrame("Patrons with Overdue Books");
		            overdueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            overdueFrame.getContentPane().setLayout(new BorderLayout());

		            JTable table = new JTable();
		            table.setModel(new DefaultTableModel(
		                new Object [][] {},
		                new String [] {"Patron_no", "ISBN", "Return_date"}
		            ));
		            DefaultTableModel model = (DefaultTableModel) table.getModel();

		            while(rs.next()) {
		                model.addRow(new Object[]{rs.getString("patron_no"), rs.getString("ISBN"), rs.getDate("return_date")});
		            }

		            JScrollPane scrollPane = new JScrollPane(table);
		            overdueFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		            overdueFrame.setSize(600, 400);
		            overdueFrame.setVisible(true);

		        } catch (Exception ex) {
		            ex.printStackTrace();
		        } finally {
		            try {
		                if(rs != null) rs.close();
		                if(ps != null) ps.close();
		                if(con != null) con.close();
		            } catch (SQLException se) {
		                se.printStackTrace();
		            }
		        }
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_1.setBounds(154, 138, 247, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Patron History");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 String patronID = JOptionPane.showInputDialog("Enter Patron ID:");

			        // Include "Title" in the table model
			        DefaultTableModel model = new DefaultTableModel(new String[]{"Patron No", "ISBN", "Title", "Date of Issue", "Return Date"}, 0);

			        try {
			            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
			            
			            // Updated SQL query to join with LibraryBooks and fetch the title
			            String sql = "SELECT issue_relation.patron_no, issue_relation.ISBN, LibraryBooks.title, issue_relation.date_of_issue, issue_relation.return_date " +
			                         "FROM issue_relation " +
			                         "JOIN LibraryBooks ON issue_relation.ISBN = LibraryBooks.ISBN " +
			                         "WHERE issue_relation.patron_no = ?";
			                         
			            PreparedStatement preparedStatement = con.prepareStatement(sql);
			            preparedStatement.setString(1, patronID);
			            ResultSet rs = preparedStatement.executeQuery();

			            while (rs.next()) {
			                model.addRow(new Object[]{rs.getString("patron_no"), rs.getString("ISBN"), rs.getString("title"), rs.getDate("date_of_issue"), rs.getDate("return_date")});
			            }

			        } catch (SQLException e1) {
			            e1.printStackTrace();
			        }

			        JTable table = new JTable(model);
			        JFrame frame = new JFrame("Patron History");
			        frame.setSize(600, 400);
			        frame.getContentPane().add(new JScrollPane(table));
			        frame.setVisible(true);
		    

			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_2.setBounds(154, 192, 247, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Fines Collected For Patron");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String patronID = JOptionPane.showInputDialog("Enter Patron ID:");
		        int totalFines = 0;

		        try {
		            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
		            
		            // SQL query to fetch sum of all fines for that patron
		            String sql = "SELECT SUM(fine_incurred) AS total_fines FROM issue_relation WHERE patron_no = ? and returnedBool=1";
		                         
		            PreparedStatement preparedStatement = con.prepareStatement(sql);
		            preparedStatement.setString(1, patronID);
		            ResultSet rs = preparedStatement.executeQuery();

		            if (rs.next()) {
		                totalFines = rs.getInt("total_fines");
		            }

		        } catch (SQLException e1) {
		            e1.printStackTrace();
		        }

		        // Display the result
		        JOptionPane.showMessageDialog(null, "Total fines for Patron ID " + patronID + ": Rs" + totalFines);
				
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_3.setBounds(162, 248, 239, 23);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Back");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(10, 11, 89, 23);
		contentPane.add(btnNewButton_4);
	}
}
