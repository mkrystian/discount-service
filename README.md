Discount service
================

This service is created for recruitment purposes.

## Functional Requirements

- Products in the system are identified by UUID.
- There should be the possibility of applying discounts based on two policies:
    - Amount-based - the more pieces of the product are ordered, the bigger the
      discount is (e.g., 10 items – 2 USD off, 100 items – 5 USD off etc).
    - Percentage-based - user gets a percentage discount – the more items ordered;
      the bigger the percentage discount is. (e.g., 10 items, 3% off, 50 items, 5% off etc).
- Policies should be configurable.

## Non-functional Requirements

- Use Java >= 17 and frameworks of your choice
- The project should be containerized and easy to build and run via Gradle or Maven.
- Please provide a README file with instructions on how to launch it
- There's no need for full test coverage. Implement only the most essential (in your
  opinion) tests
- Use the git repository for developing the project and after you’re done, send us a link to
  it
- Make sure we can run the project easily, without any unnecessary local dependencies
  (e.g., Don’t use OS-specific code)
  o Try not to spend more than one or two evenings on the assignment
  • You will eventually have a chance to explain your code in the next stage of the interview
  • If you must make some assumptions, document them as you see fit. DO the same with
  the technical assumptions you make.

## Assumptions

- For this task purposes the service uses in-memory database, but it could be easily replaced with any other database (preferably Redis, because of its speed)
- Amount discount is discount of unitPrice not the total price
- Only one discount can be applied to a product
- If discount would set price to negative value, the price is set to 0
- Rounding is done to 2 decimal places with method HALF_UP
- The service is responsible for managing only discounts
- The service has to be very quick and responsive
- The service has to be easily scalable
- The service has to be easily extendable
- The service could be started without any additional dependencies (like databases, etc.), but could be easily connected to them
- The service has no security layer for now (like authentication, etc.)

## System requirements

- Java 21

## How to run

- Clone the repository
- Run `./gradlew bootRun` in the root directory of the project
- The service will be available at `http://localhost:8080`