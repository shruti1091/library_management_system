package demoFrame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class issueConstraints {

	public int patronIdPresence(String patron_id) {
		//0- patron not present
		//1- patron is faculty
		//2- patron is student
		
		try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
            String sql = "select count(*) as present, user_type from patron_details where patron_no=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, patron_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int presence = resultSet.getInt("present");
                if (presence == 0) return 0;
                else {
                	//resultSet.next();
                    String type = resultSet.getString("user_type");
                    if ("Student".equals(type)) return 2;
                    else if ("Faculty".equals(type)) return 1;
                }
            }
         

        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return 0;
		
		
	}

	public boolean patronMaxBooksReached(String patron_id, int user_type) {
		boolean flag=false;
		try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
            String sql = "select count(*) as count from issue_relation where patron_no=? and returnedBool=0";
            PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, patron_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int numOfBookBorrowed=resultSet.getInt("count");
                if(user_type==1 && numOfBookBorrowed>=6) flag=true; 
                if(user_type==2 && numOfBookBorrowed>=3) flag=true;
            }
             
         

        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return flag;
	}

	public boolean isbnPresence(String isbn) {
		
		boolean flag=true;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
            String sql = "select count(*) as present from LibraryBooks where ISBN=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int presence=resultSet.getInt("present");
                if(presence<=0) flag=false; 
            }
            
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return flag;
	}
	
	public boolean isbnAvailable(String isbn) {
		boolean flag=true;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
            String sql = "select availability from LibraryBooks where ISBN=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	String presence=resultSet.getString("availability");
                if(presence.equals("lent")) flag=false;  
            }
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return flag;
	}

	public int isbnForLending(String isbn) {
		//1- libray use only
		//2- instructors only
		//3- to lend
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "harishruti");
            String sql = "select category from LibraryBooks where ISBN=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	String cat=resultSet.getString("category");
                if(cat.equals("in library use")) return 1;
                else if(cat.equals("for instructors")) return 2;
                else if(cat.equals("to lend")) return 3;
            	 
            }
            
           
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return 0;
	}

}
