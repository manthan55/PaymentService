## How To Run

Create new file `dev.env.yaml` with appropriate values (just like `sample.env.yaml`) at the root of the project

---

## Running Tests & Coverage

### IntelliJ Coverage
Run all tests in the project with coverage. The coverage window pops up after tests have completed

### Jacoco Coverage
1. maven clean
2. maven test
3. Open `target/site/jacoco/index.html`

---

## Clarifications

### Why not Factory pattern for switching PG Vendors?
Factory pattern is used to **create** objects, but in our case, we actually want objects of all the PG Vendors to be availbale as the client can request to go via any of them. Thus strategy pattern will be useful here.

### Why is `StripeConfig.getPriceId()` not tested as shown by Jacoco report?
`StripeConfig.getPriceId()` is called within `StripePaymentGateway.generatePaymentLink()` when creating `PaymentLinkCreateParams`. We are mocking the entire `PaymentLinkCreateParams` in our unit tests and thus we are not invoking `StripeConfig.getPriceId()`

---

## References

- https://spring.io/blog/2007/01/15/unit-testing-with-stubs-and-mocks
- https://stackoverflow.com/questions/3459287/whats-the-difference-between-a-mock-stub?page=1&tab=scoredesc#tab-top
- https://melkornemesis.medium.com/mocks-vs-stubs-choosing-the-right-tool-for-the-job-dbdbc19cf0c5

---