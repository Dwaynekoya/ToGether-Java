package com.example.together;

import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Pane());
        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchView(View.LOGIN);
        stage.setTitle("ToGether!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMinWidth(1440);
        stage.setMinHeight(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
