package org.netsim;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.netsim.cli.CommandShell;
import org.netsim.models.PartialSynchronousModel;

import java.io.File;

public class ModelTest {

    public static ModelRunner runner;

    @BeforeAll
    static void setup() {
        runner = CommandShell.getRunner();
        runner.setWorkingDirectory(new File(runner.getWorkingDirectory(), "links"));
        runner.setSelectedModel(new PartialSynchronousModel());
    }

    @Test
    public void testModel() {
        runner.run();
    }

    @AfterAll
    static void cleanup() {

    }
}
