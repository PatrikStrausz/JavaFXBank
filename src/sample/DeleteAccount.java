package sample;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DeleteAccount {
    public String token;
    public String login;
    public Button btnDelete;
    public Button btnCancelDelete;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void initialize() {
        btnDelete.setStyle("-fx-background-color: #00b33c");
        btnCancelDelete.setStyle("-fx-background-color: #b30000");

    }

    public void closeDeleteWindow() {
        Stage stage = (Stage) btnDelete.getScene().getWindow();
        stage.close();
    }

    public void deleteAccount() {
        try {
            btnDelete.setStyle("-fx-background-color: #00b33c");
            btnCancelDelete.setStyle("-fx-background-color: #b30000");

            URL url = new URL("http://localhost:8080/delete/" + getLogin());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization", getToken());


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
                is.close();
                streamReader.close();
                closeDeleteWindow();
                closeMainWindow();
                openLoginWindow();


            } else {
                is = con.getErrorStream();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                new JSONObject(responseStrBuilder.toString());
                System.out.println(responseStrBuilder.toString());
                is.close();
                streamReader.close();
            }
            is.close();
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


    public void setBtnLogout(Button btn) {

        btnDelete = btn;
    }

    public void closeMainWindow() {
        Stage s = (Stage) btnDelete.getScene().getWindow();
        Stage k = (Stage) btnCancelDelete.getScene().getWindow();
        s.close();
        k.close();
    }


}
