
package darhadit.GestionMember;


import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Fodil
 */
public class gestionMember extends Application {
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
       // Parent root = FXMLLoader.load(getClass().getResource("/darhadit/dashboard/dashboard.fxml"));
        root = FXMLLoader.load(getClass().getResource("/darhadit/GestionMember/gestionMember.fxml"));
       
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
       // DataBase dataBase= new DataBase();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}