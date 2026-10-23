## Setup
- Install Java 17 and Maven(https://dlcdn.apache.org/maven/maven-3/3.10.0/binaries/apache-maven-3.10.0-bin.zip)
- Clone the repository

## Run
mvn clean test

## Assumptions
- Used fakestoreapi.com for login and orders (it doesn't save data, so the order is not really stored)
- Payment has no public API, so I mocked it with WireMock on port 8089
- Used REST Assured because Selenium is only for browser UI
- DB is mocked with H2 in-memory to show how the validation would work
- Test card 4111111111111111 = success, 4000000000000002 = declined

## UI Automation
- UI tests use saucedemo.com, since there is no UI for the fakestore APIs.
- Payment failure can’t be simulated on the UI, so the UI failure cases are invalid login, locked user and checkout with missing details. Payment failure is covered in the API tests using the mocked payment service.
- Chrome must be installed. Selenium Manager downloads the driver automatically.

## CI/CD
How it works
- You push code to GitHub or create a pull request.
- GitHub Actions starts the workflow.
- It sets up Java 17 and Maven caching.
- It runs mvn clean test -Dheadless=true.
- It uploads the Surefire test reports, even if the tests fail.
