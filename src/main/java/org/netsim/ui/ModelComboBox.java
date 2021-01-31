package org.netsim.ui;

import javafx.scene.control.ComboBox;
import lombok.Getter;
import org.netsim.models.ClientServerModel;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;

public class ModelComboBox {

    private final @Getter ComboBox<Model> modelComboBox;

    public ModelComboBox() {
        modelComboBox = new ComboBox<>();
        modelComboBox.setId("ModelComboBox");
        modelComboBox.setLayoutY(0.0f);
        modelComboBox.getItems().addAll(
                new EmptyModel(),
                new ClientServerModel()
        );
        modelComboBox.getSelectionModel().selectFirst();
        modelComboBox.setOnAction((event) -> showModelOptions(modelComboBox.getSelectionModel().getSelectedItem()));
    }

    public void showModelOptions(Model obj) {
        System.out.println(obj.toString());
    }
}
