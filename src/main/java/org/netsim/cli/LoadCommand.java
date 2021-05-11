package org.netsim.cli;

import org.netsim.models.Model;
import org.netsim.util.ClassUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Parameters;

import java.io.File;

@Command(name = "load",
        description = "Loads a user defined model.")
public class LoadCommand implements Runnable {

    @Parameters(converter = FileConverter.class)
    private File file;

    @Override
    public void run() {
        if (!(file.exists() && file.isFile())) {
            CommandShell.printErr("Invalid Selection");
            return;
        }
        if (!ClassUtil.compileJavaClass(file)) {
            CommandShell.printErr("There was an error during compilation.");
            return;
        }
        Class<?> userModel = ClassUtil.loadClass(file.getParentFile(), file.getName().split("\\.")[0]);
        if (!Model.class.isAssignableFrom(userModel)) {
            CommandShell.printErr(String.format("Error: %s does not extend Node.\n", userModel.getName()));
            return;
        }
        @SuppressWarnings("unchecked")
        Class<? extends Model> checkedUserModel = (Class<? extends Model>) userModel;
        Model.getUserModels().add(checkedUserModel);
        System.out.println("Successfully loaded model.");
    }

    static class FileConverter implements ITypeConverter<File> {

        @Override
        public File convert(String value) {
            if (value.charAt(0) == '/') {
                return new File(value);
            } else {
                return new File(CommandShell.getRunner().getWorkingDirectory(), value);
            }
        }
    }
}
