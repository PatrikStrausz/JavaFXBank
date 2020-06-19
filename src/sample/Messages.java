package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Messages {

    private ObservableValue<String> date;
    private ObservableValue<String> from;
    private ObservableValue<String> message;

    public Messages(String date, String from, String message) {
        this.date = new SimpleStringProperty(date);
        this.from = new SimpleStringProperty(from);
        this.message = new SimpleStringProperty(message);
    }

    public ObservableValue<String> getDate() {
        return date;
    }

    public ObservableValue<String> getFrom() {
        return from;
    }


    public ObservableValue<String> getMessage() {
        return message;
    }

    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }

    public void setFrom(String from) {
        this.from = new SimpleStringProperty(from);
    }

    public void setMessage(String message) {
        this.message = new SimpleStringProperty(message);
    }
}
