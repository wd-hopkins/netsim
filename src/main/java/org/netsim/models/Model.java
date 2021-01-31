package org.netsim.models;

import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Set;

public abstract class Model {
    public static String modelId = "Choose a model";
    private static @Getter Set<Class<? extends Model>> extendingClasses;

    public Model() {
        extendingClasses = collectExtendingClasses();
    }

    public abstract void onSelect();

    public abstract void run();

    public abstract void showOptions();

    @Override
    public String toString() {
        return modelId;
    }

    private static Set<Class<? extends Model>> collectExtendingClasses() {
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
