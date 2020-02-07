package application;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import application.ProjectHoursCombo.PnoHoursCombo;

/**
 * This class handles the insertion employees into EMPLOYEE relation,
 * insertion of new assigned projects into WORKS_ON relation,
 * insertion of dependents into DEPENDENT relation,
 * and methods with queries that work directly with the COMPANY database.
 * @author Quang Vo
 */
public class CompanyDatabase {
	
	private static String DRIVERNAME = "oracle.jdbc.driver.OracleDriver";
	private static String DBINFO = "jdbc:oracle:thin:@apollo.vse.gmu.edu:1521:ite10g";
	private static String DBUSERNAME = "qvo8";
	private static String DBPASSW = "ptoams";
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;

	/**
	 * This initiates the connection to the COMPANY database.
	 * @throws SQLException
	 * @throws IOException
	 */
	public void initiateConnection() throws SQLException, IOException {
		try {
			Class.forName(DRIVERNAME);
		}catch(ClassNotFoundException x) {
			System.out.println("Driver could not be loaded");
		}
		
		this.conn = DriverManager.getConnection(DBINFO, DBUSERNAME, DBPASSW);
	}
	
	/**
	 * This checks in the COMPANY database for a valid manager's Ssn.
	 * @param ssn the entered ssn
	 * @return true if is a valid manager, false otherwise
	 * @throws SQLException
	 * @throws IOException
	 */
	public boolean SsnIsManager(String ssn) throws SQLException, IOException {
		
		initiateConnection();
		
		String mgrSSN = "";
		
		String query = "SELECT dname FROM DEPARTMENT WHERE mgrssn = ?";
		
		ps = conn.prepareStatement(query);
		ps.clearParameters();
		ps.setString(1, ssn);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			mgrSSN = rs.getString(1);
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		if(mgrSSN.isEmpty() || mgrSSN.length() == 0) {
			return false;
		}
		return true;
		
	}
	
	/**
	 * This retrieves the list of current department numbers in COMPANY database.
	 * @return the list of department numbers
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList<Integer> getCurrentDepartmentNo() throws SQLException, IOException{
		
		initiateConnection();
		
		ArrayList<Integer> departmentNoList = new ArrayList<Integer>();
		
		String query = "SELECT dnumber FROM DEPARTMENT";
		
		ps = conn.prepareStatement(query);
		ps.clearParameters();
		rs = ps.executeQuery();
		
		while(rs.next()) {
			int dnumber = rs.getInt("dnumber");
			departmentNoList.add(dnumber);
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		return departmentNoList;
	}
	
	//need to change
	/**
	 * Get the department number associated with the given project name
	 * @param projectName the given project name
	 * @return departmentNumber the department number associated with project name
	 * @throws SQLException
	 * @throws IOException
	 */
	public int getDnoForPname(String projectName) throws SQLException, IOException {
		
		initiateConnection();
		
		int departmentNumber = 0;
		
		String query = "SELECT dnum FROM PROJECT WHERE pname = ?";

		ps = conn.prepareStatement(query);
		ps.clearParameters();
		ps.setString(1, projectName);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			departmentNumber = rs.getInt(1);
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		return departmentNumber;
		
	}
	
	/**
	 * This retrieves the list of current projects in COMPANY database.
	 * @return the list of current projects
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList<String> getCurrentProjects() throws SQLException, IOException{
		
		initiateConnection();
		
		ArrayList<String> projectsList = new ArrayList<String>();
		
		String query = "SELECT pname FROM PROJECT";
		
		ps = conn.prepareStatement(query);
		ps.clearParameters();
		rs = ps.executeQuery();
		while(rs.next()) {
			String project = rs.getString("pname");
			projectsList.add(project);
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		return projectsList;
	}
	
	/**
	 * This converts the attributes of an employee into format that is supported by oracle,
	 * and insert the employee into EMPLOYEE relation.
	 * @param employee the new employee
	 * @throws SQLException
	 */
	public void insertEmployeeRelation(Employee employee) throws IOException, SQLException {
		
		initiateConnection();
		
		BigDecimal numberSalary = new BigDecimal(employee.getSalary());
		BigDecimal numberDeptNum = new BigDecimal(employee.getDepartmentNumber());

		String query = "INSERT INTO EMPLOYEE VALUES(?,?,?,?,?,?,?,?,?,?,null)";
		
		ps = conn.prepareStatement(query);
		ps.clearParameters();
		ps.setString(1, employee.getFirstName());
		ps.setString(2, employee.getMiddleInitial());
		ps.setString(3, employee.getLastName());
		ps.setString(4, employee.getSsn());
		ps.setDate(5, employee.getSQLBdate());
		ps.setString(6, employee.getAddress());
		ps.setString(7, employee.getSex());
		ps.setBigDecimal(8, numberSalary);
		ps.setString(9, employee.getSuperSsn());
		ps.setBigDecimal(10, numberDeptNum);
		ps.executeUpdate();
		
		rs.close();
		ps.close();
		conn.close();
		
	}
	
