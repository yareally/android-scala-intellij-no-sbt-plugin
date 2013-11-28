# Using Scala on Android without SBT


Want to use Scala on Android with Intellij without having to bring the entire sbt build system into your life and the pros/cons it provides?

Before you had to use something [like this with sbt](https://github.com/jberkel/android-plugin) and build externally. It's a nice solution if you want to use SBT, but sometimes I just want to do some scripting in Scala without bringing along an entire build system.


## Setup & Directions

[Text based directions are below](https://github.com/yareally/android-scala-intellij-no-sbt-plugin/blob/master/readme.md#building-with-intellij-idea-12-or-android-studio). I'll add some images soon.

Until I add some images in this guide, directions with images can be [found here](http://stackoverflow.com/a/17515300/1643939) thanks to a helpful user.


## Caveats

Any Android library that requires custom annotation processing may not work [such as Android Annotations](http://androidannotations.org/). Just mentioning it now if you find yourself spending way too much time with errors that make no sense. A detailed explanation about why it doesn't work can be found on [Stack Overflow](http://stackoverflow.com/questions/7454018/using-androidannotations-with-scala-and-gradle).

## Examples

Aside from the minimal code showing that Scala works on Android, here's some more elaborate [source code](https://github.com/yareally/SignalInfo/tree/scala) to a Scala Android app I built using a few popular libraries [ActionBarSherlock](http://actionbarsherlock.com), [support.GridLayout](http://developer.android.com/tools/support-library/features.html#v7-appcompat) and [loaderex](https://github.com/commonsguy/cwac-loaderex)).

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

2.10.3, the latest stable as of 2013-11-27

### How is debugging possible?

Proguard is set not to obfuscate so it doesn't remove the debugging symbols. That's really the key to my guide. Proguard can be tricky, but I've taken care of most of the common edge cases you might encounter with the included proguard file in this repository. If you encounter any I missed, just let me know. Things like actionbar sherlock and other libraries should work fine though.

### Why does Proguard work?

There's way too many methods in Scala for the dalvik compiler (dx) to handle if you simply add all the jars to the class path and compile. Proguard will strip out all the excess (otherwise, you'll get errors like Android Dex: [android-scala] trouble writing output: Too many methods: 66095; max is 65536.).

### Does Google's Android Studio work, since it's based on Intellij?

It should fine without Gradle. If wishing to use Gradle, it would require more work. I have not tested a Gradle based project with Intellij IDEA 13 or Android Studio yet.

### I have questions or want to add examples or documentation to this guide

Feel free to create an issue and/or send a pull request.


## Building with Intellij IDEA 12+ or Android Studio

### Prerequisites

1. [Get Scala](http://www.scala-lang.org/). If you don't have the Scala plugin for Intellij, you need that too of course. Just download the plugin from within the IDE under settings → plugins.

2. Add a Scala facet to the project, and set up the compiler (I recommend using at least Scala 10.x). You may have to [create a separate throwaway Scala module](http://confluence.jetbrains.com/display/SCA/Setting+up+Scala+plugin+project+in+IntelliJ+IDEA) first if no Scala plugin has already been added globally. Note that scala compiler is now its own section and not under compiler as of IDEA 12.1.

3. Set up the Scala facet for the Android project (add continuations if you wish). Alternatively, setting up a Scala project and then adding an Android Facet might be easier for some. It's possible to do this while creating a new project or for an exising project via *File → Project Structure → Facets*

4. Add the following jars as libraries to your project via *File → Project Structure → Libraries*:

  **scala-actors.jar** under */path-to-scala-install-dir/libs/*

  **scala-library.jar** under */path-to-scala-install-dir/libs/*

  **continuations.jar** (optional, though it's required for actors, futures, promises, async/await, etc). The jar is in your scala install directory under */path-to-scala-install-dir/misc/scala-devel/plugins*.

5. Make sure the libraries created above are set as a dependency for all modules you add a Scala facet from step 2. Module dependencies are set via *File → Project Structure → Modules → Dependencies Tab*.

  If you create any additional modules not using Scala (like Java only ones), but wish to use code you wrote from a module with a Scala facet, you just have to add that Scala module as a dependency to the Java only module as you would normally in Intellij.


### Adding Proguard to the Project

1. Use the given proguard file in this project. It should work for most projects without modifications. If you encounter an issue when adding a library, let me know and I can add a proguard fix for it.

2. Enable proguard for the building of the project via the following:

#### Intellij 12

  Go to: File → project settings → modules → select module(s) → compiler tab → enable proguard

#### Intellij 13 / Android Studio 

  Go to: *File → project settings → modules → select module(s) → compiler tab → check Run Proguard checkbox*

  Make sure both your project proguard file (proguard-project.txt) and the Android SDK provided one (found under */android-sdk-home/tools/proguard/proguard-android.txt*) are both added to the list below the checkbox.

### Compiling your Project

1. After adding proguard, build the project like how you would a normal Java Android project. Proguard is a little slow, but it's required unless you preload the libraries onto the device (which requires root or using the previously mentioned sbt plugin).

2. Run the application
