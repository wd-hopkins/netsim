package org.netsim;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import javax.tools.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ClassLoaderUtil {

    /**
     * Gets all extending classes of a given type in a package.
     * @param clazz super class
     * @param prefix package name
     * @return The set of extending classes
     */
    public static <T> Set<Class<? extends T>> collectExtendingClasses(Class<T> clazz, String prefix) {
        Reflections reflections = new Reflections(prefix);
        return reflections.getSubTypesOf(clazz);
    }

    /**
     * Uses the system java compiler to dynamically compile a java class
     * @param file file object of class to compile
     * @return true if compilation is successful
     */
    public static boolean compileJavaClass(File file) {
        // Compile Options
        List<String> options = new ArrayList<>(Arrays.asList("-classpath", System.getProperty("java.class.path")));

        // Required objects
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(file));

        // Create compilation task and run
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnit);
        return task.call();
    }

    /**
     * Dynamically loads a class file from a URL
     * @param parent file in which the class is contained
     * @param name name of class to load
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
