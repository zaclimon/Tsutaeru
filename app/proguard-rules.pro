# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /data/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Source file/line number for easier debugging
-keepattributes SourceFile,LineNumberTable

# Picasso rules
# Checks for OkHttp versions on the classpath to determine Downloader to use.
-dontnote com.squareup.picasso.Utils
# Downloader used only when OkHttp 2.x is present on the classpath.
-dontwarn com.squareup.picasso.OkHttpDownloader
# Downloader used only when OkHttp 3.x is present on the classpath.
-dontwarn com.squareup.picasso.OkHttp3Downloader