# Meetmax

## Description
Meetmax is a simple social media app where users can sign in, sign Up, forgot password, post updates (with or without images), like, and comment on posts.

## Technology Stack
- Dagger Hilt for dependency injection
- Navigation Component for seamless navigation
- DataBinding for efficient UI updates
- MVVM architecture for clean and maintainable code
- Room Database as a local data storage solution
- Retrofit (for API integration when backend is available)

## Assumptions
Due to the absence of an API, Room DB is used to simulate backend operations:
- **Sign In & Sign Up**: Room DB stores user information. Once the API is available, minor code adjustments (uncommenting specified lines) will enable live API calls.
- **Forgot Password**: Validates email against the Room DB and shows a toast message if the email exists.

For the post, like, and comment features, Room DB handles all local data storage.

## Testing
All core business logic is in the ViewModel. 
- **Initial User Setup**: On first launch, an initial user is created with:
  - **Email**: `example1@gmail.com`
  - **Password**: `12345678Aa`
  
To test the app's functionality, simply run it and sign up. Posts can be created with or without images.
