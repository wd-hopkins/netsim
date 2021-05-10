package org.netsim.ui;

import javafx.scene.control.ComboBox;
import lombok.Getter;
import org.netsim.models.Model;
import org.netsim.util.ClassUtil;

import java.util.ArrayList;
import java.util.List;

public class ModelComboBox {

    private final @Getter ComboBox<Model> modelComboBox;

    public ModelComboBox() {
        List<Model> models = new ArrayList<>();
        Model.getExtendingClasses().forEach(x -> models.add(ClassUtil.instantiate(x)));
        modelComboBox = new ComboBox<>();
        modelComboBox.setId("ModelComboBox");
        modelComboBox.setLayoutY(0.0f);
        modelComboBox.getItems().addAll(models);
        modelComboBox.getSelectionModel().selectFirst();
        modelComboBox.setOnAction((event) -> showModelOptions(modelComboBox.getSelectionModel().getSelectedItem()));
    }

    public void showModelOptions(Model obj) {
        System.out.println(obj.toString());
    }
}
