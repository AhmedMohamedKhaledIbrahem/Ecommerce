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

# Environment Setup

### WordPress Backend with XAMPP *Local*
1. Download and install [XAMPP](https://www.apachefriends.org/index.html) for your operating system.  
2. Start **Apache** and **MySQL** services via the XAMPP control panel.  
3. Download WordPress from [wordpress.org](https://wordpress.org/download/) and extract it into the `htdocs` folder inside your XAMPP installation directory (e.g., `C:\xampp\htdocs\your-project`).  
4. Create a new MySQL database using phpMyAdmin (`http://localhost/phpmyadmin`).  
5. Open `http://localhost/your-project` in your browser and follow the WordPress installation steps, connecting it to the database you created.  
6. Install and activate the WooCommerce plugin and any other necessary plugins or custom code for your REST API endpoints.  
7. Verify your custom REST API endpoints are working at `/wp-json/your-custom-namespace/`.

### Node.js / npm Commands
If your project includes JavaScript tools or builds managed by npm:

1. Install [Node.js](https://nodejs.org/) (version X.X or later).  
2. Open your terminal and navigate to your project directory.  
3. Run the following commands:

```bash
npm install        # Installs all dependencies
npm run build      # Builds the project (if applicable)
npm start          # Starts the development server (if applicable)
```

### Deployment to a Live Server
If you have a hosting server (shared hosting, VPS, or cloud server), follow these steps:

1. Upload your WordPress files to your server via FTP, SSH, or your hosting control panel.  
2. Create a MySQL database on your hosting provider (usually via cPanel or a similar tool).  
3. Update the `wp-config.php` file with your live database credentials.  
4. Import your local database to the live server database using phpMyAdmin or command line tools.  
5. Update your site URL in the database (`wp_options` table, `siteurl` and `home` fields) to your live domain.  
6. Ensure your custom plugins and REST API endpoints are active.  
7. Configure any required server settings such as PHP version, file permissions, and SSL certificates.  
8. Verify the REST API endpoints are accessible via your live domain at: *https://yourdomain.com/wp-json/your-custom-namespace/*

## Adding Custom API and Dependencies

### 1. Adding Custom API Code

- Place custom REST API endpoint code inside your WordPress theme’s `functions.php` file.
- place `fcm-notification.php` in wordpress file
- Download version 1.0.0 [here](https://github.com/yourusername/yourrepo/releases/tag/v1.0.0).

### 2. Handling Vendor Dependencies
```bash
composer install
```

```php
  require_once __DIR__ . '/vendor/autoload.php';
```
## Changing Base URL in Android App
The Android app’s backend base URL is configured using `BuildConfig` in the `build.gradle` file (Module: app).

```gradle
android {
    ...
    buildTypes {
        debug {
            buildConfigField "String", "BASE_URL", "\"https://your-debug-api-url.com/\""
        }
        release {
            buildConfigField "String", "BASE_URL", "\"https://your-release-api-url.com/\""
        }
    }
}
```
