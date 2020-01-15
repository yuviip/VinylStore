package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Classes.Customer;
import Classes.Vinyl;
import Exceptions.IllegalVinylPrice;
import enums.City;
import enums.Condition;
import enums.Format;

public class DBVinylStore {

	static String connectionUrl = "jdbc:sqlserver://localhost;databaseName=VinylStore;integratedSecurity=true;" ;  

	public ResultSet rs;
	public static Connection con;  
	public static Statement stmt;

	public static void main(String[] args) {

		DBVinylStore obj = new DBVinylStore();

	}

	public DBVinylStore() {
		con = null;
		stmt = null;
	}

	public void open() {
		try {  
			// Establish the connection.  
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
			con = DriverManager.getConnection(connectionUrl);  

		} catch (ClassNotFoundException|SQLException  e ) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void close() {
		if (rs != null) try { rs.close(); } catch(Exception e) {}  
		if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	}

	public void executeStatement(String sql) {
		try {

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean containCustomer(String customerID, String userName) {

		String sql = "SELECT COUNT(CustomerID) as 'count'\r\n" + 
				"FROM Customer\r\n" + 
				"WHERE CustomerID = '" + customerID + "' OR UserName = '" + userName + "'";

		try {

			open();

			executeStatement(sql);
			while(rs.next()) {
				if (rs != null && rs.getInt("count") > 0) {
					return true;
				}
				else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {  
			close();
		}

		return false;


	}

	public void addCustomer(Customer newCustomer) throws SQLException {	

		String sql = "INSERT INTO Customer\r\n" + 
				"VALUES ('" + newCustomer.getID() + "', '" + newCustomer.getUsername() + "', '" + newCustomer.getPassword() + "', '" + newCustomer.getFirstName() + "', '" + newCustomer.getLastName() + "', '" + newCustomer.getPhoneNumber() + "', '" + newCustomer.getEmail() + "', '" + newCustomer.getAddress().getCity().toString() + "', '" + newCustomer.getAddress().getStreet() + "', '" + newCustomer.getAddress().getNumber() + "', '" + newCustomer.getAddress().getZipCode() + "');";

		try {

			open();

			executeStatement(sql);

		} finally {  
			close();
		}

	}

	public ArrayList<Vinyl> getProducts(){
		String sql = "SELECT *" + 
				"FROM Vinyl";
		
		ArrayList<Vinyl> products = new ArrayList<>();

		try {

			open();

			executeStatement(sql);
			while(rs.next()) {
				
				if (rs != null) {
					try {
						Vinyl vinyl = new Vinyl(rs.getInt("vinylID"), rs.getString("VinylName"), rs.getString("ReleaseYear"), 
												rs.getString("Descript"), getFormat(rs.getString("VinylFormat")), getCondition(rs.getString("Condition")), 
												(int)rs.getFloat("discount"), rs.getFloat("price"));
						products.add(vinyl);
					} catch (IllegalVinylPrice e) {
						e.printStackTrace();
					}
				}

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {  
			close();
		}

		return products;
	}
	
	public int getProductsCount() {
		String sql = "SELECT COUNT(vinylID) as 'count'\r\n" + 
				"FROM Vinyl";

		try {

			open();

			executeStatement(sql);
			while(rs.next()) {

				if (rs != null) {
					return rs.getInt("count");
				}

				else
					return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {  
			close();
		}

		return -1;

	}

	private City getCity(String city) {
		City returnCity = null;

		if (city.equals("Afula"))
			returnCity = City.Afula;

		else if (city.equals("Haifa"))
			returnCity = City.Haifa;

		else if (city.equals("TLV"))
			returnCity = City.TLV;

		else if (city.equals("Other"))
			returnCity = City.Other;

		return returnCity;
	}

	private Format getFormat(String format) {
		Format returnFormat = null;
		
		if (format.equals("LP"))
			returnFormat = Format.LP;
		
		else if (format.equals("EP"))
			returnFormat = Format.EP;
		
		else if (format.equals("SEVEN"))
			returnFormat = Format.SEVEN;
		
		else if (format.equals("TEN"))
			returnFormat = Format.TEN;
		
		else if (format.equals("TWELVE_SINGLE"))
			returnFormat = Format.TWELVE_SINGLE;
		
		else if (format.equals("FORTY_FIVE_RPM"))
			returnFormat = Format.FORTY_FIVE_RPM;
		
		else if (format.equals("SEVENTY_EIGHT_RPM"))
			returnFormat = Format.SEVENTY_EIGHT_RPM;
		
		return returnFormat;
		
		
	}
	
//	'old', 'BrandNew', 'Used', 'damaged'
	
	private Condition getCondition(String condition) {
		Condition returnCondition = null;
		
		if (condition.equals("old"))
			returnCondition = Condition.old;
		
		else if (condition.equals("BrandNew"))
			returnCondition = Condition.BrandNew;
		
		else if (condition.equals("Used"))
			returnCondition = Condition.Used;
		
		else if (condition.equals("damaged"))
			returnCondition = Condition.damaged;
	
		return returnCondition;
	}

}