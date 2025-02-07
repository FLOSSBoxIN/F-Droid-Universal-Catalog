# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
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

-dontwarn org.xmlpull.v1.**
-dontwarn com.caverock.androidsvg.**
-keep class org.xmlpull.** { *; }

## called via reflection from fdroid-html
-keep class de.k3b.fdroid.domain.** { *; }

-dontnote MobileAds

# non android annotations can be removed from android code
-assumenosideeffects class javax.persistence.**  { *; } # jpa compatibility
-assumenosideeffects class org.springframework.**  { *; } # spring persistence
-assumenosideeffects class io.swagger.v3.oas.annotations.**  { *; } # inline documentation (OpenAPI + Swagger)

###############
# I use proguard only to remove unused stuff and to keep the app small.
# I donot want to obfuscate (rename packages, classes, methods, ...) since this is open source
-dontobfuscate
-dontoptimize
-keepnames class ** { *; }
-keepnames interface ** { *; }
-keepnames enum ** { *; }
