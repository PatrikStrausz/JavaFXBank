package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
    @FXML
    public Button btnLogin;

    public void initialize(){
        btnLogin.setStyle("-fx-background-color: #33C2FF");
    }

}
