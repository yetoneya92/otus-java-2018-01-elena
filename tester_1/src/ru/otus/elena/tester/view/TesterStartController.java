/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.tester.view;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.otus.elena.tester.Tester;

public class TesterStartController implements Initializable {

    @FXML
    private GridPane root;
    @FXML
    private TextField pathId;
    @FXML
    private TextField packageId;
    @FXML
    private ListView<String> pathsId;
    @FXML
    private Button findButton;
    @FXML
    private Label filesFoundId;
    @FXML
    private ProgressBar barId;
    @FXML
    private Button createButton;
    @FXML
    private Button launchButton;
    @FXML
    private ListView<String> methodsId;
    @FXML
    private Label testFolder;
    @FXML
    TextField testFolderName;
    @FXML
    Button  editTest;
    @FXML 
    private Button createTest;
    @FXML
    private ListView<String> testResultContentId;
    
    private Task task = null;
    final private Set<String> methodsForTest;
    final String[] chosenPath = new String[1];
    private String toPath;
    private String packageName;
    public Tester tester;

    public TesterStartController(){
        methodsForTest=new HashSet<String>();
    }
    public void setTester(Tester tester){
        this.tester=tester;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tester.paths.addListener((ListChangeListener) (obj -> pathsId.setItems(obj.getList())));
        pathsId.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        pathsId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TesterStartController.this.chosenPath[0] = newValue;
                if (chosenPath[0].contains("Test.class")) {
                    setState(true,false);
                } else if(chosenPath[0].contains(".class")){
                    setState(false,true);
                }     
            }
        });
        Tester.methodNameList.addListener((ListChangeListener) (obj -> methodsId.setItems(obj.getList())));
        methodsId.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        methodsId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {                   
                       methodsForTest.add(newValue);                       
                       createTest.setDisable(false);
                       editTest.setDisable(false);
                    }
                });
        
        Tester.testResults.addListener((ListChangeListener) (obj -> testResultContentId.setItems(obj.getList())));
    }

    @FXML
    protected void btnFindFiles(ActionEvent event) {
        createTest.setDisable(true);
        testFolder.setOpacity(0);
        testFolderName.setDisable(true);
        setState(true, true);
        Tester.paths.clear();
        Tester.testResults.clear();
        Tester.methodNameList.clear();
        toPath = pathId.getText();
        packageName = packageId.getText();
        try {
            task = new FindFilesTask(Tester.paths, toPath, packageName);
            filesFoundId.textProperty().unbind();
            filesFoundId.textProperty().bind(task.messageProperty());
            barId.progressProperty().unbind();
            barId.progressProperty().bind(task.progressProperty());
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.submit(task);
            es.shutdown();
        } catch (InvalidPathException e) {
        }
    }
    
    @FXML
    protected void btnLaunchTests(ActionEvent event)
            throws InstantiationException,
            IllegalAccessException, InterruptedException, ExecutionException {
        setState(true, true);
        boolean meth = getMethods();
        if (!meth) {
            return;
        }
        Tester.testResults.clear();
        task = new LaunchTestTask(Tester.testResults, getClazz(chosenPath[0], packageName));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);
        es.shutdown();
    }

    @FXML
    protected void btnCreateTest1(ActionEvent event) {
        getMethods();       
        testFolder.setOpacity(100);
        testFolderName.setDisable(false);
        testFolderName.setText(toPath);

    }
    
    private boolean getMethods() {
        setState(true, true);
        Tester.methodNameList.clear();
        Class<?>clazz=getClazz(chosenPath[0], packageName);
        if(clazz==null){
            Tester.methodNameList.add("class not found");
            return false;
        }
        task = new GetMethodsTask(Tester.methodNameList,Tester.methods,clazz);
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);
        es.shutdown();
        return true;
    }
    @FXML
    protected void btnEditTest(ActionEvent event){
        System.out.println("в разработке");
    }

    @FXML
    protected void btnCreateTest2(ActionEvent event) {
        createTest.setDisable(true);        
        String folder = testFolderName.getText();
        task = new CreateTestTemplateTask(methodsForTest, packageName, chosenPath[0], folder, Tester.toTest, tester);
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);
        es.shutdown();
    }

    protected void setState(boolean createButtonDisable, boolean launchButtonDisable) {
        createButton.setDisable(createButtonDisable);
        launchButton.setDisable(launchButtonDisable);

    }
    
    
    protected Class getClazz(String chosenPath, String packageName){       
        try {           
            String pathToURL = chosenPath.substring(0, chosenPath.lastIndexOf(packageName.replace(".", File.separator))).replaceAll("[A-Z]:", "file:");
           // System.out.println(pathToURL);
            String name = packageName + "." + chosenPath.substring(chosenPath.lastIndexOf(File.separator) + 1, chosenPath.lastIndexOf("."));
            //System.out.println(name);
            URL[] urls = {new URL(pathToURL)};
            URLClassLoader loader = new URLClassLoader(urls, null);
           // System.out.println(Class.forName(name, true, loader).toString());
            return Class.forName(name, true, loader);
        } catch (Exception ex) {
            
            return null;
        }
    }
    

    
}
