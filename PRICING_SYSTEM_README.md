# Pricing System Documentation

## Overview
The AkolaDirectory app now includes a comprehensive pricing system that allows users to purchase advertisement packages to promote their businesses. The system integrates with Stripe for secure payment processing and Firebase for data storage.

## Features

### 1. Package Management
- **Multiple Package Types**: Basic, Premium, Business, and Enterprise packages
- **Flexible Pricing**: Configurable prices, durations, and features
- **Feature-based Packages**: Each package includes specific features like:
  - Featured listings
  - Social media promotion
  - Priority support
  - Analytics dashboard
  - Custom branding
  - Multiple images/videos
  - Location targeting

### 2. Payment Processing
- **Secure Payments**: Integrated with Stripe for secure card payments
- **Multiple Payment Methods**: Support for cards, UPI, net banking, and wallets
- **Transaction Tracking**: Complete transaction history with status tracking
- **Receipt Generation**: Automatic receipt generation for successful payments

### 3. User Experience
- **Easy Package Browsing**: Clean interface to view and compare packages
- **Secure Payment Flow**: Step-by-step payment process with validation
- **Purchase History**: Users can view all their past purchases and active packages
- **Status Tracking**: Real-time status updates for transactions

### 4. Admin Features
- **Package Management**: Admins can create, update, and manage packages
- **Transaction Monitoring**: View all transactions and user purchases
- **Refund Processing**: Handle refunds and cancellations
- **Analytics**: Track sales and popular packages

## Technical Implementation

### Models
- **PricingPackage**: Represents advertisement packages with pricing and features
- **Transaction**: Handles payment transactions and status tracking
- **PaymentDetails**: Manages payment information for processing

### Activities
- **PackagesActivity**: Displays available packages for purchase
- **PaymentActivity**: Handles payment processing and validation
- **TransactionHistoryActivity**: Shows user's purchase history

### Data Storage
- **Firebase Integration**: Packages and transactions stored in Firebase Realtime Database
- **Local Caching**: Default packages available when offline
- **Data Synchronization**: Real-time updates across devices

### Payment Integration
- **Stripe SDK**: Secure payment processing with Stripe Android SDK
- **Payment Validation**: Client-side and server-side validation
- **Error Handling**: Comprehensive error handling for failed payments

## Configuration

### Setting Up Stripe
1. Create a Stripe account at https://stripe.com
2. Get your publishable and secret keys
3. Update the keys in `PaymentService.kt`:
   ```kotlin
   private const val TEST_PUBLISHABLE_KEY = "pk_test_your_key_here"
   private const val TEST_SECRET_KEY = "sk_test_your_key_here"
   ```

### Firebase Configuration
1. Ensure Firebase is properly configured in your project
2. Set up Firebase Realtime Database rules:
   ```json
   {
     "rules": {
       "pricing_packages": {
         ".read": "auth != null",
         ".write": "auth != null && auth.token.admin == true"
       },
       "transactions": {
         ".read": "auth != null && auth.uid == $uid",
         ".write": "auth != null && auth.uid == $uid"
       }
     }
   }
   ```

### Adding New Packages
Packages can be added programmatically through the `PricingRepository`:

```kotlin
val newPackage = PricingPackage(
    name = "Custom Package",
    description = "Tailored for your business needs",
    price = 1499.0,
    durationDays = 60,
    features = listOf("All Premium Features", "Custom Analytics"),
    maxAds = 10,
    isPremium = true,
    categoryType = PackageCategory.ENTERPRISE
)

pricingRepository.savePackage(newPackage)
```

## Security Considerations

### Payment Security
- **PCI Compliance**: Stripe handles PCI compliance requirements
- **No Card Storage**: Card details are not stored locally
- **Secure Transmission**: All payment data encrypted in transit
- **Input Validation**: Comprehensive validation of payment forms

### Data Protection
- **User Authentication**: All transactions tied to authenticated users
- **Data Encryption**: Sensitive data encrypted at rest
- **Access Control**: Role-based access for admin functions
- **Audit Trail**: Complete transaction logging for compliance

## Testing

### Unit Tests
Run the included unit tests to verify model functionality:
```bash
./gradlew test
```

### Integration Testing
Test the complete payment flow:
1. Browse packages in PackagesActivity
2. Initiate payment in PaymentActivity
3. Verify transaction in TransactionHistoryActivity

### Test Cards
Use Stripe's test card numbers for testing:
- **Successful Payment**: 4242424242424242
- **Declined Payment**: 4000000000000002
- **Authentication Required**: 4000002500003155

## Troubleshooting

### Common Issues

1. **Payment Fails**
   - Check internet connectivity
   - Verify Stripe keys are correct
   - Ensure test/live mode consistency

2. **Packages Not Loading**
   - Check Firebase configuration
   - Verify database rules
   - Check user authentication

3. **UI Issues**
   - Ensure all drawable resources are available
   - Check color resource definitions
   - Verify layout compatibility with device screen size

### Logging
Enable detailed logging by setting log level in `PaymentService`:
```kotlin
private const val TAG = "PaymentService"
Log.d(TAG, "Debug message here")
```

## Future Enhancements

### Planned Features
- **Subscription Management**: Recurring payment support
- **Promotional Codes**: Discount and coupon system
- **Analytics Dashboard**: Detailed package performance metrics
- **Multi-currency Support**: Support for multiple currencies
- **Offline Payments**: Handle payments when offline
- **Apple Pay/Google Pay**: Additional payment methods

### Scaling Considerations
- **Load Balancing**: Distribute payment processing load
- **Caching Strategy**: Implement Redis for package caching
- **Database Optimization**: Index optimization for transaction queries
- **Monitoring**: Implement comprehensive error monitoring

## Support

For technical support or questions about the pricing system:
- **Email**: support@akoladirectory.com
- **Documentation**: This README file
- **Code Comments**: Detailed comments in source code

## License

This pricing system is part of the AkolaDirectory application and follows the same licensing terms as the main application.