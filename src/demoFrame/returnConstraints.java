package demoFrame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
public class returnConstraints {

	public int isbnPresence(String isbn) {
		//0 not present error
		//1 present under faculty
		//2 present under student
		try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
            String sql = "select count(*) as present, patron_no from issue_relation where isbn=? and returnedBool=0 GROUP BY patron_no";
            PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
	            int presence=resultSet.getInt("present");
	            String patron_id=resultSet.getString("patron_no");
	            if(presence<=0) return 0; 
	            else {
	            	try {
	                	Class.forName("com.mysql.cj.jdbc.Driver");
	                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
	                    String sql1 = "select count(*) as present, user_type from patron_details where patron_no=? GROUP BY patron_no";
	                    PreparedStatement preparedStatement1 = connection.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
	                    preparedStatement1.setString(1, patron_id);
	                    ResultSet resultSet1 = preparedStatement1.executeQuery();
	                    if(resultSet1.next()) {
	                    	int presence1=resultSet1.getInt("present");
	                    
		                    if(presence1<=0) return 0; 
		                    else {
		                    	String type=resultSet1.getString("user_type");
		                    	if(type.equals("Student")) return 2;
		                    	else if(type.equals("Faculty")) return 1;
		                    	
		                    }
	                    }
	                 
	
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	            	
	            }
            }
         

        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return 0;
	}

		public boolean returnedOnSameDay(String isbn) {
		try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
            String sql = "SELECT date_of_issue FROM issue_relation WHERE isbn=? AND returnedBool=0";
            PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, isbn);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
	            java.sql.Date sqlDate = resultSet.getDate("date_of_issue");
	            //System.out.println(sqlDate);
	            // Get the current date
	            java.util.Date currentDate = new java.util.Date();
	
	            Calendar cal1 = Calendar.getInstance();
	            Calendar cal2 = Calendar.getInstance();
	            cal1.setTime(sqlDate);
	            cal2.setTime(currentDate);

	            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
	                              cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

	            if (sameDay) {
	                return true;
	            }
            }
            
            

        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return false;
	}
	


}
