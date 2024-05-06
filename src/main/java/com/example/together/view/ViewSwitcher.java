package com.example.together.view;

import com.example.together.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles changing the .fxml view in the main Stage for the app
 */
public class ViewSwitcher {
    private static Scene scene;
    private static Map<View, Parent> cache = new HashMap<>();

    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    public static void switchView(View view) {
        if (scene==null) {
            System.out.println("Scene was not set. NULL");
            return;
        }
        try {
            Parent root;
            if (cache.containsKey(view)){
                System.out.printf("Loading from cache: %s", view.getFileName());
                root = cache.get(view);
            } else {
                System.out.printf("Loading from fxml file: %s", view.getFileName());
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(view.getFileName()));
                root = fxmlLoader.load();
                cache.put(view,root);
            }
            scene.setRoot(root);
        } catch (IOException e) {
            System.out.println("ERROR LOADING THE FXML VIEW: " + view.getFileName());
            throw new RuntimeException(e);
        }
    }
}
