package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class Database {

	private final String USERNAME = "root";
	private final String PASSWORD  = "";
	private final String DATABASE = "stellarfest";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	public ResultSet rs;
	public java.sql.ResultSetMetaData rsm;
	
	private Connection con;
	private Statement st;
	private static Database connect;
	
	public static Database getInstance() {
		if(connect == null) return new Database();
		return connect;
	}
	
	
	private Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	 public void execUpdate(String query) {
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            e.printStackTrace();  
	        }
	    }

	    
	    public PreparedStatement prepareStatement(String query) {
	        PreparedStatement ps = null;
	        try {
	            ps = con.prepareStatement(query);
	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }
	        return ps;
	    }

	    
	    public void close() {
	        try {
	            if (rs != null) rs.close();
	            if (st != null) st.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();  
	        }
	    } 

}
