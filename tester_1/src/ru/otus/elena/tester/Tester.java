
package ru.otus.elena.tester;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.otus.elena.tester.view.TesterStartController;
import ru.otus.elena.tester.view.TesterTestController;


public class Tester extends Application {
    private BorderPane rootLayout;
    public static ObservableList<String> paths = FXCollections.observableArrayList();
    public static ObservableList<String> methodNameList = FXCollections.observableArrayList();
    public static ObservableList<String> testResults = FXCollections.observableArrayList();
    public static Map<String,Method>methods=new HashMap<>();
    public static List<Path>toTest=new ArrayList<>();
    
    private GridPane grid;
    private GridPane test;
    private TesterTestController tController;
    public static final CountDownLatch latch = new CountDownLatch(1);
    public static Tester tester = null;

    public static Tester waitTester() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return tester;
    }

    public static void setTester(Tester tes) {
        tester = tes;
        latch.countDown();
    }

    public Tester() {
        setTester(this);
    }


    @Override
    public void start(Stage stage) throws Exception {
        rootLayout = (BorderPane) FXMLLoader.load(getClass().getResource("view/TesterRoot.fxml"));
        Scene scene = new Scene(rootLayout);
        stage.setTitle("TESTER");
        stage.setScene(scene);
        stage.show();
        showTester();

    }

    public void showTester() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Tester.class.getResource("view/TesterStart.fxml"));
            grid = (GridPane) loader.load();
            TesterStartController sController = loader.getController();
            sController.setTester(this);
            rootLayout.setCenter(grid);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showTestTemplate() throws IOException {
        rootLayout.getChildren().removeAll(rootLayout.getCenter());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Tester.class.getResource("view/TesterTest.fxml"));
        test = (GridPane) loader.load();
        tController = loader.getController();
        tController.setTester(this);
        rootLayout.setCenter(test);
    }

    public TesterTestController getTesterTestController() {
        return tController;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
