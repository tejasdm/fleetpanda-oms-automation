# OMS Automation

API and UI test framework for the flow: Login -> Create Order -> Payment -> Order Validation.
Built with Java 17, Maven, TestNG, REST Assured and Selenium.

## Prerequisites
- Java 17 (check with `java -version`)
- Maven 3.9+ (check with `mvn -version`)(https://dlcdn.apache.org/maven/maven-3/3.10.0/binaries/apache-maven-3.10.0-bin.zip)
- Google Chrome (for the UI tests, Selenium downloads the driver itself)
- Internet access (tests call fakestoreapi.com and saucedemo.com)

## UI Automation
- UI tests use saucedemo.com, since there is no UI for the fakestore APIs.
- Payment failure can’t be simulated on the UI, so the UI failure cases are invalid login, locked user and checkout with missing details. Payment failure is         covered in the API tests using the mocked payment service.
- Chrome must be installed. Selenium Manager downloads the driver automatically.

## How to run the tests

Run everything (API + UI):
mvn clean test

Run without opening the browser (headless, used in CI):
mvn clean test -Dheadless=true

Run only the API tests:
mvn clean test -Dtest=OrderFlowTest

Run only the UI tests:
mvn clean test -Dtest=UiOrderFlowTest

Run one single test:
mvn clean test -Dtest=OrderFlowTest#invalidLogin_shouldFail

## Reports
After a run, open `target/surefire-reports/emailable-report.html` in a browser.
Note: `mvn clean` deletes the `target` folder, so the report is created fresh on each run.

## Project structure
src/test/java
├── api      -> API classes (LoginApi, OrderApi, PaymentApi)
├── pages    -> Selenium page objects (LoginPage, ProductsPage, CheckoutPage)
├── base     -> BaseTest (API + mock payment setup), UiBase (browser setup)
├── utils    -> TestData (all test data and constants), DbHelper (DB validation)
└── tests    -> OrderFlowTest (API), UiOrderFlowTest (UI)

## Test data
All test data (users, card numbers, statuses, expected messages) is in `utils/TestData.java`.

## Assumptions
- fakestoreapi.com is used for login and orders. It does not store data, so orders are not really saved.
- It returns 201 for order creation and a plain text body for a wrong login, so the tests assert on that.
- There is no public payment API, so payment is mocked with WireMock on port 8089.
  Card 4111111111111111 = success, 4000000000000002 = declined.
- DB is simulated with H2 in-memory to show how the validation would work.
- UI tests use saucedemo.com because the fakestore APIs have no UI.
- Payment failure can't be simulated on the saucedemo UI, so it is covered in the API tests.
- Chrome must be installed for the UI tests.

## CI
GitHub Actions runs the tests on every push (`.github/workflows/tests.yml`).
The test report is uploaded as an artifact after each run.
How it works
- You push code to GitHub or create a pull request.
- GitHub Actions starts the workflow.
- It sets up Java 17 and Maven caching.
- It runs mvn clean test -Dheadless=true.
- It uploads the Surefire test reports, even if the tests fail.
