package sample;


import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AllMessages {
    public Pagination pagination;


    private final TableView<Messages> table = createTable();
    public Button btnDelete;
    private List<Messages> data = new ArrayList<>(100);
    private String login;
    private String token;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void initialize() {
        btnDelete.setStyle("-fx-background-color: #b30000");
        isCellSelected();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        pagination.setPageFactory(this::createPage);
    }

    public void isCellSelected() {
        btnDelete.disableProperty()
                .bind(table.getSelectionModel().selectedItemProperty().isNull());
    }

    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * 10;
        int toIndex = Math.min(fromIndex + 10, data.size());

        int pages = (int) Math.ceil((double) data.size() / 10);
        pagination.setPageCount(pages);

        table.getItems().setAll(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return table;
    }

    public void delete() {

        try {
            Messages s = table.getSelectionModel().getSelectedItem();

            URL url = new URL("http://localhost:8080/deleteMessage");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization", getToken());
            String jsonInputString = "{\"login\": " + "\"" + getLogin() + "\"" + ", \"from\":" + "\"" + s.getFrom().getValue() + "\"" + ", \"time\": " + "\"" + s.getDate().getValue() + "\"" + "}";

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

                    table.getItems().remove(table.getSelectionModel().getSelectedItem());
                    getMessages();
                    table.getItems().setAll(data);
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


    private TableView<Messages> createTable() {

        TableView<Messages> tableTest = new TableView<>();

        TableColumn<Messages, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(param -> param.getValue().getDate());
        dateCol.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        dateCol.setStyle("-fx-alignment: CENTER-LEFT;");


        TableColumn<Messages, String> fromCol = new TableColumn<>("From");
        fromCol.setCellValueFactory(param -> param.getValue().getFrom());
        fromCol.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        fromCol.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Messages, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(param -> param.getValue().getMessage());
        messageCol.setMaxWidth(1f * Integer.MAX_VALUE * 70);
        messageCol.setStyle("-fx-alignment: CENTER-LEFT;");


        tableTest.setFixedCellSize(42);
        tableTest.getColumns().addAll(dateCol, fromCol, messageCol);
        return tableTest;
    }

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

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                JSONArray response = new JSONArray(responseStrBuilder.toString());


                System.out.println(responseStrBuilder.toString());
                List<Messages> listdata = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject temp = response.getJSONObject(i);
                    listdata.add(i, new Messages(temp.getString("time"), temp.getString("from"),
                            temp.getString("message")));

                }

                Collections.reverse(listdata);
                data = listdata;

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
}

