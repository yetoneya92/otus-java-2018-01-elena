/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.bancomat.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import ru.otus.elena.bancomat.Coffer;
import ru.otus.elena.bancomat.MainApp;


public class FXMLStartLayoutController implements Initializable {
     
    private MainApp mainApp;
    @FXML
    private GridPane startPane;

    public FXMLStartLayoutController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    public void setMainApp(MainApp mainApp){
        this.mainApp=mainApp;
    }

    @FXML
   protected void handlePayInButton(ActionEvent event) throws IOException {
        removeStartLayout();
         setPane("view/FXMLPayIn.fxml");
    }

    @FXML
    protected void handleWithdrawMoneyButton(ActionEvent event) throws IOException {
        removeStartLayout();
        setPane("view/FXMLWithdrawMoney.fxml");
    }

    @FXML
    protected void handleShowBalanceButton(ActionEvent event) throws IOException {
        removeStartLayout();
        setPane("view/FXMLShowBalance.fxml");
    }

    private void removeStartLayout() {
        mainApp.getRootLayout().getChildren().removeAll(mainApp.getRootLayout().getCenter());
        
       }

    private void setPane(String pane) {
        mainApp.getRootLayout().setCenter((GridPane) initPane(pane));
    }

    public Parent initPane(String pane) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(pane));
            Parent parent = loader.load();
            if ("view/FXMLPayIn.fxml".equals(pane)) {
                FXMLPayInController controller = loader.getController();
                controller.setMainApp(mainApp);

            } else if ("view/FXMLShowBalance.fxml".equals(pane)) {
                FXMLShowBalanceController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.showBalance();
            } else {
                FXMLWithdrawMoneyController controller = loader.getController();
                controller.setMainApp(mainApp);
            }
            return parent;
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return null;
        }

    }
}
