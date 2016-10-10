package com.mikeldpl.uifxconsole.controllers;

import com.mikeldpl.uifxconsole.streams.DuplicateOutputStream;
import com.mikeldpl.uifxconsole.streams.OutputStreamTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

import java.io.PrintStream;

public class MainWindowController {

    @FXML
    public CheckBox wrap;
    private OutputStreamTextArea outputStreamTextArea;
    @FXML
    private TextArea consoleWindow;
    @FXML
    private CheckBox follow;
    @FXML
    private Button cleanButton;

    @FXML
    public void initialize() throws Exception {
        outputStreamTextArea = new OutputStreamTextArea(consoleWindow);
        System.setOut(new PrintStream(new DuplicateOutputStream(outputStreamTextArea, System.out), true));
    }

    public void followAction(ActionEvent actionEvent) {
        outputStreamTextArea.setFollow(follow.isSelected());
    }

    public void cleanButtonAction(ActionEvent actionEvent) {
        consoleWindow.clear();
    }

    public void wrapAction(ActionEvent actionEvent) {
        consoleWindow.setWrapText(wrap.isSelected());
    }
}
