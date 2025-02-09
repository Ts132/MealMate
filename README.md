# **MealMate - Documentation**

## **1. Overview**

MealMate is an Android application designed to help users plan their meals efficiently. It integrates with TheMealDB API to provide meal suggestions, allow users to search meals by category, ingredient, or country, and save favorite meals for offline access. The app also includes a calendar-based meal planner where users can schedule meals for specific dates.

## **2. Features**

### **2.1 Home Screen**

- Displays the "Meal of the Day" retrieved from TheMealDB API.
- Clicking on the meal shows detailed information including ingredients, an image, and a YouTube tutorial.

### **2.2 Search Functionality**

- Users can browse meals by:
  - **Categories** (e.g., Seafood, Vegan, Dessert) with search and filter functionality.
  - **Ingredients** (e.g., Chicken, Rice, Cheese) with filtering options.
  - **Countries** (e.g., Italian, Mexican, Indian) with search and filter capabilities.
- Enhanced filtering and searching to quickly find meals within categories and countries.
- Efficiently fetches data using TheMealDB API.

### **2.3 Favorites**

- Users can save their favorite meals for offline access.
- Uses **Room Database** for local storage.

## **3. Tech Stack**

### **3.1 Languages & Frameworks**

- **Kotlin** (Primary language for Android development)
- **XML** (For UI design)

### **3.2 APIs & Libraries**

- **TheMealDB API** (Fetch meals and meal details)
- **Retrofit** (For network calls)
- **Gson** (For JSON parsing)
- **Room Database** (For local storage of favorite and planned meals)
- **Glide** (For loading images efficiently)
- **Material Components** (For UI elements like buttons, dialogs, and menus)

## **4. Database Schema**

### **4.1 Meal Table** (For storing favorite meals)

| Column          | Type    | Description           |
| --------------- | ------- | --------------------- |
| idMeal          | String  | Unique ID of the meal |
| strMeal         | String  | Name of the meal      |
| strCategory     | String  | Category of the meal  |
| strArea         | String  | Country of origin     |
| strInstructions | String  | Cooking instructions  |
| strMealThumb    | String  | Image URL             |
| isFavorite      | Boolean | Mark as favorite      |

### **4.2 Planned Meals Table** (For storing planned meals)

| Column    | Type   | Description                        |
| --------- | ------ | ---------------------------------- |
| mealId    | String | Unique ID of the meal              |
| mealName  | String | Name of the meal                   |
| mealImage | String | Image URL                          |
| date      | String | Date for which the meal is planned |

## **5. API Integration**

### **5.1 Fetch Meal of the Day**

```kotlin
@GET("random.php")
suspend fun getMealOfTheDay(): Response<MealResponse>
```

### **5.2 Search Meals**

```kotlin
@GET("filter.php")
suspend fun getMealsByCategory(@Query("c") category: String): Response<MealResponse>
```

```kotlin
@GET("filter.php")
suspend fun getMealsByCountry(@Query("a") country: String): Response<MealResponse>
```

### **5.3 Get Meal Details**

```kotlin
@GET("lookup.php")
suspend fun getMealById(@Query("i") mealId: String): Response<MealResponse>
```

 
## **6. Conclusion**

MealMate is a robust and user-friendly meal planning application that helps users explore and organize their meals seamlessly. Using modern Android development practices, APIs, and a structured database approach, it delivers a smooth user experience.

---

### **How to Run the Project**

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repository/MealMate.git
   ```
2. Open the project in **Android Studio**.
3. Sync dependencies and build the project.
4. Run the application on an emulator or a physical device.

 

