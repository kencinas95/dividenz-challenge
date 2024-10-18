package com.dividenz.challenge.kencinas.models;

public class Operator extends Employee {
    public Operator(String name) {
        super(name);
    }

    /**
     * Returns a message of an Operator
     *
     * @return message
     */
    @Override
    protected String getMessage() {
        return "I'm an operator, how can I help you?";
    }
}
