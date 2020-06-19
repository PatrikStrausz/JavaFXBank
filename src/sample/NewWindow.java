package sample;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.time.LocalTime;
import java.util.*;

import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class NewWindow {
    public Label lblName;
    public String fullName;
    public String token;
    public String login;
    public Pane infoPane;
    public Button btnLogout;
    public ListView<String> lvLog;
    public ListView<String> lvMessage;
    public TextArea areaMessage;
    public TextField tfTo;
    public Button btnSendMessage;
    public Label lblMessageError;
    public Button btnChangePassword;
    public Button btnLog;
    public Button btnDeleteAccount;
    public Button btnDeleteMessage;
    public Label lblTime;
    public Button openAll;
    private String from;
    private String time;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }

    public void setName(String fname, String lname) {
        lblName.setText(fname + " " + lname);
        this.fullName = fname + " " + lname;

    }

    public void setToken(String token) {
        this.token = token;

    }

    public void setLogin(String login) {
        this.login = login;

    }


    public void initialize() {
        lvLog.setVisible(false);
        infoPane.setStyle("-fx-background-color: #d9d9d9");
        btnSendMessage.setStyle("-fx-background-color: #33C2FF");
        lblMessageError.setVisible(false);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(0);
        executor.scheduleAtFixedRate(helloRunnable, 0, 10, TimeUnit.SECONDS);
        btnDeleteMessage.setDisable(true);
        btnDeleteMessage.setStyle("-fx-background-color: #b30000");
        ss();
        btnSendMessage.setDisable(true);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


    }

    public void ss() {
        btnDeleteMessage.disableProperty()
                .bind(lvMessage.getSelectionModel().selectedItemProperty().isNull());
    }


    public void deleteMessage() {

        try {
            URL url = new URL("http://localhost:8080/deleteMessage");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization", getToken());
            String jsonInputString = "{\"login\": " + "\"" + getLogin() + "\"" + ", \"from\":" + "\"" + getFrom() + "\"" + ", \"time\": " + "\"" + getTime() + "\"" + "}";

            System.out.println(jsonInputString);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);


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

                            lvMessage.getItems().remove(lvMessage.getSelectionModel().getSelectedItem());

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
                    is.close();
                    streamReader.close();
                    con.disconnect();
                }
                is.close();
                con.disconnect();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
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
                is.close();
                streamReader.close();
                openLoginWindow();
                closeWindow();
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
                is.close();
                streamReader.close();
                con.disconnect();
            }
            is.close();
            con.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void getLog() {
        try {
            URL url = new URL("http://localhost:8080/log");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
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


                isLogVisible();

                JSONArray response = new JSONArray(responseStrBuilder.toString());
                List<String> listdata = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject temp = response.getJSONObject(i);
                    listdata.add(i, temp.getString("datetime") + "  " + temp.getString("type"));
                }
                lvLog.getItems().setAll(listdata);
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
                is.close();
                streamReader.close();
                con.disconnect();
            }
            is.close();
            con.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> test = new ArrayList<>();

    public void getMessages() {
        try {
            URL url = new URL("http://localhost:8080/message");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
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

                lblMessageError.setVisible(false);
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                JSONArray response = new JSONArray(responseStrBuilder.toString());


                System.out.println(responseStrBuilder.toString());
                List<String> listdata = new ArrayList<>();
                if(response.length()>8){
                    for (int i = 0; i < 7; i++) {
                        JSONObject temp = response.getJSONObject(i);
                        listdata.add(i, temp.getString("time").substring(6) + "                          " + temp.getString("time").substring(0, 6) + "\n" +
                                "  " + temp.getString("from") + ": " + temp.getString("message"));


                        setTime(temp.getString("time"));
                        setFrom(temp.getString("from"));
                    }
                } else {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject temp = response.getJSONObject(i);
                        listdata.add(i, temp.getString("time").substring(6) + "                          " + temp.getString("time").substring(0, 6) + "\n" +
                                "  " + temp.getString("from") + ": " + temp.getString("message"));


                        setTime(temp.getString("time"));
                        setFrom(temp.getString("from"));
                    }
                }


                test = listdata;
                Collections.reverse(test);

                lvMessage.getItems().setAll(test);
                lvMessage.setFixedCellSize(55);
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
                con.disconnect();
                is.close();
                streamReader.close();
            }
            is.close();
            con.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendMessage() {
        try {
            URL url = new URL("http://localhost:8080/message/new");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization", getToken());
            String jsonInputString = "{\"from\": " + "\"" + getLogin() + "\"" + ",\"to\": " + "\"" + tfTo.getText() + "\"" + ", \"message\": " + "\"" + areaMessage.getText() + "\"" + "}";


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

                getMessages();

                System.out.println(responseStrBuilder.toString());
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
                lblMessageError.setText(ss);
                lblMessageError.setVisible(true);
                is.close();
                streamReader.close();
                con.disconnect();
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
            Scene scene = new Scene(root1, 250, 270);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void isLogVisible() {
        if (lvLog.isVisible()) {
            lvLog.setVisible(false);
        } else {
            lvLog.setVisible(true);
        }
    }

    public void closeWindow() {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();
    }


    public void openConfirmWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("confirmWindow.fxml"));
            Parent root1 = loader.load();

            DeleteAccount deleteAccount = loader.getController();
            deleteAccount.setToken(getToken());
            deleteAccount.setLogin(getLogin());
            deleteAccount.setBtnLogout(btnLogout);

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 207, 200);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Runnable helloRunnable = new Runnable() {
        public void run() {
            try {
                getMessages();
                lvMessage.getItems().setAll(test);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void handleKeyReleased() {


        String messageText = areaMessage.getText();
        boolean disableBtn =
                messageText.isEmpty() || messageText.trim().isEmpty();

        btnSendMessage.setDisable(disableBtn);

    }

    public void openAllMessages() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("allMessages.fxml"));
            Parent root1 = loader.load();

            AllMessages all = loader.getController();
            all.setLogin(getLogin());
            all.setToken(getToken());
            all.getMessages();

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


}

