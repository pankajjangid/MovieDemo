# Android VIPER Architecture Example (Kotlin)

This project teaches you how to use the VIPER Architecture In Android using Kotlin.In this project we are using the [themoviedb.org](https://developers.themoviedb.org/4/getting-started/authorization)

Within the framework of an Android app, the VIPER layers are assigned according to the following scheme:


![N|Solid](https://koenig-media.raywenderlich.com/uploads/2018/02/viper-scheme-480x273.png)

[Image credit: www.raywenderlich.com](https://www.raywenderlich.com/5192-android-viper-tutorial#:~:text=VIPER%20stands%20for%20View%2C%20Interactor,following%20the%20Single%20Responsibility%20Principle)

## Files description 


### Views
The View corresponds to an Activity or Fragment in the app. A goal is to make the View as dumb as possible, so that it only takes care of showing the UI.
#### [SplashActivity](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/view/acitivty/SplashActivity.kt)
#### [MainActivity](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/MainActivity.kt)
#### [DetailActivity](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/view/acitivty/DetailActivity.kt)
#### [PopularFragment](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/view/fragments/PopularFragment.kt)
#### [TopRatedFragment](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/view/fragments/TopRatedFragment.kt)
#### [UpcomingFragment](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/view/fragments/UpcomingFragment.kt)


### Interactor  
The Interactor takes care of performing any action, when the Presenter says to.

#### [PopularIntegrator](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/interactor/PopularIntegrator.kt)
#### [TopRatedIntegrator](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/interactor/TopRatedIntegrator.kt)
#### [UpcomingIntegrator](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/interactor/UpcomingIntegrator.kt)
### Presenter 
The Presenter acts as a “Head-of-Department”. In other words, it commands any action making use of the Interactor, tells the View to display content, and orders the navigation to other screens using the Router.

#### [SplashPresenter](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/presenter/SplashPresenter.kt)
#### [MainPresenter](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/presenter/MainPresenter.kt)
#### [PopularPresenter](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/presenter/PopularPresenter.kt)
#### [TopRatedPresenter](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/presenter/TopRatedPresenter.kt)
#### [UpcomingPresenter](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/presenter/UpcomingPresenter.kt)
#### [DetailPresenter](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/presenter/DetailPresenter.kt)

### Entity  

The Entity represents the app data. In short, it acts likes the Model in the MVP architecture pattern.

#### [MoviesResponse](https://github.com/pankajjangid/MovieDemo/blob/master/app/src/main/java/com/demo/themoviedb/entity/MoviesResponse.kt)

### Router 

The Router handles navigating to other screens during the app lifecycle. Every view has a router with in so please check the views.

These are the main components of VIPER Architecture. In this project we use the [Fuel HTTP library](https://github.com/kittinunf/Fuel) and for persistence storage we are using the [Room Database](https://developer.android.com/training/data-storage/room).
