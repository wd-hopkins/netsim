package org.netsim.models;

import lombok.SneakyThrows;
import org.netsim.ClassLoaderUtil;

import java.util.Set;

public abstract class Model {
    public static String modelId = "Choose a model";
    private static final Set<Class<? extends Model>> extendingClasses = ClassLoaderUtil.collectExtendingClasses(Model.class, "org.netsim.models");

    public Model() {

    }

    public abstract void init();

    public abstract void init(Class<?> userNode);

    public abstract void onSelect();

    public abstract void run();

    @Override
    public String toString() {
        return modelId;
    }

    @SneakyThrows
    public static Model getExtendingClassById(String id) {
        Model m = null;
        for (Class<? extends Model> model : extendingClasses) {
            if (id.equals(model.getField("modelId").get(null))) {
                m = model.getDeclaredConstructor().newInstance();
                break;
            }
        }
        return m;
    }
}
