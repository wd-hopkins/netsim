package org.netsim.models;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Set;

public abstract class Model {
    public static String modelId = "Choose a model";

    public Model() {

    }

    public abstract void onSelect();

    public abstract void run();

    public abstract void showOptions();

    @Override
    public String toString() {
        return modelId;
    }

    public static Set<Class<? extends Model>> getExtendingClasses() {
        Reflections reflections = new Reflections("org.netsim.models");
        return reflections.getSubTypesOf(Model.class);
    }

    @SneakyThrows
    public static Model getExtendingClassById(String id) {
        Model m = null;
        for (Class<? extends Model> model : getExtendingClasses()) {
            if (id.equals(model.getField("modelId").get(null))) {
                m = model.getDeclaredConstructor().newInstance();
            }
        }
        return m;
    }
}
