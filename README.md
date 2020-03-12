# Achilles

A Simple Retrofit, Scarlet Inspired Android Websocket Client

[![](https://jitpack.io/v/ibrahimsn98/achilles.svg)](https://jitpack.io/#ibrahimsn98/achilles)
[![API](https://img.shields.io/badge/API-22%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=22)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Achilles-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/8054)

## Motivation
I have created this library to communicate the apps with their own backends. All receiver and send methods require to specify event name, so both client and server applications distinguish the right payload.


## Payload
-   Data sent
```
{"event": "echo", "data": {"name": "Mark", "surname": "Bond"}}
```
-   Data received
```
{"event": "echo", "data": {"name": "Mark", "surname": "Bond"}}
```


## Usage
-   Create service interface
```kotlin
interface SocketService {

    @SendEvent("echo")
    fun sendEcho(@Field("name") name: String,
                 @Field("surname") surname: String)

    @ReceiveEvent("echo")
    fun receiveEcho(): Observable<Response>
}
```

-   Use Achilles to create an implementation:
```kotlin
    val achilles = Achilles.Builder()
        .baseUrl("wss://echo.websocket.org")
        .client(OkHttpClient().newBuilder().build())
        .build()

    val service = achilles.create(SocketService::class.java)
```

-   Send and observe socket event data
```kotlin
    disposable.add(service.receiveEcho()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            // TODO
        }, {
            // Handle error
        }))

    service.sendEcho("Mark", "Bond")
```


## Setup
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
    implementation 'com.github.ibrahimsn98:achilles:1.0'
}
```


## TODO
- [ ] Lifecycle aware connection
- [ ] Reconnect on disconnection
- [ ] Socket status events
- [ ] Payload encryption


## License
```
MIT License

Copyright (c) 2019 İbrahim Süren

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
SOFTWARE.
```