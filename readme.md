# Scala on Android without SBT


Want to use Scala on Android with Intellij without having to bring the entire sbt build system into your life and the pros/cons it provides?

Before you had to use something [like this with sbt](https://github.com/jberkel/android-plugin) and build externally. It's a nice solution if you want to use SBT, but sometimes I just want to do some scripting in Scala without bringing along an entire build system.


## Setup & Directions

Directions are below or [click here](https://github.com/yareally/android-scala-intellij-no-sbt-plugin/blob/master/readme.md#building-with-intellij-idea-12-or-android-studio) to jump directly to them. Just added images and an Intellij project file. With any luck, the addition of the project file + images should help anyone experiencing issues.


## Caveats

Any Android library that requires custom annotation processing may not work [such as Android Annotations](http://androidannotations.org/). Just mentioning it now if you find yourself spending way too much time with errors that make no sense. A detailed explanation about why it doesn't work can be found on [Stack Overflow](http://stackoverflow.com/questions/7454018/using-androidannotations-with-scala-and-gradle).

## Examples

Aside from the minimal code showing that Scala works on Android, here's some more elaborate [source code](https://github.com/yareally/SignalInfo/tree/scala) to a Scala Android app I built using a few popular libraries [ActionBarSherlock](http://actionbarsherlock.com), [support.GridLayout](http://developer.android.com/tools/support-library/features.html#v7-appcompat) and [loaderex](https://github.com/commonsguy/cwac-loaderex)).

An [example](https://gist.github.com/yareally/589855a3c5e4d73410b1) using Scala's [Pimp my Library](http://alvinalexander.com/scala/scala-2.10-implicit-class-example) pattern to get rid of some of the verbosity of Java Android for setting View.onClick listeners and getting view types from a resource ID.

A [small example](https://gist.github.com/yareally/5941536) using Typesafe's [Async/Await library](http://docs.scala-lang.org/sips/pending/async.html) and an example of how to convert a Java Anonymous function to a Scala lambda. Also requires [adding the continuations library](https://github.com/yareally/android-scala-intellij-no-sbt-plugin#prerequisites).

## FAQ

### What works?

Everything as far as I can tell (even debugging)

### What doesn't work?

Nothing as far as I know so far.

### Why use this over the sbt plugin?

#### A few reasons:

- No additional tools or code needed (other than ProGuard) so less potential for bugs or issues with Intellij/Android updates.

- Simpify your build process by doing what you normally do when building apps in Intellij.

- You're used to using Java and would like to keep building apps mostly how you did beforehand.

- Don't need a formal build system like sbt or just want to start writing code instead of also learning a useful, but somewhat complex build system.

### What version of Scala did you test with?

2.11.2, the latest stable as of 2014-11-07

In 2.11.x, Scala's XML library, parser/combinators library, and continuations library have been decoupled from scala-library.jar and require being added in addition to scala-library.jar if needed. Those libraries can be found under the same directory as scala-library (/lib/ under your scala install directory). Proguard may also optional if you do not require the full library (but recommended to reduce size for market apps) for Scala 2.11.x due to the reduced size of scala-library.jar

### How is debugging possible?

Proguard is set not to obfuscate so it doesn't remove the debugging symbols. That's really the key to my guide. Proguard can be tricky, but I've taken care of most of the common edge cases you might encounter with the included proguard file in this repository. If you encounter any I missed, just let me know. Things like actionbar sherlock and other libraries should work fine though.

### Why does Proguard work?

In Scala 2.10.0, there are way too many methods in Scala for the dalvik compiler (dx) to handle if you simply add all the jars to the class path and compile. Proguard will strip out all the excess (otherwise, you'll get errors like Android Dex: [android-scala] trouble writing output: Too many methods: 66095; max is 65536.).

In Scala 2.11.x, the jar size for scala-library has been reduced considerably by decoupling the XML, parser and continuation libraries and may not require Proguard (though it's recommended if you want to reduce the size of the apk for market apps). 

### Does Google's Android Studio work, since it's based on Intellij?

Last I checked, I did not see a way to compile an Android app in Android Studio without Gradle, so my guide will not work. There exists a [Android + Scala + Gradle plugin](https://github.com/saturday06/gradle-android-scala-plugin) that would work for Android Studio, but I have never used it. You're probably better off using SBT though, if you want a build system for Scala on Android.

### I have questions or want to add examples or documentation to this guide

Feel free to create an issue and/or send a pull request.

## Building with Intellij IDEA 12+ or Android Studio

### Intellij IDEA 14 Update

If you're coming from a project on Intellij IDEA 13, Intellij IDEA 14 will automatically update it for you and work without an issue (unless you were using continuations, you may have to re-add the library). If you're starting out with IDEA 14, there have been some changes (mentioned below). I will update the images in my guide to reflect that soon, but do not have time until later in November.

I recently tested (2014-11-07) Intellij IDEA 14 with my guide and it works fine. However some of the locations for configuration changes have moved since version 13. Scala specific settings are now located under File → Settings → Build, Execution, Deployment → Compiler → Scala Compiler (new location for enabling/adding the continuations library) while others remain under "Project Structure." 

Certain steps, like adding a Scala facet are no longer needed (thanks JetBrains!) and you can add Scala directly into an existing Android project under Project Settings → Modules → Add (the + icon) → New Module. This will also guide you through setting up the Scala Compiler and libraries if you do not already done so. Proguard settings are under Facets → Android → Proguard Tab and still need to be configured as usual.

Scala Android projects also compile a bit faster with IDEA 14 (about 5-10% depending on the project size and libraries). Scala performance in the editor is also much improved. 

### Prerequisites

1. [Get Scala](http://www.scala-lang.org/). If you don't have the Scala plugin for Intellij, you need that too of course. Just download the plugin from within the IDE under settings → plugins.

2. Add a Scala facet to the project, and set up the compiler (I recommend using at least Scala 10.x). You may have to [create a separate throwaway Scala module](http://confluence.jetbrains.com/display/SCA/Setting+up+Scala+plugin+project+in+IntelliJ+IDEA) first if no Scala plugin has already been added globally. Note that scala compiler is now its own section and not under compiler as of IDEA 12.1.

3. Set up the Scala facet for the Android project (add continuations if you wish). Alternatively, setting up a Scala project and then adding an Android Facet might be easier for some. It's possible to do this while creating a new project or for an exising project via *File → Project Structure → Facets*

  ![module settings](/pics/facets.png "Project Facets")

4. Add the following jars as libraries to your project via *File → Project Structure → Libraries*:

  **scala-actors.jar** under */path-to-scala-install-dir/libs/*

  **scala-library.jar** under */path-to-scala-install-dir/libs/*

  **continuations.jar** (optional, though it's required for actors, futures, promises, async/await, etc). The jar is in your scala install directory under */path-to-scala-install-dir/misc/scala-devel/plugins*.

5. Make sure the libraries created above are set as a dependency for all modules you add a Scala facet from step 2. Module dependencies are set via *File → Project Structure → Modules → Dependencies Tab*.

  ![module settings](/pics/modules-deps.png "Android Project Dependencies")

  ![module settings](/pics/modules-scala.png "Scala Project Dependencies")

  If you create any additional modules not using Scala (like Java only ones), but wish to use code you wrote from a module with a Scala facet, you just have to add that Scala module as a dependency to the Java only module as you would normally in Intellij.


### Adding Proguard to the Project

1. Use the given proguard file in this project. It should work for most projects without modifications. If you encounter an issue when adding a library, let me know and I can add a proguard fix for it.

2. Enable proguard for the building of the project via the following:

#### Intellij 12

  Go to: File → project settings → modules → select module(s) → compiler tab → enable proguard

#### Intellij 13 / Android Studio

  Go to: *File → project settings → modules → select module(s) → compiler tab → check Run Proguard checkbox*

  ![module settings](/pics/modules.png "Compiler Settings")

  Make sure both your project proguard file (proguard-project.txt) and the Android SDK provided one (found under */android-sdk-home/tools/proguard/proguard-android.txt*) are both added to the list below the checkbox.

### Compiling your Project

1. After adding proguard, build the project like how you would a normal Java Android project. Proguard is a little slow, but it's required unless you preload the libraries onto the device (which requires root or using the previously mentioned sbt plugin). You should see no errors in the example activity (other than a highlight bug on * symbol for the test() method you can ignore). If anyone has time, feel free to file an issue about it with JetBrains, but it's not a major issue.

  ![module settings](/pics/activity.png "Android Project Dependencies")

2. Run the application
