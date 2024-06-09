package com.example.together.view;

import com.example.together.view.View;
import com.example.together.view.ViewSwitcher;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Feed extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Pane());
        ViewSwitcher.scene = scene;
        ViewSwitcher.switchView(View.FEED);
        stage.setTitle("ToGether!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaxWidth(1440);
        stage.setMaxHeight(800);
        setKeyboardShortcuts();
        stage.show();
    }
    /**
     * Sets keyboard shortcuts to switch between scenes using the keyboard
     */
    private void setKeyboardShortcuts() {
        ViewSwitcher.scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN),
                () -> ViewSwitcher.switchView(View.TASKLIST)
        );
        ViewSwitcher.scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN),
                () -> ViewSwitcher.switchView(View.GROUPS)
        );
        ViewSwitcher.scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
                () -> ViewSwitcher.switchView(View.FEED)
        );
        ViewSwitcher.scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN),
                () -> ViewSwitcher.switchView(View.NEWTASK)
        );
    }

}
