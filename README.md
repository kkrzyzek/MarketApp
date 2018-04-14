
# MarketApp  ver1.0

Tech task solution  - create an Android application presenting the list of markets available for different countries.

## About
Application meets all specified requirements.
UI is designed to be user-friendly.
 * Markets are presented in a form of a scrollable list
 * Each list item consists of instrument name and offer
 * List is sorted alphabetically by instrument name
 * User can reload list with a swipe down gesture 
 * Markets for UK are selected as default
 * User can choose country using Floating Action Menu (UK, Germany, France)
 * Application:
 1. supports different screen sizes
 2. handles screen orientation changes
 3. handles network connection errors


### Requirements

* Android 4.03 or newer
* Internet connection

### Screenshots
<img src="https://github.com/kkrzyzek/MarketApp/blob/master/appScreenshots/splashScreen.png?raw=true" width="300"/>
<img src="https://github.com/kkrzyzek/MarketApp/blob/master/appScreenshots/mainView.png?raw=true" width="300"/>
<img src="https://raw.githubusercontent.com/kkrzyzek/MarketApp/master/appScreenshots/dialog.png" width="300"/>
<img src="https://github.com/kkrzyzek/MarketApp/blob/master/appScreenshots/floatingActionMenu.png?raw=true" width="300"/>
<img src="https://github.com/kkrzyzek/MarketApp/blob/master/appScreenshots/sthWentWrong.png?raw=true" width="300"/>

## Libraries

* [Retrofit 2.4.0](http://square.github.io/retrofit/) - Networking
* [gson](https://github.com/google/gson) - JSON deserialization
* [clans/FAB](https://github.com/Clans/FloatingActionButton) - Android Floating Action Menu

## Author

* **Krzysztof Krzyzek** - *Initial work* - [LinkedIn](https://www.linkedin.com/in/kkrzyzek/)


## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/kkrzyzek/MarketApp/blob/master/README.md) file for details.
