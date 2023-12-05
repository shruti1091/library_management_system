package demoFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 

public class addLibrarian extends JFrame implements Serializable {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField text_libID;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    addLibrarian frame = new addLibrarian();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public addLibrarian() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 547, 496);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblAdmin = new JLabel("Add New Librarian");
        lblAdmin.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblAdmin.setBounds(202, 51, 168, 45);
        contentPane.add(lblAdmin);

        text_libID = new JTextField();
        text_libID.setBounds(287, 177, 126, 32);
        contentPane.add(text_libID);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String librarianNo = generateAndDisplayLibrarianNo();
                if (librarianNo != null) {
                    // Display a success message
                    JOptionPane.showMessageDialog(contentPane, "Librarian added successfully with ID: " + librarianNo);
                    //dispose();
                }
            }
        });
        btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnAdd.setBounds(156, 326, 208, 45);
        contentPane.add(btnAdd);

        passwordField = new JPasswordField();
        passwordField.setBounds(287, 252, 126, 29);
        contentPane.add(passwordField);

        JLabel lblNewLabel = new JLabel("Enter Librarian Name");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel.setBounds(102, 177, 152, 32);
        contentPane.add(lblNewLabel);

        JLabel lblEnterPassword = new JLabel("Enter Password");
        lblEnterPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEnterPassword.setBounds(102, 252, 152, 32);
        contentPane.add(lblEnterPassword);
        
        JButton btnNewButton = new JButton("Home");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		home.main(new String[]{});
        		dispose();
        	}
        });
        btnNewButton.setBounds(10, 11, 89, 23);
        contentPane.add(btnNewButton);
    }

    @SuppressWarnings("resource")
	private String generateAndDisplayLibrarianNo() {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Connect to your database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");

            // Query to find the highest existing librarian number
            String query = "SELECT MAX(librarian_no) FROM librarians";
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            // Extract the numeric part and increment it
            int numericPart = 0;
            if (resultSet.next()) {
                String maxLibrarianNo = resultSet.getString(1);
                if (maxLibrarianNo != null) {
                    numericPart = Integer.parseInt(maxLibrarianNo.substring(1)) + 1;
                }
            }

            // Format the incremented numeric part with leading zeros
            String formattedNumericPart = String.format("%03d", numericPart);

            String librarianNo = "L" + formattedNumericPart; // This is the librarian number
            String librarianName = text_libID.getText(); // Assuming text_libID contains the librarian's name

            // Insert librarian data into the database
            String insertQuery = "INSERT INTO librarians (librarian_no, librarian_name, password) VALUES (?, ?, ?)";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, librarianNo);
            preparedStatement.setString(2, librarianName); // Use the librarian's name
            preparedStatement.setString(3, new String(passwordField.getPassword())); // Assuming passwordField contains the password
            preparedStatement.executeUpdate();

            // Display the librarian number in the text field
            text_libID.setText(librarianNo);

            return librarianNo;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null; // Return null to indicate failure
        } finally {
            // Close database resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
