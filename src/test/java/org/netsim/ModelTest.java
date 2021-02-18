package org.netsim;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.netsim.models.ClientServerModel;

public class ModelTest {

    public static ModelRunner runner;

    @BeforeAll
    static void setup() {
        NetworkSimulator.runWithGui = false;
        runner = new ModelRunner();
    }

    @Test
    public void testNullMessagesDoNotExist() {
        runner.setSelectedModel(new ClientServerModel());
        runner.run();
    }

    @AfterAll
    static void cleanup() {

    }
}
