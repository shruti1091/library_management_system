package demoFrame;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class addNewPatron extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JRadioButton rdbtnStudent;
    private JRadioButton rdbtnFaculty;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    addNewPatron frame = new addNewPatron();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public addNewPatron() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 534, 422);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Enter Patron Name");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setBounds(62, 129, 134, 30);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(226, 136, 201, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Select Patron Type");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_2.setBounds(62, 193, 134, 14);
        contentPane.add(lblNewLabel_2);

        JButton btnNewButton = new JButton("Submit");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Receive "name" and "patron type" from the user interface (Swing).
                String name = textField.getText().trim(); // Get the name from the text field and remove leading/trailing spaces

                // Check if the name field is empty
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter Patron Name.");
                } else if (!rdbtnStudent.isSelected() && !rdbtnFaculty.isSelected()) {
                    // Check if a Patron Type is selected
                    JOptionPane.showMessageDialog(null, "Please select a Patron Type.");
                } else {
                    String patronType = (rdbtnStudent.isSelected()) ? "Student" : "Faculty"; // Determine patron type based on radio button selection

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");

                        // Generate the patron_no
                        String patronNo = generateUniquePatronNo(con);

                        String sql = "INSERT INTO patron_details (patron_no, user_type, patron_name) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, patronNo);
                        preparedStatement.setString(2, patronType);
                        preparedStatement.setString(3, name);

                        int affectedRows = preparedStatement.executeUpdate();

                        if (affectedRows > 0) {
                            JOptionPane.showMessageDialog(null, "The patron has been successfully added with the ID: " + patronNo);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Insertion failed");
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton.setBounds(203, 282, 89, 23);
        contentPane.add(btnNewButton);

        rdbtnStudent = new JRadioButton("Student");
        rdbtnStudent.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rdbtnStudent.setBounds(226, 191, 111, 23);
        contentPane.add(rdbtnStudent);

        rdbtnFaculty = new JRadioButton("Faculty");
        rdbtnFaculty.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rdbtnFaculty.setBounds(346, 191, 81, 23);
        contentPane.add(rdbtnFaculty);
        
        // Create a ButtonGroup to ensure only one radio button can be selected
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rdbtnStudent);
        buttonGroup.add(rdbtnFaculty);
    }

    private String generateUniquePatronNo(Connection con) {
        String patronNoPrefix = "pt";
        int defaultNumber = 1; // Default patron number
        
        try {
            // Query the database to find the last used patron_no
            String query = "SELECT MAX(patron_no) FROM patron_details";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String lastPatronNo = resultSet.getString(1);
                if (lastPatronNo != null) {
                    // Extract the numeric part, increment it, and format it as "0000"
                    int lastNumber = Integer.parseInt(lastPatronNo.substring(2));
                    int newNumber = lastNumber + 1;
                    return patronNoPrefix + String.format("%04d", newNumber);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return patronNoPrefix + String.format("%04d", defaultNumber);
    }
}
