# android-trupple-mvp
This is a framework designed to provide flexibility for android developers to implement [Model View Presenter (MVP) pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter). It handles states perfectly and fixes android rotation issues. This library was design to be a plug and play model and fits into the ideaology of seperation of concerns, Strategy Design pattern, Data and Task recovery and Handles android UI tear down issues, due to device orientation change.
by ensuring the following:

 1.   All the bussiness logic is placed in the separate Presenter which routes commands between the Model and the View.
 2.   Android Rotation Issues: The presenter class is stashed in a Memento during rotation and gotten back on resume, as such background task performed using AsyncTask or RxJava/RxAndroid, can continue without breaking, or showing the ANR dialog.
 3.   MVP seperates Views from Business logic as such, it Insulation of Java Code from Android, which makes JUnit Testing and Migration easy, without all the Android dependencies.

This library was inspired by Douglas Shmidth [POSA 14](https://github.com/douglascraigschmidt/POSA-14) and [POSA 15](https://github.com/douglascraigschmidt/POSA-15) class.

# Installation and usage

1. To include the library in yourr project, first add it via gradle

a. In build.gradle config file
```
allprojects {
     repositories {
         ...
               maven { url "http://dl.bintray.com/geniusgeek/maven" }
              }
             }
             ```
b. In app.gradle dependency file
```
dependencies{
    compile 'com.github.geniusgeek:trupple-mvp:0.1.1'
}
```
2. Usage
a. In activity Class: 
```
public class MainActivity extends GenericActivity<MainPresenter> {
....
}
```

b. In presenter Class:

i.    Implement the GenericPresenter interface, and pass in teh Model(any object) and the View(which is the Activity)

```
public class MainPresenter implements MVP.GenericPresenter<Model, MainActivity> {
.....
}
```

ii.    Extend the GenericPresenter abstract class, and pass in teh ModelOps: this defines the [strategy](https://en.wikipedia.org/wiki/Strategy_pattern) to be used for the Model and the and the View(which is the Activity).

```
public class MainPresenter extends  GenericPresenter<ModelOps extends MVP.GenericModelOps, MainActivity> {
.....
}
```

c. Optional: override initViews in the Activity class, so you can innitialize all the views here or better still, you can get access to the binder for the layout(but you must enable binder in you gradle dependency file).

d. Utility Classes:

1. File Utils
2. Bitmap Utils
3. Drawable Utils
4. ConnectionHelper: a helper class for handling connection and internet issues
5. ValidationUtils
6. VersionUtils
7. StorageAccessUtils
8. ObscuredSharedPreferences
9. LocationUtils
10. NotificationUtils etc

# Documentation
coming soon

# Contribution

Feel free to contribute to this library and help to improve it!
And for suggestions, You can send a mail to me at geniusgeek2014@gmail.com

# License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
