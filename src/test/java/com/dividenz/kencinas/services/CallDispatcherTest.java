package com.dividenz.kencinas.services;

import com.dividenz.challenge.kencinas.services.CallDispatcher;
import com.dividenz.challenge.kencinas.services.EmployeeService;
import com.dividenz.kencinas.utils.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.test.StepVerifier;

public class CallDispatcherTest {

    private CallDispatcher dispatcher;

    @BeforeEach
    void setup() {
        // I know this is wrong, we shouldn't use the service in a unit test but:
        // I just gave up trying to mock this service...
        var service = new EmployeeService();
        DataUtils.loadEmployees(service);

        dispatcher = new CallDispatcher(service);
    }

    @Test
    void test10IncomingCallsSuccess() {
        ParallelFlux<String> tasks = Flux.range(1, 10)
                .parallel()
                .flatMap(dispatcher::dispatch);

        // I don't know what else I could verify in this situation...
        StepVerifier.create(tasks)
                .expectSubscription()
                .expectNextCount(10L)
                .expectComplete()
                .verify();
    }

    @Test
    void test20IncomingCallsNoEmployeeAvailableSuccess() {
        // This is dope, I mean, reactor takes care of the remaining tasks until they're taken by the scheduler...
        // I only had to customize the waiting queue when no employee is available
        ParallelFlux<String> tasks = Flux.range(1, 20)
                .parallel()
                .flatMap(dispatcher::dispatch);

        StepVerifier.create(tasks)
                .expectSubscription()
                .expectNextCount(20L)
                .expectComplete()
                .verify();

    }
}
