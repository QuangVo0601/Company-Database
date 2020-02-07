package application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class contains all the attributes of an EMPLOYEE in COMPANY database.
 * @author Quang Vo
 */
public class Employee {
	
	private ArrayList<ProjectHoursCombo> assignedProjects = new ArrayList<ProjectHoursCombo>();
	private ArrayList<Dependent> dependents = new ArrayList<Dependent>();
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String sex;
	private String address;
	private String birthDate;
	private java.sql.Date sqlBdate;
	private String ssn;
	private String superSsn;
	private int salary;
	private int departmentNumber;
	
	public Employee(String firstName, String middleInitial, String lastName, String sex, String birthDate, String address,
			 String ssn, String superSsn, int salary, int departmentNumber) {
		super();
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.sex = sex;
		this.birthDate = birthDate;
		this.address = address;
		this.ssn = ssn;
		this.superSsn = superSsn;
		this.salary = salary;
		this.departmentNumber = departmentNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	/**
	 * Return birthDate in java.SQL.Date format
	 * @return - (java.SQL.date) sqlBirthDate
	 */
	public java.sql.Date getSQLBdate(){
		return this.sqlBdate;
	}
	
	/**
	 * Set birthDate in java.SQL.Date format
	 */
	public void setSQLBdate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
		try {
			java.util.Date javaDate = dateFormat.parse(this.getBirthDate());
			this.sqlBdate = new java.sql.Date(javaDate.getTime());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getSuperSsn() {
		return superSsn;
	}

	public void setSuperSsn(String superSsn) {
		this.superSsn = superSsn;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public int getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(int departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	public ArrayList<ProjectHoursCombo> getAssignedProjects() {
		return assignedProjects;
	}
	
	public String getProjectName(int i) {
		ProjectHoursCombo project = assignedProjects.get(i);
		return project.getProjectName();
	}
	
	public double getProjectHours(int i) {
		ProjectHoursCombo project = assignedProjects.get(i);
		return project.getHours();
	}

	public void setAssignedProjects(ArrayList<ProjectHoursCombo> assignedProjects) {
		this.assignedProjects = assignedProjects;
	}

	public ArrayList<Dependent> getDependents() {
		return this.dependents;
	}
	
	public void setDependents(ArrayList<Dependent> dependents) {
		this.dependents = dependents;
	}
	
	
	
}
