# VillagePlanner

## Requirements
* Device = `PIXEL 2 API 26`
* Emulator version = `31.3.13`
* Before executing the app, please set a location for the emulator in `extended controls`. When clicking that button, it should open a new window and default at "Location" session. Choose a location to represent the current location since emulator does not have a GPS to find the current location.
* Our app requires the user to be connected to Firebase for authentication, database, and file storage reasons: referring to Connecting to Firebase below

## Connecting to Firebase
* Please go to `Tools`->`Firebase` in Android Studio
  * Click `Authentication`->`Authenticate using a custom authentication system`->`Connect your app to Firebase`, a green check mark means it's all set
  * Click `Real Database`->`Get Started with Realtime Database`->`Connect your app to Firebase`, a green check mark means it's all set
  * Click `Cloud Storage for Database`->`Get Started with Cloud Storage`->`Connect your app to Firebase`, a green check mark means it's all set
* If `Existing Connection Found` is prompted, that means you are succcessfully connected to Firebase, just click `Cancel` and proceed with the App. If there are any errors, please email <a href="mailto:muyangye@usc.edu">muyangye@usc.edu</a>, as it's not possible to run the app without Firebase.