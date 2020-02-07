package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * This class allows the manager to fill out the information, 
 * assign projects, and add dependents for an employee.
 * @author Quang Vo
 */
public class MyManagerController {
	@FXML
	private ComboBox<String> projects_comboBox;
	
	@FXML
	private ComboBox<String> departmentNumber_comboBox;
	
	@FXML
	private TextField firstName_input;

	@FXML
	private TextField lastName_input;
	
	@FXML
	private TextField address_input;
	
	@FXML
	private TextField sex_input;
	
	@FXML
	private TextField projectHours_input;
	
	@FXML
	private TextField middleInitial_input;
	
	@FXML
	private TextField ssn_input;
	
	@FXML
	private TextField superSsn_input;
	
	@FXML
	private TextField birthDate_input;
	
	@FXML
	private TextField departmentNumber_input; //???
	
	@FXML
	private TextField dependentFirstName_input;
	
	@FXML
	private TextField dependentSex_input;
	
	@FXML
	private TextField salary_input;
	
	@FXML
	private TextArea database_output;
	
	@FXML
	private CheckBox dependent_yes;
	
	@FXML
	private CheckBox dependent_no;
	
	@FXML
	private TextField dependentBdate_input;
	
	@FXML
	private TextField dependentRelationship_input;
	
	@FXML
	private Button addEmployee_button;
	
	@FXML
	private Button assignProject_button;
	
	@FXML
	private Button removeEmployeeProject_button;
	
	@FXML
	private Button addDependent_button;
	
	@FXML
	private Button scan_button;
	
	
	private static final double MAX_ALLOWED_HOURS = 40.0;
	private ArrayList<String> projectsList;
	private ObservableList<String> observableProjectsList = FXCollections.observableArrayList();
	private ArrayList<Integer> departmentNoList;
	private ObservableList<String> observableDepartmentNoList = FXCollections.observableArrayList();
	private ArrayList<ProjectHoursCombo> selectedProjects = new ArrayList<ProjectHoursCombo>();
	private ArrayList<Dependent> dependentsList = new ArrayList<Dependent>();
	private double totalHours = 0.0;
	private String emptyFields = "";
	private Alert alert = new Alert(AlertType.ERROR);
	private String outputArea = "";
	//MyGUIController newloginGUI = new MyGUIController();
	CompanyDatabase companyDB;
	Employee newEmployee;
	ProjectHoursCombo PHcombo;
	Dependent newDependent;
	
