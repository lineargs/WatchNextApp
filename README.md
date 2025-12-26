WatchNext
=========
Simple application to track and explore all the information from your favourite movies and series.

<img src="app/src/main/ic_launcher-web.png" width="200" height="200"/>

Features
--------
- Discover Popular, Top Rated, and Upcoming Movies.
- Discover Popular, Top Rated, and On The Air TV Series.
- See what Movies are currently in Theaters.
- Universal Search for Movies and Series.
- Manage your Favorites.
- Subscribe to Series to get reminders for all upcoming episodes.
- Manually schedule reminders for individual episodes.
- Offline support for viewing cached content.
- Choose from Grey / Blue Grey Theme.

<a href='https://play.google.com/store/apps/details?id=com.lineargs.watchnext&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://github.com/lineargs/WatchNextApp/blob/master/google_play.png' height="70"/></a>

Getting Started
---------------
1.  **Clone the repository**:
    ```bash
    git clone https://github.com/lineargs/WatchNextApp.git
    ```
2.  **Open in Android Studio**:
    Open the `WatchNextApp` folder in Android Studio.

3.  **Get a TMDB API Key**:
    -   Create an account at [The Movie Database (TMDb)](https://www.themoviedb.org/).
    -   Go to your account settings and generate an API key in the API section.

4.  **Configure API Key**:
    -   Open or create the `gradle.properties` file in the root directory of the project.
    -   Add the following line, replacing `YOUR_API_KEY` with the key you obtained:
        ```properties
        TMDbApiKey="YOUR_API_KEY"
        ```

5.  **Run the App**:
    -   Sync the project with Gradle files.
    -   Run the app on an emulator or a physical device.

Contributing
------------
Contributions are welcomed! If you'd like to improve WatchNext, please fork the repository and submit a pull request.

Powered by
----------

<a href='https://github.com/lineargs/WatchNextApp/blob/master/powered_by.png'><img alt='The TMDb' src='https://github.com/lineargs/WatchNextApp/blob/master/powered_by.png' height="90"/></a>


License
-------

```MIT License

Copyright (c) 2017 Goran Minov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.```
