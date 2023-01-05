# Serial-Decoder-Youtube
This is the sourcecode for a Youtube playlist of native android app called Serial Decoder

Playstore: https://play.google.com/store/apps/details?id=com.serial.decoder

Youtube: https://www.youtube.com/playlist?list=PL_97vI-C8EX-vzanEBv-8opNaW-HXEdKk

Figma: https://www.figma.com/file/BH7pkGmfBIkYUyV5F0mr8q/Serial-Decoder?node-id=49%3A2&t=94Jrykv7hLwY20yO-0

Currently (Finished):
- Low Fidelity vs High Fidelity Designs using Figma
- Architecture
- Localization to multi-language
- Permission Handling
- How Does QR code work (src: https://www.javatpoint.com/generating-qr-code-in-java)

User Interface (Finished):
- Bottom Sheet
- Pager layout (ViewPager in XML)
- Simple Animation
- Adaptive Layout
- Theming
- Material Design Components


Side Effects (Finished):
- DisposableEffect
- LaunchedEffect

Android Based Tech (Finished):
- Navigation Component
- SharedPreference vs Preference Data Store vs Proto Data Store
- Deep Link (communication with other apps/website)
- Google’s MLKit for barcode scanning (3rd party)
- CameraX & Camera2

Dependency Injection (Finished):
- Dagger Hilt Framework

Design Patterns (Finished):
- Factory Design Pattern
- Strategy Design Pattern



===============================================================================

Testing (Finished but not recorded on Youtube):
- Instrumentation Test (User Interface)
- Unit Testing (Local JVM)
- Testing Corountines
- Test Doubles
- Manual DI vs Dagger Hilt Framework



Testing is not included in this version yet, to view the test cases, check the other repo, which I essentially made for myself:

https://github.com/devMohaned/SerialDecoderYT

SerialDecoderYT repo is the same as this one, except for 2 aspects:
- Testing (I did not record Testing on youtube yet, so there's no testing in this repo)
- Feature Decoding 
The only difference in the feature_decoding package, where on Youtube I applied the 2 design patterns, Factory, and Strategy design patterns
to improve the scalability of the app to large scale opportunities, other than that they're all the same on terms of features


Feel free to check the source code for the testing here:
https://github.com/devMohaned/SerialDecoderYT/tree/Finished-App/app/src

====================================================================


Polishing (Work In Progress):
- Made App Icon (Finished)
- ASO (WIP)


How it looks on Phone?


https://user-images.githubusercontent.com/60556571/210603465-714736b4-153b-4bae-808c-8bd8066309ba.mp4

How it looks on Tablet?


https://user-images.githubusercontent.com/60556571/210603791-346998dc-0f6d-43ff-88aa-d5a7af5edb14.mp4


How it look on Localized Language (Another Language other than English)?


https://user-images.githubusercontent.com/60556571/210882725-9e714cb5-13dc-47eb-8304-c8f85cf5afc8.mp4



Before Open Closed Principle of SOLID:

Image:

![Decoding Utility Class Before Open Closed Principle](https://user-images.githubusercontent.com/60556571/210606719-a700d935-5baa-4181-8646-d30d2e547bda.png)

Video (Before Open Closed Principle):

https://user-images.githubusercontent.com/60556571/210607130-50b484fb-c1d4-4af8-820a-15df36cb7e62.mp4

Video (After Open Closed Principle):


https://user-images.githubusercontent.com/60556571/210607755-a9255ffc-2614-440e-9f88-1b4fca85506d.mp4



Worth Mentions:
- [Disable Camera when decomposition occur](https://stackoverflow.com/questions/74591909/how-to-stop-camera-from-working-when-it-no-longer-visible-in-the-compisition/74605151#74605151)

- [Disable Dragging PeekBar](https://stackoverflow.com/questions/74578873/how-to-allow-drag-only-on-part-of-the-sheetpeek-of-a-bottomcontent-in-bottomshee)

If you like this repo, feel free to star it, subcribe to the channel, do charity, just do anything positive :D

Thanks and good luck to all of you!
