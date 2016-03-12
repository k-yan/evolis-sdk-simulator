package com.kyanlife.code.evolis;

import com.kyanlife.code.evolis.io.JSONRequestListener;
import com.kyanlife.code.evolis.printer.PrinterManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Properties;

public class ESPFSimulator extends Application {

    JSONRequestListener requestListener;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("fxml/MainView.fxml"));
        primaryStage.setTitle(MainProperties.get("app.title"));
        primaryStage.setScene(new Scene(root,
                MainProperties.getInteger("app.window.width"),
                MainProperties.getInteger("app.window.height")));
        primaryStage.show();

        requestListener = new JSONRequestListener(
                MainProperties.getInteger("app.server.port")
        );
        requestListener.start();

        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Stopping service");
            requestListener.stopListening();
        });

        PrinterManager.getInstance().addPrinter(
                MainProperties.get("app.printer.name"),
                MainProperties.get("app.printer.ribbon"));
    }


    public static void main(String[] args) {
        launch(args);
    }

}
