
package ru.otus.elena.bancomat;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.otus.elena.bancomat.view.FXMLRootLayoutController;
import ru.otus.elena.bancomat.view.FXMLStartLayoutController;
public class MainApp extends Application {
    private BorderPane rootLayout;
    private GridPane startLayout;
    public Coffer coffer=new Coffer();

    
    @Override
    public void start(Stage stage) throws Exception {
        rootLayout = (BorderPane)initPane("view/FXMLRootLayout.fxml");
        startLayout=(GridPane)initPane("view/FXMLStartLayout.fxml");
        Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(MainApp.class.getResource("CascadeSS.css").toExternalForm());
        stage.setTitle("TRY BANCOMAT");
        
        stage.setScene(scene);
        stage.show();
        rootLayout.setCenter(startLayout);
    }
    
    public BorderPane getRootLayout(){
        return rootLayout;
    }
    public GridPane getStartLayout(){
        return startLayout;
    }

    public Parent initPane(String pane) {
        try {
            FXMLLoader loader = new FXMLLoader();
 
            loader.setLocation(MainApp.class.getResource(pane));
            Parent parent = loader.load();
            if("view/FXMLRootLayout.fxml".equals(pane)){
               FXMLRootLayoutController controller=loader.getController();
                controller.setMainApp(this);
            } else if ("view/FXMLStartLayout.fxml".equals(pane)) {
                FXMLStartLayoutController controller = loader.getController();
                controller.setMainApp(this);
            } 
            return parent;
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
