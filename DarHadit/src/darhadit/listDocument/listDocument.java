package darhadit.listDocument;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ....
 */
public class listDocument extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/darhadit/listDocument/ListDocument.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/darhadit/images/dar0.png"));
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