	/**
	 * This initializes the department numbers and projects comboBoxes 
	 * with the actual values from COMPANY database before adding new employees.
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
	public void initialize() throws SQLException, IOException {
		
		companyDB = new CompanyDatabase();
		
		departmentNoList = companyDB.getCurrentDepartmentNo(); //get the current dno list here
	
		for(int i = 0; i < departmentNoList.size(); i++) {
			observableDepartmentNoList.add(departmentNoList.get(i).toString());
		}
		
		departmentNumber_comboBox.setItems(observableDepartmentNoList); //load values to the comboBox
	
		projectsList = companyDB.getCurrentProjects(); //get the current projects list here
		
		for(int i = 0; i < projectsList.size(); i++) {
			observableProjectsList.add(projectsList.get(i).toString());
		}
		
		projects_comboBox.setItems(observableProjectsList); //load values to the comboBox
		
		//add new employee when click on the button
		addEmployee_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewEmployee();
			}
		});
		
		//assign new project when click on the button
		assignProject_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				assignNewProjects();
			}
		});
		
		//remove an employee from a project when click on the button
		removeEmployeeProject_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					removeEmployeeFromProject();
				} catch (IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//when checkBox is 'yes' for dependents
		dependent_yes.selectedProperty().addListener(new ChangeListener<Boolean>(){
			public void changed(ObservableValue<? extends Boolean> ov, 
					Boolean old_val, Boolean new_val) { //need to change

				if(new_val) {
					dependentFirstName_input.setEditable(true);
					dependentSex_input.setEditable(true);
					dependentBdate_input.setEditable(true);
					dependentRelationship_input.setEditable(true);
				}
				else {
					dependentFirstName_input.setEditable(false);
					dependentSex_input.setEditable(false);
					dependentBdate_input.setEditable(false);
					dependentRelationship_input.setEditable(false);
				}
			}
		});
		
		//when checkBox is 'no' for dependents
		dependent_no.selectedProperty().addListener(new ChangeListener<Boolean>(){
			public void changed(ObservableValue<? extends Boolean> ov, 
					Boolean old_val, Boolean new_val) { //need to change

				if(new_val) {
					dependentFirstName_input.setEditable(false);
					dependentSex_input.setEditable(false);
					dependentBdate_input.setEditable(false);
					dependentRelationship_input.setEditable(false);
					addDependent_button.setDisable(true);
				}
				else {
					dependentFirstName_input.setEditable(true);
					dependentSex_input.setEditable(true);
					dependentBdate_input.setEditable(true);
					dependentRelationship_input.setEditable(true);
					addDependent_button.setDisable(false);
				}
			}
		});
		
		//add new dependent when click on the button
		addDependent_button.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				addNewDependents();
			}
		});	
		
		//scan employee database
		scan_button.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				try {
					scanEmployeeDatabase();
				} catch (IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});	
	}
	
	/**
	 * This gets attributes for an employee from textFields,
	 * then insert the employee with assigned projects and dependents
	 * to EMPLOYEE, WORKS_ON, and DEPENDENT relations respectively.
	 */
	@FXML
	public void addNewEmployee() {
		
		emptyFields = "";
		
		try {
			if(firstName_input.getText().isEmpty()) {
				emptyFields = emptyFields + "\nFirst Name needed";
			}
			if(middleInitial_input.getText().isEmpty()) {
				emptyFields = emptyFields + "\nMiddle Initial needed";
			}
			if(lastName_input.getText().isEmpty()) {
				emptyFields = emptyFields + "\nLast Name needed";
			}
			if(ssn_input.getText().isEmpty() || ssn_input.getText().length() != 9) {
				emptyFields = emptyFields + "\nSSN must be 9-digit";
			}
			if(birthDate_input.getText().isEmpty() || birthDate_input.getText().length() != 8) {
				emptyFields = emptyFields + "\nBirthdate must be in format dd-mm-yy";
			}
			if(address_input.getText().isEmpty()) {
				emptyFields = emptyFields + "\nAddress needed";
			}
			if(sex_input.getText().isEmpty()){
				emptyFields = emptyFields + "\nSex needed";
			}
			if(salary_input.getText().isEmpty()) {
				emptyFields = emptyFields + "\nSalary needed";
			}
			if(superSsn_input.getText().isEmpty() || superSsn_input.getText().length() != 9) {
				emptyFields = emptyFields + "\nSupervisor SSN must be 9-digit";
			}
			if(departmentNumber_comboBox.getSelectionModel().getSelectedItem() == null) {
				emptyFields = emptyFields + "\nDepartment number needed";
			}
			
			if(selectedProjects.size() == 0) {
				emptyFields = emptyFields +"\nNo project was assigned";
			}
			else {
				
				int selectedDno = Integer.parseInt(departmentNumber_comboBox.getSelectionModel().getSelectedItem());
				if(!moreThanTwoProjectsSameDepartment(selectedProjects, selectedDno) 
				&& oneProjectFromDepartment(selectedProjects, selectedDno)) {
					
				}
			}
			
			if(projectHours_input.getText().isEmpty()) {
				emptyFields = emptyFields + "\nNumber of Hours for Project needed";
			}
			if(dependent_yes.isSelected() && dependent_no.isSelected() || 
					!dependent_yes.isSelected() && !dependent_no.isSelected()) 
			{
				emptyFields = emptyFields + "\nPick either yes or no for dependents";
			}

			if(emptyFields.length() > 0) {
				alert.setHeaderText("Fields need to be filled:");
				alert.setContentText(emptyFields);
				alert.showAndWait();
				emptyFields ="";
			}
			else {
				String firstName = firstName_input.getText();
				String middleInitial = middleInitial_input.getText();
				String lastName = lastName_input.getText();
				String ssn = ssn_input.getText();
				String birthDate = birthDate_input.getText();
				String address = address_input.getText();
				String sex = sex_input.getText();
				int salary = Integer.parseInt(salary_input.getText());
				String superSsn = superSsn_input.getText();
				int departmentNumber = Integer.parseInt(departmentNumber_comboBox.getSelectionModel().getSelectedItem());
				
				newEmployee = new Employee(firstName, middleInitial, lastName, sex, birthDate, 
											address, ssn, superSsn, salary, departmentNumber);
				
				newEmployee.setSQLBdate();
				
				newEmployee.setAssignedProjects(selectedProjects); //assign projects for the employee
				
				database_output.setText("Adding new employee to company database...");

				companyDB.insertEmployeeRelation(newEmployee); //insert to EMPLOYEE relation
				companyDB.insertWorksOnRelation(newEmployee); //insert to WORKS_ON relation
				
				if(dependentsList.size() > 0) {
					newEmployee.setDependents(dependentsList); //add dependents for the employee
					companyDB.insertDependentRelation(newEmployee); //insert to DEPENDENT relation
				}

				database_output.setText("The employee was added successfully");
				printReport(ssn_input.getText()); //print report here
				resetScreenInputs(); //reset all the input fields after the successful insertion
				
				//on Friday ask which report is needed, in eclipse or GUI?

			}
		}
		catch(Exception e) {
			e.printStackTrace();
			resetScreenInputs(); //reset all the input fields if encounters errors
			alert.setTitle("Error Error");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
	
	/**
	 * This gets new projects from comboBox and hours from textField,
	 * then add the new (project, hours) combo to 'selectedProjects' ArrayList
	 */
	@FXML
	public void assignNewProjects() {
		
		outputArea = "";
		database_output.setText(outputArea);
		
		try {
			totalHours = 0.0;
			String projectName = projects_comboBox.getSelectionModel().getSelectedItem().toString();
			double hours = Double.parseDouble(projectHours_input.getText());
			
			PHcombo = new ProjectHoursCombo(projectName, hours);
			
			selectedProjects.add(PHcombo); //add new (project, hours) combo to ArrayList here
						
			if(!checkMaxHours(selectedProjects) && !checkDuplicateProjects(selectedProjects, projectName)) {
				
				outputArea = outputArea + "\n" + "Project " + PHcombo.getProjectName() + " was assigned, Hours: " 
							 + PHcombo.getHours() + "\n" + "Total hours: " + totalHours;
				database_output.setText(outputArea);
				projects_comboBox.getSelectionModel().clearSelection();
				
			}
		}catch(Exception e){
			alert.setHeaderText("Assign Projects Error");
			alert.setContentText("Please select a project or enter the hours");
			alert.showAndWait();
		}
		
	}
	
	/**
	 * This checks to make sure total hours assigned don't exceed 40 hours
	 * @param selectedProjects the list of assigned projects
	 * @return true if exceeded the max hours, false otherwise
	 */
	private boolean checkMaxHours(ArrayList<ProjectHoursCombo> selectedProjects) {
		
		for(int i = 0; i < selectedProjects.size(); i++) {
			PHcombo = selectedProjects.get(i);
			totalHours += PHcombo.getHours();
			if(totalHours > MAX_ALLOWED_HOURS) {
				
				outputArea = "";
				selectedProjects.clear();
				alert.setHeaderText("Exceeded max hours"); 
				alert.setContentText("Cannot assign more than 40 hours total"); 
				alert.showAndWait();
				database_output.setText(outputArea);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This checks to ensure no duplicate projects were assigned
	 * @param selectedProjects the projects to be checked
	 * @return true if there are duplicate projects, false otherwise
	 */
	private boolean checkDuplicateProjects(ArrayList<ProjectHoursCombo> selectedProjects, String newProjectName) {
	
	HashSet<String> hs = new HashSet<String>();
		
	String projectName = "";
	
	for(int i = 0; i < selectedProjects.size(); i++) {
		
		projectName = selectedProjects.get(i).getProjectName();
		
		if(i > 0 && hs.contains(projectName)) {
			outputArea = "";
			selectedProjects.clear();
			alert.setHeaderText("Duplicate projects found"); 
			alert.setContentText("You have assigned this project already. All projects were cleared"); 
			alert.showAndWait();
			database_output.setText(outputArea);
			return true;
		}
		
		hs.add(projectName);
		
	}
		return false;
	}
	
	//extra credit
	/**
	 * This checks to ensure that an employee don't work on > 2 projects managed by his/her department.
	 * @return true if > 2 assigned projects from his/her department, false otherwise
	 * @param selectedProjects list of selected projects
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private Boolean moreThanTwoProjectsSameDepartment(ArrayList<ProjectHoursCombo> selectedProjects, int selectedDno) 
			throws IOException, SQLException {
		
		int departmentNumber = 0;
		int count = 0;
		
		for(int i = 0; i < selectedProjects.size(); i++) {
			
			departmentNumber = companyDB.getDnoForPname(selectedProjects.get(i).getProjectName());
			
			if(departmentNumber == selectedDno) {
				count++;
				if(count > 2) {
					outputArea = "";
					selectedProjects.clear();
					alert.setHeaderText("Exceeded number of projects"); 
					alert.setContentText("Cannot assign more than 2 projects from employee's department"); 
					alert.showAndWait();
					database_output.setText(outputArea);
					return true;
				}	
			}
		}

		return false;
	}
	
	//extra credit
	/**
	 * This checks to ensure that at least one project controlled by his/her department. 
	 * @param selectedProjects list of selected projects
	 * @param selectedDno the employee's department number
	 * @return true if at least one project controlled by department, false otherwise
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private Boolean oneProjectFromDepartment(ArrayList<ProjectHoursCombo> selectedProjects, int selectedDno) 
				throws SQLException, IOException {
		
		HashSet<Integer> hs = new HashSet<Integer>();
		
		String projectName = "";
		int departmentNumber = 0;
		
		for(int i = 0; i < selectedProjects.size(); i++) {
			projectName = selectedProjects.get(i).getProjectName();
			departmentNumber = companyDB.getDnoForPname(projectName);
			hs.add(departmentNumber);
		}
		
		if(hs.contains(selectedDno) == false) { //&& selectedProjects.size() > 0) {
			alert.setHeaderText("Constraints are violated");
			alert.setContentText("An employee must work on at least one project controlled by his/her department.");
			alert.showAndWait();
			return false;
		}
		return true;
	}
	
	/**
	 * This gets attributes for a dependent from textFields,
	 * then add the dependent to 'dependentsList' ArrayList.
	 */
	@FXML
	public void addNewDependents() {
		
		emptyFields = "";
		
		if(dependent_yes.isSelected() && dependentFirstName_input.getText().isEmpty()) {
			emptyFields = emptyFields +"\nDependent first name needed";
		}
		
		if(dependent_yes.isSelected() && dependentSex_input.getText().isEmpty()) {
			emptyFields = emptyFields +"\nDependent sex needed";
		}
		
		if(dependent_yes.isSelected() && dependentBdate_input.getText().length() != 8 ) {
			emptyFields = emptyFields + "\nDependent birthdate format dd-mm-yy needed";
		}
		if(dependent_yes.isSelected() && dependentRelationship_input.getText().isEmpty()) {
			emptyFields = emptyFields +"\nDependent relationship needed";
		}
		
		if(emptyFields.length() > 0) {
			alert.setHeaderText("Fields need to be filled:");
			alert.setContentText(emptyFields);
			alert.showAndWait();
			emptyFields ="";
		}
		else {
			
			String firstName = dependentFirstName_input.getText();
			String sex = dependentSex_input.getText();
			String birthDate = dependentBdate_input.getText();
			String relationship = dependentRelationship_input.getText();
			
			newDependent = new Dependent(firstName, sex, birthDate, relationship);
			newDependent.setSQLBirthDate();
			
			dependentsList.add(newDependent); //add new dependent to ArrayList here
						
			outputArea = outputArea + "\nDependent " + dependentFirstName_input.getText()
			+ " " + dependentSex_input.getText() + " " + dependentBdate_input.getText()+ " " 
			+ dependentRelationship_input.getText() + "\nAdded";
			
			database_output.setText(outputArea);
			dependentFirstName_input.clear();
			dependentSex_input.clear();
			dependentBdate_input.clear();
			dependentRelationship_input.clear();
		
		}
	}
	
	/**
	 * This resets all the input fields on screen.
	 */
	private void resetScreenInputs() {
		//screen part
		firstName_input.clear();
		middleInitial_input.clear();
		lastName_input.clear();
		ssn_input.clear();
		birthDate_input.clear();
		address_input.clear();
		sex_input.clear();
		salary_input.clear();
		superSsn_input.clear();
		departmentNumber_comboBox.getSelectionModel().clearSelection();
		projects_comboBox.getSelectionModel().clearSelection();
		projectHours_input.clear();
		dependent_yes.setSelected(false);
		dependent_no.setSelected(false);
		
		//code part
		outputArea = "";
		selectedProjects.clear();
		dependentsList.clear();
	}
	
	/**
	 * Print the report to ensure that everything was inserted into EMPLOYEE, WORKS_ON, and DEPENDENT relations
	 * @param checkingSsn
	 */
	private void printReport(String checkingSsn) {
				
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, 1900);
		
		Employee employee = null;
		ArrayList<ProjectHoursCombo> assignedProjects;
		Dependent dependent = null;
		
		String reportOutput = "-------------------REPORT-------------------\n-------Employee Information:\n";
		
		try {
			employee = companyDB.getEmployeeFromDatabase(checkingSsn); //get employee from EMPLOYEE relation here
			
			reportOutput += employee.getFirstName() + " " + employee.getMiddleInitial() + " " + employee.getLastName() + "\n"
							+ "Ssn: " + employee.getSsn() + "\t" + "Birthdate: " + employee.getBirthDate()  + "\n"
							+ "Address: " + employee.getAddress() + "\t" + "Sex: " + employee.getSex() +"\n"
							+ "Salary: " + employee.getSalary() + "\t  " + "SupervisorSsn: " + employee.getSuperSsn() + "\n"
							+ "Department Number: "+ employee.getDepartmentNumber() + "\n"
							+ "-------Assigned Projects:\n";
			
			assignedProjects = companyDB.getEmployeeProjectsFromDatabase(employee); //get projects of employee from WORKS_ON relation
			
			for(int i = 0; i < assignedProjects.size(); i++) {
				reportOutput += assignedProjects.get(i).getProjectName() + ", hours: " + assignedProjects.get(i).getHours() + "\n";
			}
			
			companyDB.getEmployeeDependentsFromDatabase(employee); //get dependents of employee from DEPENDENT relation
			
			if(employee.getDependents().size()>0) {
				reportOutput += "-------Dependents:\n";
				for(int i = 0; i < employee.getDependents().size(); i++) {
					dependent = employee.getDependents().get(i);
					reportOutput += "First name: " + dependent.getFirstName() + "\t"
									+ "Sex: " + dependent.getSex() + "\n"
									+ "Birthdate: " + dependent.getBirthDate() + "\t"
									+ "Relationship: " + dependent.getRelationship() + "\n";
				}
			}
			reportOutput += "---------------END OF REPORT--------------";
			database_output.setText(reportOutput); //show report on GUI
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void scanEmployeeDatabase() throws IOException, SQLException {
		
		int selectedDno = Integer.parseInt(departmentNumber_comboBox.getSelectionModel().getSelectedItem());
		
		if(!moreThanTwoProjectsSameDepartment(selectedProjects, selectedDno) 
				&& oneProjectFromDepartment(selectedProjects, selectedDno)) {
			outputArea = "No employees violated the rules";
			database_output.setText(outputArea);
		}
		
	}
	
	@FXML
	public void removeEmployeeFromProject() throws IOException, SQLException {
		
//		try {
//			totalHours = 0.0;
//			String projectName = projects_comboBox.getSelectionModel().getSelectedItem().toString();
//			double hours = Double.parseDouble(projectHours_input.getText());
//			
//			PHcombo = new ProjectHoursCombo(projectName, hours);
//			
//			selectedProjects.add(PHcombo); //add new (project, hours) combo to ArrayList here
//			
//			int selectedDno = Integer.parseInt(departmentNumber_comboBox.getSelectionModel().getSelectedItem());
//			
//			if(!checkMaxHours(selectedProjects) && !moreThanTwoProjectsSameDepartment(selectedProjects, selectedDno)
//					&& !checkDuplicateProjects(selectedProjects)) {
//				
//				outputArea = outputArea + "\n" + "Project " + PHcombo.getProjectName() + " was assigned, Hours: " 
//							 + PHcombo.getHours() + "\n" + "Total hours: " + totalHours;
//				database_output.setText(outputArea);
//				projects_comboBox.getSelectionModel().clearSelection();
//				
//			}
//		}catch(Exception e){
//			alert.setHeaderText("Assign Projects Error");
//			alert.setContentText("Please select a project or enter the hours");
//			alert.showAndWait();
//		}
		
		//
				
		int selectedDno = Integer.parseInt(departmentNumber_comboBox.getSelectionModel().getSelectedItem());
		
		moreThanTwoProjectsSameDepartment(selectedProjects, selectedDno);
		oneProjectFromDepartment(selectedProjects, selectedDno);
		
		alert.setHeaderText("Constraints are violated");
		alert.setContentText("An employee must work on at least one project controlled by his/her department.");
		alert.showAndWait();
		
	}
}
