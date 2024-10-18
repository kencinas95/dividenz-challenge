# dividenz-challenge
### Employee class
Abstract class which holds an abstract method `getMessage`, which should be implemented by `Operator`, `Supervisor`, `Director`.

### Operator class
Implementation of `Employee`: implements the `getMessage` by returning a custom message.
> getMessage: 'I'm a operator, how can I help you?'

### Supervisor class
Implementation of `Employee`: implements the `getMessage` by returning a custom message.
> getMessage: 'I'm a supervisor, how can I help you?'


### Director class
Implementation of `Employee`: implements the `getMessage` by returning a custom message.
> getMessage: 'I'm a director, how can I help you?'

### CallDispatcher class
A dispatcher which holds an instance of `EmployeeService`, and a thread pool of 10 workers.
> __async dispatch(callId: integer) => promise[str]__
> 
> This method consumes a _callId_ integer value and returns a promise or a string, which is the
> message from the attending employee.
>
> The dispatcher holds a thread pool with 10 workers. If all workers are full, the next call will be enqueued until a worker is set to free.
> If all employees are occupied with a call, the next call will be enqueued until an employee is set to free once again and it could be available to attend this call.
> No timeout nor amount limit is set for this queue (this is managed by the __reactor__ lib, but it could be configured)

### EmployeeService
The service holds the `employee pool`, which is a `ConcurrentMap` of `Class<? extends Employee>` and `Queue<Employee>`.
When the service is initialized, the pool is created and filled with `Operator.class`, `Supervisor.class`, `Director.class` (in this order)
as keys and empty `ConcurrentLinkedQueue` instances.
> __getNextAvailableEmployee() => Optional[Employee]__
> 
> This method (thread safe) will check all queues (in order from Operator, Supervisor, and finally Director) for an available employee. 
> Then, this employee will be dequeued and returned as the next value. It can return an empty value if all queues are empty.

> __assignCall(callId: integer, employee: Employee) => promise[str]__
> 
> This method will simulate the call duration. It will print the message from the employee when it answers the call. 
> If the execution finished (by success or by error), the employee is set to free once again (it will be enqueued).