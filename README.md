# ECommerce App
Android application integrated with a WordPress-based e-commerce backend.
This project involved creating custom REST API endpoints for authentication, orders, users, products, categories, and customer details, as well as managing FCM tokens to enable real-time push notifications.
By integrating Firebase Cloud Messaging with WordPress, the app can now receive notifications for order status changes and new product availability, enhancing the user experience with timely updates.
The app supports both Arabic and English languages and offers dark and light mode themes to provide a flexible and accessible user experience.
# Android App Features
- Full product listing with pagination and category filtering
- Product search functionality
- Add to cart, place orders, and view order history
- View product details with image, price, and description
- Real-time order status tracking
- Push notifications for order updates and new product arrivals
- User authentication (login/register/logout/forget password / verification email by otp)
- Edit and view user profile details
- Multilingual support: Arabic and English
- Theme support: Light mode and Dark mode
- Offline caching for better performance
- Smooth and modern UI with responsive layouts
# Backend Integration
This project uses **WordPress + WooCommerce** as the backend, with custom REST API endpoints created for:
- Authentication
- Orders
- Users
- Products
- Categories
- Customer details
- FCM token registration

### Firebase Cloud Messaging Integration
The backend is integrated with **Firebase Cloud Messaging (FCM)** to trigger real-time push notifications in the Android app when:
- Order status changes
- New product is available

#  Technologies & Architecture:
- Kotlin and XML
- MVVM + UDF (Unidirectional Data Flow)
- Clean Architecture
- Retrofit for API calls
- Hilt (Dagger) for dependency injection
- Room for local database
- Coroutines for async operations
- StateFlow for state and Channel for one-time events
- Jetpack Navigation Component
- WorkManager for background tasks
- Firebase SDK
- Unit testing for core business logic
