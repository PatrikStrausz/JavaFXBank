package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChangePassword {
    public AnchorPane pane;
    public Button btnAccept;

    public String token;
    public String login;
    public Label lblError;
    public TextField oldPassword;
    public TextField newPassword1;
    public TextField newPassword2;

    public void setToken(String token){
        this.token = token;

    }

    public void setLogin(String login){
        this.login = login;

    }

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }


    public void initialize(){
        pane.setStyle("-fx-background-color: #d9d9d9");
        btnAccept.setStyle("-fx-background-color: #33C2FF");

    }



    public void changePassword(){
        try {
            if(newPassword1.getText().equals(newPassword2.getText())) {
                URL url = new URL("http://localhost:8080/changePassword");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Authorization", getToken());
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                String jsonInputString = "{\"login\": " +"\""+ getLogin()+"\"" + ", \"oldPassword\": " +"\""+ oldPassword.getText()+"\"" + ",\"newPassword\": " + "\""+ newPassword1.getText()+"\"" + "}";
                System.out.println(jsonInputString);

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
                    String ss = responseStrBuilder.toString().replaceAll("\\p{P}", "")
                            .replace("error", "");
                    lblError.setText(ss);

                }
                con.disconnect();
            } else{
                lblError.setText("Password must match");
            }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeWindow() {
        Stage stage = (Stage) btnAccept.getScene().getWindow();
        stage.close();
    }
}
