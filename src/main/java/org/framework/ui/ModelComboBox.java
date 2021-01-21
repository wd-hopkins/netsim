package org.framework.ui;

import javafx.scene.control.ComboBox;
import lombok.Getter;
import org.framework.models.ClientServerModel;
import org.framework.models.EmptyModel;
import org.framework.models.Model;

public class ModelComboBox {

    private @Getter
    final ComboBox<Model> modelComboBox;

    public ModelComboBox() {
        modelComboBox = new ComboBox<>();
        add(new EmptyModel("Select a model"));
        add(new ClientServerModel());
        modelComboBox.getSelectionModel().selectFirst();
        modelComboBox.setOnAction((event) -> modelComboBox.getSelectionModel().getSelectedItem().onSelect());
    }

    public void add(Model option) {
        modelComboBox.getItems().add(option);
    }
}
