package application;
import java.io.IOException;
import java.sql.*;

/**
 * Part 1 of the CS450 project
 * @author Quang Vo
 */
public class Part1 {
	
	private static String DRIVERNAME = "oracle.jdbc.driver.OracleDriver";
	private static String DBINFO = "jdbc:oracle:thin:@apollo.vse.gmu.edu:1521:ite10g";
	private static String DBUSERNAME = "qvo8";
	private static String DBPASSW = "ptoams";
	private static Connection conn;

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
		
		initiateConnection();
		
		System.out.println("Query 1: ");
		printProblem1();
		System.out.println("");
		System.out.println("Query 2: ");
		printProblem2();		

		conn.close();
	}
	
	/**
	 * This method initiates the connection to Oracle Database
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void initiateConnection() throws SQLException, IOException {
		try {
			Class.forName(DRIVERNAME);
		}catch(ClassNotFoundException x) {
			System.out.println("Driver could not be loaded");
		}
		
		conn = DriverManager.getConnection(DBINFO, DBUSERNAME, DBPASSW);
	}
	
	/**
	 * A program segment that retrieves the employees who work in the Research department
	 * and print the employee’s last name and their SSN
	 * @throws SQLException, IOException
	 */
	public static void printProblem1() throws SQLException, IOException {
		
		String query = "SELECT lname, ssn FROM EMPLOYEE, DEPARTMENT WHERE dno = dnumber AND dname = 'Research'";
		
		PreparedStatement ps = conn.prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			String lname = rs.getString("lname");
			String ssn = rs.getString("ssn");
			System.out.println(lname + " " + ssn);
		}
	}
	
	/**
	 * A program segment that retrieves the employees who work in departments located in Houston 
	 * and work on the project ‘ProductZ’. List their last name, SSN, and the number of hours 
	 * that the employee works on that project.
	 * @throws SQLException, IOException
	 */
	public static void printProblem2() throws SQLException, IOException {
		
		String query = "SELECT lname, ssn, SUM(hours) " + 
				"FROM EMPLOYEE, WORKS_ON " + 
				"WHERE DNO IN " + 
				"(SELECT dnumber FROM DEPT_LOCATIONS WHERE dlocation = 'Houston') " + 
				"AND ssn = essn " + 
				"AND pno IN " + 
				"(SELECT pnumber FROM PROJECT WHERE pname = 'ProductZ') " + 
				"GROUP BY lname, ssn";
		
		PreparedStatement ps = conn.prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			String lname = rs.getString("lname");
			String ssn = rs.getString("ssn");
			float hours = rs.getFloat("SUM(hours)");
			System.out.println(lname + " " + ssn + " " + hours);
			
		}
		
	}
}


