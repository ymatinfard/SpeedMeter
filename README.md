# SpeedMeter

## Overview
SpeedMeter is a beautifully designed, modular Android project built with Clean Architecture principles. It ensures scalability, maintainability, and testability while providing a seamless user experience. The project is structured with core modules that handle essential functionalities and feature modules that focus on the presentation layer.

## Project Structure
The project is divided into several modules to enhance modularity and separation of concerns:

### **Core Modules**
These modules encapsulate the core functionalities and provide a robust foundation for the feature modules.

- **designsystem**: Manages theming and shared UI components.
- **data**: Implements the repository pattern and handles data requests from various sources.
- **network**: Manages API interactions and network calls.
- **database**: Handles local storage using Room database. Player sessions are stored in the database to maintain session persistence and manage user interactions efficiently.
- **testing**: Provides testing utilities to ensure code reliability.
- **common**: Contains shared utility functions and extensions.

### **Feature Modules**
Feature modules focus on specific functionalities within the application while following the Clean Architecture principles.

- **feature/stopwatch**: Implements the stopwatch functionality and manages the presentation layer.

### **Worker Module**
- **worker**: Handles long-running operations such as creating CSV files and syncing data periodically with the server using WorkManager.

### **Model Module**
- **model**: Defines domain models that are shared across the application, ensuring better separation of concerns and code reusability.

## **Dependency Injection**
SpeedMeter utilizes **Hilt** as its dependency injection framework, enhancing modularity and facilitating better testability.

## **Multi-Level Caching Strategy**
To optimize performance and provide offline support, SpeedMeter implements a **multi-level caching mechanism**:
1. **Memory Cache**: Data is first retrieved from in-memory storage.
2. **Local Storage (Room Database)**: If not available in memory, the data is fetched from the local database.
3. **Network Call**: If no cached data is found, a network request is made.

These operations are handled automatically within the **data** module, ensuring a seamless experience for data requesters without requiring them to manage caching logic.

## **Repository Pattern**
The **data** module follows the repository pattern, providing a clean and structured interface for other modules to request data without worrying about the underlying data sources.

## **Design System**
The **designsystem** module standardizes theming and UI components, ensuring consistency across the application.

## **Build Script Optimization**
To improve maintainability, the project defines **convention plugins** to manage build scripts efficiently across different modules, promoting a modularized build process.

## **App flavors**
- **Demo**: fetches fake players data wihout making network call, useful for testing and development.
- **Prod**: Uses real netowrk call to fetch players information for production use.  

## **Testing**
Unit tests have been written for both the ViewModel and data sections. The project uses MockK for mocking dependencies and Turbine for testing Flow operations

## Screenshots

<p float="left">
  <img src="https://github.com/ymatinfard/SpeedMeter/blob/develop/screenshots/stopwatch_screen.png" alt="Stopwatch Screenshot" width="400" height="800" />
  <img src="https://github.com/ymatinfard/SpeedMeter/blob/develop/screenshots/leaderboard_screen.png" alt="Leaderboard Screenshot" width="400" height="800" /> 
  <img src="https://github.com/ymatinfard/SpeedMeter/blob/develop/screenshots/player_selection_screen.png" alt="Player selection Screenshot" width="400" height="800" />
  <img src="https://github.com/ymatinfard/SpeedMeter/blob/develop/screenshots/distance_setup_screen.png" alt="Distance setup Screenshot" width="400" height="800" /> 
  <img src="https://github.com/ymatinfard/SpeedMeter/blob/develop/screenshots/player_metric_chart_screen.png" alt="Player metric Screenshot" width="400" height="800" />
  <img src="https://github.com/ymatinfard/SpeedMeter/blob/develop/screenshots/stopwatch_dark_screen.png" alt="Stopwaatch dark screen Screenshot" width="400" height="800" />
</p>

