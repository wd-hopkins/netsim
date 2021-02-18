package org.netsim.models;

import lombok.SneakyThrows;
import org.netsim.util.ClassUtil;

import java.util.Set;

public abstract class Model {
    private static final Set<Class<? extends Model>> extendingClasses = ClassUtil.collectExtendingClasses(Model.class, "org.netsim.models");
    public static String modelId = "Choose a model";

    public Model() {

    }

    public void init() {
        init(Node.class);
    }

    public abstract void init(Class<?> nodeImpl);

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
