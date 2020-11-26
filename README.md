# COVIDTracker
[[Leer en Español]](https://github.com/ljcamargo/covidtracker/blob/main/README_es.md)
This is an open-source white-label android app to check in to a venue using a QR code and cellphone number for COVID contact tracing and to allow authorities to contact visitors in case of a possible exposure.

The aim of this white-label app is to be themed, customized and implemented by governments or localities to speed up the website checkin process and to avoid long wait lines to enter places and to diminish COVID spread.

The process of the app is inspired on the Attendance Record of the Government of Mexico City (CDMX) and was developed independently and unofficially.

## Warning
This app was developed independently and without relation to any government or institution.

For demonstration purposes, the app implements an unofficial connection to Mexico City Attende Record by simulating the manual process on the website and it may change and stop working any time. It's not meant to be used as an official substitute for official mechanisms. Use for testing purposes and under your responsibility.

## How it works
* Open the app
* Scan the venue QR at the entrance
* Receive the confirmation and show it to the clerk (when required)

## Features
* Assisted QR scanning (zxing)
* Store your cell phone number just once to avoid dialing each time
* Manual code alternative if the QR code is illegible or the camera is not available
* Regex check for correct phone number, manual code and QR code.
* List of recent attendances
* Multilingual support

## Steps to implement your own app
1. Fork the project
2. Change hex color values
3. Change strings and translations
4. Change icons and other image assets
5. Change regex patterns to valide phone numbers and codes according to your locale or specification
6. Implement your repository and datasource to send qr scan codes.
7. Change privacy, terms and about template HTML files, you may also point to external website
8. Change app package name
9. Build and Deploy

## Tech Specs
* Kotlin 1.4+
* Coroutines
* Ktor
* Koin (DI)
* Material Design
* MVVC

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/ljcamargo/covidtracker/issues).

## License
Licensed under the Apache License, Version 2.0 (the "License");

you may not use this file except in compliance with the License.

You may obtain a copy of the License at

  

http://www.apache.org/licenses/LICENSE-2.0

  

Unless required by applicable law or agreed to in writing, software

distributed under the License is distributed on an "AS IS" BASIS,

WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and

limitations under the License.
