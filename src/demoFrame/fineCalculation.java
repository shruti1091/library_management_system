package demoFrame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class fineCalculation {


	public boolean collectFine(String isbn, int fineAmount) {
		
		final boolean[] isPaid = {false};
        final JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Collect Fine");
        dialog.setSize(300, 200);  // Set the dimensions of the dialog

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new FlowLayout()); // Panel for buttons

        JLabel fineLabel = new JLabel("Fine Amount: Rs." + fineAmount);
        fineLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the label

        JButton paidButton = new JButton("Paid");
        JButton notPaidButton = new JButton("Not Paid");

        // Set the dimensions of the buttons
        paidButton.setPreferredSize(new Dimension(100, 50));
        notPaidButton.setPreferredSize(new Dimension(100, 50));

        paidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPaid[0] = true;
                dialog.dispose();
            }
        });

        notPaidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPaid[0] = false;
                dialog.dispose();
            }
        });

        buttonPanel.add(paidButton);
        buttonPanel.add(notPaidButton);

        mainPanel.add(fineLabel);
        mainPanel.add(buttonPanel);

        dialog.getContentPane().add(mainPanel);
        dialog.setVisible(true);

        return isPaid[0];
    }

    
	

	public int calculateFine(String isbn, int user_type_int) {
		// TODO Auto-generated method stub
		int fine_amount=0;
		try {
         	Class.forName("com.mysql.cj.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
             String sql = "select return_date from issue_relation where isbn=? and returnedBool=0";
             PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
             preparedStatement.setString(1, isbn);
             ResultSet resultSet = preparedStatement.executeQuery();
             if (resultSet.next()) {
            	// Get the return date from the database
                 java.sql.Date sqlReturnDate = resultSet.getDate("return_date");
                 
                 // Convert to java.util.Date
                 Date returnDate = new Date(sqlReturnDate.getTime());
                 
                 // Get current date
                 java.util.Date currentDate = new java.util.Date();
                 
                 // Calculate the number of days
                 long diff = currentDate.getTime() - returnDate.getTime();
                 int daysPassed = (int) (diff / (24 * 60 * 60 * 1000));
                 
                 if(daysPassed>0) {
                	 if(user_type_int==1) {
                		 //faculty
                		 fine_amount=daysPassed*5;
                	 }
                	 else if(user_type_int==2) {
                		 //student
                		 fine_amount=daysPassed*7;
                	 }
                	 
                	 return fine_amount;
                 }
                 else return 0;
                 
             } else {
                 return -1;
             }
            
             

         } catch (Exception e1) {
             e1.printStackTrace();
         }
		return -1;
	}
	
	public static void main(String args[]) {
		fineCalculation fine = new fineCalculation();
		fine.collectFine("12345678",25);
	}

}
