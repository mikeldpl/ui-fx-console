package com.mikeldpl.uifxconsole;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ConsoleWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            initStage(stage);
            initView(stage);
            stage.show();
        } finally {
            synchronized (UiFxConsole.LOCK) {
                UiFxConsole.LOCK.notifyAll();
            }
        }
    }

    private void initView(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("console.fxml"));
        stage.setScene(new Scene(root));
    }

    private void initStage(Stage stage) {
        stage.setTitle(UiFxConsole.title);
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}