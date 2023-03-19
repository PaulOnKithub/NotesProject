package com.notes.notesproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReaderController {

    @FXML
    private TextArea readerText;

    void setReaderText(String text) {
        readerText.setText(text);
    }

    @FXML
    void closeReader(ActionEvent event) {
        Scene readerScene=((Node)event.getTarget()).getScene();
        Stage readerStage=(Stage) readerScene.getWindow();
        readerStage.close();
        event.consume();
    }

}

