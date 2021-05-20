package org.netsim.util;

import lombok.SneakyThrows;
import org.netsim.cli.CommandShell;
import org.reflections.Reflections;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ClassUtil {

    /**
     * Gets all extending classes of a given type in a package.
     *
     * @param clazz  super class
     * @param prefix package name
     * @return The set of extending classes
     */
    public static <T> Set<Class<? extends T>> collectExtendingClasses(Class<T> clazz, String prefix) {
        Reflections reflections = new Reflections(prefix);
        return reflections.getSubTypesOf(clazz);
    }

    /**
     * Uses the system java compiler to dynamically compile a java class. Results in a class file in the same directory.
     *
     * @param file file object of class to compile
     * @return true if compilation is successful
     */
    public static boolean compileJavaClass(File... file) {
        // Compile Options
        List<String> options = new ArrayList<>(Arrays.asList("-classpath", System.getProperty("java.class.path")));

        // Required objects
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(file));

        // Create compilation task and run
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnit);
        return task.call();
    }

    /**
     * Infers the corresponding constructor based on the given arguments and provides an instance of the given class.
     *
     * @param clazz class to instantiate
     * @param args  constructor arguments
     * @return instantiated object
     */
    @SneakyThrows
    public static <T> T instantiate(Class<T> clazz, Object... args) {
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return clazz.getDeclaredConstructor(types).newInstance(args);
    }

    /**
     * Dynamically loads a class file from a directory.
     *
     * @param parent file in which the class is contained
     * @param name   name of class to load
     * @return the loaded class
     */
    @SneakyThrows
    public static Class<?> loadClass(File parent, String name) {
        URL url = parent.toURI().toURL();
        URL[] urls = new URL[]{url};
        ClassLoader cl = new URLClassLoader(urls);
        return Class.forName(name, true, cl);
    }

}
