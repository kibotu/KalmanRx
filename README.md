[![Donation](https://img.shields.io/badge/buy%20me%20a%20coffee-brightgreen.svg)](https://www.paypal.me/janrabe/5) [![About Jan Rabe](https://img.shields.io/badge/about-me-green.svg)](https://about.me/janrabe) 
# KalmanRx [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-KalmanRx-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4539) [![](https://jitpack.io/v/kibotu/KalmanRx.svg)](https://jitpack.io/#kibotu/KalmanRx) [![](https://jitpack.io/v/kibotu/KalmanRx/month.svg)](https://jitpack.io/#kibotu/KalmanRx) [![Javadoc](https://img.shields.io/badge/javadoc-SNAPSHOT-green.svg)](https://jitpack.io/com/github/kibotu/KalmanRx/master-SNAPSHOT/javadoc/index.html) [![Build Status](https://travis-ci.org/kibotu/KalmanRx.svg?branch=master)](https://travis-ci.org/kibotu/KalmanRx) [![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)  [![Gradle Version](https://img.shields.io/badge/gradle-5.4.0-green.svg)](https://docs.gradle.org/current/release-notes) [![Kotlin](https://img.shields.io/badge/kotlin-1.3.30-green.svg)](https://kotlinlang.org/) [![Licence](https://img.shields.io/badge/licence-Apache%202-blue.svg)](https://raw.githubusercontent.com/kibotu/KalmanRx/master/LICENSE) [![androidx](https://img.shields.io/badge/androidx-brightgreen.svg)](https://developer.android.com/topic/libraries/support-library/refactor)

## Introduction

Removes the noise from float streams using [Kalman Filter](https://en.wikipedia.org/wiki/Kalman_filter). Useful to smoothen sensory data e.g.: [gps location](https://github.com/villoren/KalmanLocationManager), or [Accelerometer](https://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel). 

![Screenshot](https://raw.githubusercontent.com/kibotu/KalmanRx/master/screenshot.png) ![Screenshot](https://raw.githubusercontent.com/kibotu/KalmanRx/master/screenshot2.png)

## [How to use](https://github.com/kibotu/KalmanRx/blob/master/app/src/main/java/net/kibotu/kalmanrx/app/ui/AccelerationSensorKalmanFragment.java#L16-L19)

Library is supporting up to 3 values smoothened from a stream.

(float) stream

```java
KalmanRx.createFrom1D(floatObservable..map(e -> e.value))
    .subscribe(value->{}, Throwable::printStackTrace);
```

(float, float) stream

```java
KalmanRx.createFrom2D(floatObservable..map(e -> e.values))
    .subscribe(values->{}, Throwable::printStackTrace);
```

(float, float, float) stream

```java
KalmanRx.createFrom3D(floatObservable..map(e -> e.values))
    .subscribe(value->{}, Throwable::printStackTrace);
```

## How to install

```groovy
    implementation 'com.github.kibotu:KalmanRx:-SNAPSHOT'
```

## How to build

```bash    
graldew clean build
```

### CI 

```bash    
gradlew clean assembleRelease test javadoc
```

#### Build Requirements

- JDK8
- Android Build Tools 27.0.3
- Android SDK 27

### Notes

Follow me on Twitter: [@wolkenschauer](https://twitter.com/wolkenschauer)

Let me know what you think: [jan.rabe@kibotu.net](mailto:jan.rabe@kibotu.net)

Contributions welcome!

### License
<pre>
Copyright 2016 Jan Rabe

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
