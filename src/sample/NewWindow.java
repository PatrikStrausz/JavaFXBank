package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NewWindow {
    public Label lblName;
    public String fullName;
    public String token;
    public String login;
    public Pane infoPane;
    public Button btnLogout;

    public String getFullName() {
        return fullName;
    }

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }

    public void setName(String fname, String lname){
        lblName.setText(fname + " "+ lname);
        this.fullName = fname + " " + lname;

    }

    public void setToken(String token){
        this.token = token;

    }

    public void setLogin(String login){
        this.login = login;

    }



    public void initialize(){
       infoPane.setStyle("-fx-background-color: #d9d9d9");


    }

    @FXML
    private void logout(){
        try {
            URL url = new URL("http://localhost:8080/logout");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization", getToken());
            String jsonInputString = "{\"login\": " + getLogin() + "}";

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

                System.out.println(responseStrBuilder.toString());

                openLoginWindow();
                closeWindow();


            } else {
                is = con.getErrorStream();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                new JSONObject(responseStrBuilder.toString());
                System.out.println(responseStrBuilder.toString());

            }

            con.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root1 = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 250, 300);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initStyle(StageStyle.UTILITY);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void closeWindow() {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();
    }

    public void openChangePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("changePassword.fxml"));
            Parent root1 = loader.load();

            ChangePassword changePassword = loader.getController();
            changePassword.setLogin(getLogin());
            changePassword.setToken(getToken());

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 250, 300);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initStyle(StageStyle.UTILITY);

            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