	/**
	 * This retrieves the employee associated with a unique ssn from EMPLOYEE relation
	 * @param ssn the unique ssn associated with an employee 
	 * @return employee an employee retrieved from EMPLOYEE relation
	 * @throws SQLException
	 * @throws IOException 
	 */
	public Employee getEmployeeFromDatabase(String checkingSsn) throws SQLException, IOException {

		initiateConnection();
		
		Employee employee = null;

		String query = "SELECT fname, minit, lname, ssn, bdate, address, sex, " +
						"salary, superssn, dno FROM EMPLOYEE WHERE ssn = ?";
		
		ps = conn.prepareStatement(query);
		ps.clearParameters();
		ps.setString(1, checkingSsn);
		rs = ps.executeQuery();
		
		while(rs.next()) {			
			String firstName = rs.getString(1);
			String middleInitial = rs.getString(2);
			String lastName = rs.getString(3);
			String ssn = rs.getString(4);
			String birthDate = rs.getDate(5).toString();
			String address = rs.getString(6);
			String sex = rs.getString(7);
			int salary = rs.getInt(8);
			String superSsn = rs.getString(9);
			int departmentNumber = rs.getInt(10);
			
			employee = new Employee(firstName, middleInitial, lastName, sex, birthDate, 
					address, ssn, superSsn, salary, departmentNumber);
		}

		rs.close();
		ps.close();
		conn.close();
		
		return employee;
	}
	
	/**
	 * This retrieves the list of assigned projects of the employee from WORKS_ON and PROJECT relations
	 * @param employee the employee we need to retrieve the assigned projects for
	 * @return the list of assigned projects
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList<ProjectHoursCombo> getEmployeeProjectsFromDatabase(Employee employee) throws SQLException, IOException {
		
		initiateConnection();
		
		ArrayList<ProjectHoursCombo> assignedProjects = new ArrayList<ProjectHoursCombo>();
		ArrayList<ProjectHoursCombo.PnoHoursCombo> pnoHoursComboList  = new ArrayList<PnoHoursCombo>();
		ProjectHoursCombo PHcombo = new ProjectHoursCombo();
		PnoHoursCombo pnoHoursCombo = null;
		
		String pnoQuery = "SELECT pno, hours FROM WORKS_ON WHERE essn = ?";
		
		ps = conn.prepareStatement(pnoQuery);
		ps.clearParameters();
		ps.setString(1, employee.getSsn());
		rs = ps.executeQuery();
		
		while(rs.next()) {
						
			BigDecimal pno = rs.getBigDecimal(1);
			BigDecimal hours = rs.getBigDecimal(2);
			PHcombo.setPnoHours(pno, hours);
			pnoHoursCombo = PHcombo.getPnoHours();
			pnoHoursComboList.add(pnoHoursCombo);
			
			
		}
		
		String pnameQuery = "SELECT pname FROM PROJECT WHERE pnumber = ?";
		
		ps.close();
		ps = conn.prepareStatement(pnameQuery);
		
		for(int i = 0; i < pnoHoursComboList.size(); i++) {
			ps.clearParameters();
			ps.setBigDecimal(1, pnoHoursComboList.get(i).getPno());
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String projectName = rs.getString(1);
				double projectHours = pnoHoursComboList.get(i).getHours().doubleValue();
				
				PHcombo = new ProjectHoursCombo(projectName, projectHours);
				assignedProjects.add(PHcombo);
			}
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		return assignedProjects;
	}
	
	/**
	 * This retrieves the list of dependents of the employee from DEPENDENT relation
	 * @param employee the employee we need to retrieve the dependents for
	 * @throws SQLException
	 * @throws IOException 
	 */
	public void getEmployeeDependentsFromDatabase(Employee employee) throws SQLException, IOException { 
		
		initiateConnection();
		
		ArrayList<Dependent> dependentsList = new ArrayList<Dependent>();
		Dependent dependent = null;

		String query = "SELECT dependent_name, sex, bdate, relationship "
						+ "FROM DEPENDENT WHERE essn = ?";
		
		ps = conn.prepareStatement(query);
		ps.clearParameters();
		ps.setString(1, employee.getSsn());
		rs = ps.executeQuery();
		
		while(rs.next()) {
			
			String dependentFirstName = rs.getString(1);
			String dependentSex = rs.getString(2);
			String dependentBirthdate = rs.getDate(3).toString();
			String dependentRelationship = rs.getString(4);
			
			dependent = new Dependent(dependentFirstName, dependentSex, dependentBirthdate, dependentRelationship);
			dependentsList.add(dependent);
		}
		
		employee.setDependents(dependentsList);
		
		rs.close();
		ps.close();
		conn.close();
		
	}
	
