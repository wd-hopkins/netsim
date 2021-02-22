package org.netsim;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.netsim.models.ClientServerModel;

import java.io.File;

public class ModelTest {

    public static ModelRunner runner;

    @BeforeAll
    static void setup() {
        NetworkSimulator.runWithGui = false;
        runner = new ModelRunner();
        runner.setWorkingDirectory(new File(runner.getWorkingDirectory(), "my-network"));
        runner.setSelectedModel(new ClientServerModel());
    }

    @Test
    public void testNullMessagesDoNotExist() {
        //runner.run();
    }

    @AfterAll
    static void cleanup() {

    }
}
