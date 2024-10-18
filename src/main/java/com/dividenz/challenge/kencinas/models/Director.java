package com.dividenz.challenge.kencinas.models;

public class Director extends Employee {
    public Director(String name) {
        super(name);
    }

    /**
     * Returns the message of a Director.
     *
     * @return message
     */
    @Override
    protected String getMessage() {
        return "I'm a director, how can I help you?";
    }
}
