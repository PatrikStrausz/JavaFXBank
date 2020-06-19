package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Signup {


    public TextField txtFname;
    public TextField txtLname;
    public TextField txtLogin;
    public PasswordField pwdPassword;
    public Label lblError;
    public Button btnSignup;

    public void initialize() {
        btnSignup.setStyle("-fx-background-color: #33C2FF");
        btnSignup.setDisable(true);

    }


    public void signup() {

        try {
            URL url = new URL("http://localhost:8080/signup");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = "{fname: " + "\"" + txtFname.getText() + "\"" + ", lname: " + "\"" + txtLname.getText() + "\""
                    + ",login: " + "\"" + txtLogin.getText() + "\"" + ",password: " + "\"" + pwdPassword.getText() + "\"" + "}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);

            }
            int statusCode = con.getResponseCode();

            InputStream is = null;
            lblError.setVisible(false);
            if (statusCode >= 200 && statusCode < 400) {
                is = con.getInputStream();
                System.out.println(is.toString());
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                is.close();
                streamReader.close();
                openLoginWindow();
                closeWindow();


                System.out.println(responseStrBuilder.toString());


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

                is.close();
                streamReader.close();
            }
            is.close();

            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleKeyReleased() {
        String loginText = txtLogin.getText();
        String passwordText = pwdPassword.getText();
        String fnameText = txtFname.getText();
        String lnameText = txtLname.getText();
        boolean disableBtn = fnameText.isEmpty() || fnameText.trim().isEmpty()
                && lnameText.isEmpty() || lnameText.trim().isEmpty()
                && loginText.isEmpty() || loginText.trim().isEmpty()
                && passwordText.isEmpty() || passwordText.trim().isEmpty();
        btnSignup.setDisable(disableBtn);

    }

    public void closeWindow() {
        Stage stage = (Stage) btnSignup.getScene().getWindow();
        stage.close();
    }

    public void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root1, 250, 270);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
