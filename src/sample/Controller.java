package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.Timer;


public class Controller {

    public Button btnLogin;
    public PasswordField password;
    public Label lblError;
    public TextField login;

    public String token;
    public String lname;
    public String fname;
    public Button btnSignup;

    public String getLoginss() {
        return loginss;
    }

    public void setLoginss(String loginss) {
        this.loginss = loginss;
    }

    public String loginss;

    public void initialize() {
        btnLogin.setStyle("-fx-background-color: #33C2FF");
        btnLogin.setDisable(true);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void login() throws IOException {

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
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            JSONObject temp = new JSONObject(responseStrBuilder.toString());


            setLname(temp.getString("lname"));
            setToken(temp.getString("token"));
            setFname(temp.getString("fname"));
            setLoginss(temp.getString("login"));


            System.out.println(responseStrBuilder.toString());


            openNewWindow();
            closeLoginWindow();
            is.close();
            streamReader.close();
            con.disconnect();

        } else {
            is = con.getErrorStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            new JSONObject(responseStrBuilder.toString());
            System.out.println(responseStrBuilder.toString());
            String ss = responseStrBuilder.toString().replaceAll("\\p{P}", "")
                    .replace("error", "");
            lblError.setText(ss);
            lblError.setVisible(true);
            login.setText(" ");
            password.setText("");
            btnLogin.setDisable(true);
            is.close();
            streamReader.close();
            con.disconnect();

        }

        is.close();


        con.disconnect();

    }


    public void handleKeyReleased() {
        String loginText = login.getText();
        String passwordText = password.getText();
        boolean disableBtn = loginText.isEmpty() || loginText.trim().isEmpty()
                && passwordText.isEmpty() || passwordText.trim().isEmpty();
        btnLogin.setDisable(disableBtn);

    }

    public void openNewWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newWindow.fxml"));
            Parent root1 = loader.load();

            NewWindow newWindow = loader.getController();
            newWindow.setName(getFname(), getLname());
            newWindow.setToken(getToken());
            newWindow.setLogin(getLoginss());
            newWindow.getMessages();


            Stage stage = new Stage();
            Scene scene = new Scene(root1, 895, 500);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initStyle(StageStyle.UTILITY);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root1 = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 250, 270);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initStyle(StageStyle.UTILITY);

            stage.setScene(scene);
            stage.show();
            Stage login = (Stage) btnSignup.getScene().getWindow();
            login.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeLoginWindow() {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();
    }


}
