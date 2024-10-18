package com.dividenz.challenge.kencinas.models;

public class Supervisor extends Employee {
    public Supervisor(String name) {
        super(name);
    }

    /**
     * Returns the message of a Supervisor.
     *
     * @return message
     */
    @Override
    protected String getMessage() {
        return "I'm a supervisor, how can I help you?";
    }
}
