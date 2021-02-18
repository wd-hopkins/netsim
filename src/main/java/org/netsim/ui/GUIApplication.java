package org.netsim.ui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.netsim.ModelRunner;

public class GUIApplication extends Application {

    private static final @Getter ModelRunner runner = new ModelRunner();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group group = new Group();
        Scene scene = new Scene(group, 1200, 800);

        ModelComboBox comboBox = new ModelComboBox();
        StartButton startButton = new StartButton();

        group.getChildren().add(comboBox.getModelComboBox());
        group.getChildren().add(startButton.getStartButton());

        primaryStage.setTitle("Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
