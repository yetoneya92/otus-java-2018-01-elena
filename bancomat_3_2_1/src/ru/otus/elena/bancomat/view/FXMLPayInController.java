
package ru.otus.elena.bancomat.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.otus.elena.bancomat.MainApp;

public class FXMLPayInController implements Initializable {

    @FXML 
    Label paidLabel;       
    MainApp mainApp;
    int sum;
    
    public FXMLPayInController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleTakeDenomination(ActionEvent event) {
        
        Button b = (Button) event.getSource();
        System.out.println(b.getText());
        try {
            int add = Integer.parseInt(b.getText());           
            mainApp.coffer.addMoney(add);
            sum+=add;
            paidLabel.setText("Внесено "+String.valueOf(sum));
        } catch (NumberFormatException nfe) {
        }
    }


}
