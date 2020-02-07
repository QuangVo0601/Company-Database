package application;

import java.text.SimpleDateFormat;

/**
 * This class contains all the attributes of a DEPENDENT in COMPANY database.
 * @author Quang Vo
 */
public class Dependent {

	private String firstName;
	private String sex;
	private String birthDate;
	private java.sql.Date sqlBirthDate;
	private String relationship;
	
	public Dependent( String firstName, String sex, 
			String birthDate, String relationship) 
	{
		this.firstName = firstName;
		this.sex = sex;
		this.birthDate = birthDate;
		this.relationship = relationship;
	}

	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getSex() {
		return this.sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
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
	public java.sql.Date getSQLBirthDate(){
		return this.sqlBirthDate;
	}
	
	/**
	 * Set birthDate in java.SQL.Date format
	 */
	public void setSQLBirthDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
		try {
			java.util.Date javaDate = dateFormat.parse(this.getBirthDate());
			this.sqlBirthDate = new java.sql.Date(javaDate.getTime());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getRelationship() {
		return this.relationship;
	}
	
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
}
