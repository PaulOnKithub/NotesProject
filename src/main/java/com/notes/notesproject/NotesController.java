package com.notes.notesproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;


public class NotesController {
    @FXML
    private DatePicker date;

    @FXML
    private ListView<?> entireList;

    @FXML
    private TextArea noteText;

    @FXML
    private TextField tag;

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

    }

    @FXML
    void loadEntry(ActionEvent event) {

    }

    @FXML
    void minimizeButton(ActionEvent event) {

    }

    @FXML
    void saveNote(ActionEvent event) {

    }

}