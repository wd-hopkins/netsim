package org.netsim;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.netsim.ui.ModelComboBox;
import org.netsim.ui.StartButton;

public class Program extends Application {

    public static void main(String[] args) {
        if (args.length > 1)
            System.out.println(args[1]);
        launch(args);
    }

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
