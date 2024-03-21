### Why not Factory pattern for switching PG Vendors?
Factory pattern is used to **create** objects, but in our case, we actually want objects of all the PG Vendors to be availbale as the client can request to go via any of them. Thus strategy pattern will be useful here.

### Why is `StripeConfig.getPriceId()` not tested as shown by Jacoco report?
`StripeConfig.getPriceId()` is called within `StripePaymentGateway.generatePaymentLink()` when creating `PaymentLinkCreateParams`. We are mocking the entire `PaymentLinkCreateParams` in our unit tests and thus we are not invoking `StripeConfig.getPriceId()`