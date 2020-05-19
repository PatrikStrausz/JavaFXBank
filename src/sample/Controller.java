package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Controller {
    @FXML
    public Button btnLogin;
    public PasswordField password;
    public Label lblError;
    public TextField login;

    public void initialize(){
        btnLogin.setStyle("-fx-background-color: #33C2FF");
    }



    public void login() throws IOException, InterruptedException {

        URL url = new URL ("http://localhost:8080/login");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"login\": " +login.getText()+ ", password: "+ password.getText()+"}";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

        }

        InputStream in = new BufferedInputStream(con.getInputStream());
        String result = IOUtils.toString(in, StandardCharsets.UTF_8);
        System.out.println(result + " sasasaassa");

        in.close();
        con.disconnect();
        
    }



}
