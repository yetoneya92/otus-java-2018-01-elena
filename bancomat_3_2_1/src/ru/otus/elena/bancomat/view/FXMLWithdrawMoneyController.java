
package ru.otus.elena.bancomat.view;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.otus.elena.bancomat.Coffer;
import ru.otus.elena.bancomat.MainApp;

public class FXMLWithdrawMoneyController implements Initializable {

    @FXML
    TextField sumOfMoney;
    @FXML
    Label messageLabel;
    MainApp mainApp;

    public FXMLWithdrawMoneyController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    protected void handleWithdrawMoneyButton(ActionEvent event) {
        messageLabel.setText("");
        
        try {
            int sum = Integer.parseInt(sumOfMoney.getText());
            if (sum <= 0 || sum > Integer.MAX_VALUE) {
                throw new IllegalArgumentException();
                
            }
            else{
                messageLabel.setText(mainApp.coffer.withdrawMoney(sum));
                 
            }
           
        } catch (NumberFormatException nfe) {
            messageLabel.setText("Невозможно выполнить операцию");
        } catch (IllegalArgumentException iae) {
            messageLabel.setText("Невозможно выполнить операцию");
        }

    }
}
