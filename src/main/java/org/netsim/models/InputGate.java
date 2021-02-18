package org.netsim.models;

import org.netsim.util.ObservableQueue;

import java.util.LinkedList;

public class InputGate {

    public OutputGate connection;
    public ObservableQueue<String> buffer = new ObservableQueue<>(new LinkedList<>());

    public InputGate() {

    }

}
