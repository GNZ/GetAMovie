# GetAMovie


| environment | status             |
|-------------|--------------------|
| name        | ![Build Status](https://app.bitrise.io/app/1fe3d50e817deb29/status.svg?token=IP0pY5OUf5q0s44sMreunA&branch=master) |

## Synopsis
Simple proyect to fetch movies currently on the cinema and see details of it. It gives the possibility to search as well.

## Configuration

### Instructions

1. Clone repo
2. Get an API key from [The Movie DB](https://www.themoviedb.org)
3. Create secret.properties file in main folder (atstats-android) and paste contents from table:

| Property         
|---------------------------|
| MovieDbApiKey=[1]          | 
[1] = Your API key

4. Open project in Android Studio
5. That's it!

## Integrations

### Development
This project uses the Pagination library from the Google's Architectural components for showing the list of movies, 
Glide for fetching and showing the images,
Retrofit and OkHttp for API calls and network client
RxJava 2 and Kotlin
