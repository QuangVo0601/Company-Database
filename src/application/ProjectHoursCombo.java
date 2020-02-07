package application;

import java.math.BigDecimal;

/**
 * An Object that represents a combo (projectName, hours)
 * @author Quang Vo
 */
public class ProjectHoursCombo {

	private String projectName;
	private double hours;
	private PnoHoursCombo PnoHoursCombo;
	
	public ProjectHoursCombo(String projectName, double hours) {
		this.projectName = projectName;
		this.hours = hours;
	}
	
	public ProjectHoursCombo() {
		
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}
	
	public PnoHoursCombo getPnoHours(){
		return this.PnoHoursCombo;
	}
	
	public void setPnoHours(BigDecimal Pno, BigDecimal Hours) {
		this.PnoHoursCombo = new PnoHoursCombo(Pno, Hours);
	}
	
	/**
	 * An Object that represents a combo (projectNumber, hours)
	 * @author Quang Vo
	 */
	public class PnoHoursCombo  {
		private BigDecimal Pno;
		private BigDecimal Hours;
		
		public PnoHoursCombo(BigDecimal Pno, BigDecimal Hours) {
			this.Pno = Pno;
			this.Hours = Hours;
		}
		
		public BigDecimal getPno() {
			return this.Pno;
		}
		
		public BigDecimal getHours() {
			return this.Hours;
		}
	}
}
