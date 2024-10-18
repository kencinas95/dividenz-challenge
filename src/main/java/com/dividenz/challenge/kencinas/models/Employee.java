package com.dividenz.challenge.kencinas.models;

import lombok.Getter;

@Getter
public abstract class Employee {
    private final String name;

    /**
     * Employee constructor.
     *
     * @param name: name
     */
    public Employee(String name) {
        this.name = name;
    }

    /**
     * Returns the message for the call answer.
     *
     * @return message
     */
    public String answer() {
        // This is thread safe, I suppose... but is it really needed?
        return new StringBuffer("\"Hello, my names is ")
                .append(name)
                .append(". ")
                .append(getMessage())
                .append("\"")
                .toString();
    }

    /**
     * Abstract signature for getMessage.
     *
     * @return message
     */
    protected abstract String getMessage();

}
