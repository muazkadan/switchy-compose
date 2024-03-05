# Switchy-compose

[![](https://jitpack.io/v/muazkadan/switchy-compose.svg)](https://jitpack.io/#muazkadan/switchy-compose)

SwitchyCompose is an Android library that helps you to easily create a custom Switches.

## Preview
<img src="/preview/switchy_compose_preview.gif" width="300" >

## Installation
Add the jitpack.io repository:

```groovy
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

and the dependency

```groovy
dependencies {

    implementation "com.github.muazkadan:switchy-compose:$version"
}
```

## Usage

TextSwitch
```kotlin
var switchValue by rememberSaveable { mutableStateOf(false) }
TextSwitch(
    modifier = Modifier
        .padding(horizontal = 16.dp),
    switchValue = switchValue,
    onValueChanged = {
        switchValue = it
    },
)
```

ColoredSwitch
```kotlin
var switchValue by rememberSaveable { mutableStateOf(false) }
ColoredSwitch(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    switchValue = switchValue,
    onValueChanged = {
        switchValue = it
    },
)
```

ISwitch
```kotlin
var switchValue by rememberSaveable { mutableStateOf(false) }
ISwitch(
    switchValue = switchValue,
    onValueChanged = {
        switchValue = it
    },
)
```

IconISwitch
```kotlin
var switchValue by rememberSaveable { mutableStateOf(false) }
IconISwitch(
    switchValue = switchValue,
    onValueChanged = {
        switchValue = it
    },
)
```

Also, you can customize your own switch or iswitch. Check the demo project for more information.


### License
    Copyright 2023 Muaz KADAN

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.