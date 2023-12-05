package demoFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Search extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField text_title;
    private JTextField text_author;
    private JFrame resultFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Search frame = new Search();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Search() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 547, 496);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel label_heading = new JLabel("Search by book title or author");
        label_heading.setFont(new Font("Tahoma", Font.PLAIN, 20));
        label_heading.setBounds(142, 46, 287, 60);
        contentPane.add(label_heading);

        JLabel label_title = new JLabel("Title");
        label_title.setFont(new Font("Tahoma", Font.PLAIN, 15));
        label_title.setBounds(147, 164, 70, 46);
        contentPane.add(label_title);

        JLabel label_author = new JLabel("Author");
        label_author.setFont(new Font("Tahoma", Font.PLAIN, 15));
        label_author.setBounds(142, 251, 70, 46);
        contentPane.add(label_author);

        text_title = new JTextField();
        text_title.setBounds(259, 174, 119, 31);
        contentPane.add(text_title);
        text_title.setColumns(10);

        text_author = new JTextField();
        text_author.setBounds(259, 261, 119, 31);
        contentPane.add(text_author);
        text_author.setColumns(10);

        JButton button_search = new JButton("Search");
        button_search.setFont(new Font("Tahoma", Font.PLAIN, 14));
        button_search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = text_title.getText();
                String author = text_author.getText();

                // Call a method to fetch data based on the search criteria
                searchBooks(title, author);
            }
        });
        button_search.setBounds(199, 363, 119, 46);
        contentPane.add(button_search);
        
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

    private void searchBooks(String title, String author) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti"); 
            StringBuilder sql = new StringBuilder("SELECT * FROM LibraryBooks WHERE ");
            boolean hasTitle = !title.trim().isEmpty();
            boolean hasAuthor = !author.trim().isEmpty();
            
            if (hasTitle) {
                sql.append("title LIKE ?");
            }
            
            if (hasAuthor) {
                if (hasTitle) {
                    sql.append(" OR ");
                }
                sql.append("author LIKE ?");
            }
            
            if (!hasTitle && !hasAuthor) {
                JOptionPane.showMessageDialog(null, "Please enter a search criteria.");
                return;
            }

            PreparedStatement preparedStatement = con.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (hasTitle) {
                preparedStatement.setString(paramIndex, "%" + title + "%");
                paramIndex++;
            }
            
            if (hasAuthor) {
                preparedStatement.setString(paramIndex, "%" + author + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "No books found with the given criteria.");
                return;
            }

            resultFrame = new JFrame("Search Results");
            resultFrame.setBounds(100, 100, 600, 400);
            resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            resultFrame.getContentPane().setLayout(new BorderLayout());

            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);
            resultFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            resultFrame.setVisible(true);

            resultSet.close();
            preparedStatement.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private DefaultTableModel buildTableModel(ResultSet resultSet) {
        try {
            java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                tableModel.addColumn(metaData.getColumnLabel(columnIndex));
            }
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                tableModel.addRow(rowData);
            }
            return tableModel;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
