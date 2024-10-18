package com.dividenz.kencinas.utils;

import com.dividenz.challenge.kencinas.models.Director;
import com.dividenz.challenge.kencinas.models.Operator;
import com.dividenz.challenge.kencinas.models.Supervisor;
import com.dividenz.challenge.kencinas.services.EmployeeService;

public class DataUtils {
    public static void loadEmployees(final EmployeeService service) {
        // Set operators
        service.getEmployees().get(Operator.class).add(new Operator("Joe"));
        service.getEmployees().get(Operator.class).add(new Operator("Kevin"));
        service.getEmployees().get(Operator.class).add(new Operator("Ashley"));
        service.getEmployees().get(Operator.class).add(new Operator("Diana"));
        service.getEmployees().get(Operator.class).add(new Operator("Matt"));
        service.getEmployees().get(Operator.class).add(new Operator("Thomas"));
        service.getEmployees().get(Operator.class).add(new Operator("Bruce"));

        // Set supervisors
        service.getEmployees().get(Supervisor.class).add(new Supervisor("David"));
        service.getEmployees().get(Supervisor.class).add(new Supervisor("Rose"));
        service.getEmployees().get(Supervisor.class).add(new Supervisor("Ruth"));

        // Set directors
        service.getEmployees().get(Director.class).add(new Director("Adam"));
    }
}
