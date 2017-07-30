IPTV Provider Framework (IPF)
=========================

This is a simple document illustrating a possibility of doing an application framework for IPTV providers. The main reason for this decision is to create an easy, centralized solution for all IPTV providers whilst giving a customized one at the same time. 

Initial layout
------------------
As of July 2017, the initial layout of Ace TV was compromised of two main parts:

 - The app module
 - The TIF (TV Input Framework)

The app module as it name implies, is the app in itself. The TIF is a modified version from the one created fromt the Android sample found here:

[https://github.com/googlesamples/androidtv-sample-inputs](https://github.com/googlesamples/androidtv-sample-inputs)

Elements to consider
-----------------------------
In order to create a situable framework, most parts will be centralized. These will include:

 - M3U/XMLTV parsing (AceChannelUtil/AvContentUtil)
 - EPG guide synchronization with the Live Channels application (AceJobService)
 - Interaction with the Live Channels application (AceTvInputService/AcePlayer)
 - Media playback outside of the Live Channel application (ExoPlayerAdapter/AceVideoMediaPlayerGlue)

Some others, on the other hand, are to be separated:

 - Video-On-Demand features (VOD's/TV Series/TV Catchup)
 - Picture processing for ImageCardViews
 - UI/UX customization (Colors, branding, custom Activity/Fragment classes, custom xml attributes)
 - Data persistance (Database usage)


Proposition
----------------
The offer in this case would be to define a way to abstract the elements to separate in order to give enough customization based on the IPTV provider needs.

### Video-On-Demand

Since the video on demand features might vary from one provider to another, the solution is to leave the provider create a custom class extending  `MainActivityTv` and some others extending  the `VodTvSectionFragment` class so it can be added. In that case `MainActivityTv` would be responsible for handling everything using [FragmentFactory](https://developer.android.com/reference/android/support/v17/leanback/app/BrowseFragment.FragmentFactory.html).

### ImageCardView image processing

This one already had been made for Ace TV. It basically consists of creating a custom interface in which the provider is free to use it's implementation in order to render an image to an ImageCardView.

For Ace TV, the responsible class was `CardViewImageProcessor`

### UI customization
For this one, there are several possibilities. That said, it might be worthwhile to note them here:

 1. Create abstract methods to classes to return a UI value
 2. Create custom XML attributes
 3. Leave the user/provider to modify Android values

The third option is the preferred one as it leave some leverage for customization. Even though simplicity would be one of the main objectives for this framework, leaving some choice is always good.

### Data persistance

Most likely one of the most challenging pieces. In that regard it would be to create a set of interfaces which would be independant of the implementation used for data persistance. In that regard, basic interactions with the on-disk storage would need to be implemented:

 - Reading from/to Java object from/to the disk 
 - Writing from/to Java object from/to the disk
 - Searching elements from the disk

In the case of Ace TV and probably other IPTV providers as well would be to use data persistance as a cache for media content, with it being fully populated the first time a user connects or is being switched. A `Service` could be used for scheduled or live updating every time the app is launched.

#### Testing
As a proof of concept of the possible improvements, a test was made in July 2017 on an Ace TV premium membership. Using the Android TV emulator for premium Ace TV users, there was about 14000 media content available for consumption.

The TV Catchup was the section needing the most time to load with an average of 13.3 seconds to entirely load it's content. By persisting the data beforehand and updating in the background, considerable savings can be made.

Structurally speaking saving each `AvContent` would be a good choice but saving `ArrayObjectAdapter` could be considered as well.

 Note: The results of the tests are available in "PerformanceVOD.txt"