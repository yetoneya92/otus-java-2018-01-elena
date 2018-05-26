/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.tester.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Lena
 */
public class TesterRootController implements Initializable {

    @FXML
    BorderPane rootPane;
    @FXML
    Button exitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    @FXML
    protected void btnExit(ActionEvent event){
       System.exit(0);
    }
}
