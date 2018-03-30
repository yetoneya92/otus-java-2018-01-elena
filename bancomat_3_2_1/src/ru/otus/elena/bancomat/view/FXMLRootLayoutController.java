
package ru.otus.elena.bancomat.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import ru.otus.elena.bancomat.MainApp;

public class FXMLRootLayoutController implements Initializable {

    MainApp mainApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleReturnButton(ActionEvent event) throws IOException {
        mainApp.getRootLayout().getChildren().removeAll(mainApp.getRootLayout().getCenter());
        mainApp.getRootLayout().setCenter(mainApp.getStartLayout());
    }

    @FXML
    private void handleExitButton(ActionEvent event) {
        System.exit(0);

    }


}
