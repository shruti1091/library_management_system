package demoFrame;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JPasswordField;

public class LibrarianLogin extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField text_libID;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LibrarianLogin frame = new LibrarianLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LibrarianLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 547, 496);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel label_libLogin = new JLabel("Librarian Login");
        label_libLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
        label_libLogin.setBounds(202, 51, 168, 45);
        contentPane.add(label_libLogin);

        JLabel label_libID = new JLabel("Enter Librarian ID");
        label_libID.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label_libID.setBounds(117, 170, 111, 45);
        contentPane.add(label_libID);

        JLabel label_pwd = new JLabel("Enter Password");
        label_pwd.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label_pwd.setBounds(117, 268, 111, 33);
        contentPane.add(label_pwd);

        text_libID = new JTextField();
        text_libID.setBounds(287, 177, 126, 32);
        contentPane.add(text_libID);
        text_libID.setColumns(10);

        JButton button_login = new JButton("Login");
        button_login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the entered librarian number and password
                String librarianNo = text_libID.getText();
                String password = new String(passwordField.getPassword());

                if (librarianNo.isEmpty() || password.isEmpty()) {
                    if (librarianNo.isEmpty() && password.isEmpty()) {
                        // Both fields are empty
                        JOptionPane.showMessageDialog(contentPane, "Please enter both Librarian ID and Password.");
                    } else if (librarianNo.isEmpty()) {
                        // Librarian ID is not entered
                        JOptionPane.showMessageDialog(contentPane, "Please enter Librarian ID.");
                    } else {
                        // Password is not entered
                        JOptionPane.showMessageDialog(contentPane, "Please enter Password.");
                    }
                } else {
                    // Authenticate librarian
                    int loginStatus = authenticateLibrarian(librarianNo, password);
                    switch (loginStatus) {
                        case 0:
                            // Login successful, perform the necessary actions
                            JOptionPane.showMessageDialog(contentPane, "Login successful");
                            LibrarianMenu.main(new String[]{});
                            dispose();
                            break;
                        case 1:
                            // Incorrect librarian ID
                            JOptionPane.showMessageDialog(contentPane, "Librarian ID does not exist, please check.");
                            break;
                        case 2:
                            // Incorrect password
                            JOptionPane.showMessageDialog(contentPane, "Incorrect Password, please check.");
                            break;
                        default:
                            // Error occurred
                            JOptionPane.showMessageDialog(contentPane, "An error occurred during login.");
                    }
                }
            }
        });


        button_login.setFont(new Font("Tahoma", Font.PLAIN, 13));
        button_login.setBounds(202, 361, 112, 33);
        contentPane.add(button_login);

        passwordField = new JPasswordField();
        passwordField.setBounds(287, 269, 126, 33);
        contentPane.add(passwordField);
    }

    private int authenticateLibrarian(String librarianNo, String password) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Connect to your database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");

            // Query the librarians table to find a matching row
            String query = "SELECT * FROM librarians WHERE librarian_no = ?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, librarianNo);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Check if the password matches
                if (resultSet.getString("password").equals(password)) {
                    // Successful login
                    return 0;
                } else {
                    // Incorrect password
                    return 2;
                }
            } else {
                // Incorrect librarian ID
                return 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1; // Error occurred
        } finally {
            // Close database resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