	/**
	 * This uses the (projectName, hours) combo to retrieve the associated project numbers pno
	 * from COMPANY database, then inserts the (projectNumber, hours) combo into WORKS_ON relation.
	 * @param employee the new employee
	 * @throws SQLException
	 */
	public void insertWorksOnRelation(Employee employee) throws SQLException, IOException {
		
		String Pname = "";
		BigDecimal Pno = null;
		BigDecimal Hours = null;
		
		ArrayList<ProjectHoursCombo> assignedProjects = employee.getAssignedProjects();
		ArrayList<ProjectHoursCombo.PnoHoursCombo> projectNumbers = new ArrayList<PnoHoursCombo>();
		ProjectHoursCombo PHcombo = null;
		PnoHoursCombo combo = null;
		
		for(int i = 0; i < assignedProjects.size(); i++) {
			Pname = employee.getProjectName(i);
			Pno = getProjectNumber(Pname); //call method to get Pno here
			Hours = new BigDecimal(employee.getProjectHours(i));	
			PHcombo = assignedProjects.get(i);
			PHcombo.setPnoHours(Pno, Hours);
			combo = PHcombo.getPnoHours();
			projectNumbers.add(combo);
		}
		
		String query = "INSERT INTO WORKS_ON VALUES(?,?,?)";
		
		initiateConnection();
		
		for(int i = 0; i < projectNumbers.size(); i++) {
			combo = projectNumbers.get(i);
			ps = conn.prepareStatement(query);
			ps.clearParameters();
			ps.setString(1, employee.getSsn());
			ps.setBigDecimal(2, combo.getPno());
			ps.setBigDecimal(3, combo.getHours());
			ps.executeUpdate();
		}
		
		ps.close();
		conn.close();
		
	}
	
	/**
	 * This retrieves the project number associated with the given project name from PROJECT relation.
	 * @param projectName the given project name
	 * @return pno associated with the project name
	 * @throws SQLException
	 * @throws IOException 
	 */
	public BigDecimal getProjectNumber(String projectName) throws SQLException, IOException {
		
		initiateConnection();
		
		BigDecimal projectNumber = null;

		String query = "SELECT pnumber FROM PROJECT WHERE pname = ?";
		
		ps = conn.prepareStatement(query);
		ps.clearParameters();
		ps.setString(1, projectName);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			projectNumber = rs.getBigDecimal(1);
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		return projectNumber;
		
	}
	
	/**
	 * This inserts the dependents of an employee into the DEPENDENT relation.
	 * @param employee the new employee
	 * @throws SQLException
	 */
	public void insertDependentRelation(Employee employee) throws SQLException, IOException {

		initiateConnection();
		
		ArrayList<Dependent> listOfDependents = employee.getDependents();;
		Dependent newDependent;
		
		String query = "INSERT INTO DEPENDENT VALUES(?,?,?,?,?)";

		for(int i = 0; i < listOfDependents.size();i++) {
			newDependent = listOfDependents.get(i);
			ps = conn.prepareStatement(query);
			ps.clearParameters();
			ps.setString(1, employee.getSsn());
			ps.setString(2, newDependent.getFirstName());
			ps.setString(3, newDependent.getSex());
			ps.setDate(4, newDependent.getSQLBirthDate());
			ps.setString(5,newDependent.getRelationship());
			ps.executeUpdate();
			
		}
		
		ps.close();
		conn.close();

	}
	
	
		
	
}
