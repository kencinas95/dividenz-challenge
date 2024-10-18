package com.dividenz.challenge.kencinas.services;

import com.dividenz.challenge.kencinas.models.Director;
import com.dividenz.challenge.kencinas.models.Employee;
import com.dividenz.challenge.kencinas.models.Operator;
import com.dividenz.challenge.kencinas.models.Supervisor;
import com.dividenz.challenge.kencinas.utils.DelayUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Getter
// When using Spring, annotate this with @Service
public class EmployeeService {
    private final static List<Class<? extends Employee>> EMPLOYEE_ORDER = List.of(
            Operator.class, Supervisor.class, Director.class);

    // Employees pool
    private final ConcurrentMap<Class<? extends Employee>, Queue<Employee>> employees;

    /**
     * EmployeeService constructor.
     */
    public EmployeeService() {
        // Using ConcurrentHashMap because it's thread safe
        employees = new ConcurrentHashMap<>();

        // Using Queues instead of List/Stack because the operations would be easier implementing queues
        // All queues are ConcurrentLinkedQueue because they're thread safe as well.
        for (var cls : EMPLOYEE_ORDER) {
            employees.putIfAbsent(cls, new ConcurrentLinkedQueue<>());
        }
    }

    /**
     * Gets the next available employee (thread safe).
     *
     * @return null safe employee instance
     */
    public synchronized Optional<Employee> getNextAvailableEmployee() {
        // It will prioritize the order: operator -> supervisor -> director
        return EMPLOYEE_ORDER
                .stream()
                .map(employees::get)
                .filter(queue -> !queue.isEmpty())
                .findFirst()
                .map(Queue::remove);
    }

    /**
     * Assigns a call to an employee.
     *
     * @param callId: call identifier
     * @param employee: employee
     *
     * @return async task
     */
    @SneakyThrows
    public Mono<String> assignCall(final int callId, final Employee employee) {
        // Simulates the duration between 5s and 10s
        var duration = DelayUtils.getRandomSeconds(5L, 10L);

        // No matter if the task raises an error, nor it successfully completed:
        // when the task finishes the resource must be set free!
        return Mono.just(employee)
                .map(Employee::answer)
                .delayElement(duration)
                .doOnSuccess(message -> log.info("The call #{} has been answered: {}", callId, message))
                .doOnTerminate(() -> freeEmployee(employee));
    }

    /**
     * Adds the given employee to the pool when the async task has been finalized (thread safe).
     *
     * @param employee: employee
     */
    private synchronized void freeEmployee(final Employee employee) {
        employees.get(employee.getClass()).add(employee);
    }

}
