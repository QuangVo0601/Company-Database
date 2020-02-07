package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
/**
 * Main class to read the FXML files and launch the GUI (Manager Login Screen)
 * @author Quang Vo
 */
public class MyMain extends Application {
    
  
    @Override
    public void start(Stage primaryStage) {
        try {
            
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/application/GUI.fxml"));
 
            primaryStage.setTitle("Welcome to CS450 Company Database");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
         
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
