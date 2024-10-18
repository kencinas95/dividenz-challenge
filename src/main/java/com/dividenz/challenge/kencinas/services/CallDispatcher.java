package com.dividenz.challenge.kencinas.services;

import com.dividenz.challenge.kencinas.models.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executors;

@Slf4j
@Getter
@RequiredArgsConstructor
// When using Spring, annotate this with @Component
public class CallDispatcher {
    // Scheduler with 10 thread workers fixed
    private final static Scheduler SCHEDULER = Schedulers.fromExecutor(Executors.newFixedThreadPool(10));

    private final EmployeeService service;

    /**
     * Dispatch the call to the next available employee.
     *
     * @param callId: call identifier
     * @return async task to answer the call.
     */
    @SneakyThrows
    public Mono<String> dispatch(final int callId) {
        return Mono.justOrEmpty(service.getNextAvailableEmployee())
                .switchIfEmpty(handleWaitingCall()
                    .doOnSubscribe(s ->
                        log.warn("No available employees to answer this call at the moment #{}", callId)))
                .flatMap(employee -> service.assignCall(callId, employee))
                .doOnSubscribe(s -> log.debug("A new call has been acquired: #{}", callId))
                .doOnError(ex -> log.error("Something went wrong in call: #{}", callId, ex))
                .subscribeOn(SCHEDULER);
    }

    /**
     * Waits for the next available employee to answer the call for an infinite time.
     *
     * @return async task to answer the call by the next available employee
     */
    private Mono<Employee> handleWaitingCall() {
        var duration = Duration.ofMillis(100);

        // An async task that will repeat until an employee is available with an interval of 100ms between each check.
        return Mono.delay(duration)
                .map(t -> service.getNextAvailableEmployee())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .repeatWhenEmpty(pipe -> pipe.delayElements(duration))
                .subscribeOn(SCHEDULER);
        // Maybe we should put a timeout here and handle the error above?
        // .timeout(Duration.ofSeconds(60));

    }
}
