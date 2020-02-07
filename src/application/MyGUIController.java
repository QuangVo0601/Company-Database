package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
 
/**
 * This class allows the manager to enter his/her ssn 
 * to log in the COMPANY database.
 * @author Quang Vo
 */
public class MyGUIController implements Initializable {
 
   @FXML
   private Button loginButton;
  
   @FXML
   private TextField managerSSN;
   
   @FXML
   private Button resetButton;
  
   @Override
   public void initialize(URL location, ResourceBundle resources) {
 
       // TODO (don't really need to do anything here).
	   
   }
 
   /**
    * This checks the validity of the manager ssn.
    * @param event
    * @throws SQLException
    * @throws IOException
    */
   public void checkValidSsn(ActionEvent event) throws SQLException, IOException {
	   
	   String ssn = managerSSN.getText();
	   
	   if(ssn.isEmpty()) {
		   Alert alert = new Alert(AlertType.ERROR);
		   alert.setHeaderText(null);
		   alert.setContentText("Please enter your SSN");
		   alert.showAndWait();
	   }
	   else {
		   CompanyDatabase companyDB = new CompanyDatabase();
		   
		   boolean isManager = companyDB.SsnIsManager(ssn); //check for valid manager ssn here
		   
		   if(isManager) {
			   
			   Parent root = FXMLLoader.load(getClass()
	                    .getResource("/application/ManagerControlScreen.fxml"));
			   Stage newStage = new Stage();
			   newStage.setTitle("Manager Control Screen");
			   newStage.setScene(new Scene(root, 600, 500));
			   newStage.show();
		   }
		   else {
			   Alert alert = new Alert(AlertType.ERROR);
			   alert.setHeaderText(null);
			   alert.setContentText("Invalid Manager SSN! Please try again.");
			   alert.showAndWait();
		   }
	   }   
   }
   
   /**
    * This clears the input field.
    * @param event
    */
   public void reset(ActionEvent event) {
	   
	   managerSSN.setText("");
   }
   
   
  
}
