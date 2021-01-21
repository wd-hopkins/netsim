package org.framework;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.framework.ui.ModelComboBox;

public class Program extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group group = new Group();
        Scene scene = new Scene(group, 1200, 800);

        ModelComboBox comboBox = new ModelComboBox();
        group.getChildren().add(comboBox.getModelComboBox());

        primaryStage.setTitle("Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
