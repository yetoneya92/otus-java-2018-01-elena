
package ru.otus.elena.atmmashine;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.otus.elena.atmmashine.view.FXMLRootLayoutController;
import ru.otus.elena.atmmashine.view.FXMLStartLayoutController;

public class MainApp extends Application {

    private BorderPane rootLayout;
    private GridPane startLayout;
    public Coffer coffer = new Coffer();
    public static final CountDownLatch latch = new CountDownLatch(1);
    public static MainApp mainApp = null;

    public static MainApp waitMainApp() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return mainApp;
    }

    public static void setMainApp(MainApp ma) {
        mainApp = ma;
        latch.countDown();
    }

    public MainApp() {
        setMainApp(this);
    }

    public void printS() {
        System.out.println("true");
    }
    /*public static void launchMainApp() {
        Runnable launch=()-> Application.launch(MainApp.class, null);
        new Thread(launch).start();        
    }*/
    public void setCoffer(Coffer coffer){
        this.coffer=coffer;
    }


    @Override
    public void start(Stage stage) {
        try {
            rootLayout = (BorderPane) initPane("view/FXMLRootLayout.fxml");
            startLayout = (GridPane) initPane("view/FXMLStartLayout.fxml");
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add(MainApp.class.getResource("CascadeSS.css").toExternalForm());
            stage.setTitle("TRY ATM");
            stage.setScene(scene);
            stage.show();
            rootLayout.setCenter(startLayout);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public GridPane getStartLayout() {
        return startLayout;
    }

    private Parent initPane(String pane) {
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
