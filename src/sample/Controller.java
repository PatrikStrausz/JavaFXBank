package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.URL;

import java.nio.charset.StandardCharsets;


public class Controller {
    @FXML
    public Button btnLogin;
    public PasswordField password;
    public Label lblError;
    public TextField login;

    public void initialize() {
        btnLogin.setStyle("-fx-background-color: #33C2FF");
        btnLogin.setDisable(true);
    }


    public void login() throws IOException, InterruptedException {

        URL url = new URL("http://localhost:8080/login");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"login\": " + login.getText() + ", password: " + password.getText() + "}";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

        }
        int statusCode = con.getResponseCode();

        InputStream is = null;

        if (statusCode >= 200 && statusCode < 400) {
            is = con.getInputStream();
            System.out.println(is.toString());
        } else {
            is = con.getErrorStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            new JSONObject(responseStrBuilder.toString());
            System.out.println(responseStrBuilder.toString());
            String ss =responseStrBuilder.toString().replaceAll("\\p{P}","")
                    .replace("error", "");
            lblError.setText(ss);
            lblError.setVisible(true);
            login.setText(" ");
            password.setText("");
            btnLogin.setDisable(true);



        }

        con.disconnect();

    }

    public void handleKeyReleased() {
        String loginText = login.getText();
        String passwordText = password.getText();
        boolean disableBtn = loginText.isEmpty() || loginText.trim().isEmpty()
                && passwordText.isEmpty() || passwordText.trim().isEmpty();
       btnLogin.setDisable(disableBtn);

    }


}
