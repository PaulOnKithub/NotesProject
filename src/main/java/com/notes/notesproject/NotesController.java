package com.notes.notesproject;

import com.almasb.fxgl.app.PrimaryStageWindow;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


public class NotesController implements Initializable{
    @FXML
    private DatePicker date;
    @FXML
    private Button SaveButton;

    @FXML
    private ListView<NoteRecord> entireList;

    @FXML
    private TextArea noteText;

    @FXML
    private TextField tag;

    Database db = new Database();

    @FXML
    void clearTextArea(ActionEvent event) {
        tag.setText("");
        noteText.setText("");
        date.setValue(LocalDate.now());
    }

    @FXML
    void deleteEntry(ActionEvent event) {

    }

    @FXML
    void exitButton(ActionEvent event) {
        Platform.exit();
        event.consume();

    }

    @FXML
    void loadEntry(ActionEvent event) {
        NoteRecord selectedItem=entireList.getSelectionModel().getSelectedItem();
        clearTextArea(event);
        tag.setText(selectedItem.tag());
        noteText.setText(selectedItem.noteValue());

    }
    @FXML
    void modifyEntry(ActionEvent event) {

    }

    @FXML
    void minimizeButton(ActionEvent event) {
        Stage stage= (Stage) ((Node)event.getTarget()).getScene().getWindow();
        stage.setIconified(true);

    }

    @FXML
    void saveNote(ActionEvent event) {

        if (db.dbConnection()) db.tableCheck();
        String tagText = tag.getText();
        String notes = noteText.getText();
        if (!(StringUtils.isEmpty(tagText) && StringUtils.isEmpty(notes))) {
            String dateText = StringUtils.isEmpty(date.toString()) ? LocalDate.now().toString() : date.getValue().toString();
            NoteRecord recordVal = new NoteRecord(tagText, notes, dateText);
            if (db.insertRecord(recordVal)) clearTextArea(event);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(db.dbConnection()){
            db.tableCheck();
            entireList.getItems().addAll(db.getRecords());
        }
    }
}







