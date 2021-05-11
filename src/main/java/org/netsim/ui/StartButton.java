package org.netsim.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import lombok.Getter;
import org.netsim.models.Model;

public class StartButton {

    private final @Getter Button startButton;

    public StartButton() {
        startButton = new Button("Start");
        startButton.setLayoutY(50f);
        startButton.setOnAction((action) -> {
            Node mcb = startButton.getParent().lookup("#ModelComboBox");
            Model model = (Model) ((ComboBox<?>) mcb).getSelectionModel().getSelectedItem();
            model.start();
        });
    }
}
