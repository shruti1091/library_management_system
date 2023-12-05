package demoFrame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class addBookForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField title;
    private JTextField author;
    private JTextField availability;
    private JTextField shelf_no;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    addBookForm frame = new addBookForm();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public addBookForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 525, 605);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 240, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Add Book Form");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setBounds(200, 57, 151, 36);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_3 = new JLabel("Category");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_3.setBounds(74, 167, 79, 20);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("Title");
        lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_4.setBounds(74, 300, 79, 14);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("Author");
        lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_5.setBounds(74, 350, 64, 14);
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Availability");
        lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_6.setBounds(71, 400, 96, 14);
        contentPane.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("Shelf number");
        lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_7.setBounds(77, 450, 113, 14);
        contentPane.add(lblNewLabel_7);

        title = new JTextField();
        title.setColumns(10);
        title.setBounds(248, 300, 154, 20);
        contentPane.add(title);

        author = new JTextField();
        author.setColumns(10);
        author.setBounds(248, 350, 154, 20);
        contentPane.add(author);

        availability = new JTextField();
        availability.setColumns(10);
        availability.setBounds(248, 400, 154, 20);
        contentPane.add(availability);

        shelf_no = new JTextField();
        shelf_no.setColumns(10);
        shelf_no.setBounds(248, 450, 154, 20);
        contentPane.add(shelf_no);

        final JRadioButton instOnlyButton = new JRadioButton("for instructors only");
        instOnlyButton.setBounds(236, 133, 132, 23);
        contentPane.add(instOnlyButton);

        final JRadioButton toLendButton = new JRadioButton("To Lend");
        toLendButton.setBounds(236, 182, 111, 23);
        contentPane.add(toLendButton);

        final JRadioButton libUseButton = new JRadioButton("in library use only");
        libUseButton.setBounds(236, 232, 132, 23);
        contentPane.add(libUseButton);

        JButton btnNewButton = new JButton("Add");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");

                    String category = null;
                    if (instOnlyButton.isSelected()) {
                        category = "for instructors only";
                    } else if (toLendButton.isSelected()) {
                        category = "To Lend";
                    } else if (libUseButton.isSelected()) {
                        category = "in library use only";
                    }

                    // Generate the ISBN
                    // Generate the ISBN
                    String generatedISBN = generateISBN();

                    // Check if a book with the same title and author already exists
                    String existingBookCode = checkForExistingBook(title.getText(), author.getText(), con);

                    String book_code;
					if (existingBookCode != null) {
                        // If a book with the same title and author exists, use the same book code
                        book_code=(existingBookCode);
                    } else {
                        // If not, generate a new book code
                        String departmentAbbreviation = JOptionPane.showInputDialog("Enter Department Abbreviation (e.g., CSE):");
                        String generatedBookCode = departmentAbbreviation + generateBookCode();
                        book_code=(generatedBookCode);
                    }
					String shelfNumber = shelf_no.getText();
                    
                    // Check if the entered shelf number matches the specified format "ax-xxx"
                    if (isShelfNumberValid(shelfNumber)) {
                        // Shelf number is in the correct format
                        // Proceed with adding the book
                    } else {
                        JOptionPane.showMessageDialog(null, "Shelf number is not in the correct format 'ax-xxx'");
                        return; // Exit without adding the book
                    }
                    if (generatedISBN.isEmpty() || book_code.isEmpty() || category.isEmpty() || 
                    	    title.getText().isEmpty() || author.getText().isEmpty() || 
                    	    availability.getText().isEmpty() || shelf_no.getText().isEmpty()) {
                    	    JOptionPane.showMessageDialog(null, "All fields must be filled out.");
                    	    return;
                    	}

                    String sql = "INSERT INTO librarybooks (ISBN, book_code, category, title, author, availability, shelf_no) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, generatedISBN);
                    pstmt.setString(2, book_code);
                    pstmt.setString(3, category);
                    pstmt.setString(4, title.getText());
                    pstmt.setString(5, author.getText());
                    pstmt.setString(6, availability.getText());
                    pstmt.setString(7, shelf_no.getText());
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book details added successfully");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnNewButton.setBounds(200, 500, 89, 23);
        contentPane.add(btnNewButton);
    }

    private String generateISBN() {
        Random rand = new Random();
        int code = rand.nextInt(90000000) + 10000000; // Generates a random 8-digit number
        return String.valueOf(code);
    }

    private String generateBookCode() {
        Random rand = new Random();
        int code = rand.nextInt(9000) + 1000; // Generates a random 4-digit number
        return String.valueOf(code);
    }

    private String checkForExistingBook(String bookTitle, String bookAuthor, Connection connection) {
        try {
            String sql = "SELECT book_code FROM librarybooks WHERE title = ? AND author = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, bookTitle);
            pstmt.setString(2, bookAuthor);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("book_code");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return null;
    }
    private boolean isShelfNumberValid(String shelfNumber) {
        // Define the regular expression pattern for "sX-XXX" format
        String regex = "\\w\\d{1}-\\d{3}";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(shelfNumber);

        // Check if the entered shelf number matches the pattern
        return matcher.matches();
    }

}
