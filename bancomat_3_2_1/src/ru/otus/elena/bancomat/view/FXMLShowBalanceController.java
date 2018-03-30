
package ru.otus.elena.bancomat.view;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ru.otus.elena.bancomat.Coffer;
import ru.otus.elena.bancomat.MainApp;

public class FXMLShowBalanceController implements Initializable{
     public MainApp mainApp;
    @FXML
    private GridPane showBalancePane;
    @FXML
    private Label balanceLabel;

    public FXMLShowBalanceController() {      
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
 
    protected void showBalance() {
        
        int balance=mainApp.coffer.getBalance();       
        try {
            balanceLabel.setText(String.valueOf(balance));
        } catch (Exception e) {
            System.out.println("ERROR=" + e.getMessage());
        }
    }
}
