/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.tester.view;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import ru.otus.elena.tester.Tester;

public class TesterTestController implements Initializable {

    @FXML
    private GridPane testPane;
    @FXML
    TextArea text;   
    @FXML
    private Button saveButton;
    private Tester tester;
    private Task task = null; 
    protected String content;
    private static String className;
    public TesterTestController() {
    }
    public void setTester(Tester tester){
        this.tester=tester;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    protected void setClassName(String className){
        this.className=className;
    }
    protected void setText() {
        try {
            Path path = Tester.toTest.get(0);
            System.out.println(path.toString());
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            System.out.println("tru");
            for (String line : lines) {
                text.appendText(line);
                text.appendText("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void btnSaveFile(ActionEvent event) throws IOException {
       Path path = Tester.toTest.get(0);
       content=text.getText();
       Files.write(path,content.getBytes(StandardCharsets.UTF_8));
       
    }
    @FXML
    protected void btnReturn(ActionEvent event){
        tester.showTester();
    }
    @FXML
    protected void btnCompile(ActionEvent event) {
        task = new CompileTask(Tester.toTest,content,className);
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);
        es.shutdown();
    }
}
