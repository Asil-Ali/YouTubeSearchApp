# YouTube Video Search App

An Android app that lets you search YouTube videos using the YouTube Data API v3.

## Features

- Search YouTube videos by keyword
- Sort results by Relevance, Date, Rating, or View Count
- Displays video title, channel name, publish date, description, and thumbnail
- Loading indicator while fetching results
- Error handling for empty input, no results, network failure, and API key issues

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Let Gradle sync finish
4. Run on an emulator or physical device (Android 7.0+)

> The API key is already included in the source code for this assignment.

## Tech Stack

- Kotlin
- Retrofit + Gson for networking
- Glide for image loading
- Coroutines for background tasks
- ViewModel + LiveData
- RecyclerView with DiffUtil
- ViewBinding
