object Dependencies {
  const val kotlinStandardLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
  const val ktx = "androidx.core:core-ktx:${Versions.ktx}"

  const val design = "com.google.android.material:material:${Versions.design}"
  const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.androidx}"
  const val appcompat = "androidx.appcompat:appcompat:${Versions.androidx}"
  const val fragments_ktx = "androidx.fragment:fragment-ktx:${Versions.fragments}"
  const val constraintLayout =
    "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
  const val navigationFragment =
    "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
  const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

  const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebase_core}"
  const val firebaseMessaging =
    "com.google.firebase:firebase-messaging:${Versions.firebase_messaging}"
  const val firebaseConfig =
    "com.google.firebase:firebase-config:${Versions.firebase_remote_config}"

  const val facebookLogin = "com.facebook.android:facebook-login:${Versions.facebook_sdk}"
  const val playServicesBase = "com.google.android.gms:play-services-base:${Versions.play_services}"
  const val gson = "com.google.code.gson:gson:${Versions.gson}"
  const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
  const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
  const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  const val retrofitRxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
  const val okhttp3 = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
  const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
  const val fresco = "com.facebook.fresco:fresco:${Versions.fresco}"
  const val rootBeer = "com.scottyab:rootbeer-lib:${Versions.rootbeer}"
  const val licensesDialog = "de.psdev.licensesdialog:licensesdialog:${Versions.licensesDialog}"
  const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"
  const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
  const val leakCanaryInstrumentation =
    "com.squareup.leakcanary:leakcanary-android-instrumentation:${Versions.leakCanary}"
  const val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"
  const val stethoOkHttp3 = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"
  const val traceur = "com.tspoon.traceur:traceur:${Versions.traceur}"
  const val okhttp3LogInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
  const val flipper = "com.facebook.flipper:flipper:${Versions.flipper}"
  const val soloader = "com.facebook.soloader:soloader:${Versions.soloader}"

  const val archRuntime = "androidx.lifecycle:lifecycle-runtime:${Versions.arch}"
  const val archExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.arch}"
  const val archCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.arch}"

  const val dagger = "com.google.dagger:dagger-android:${Versions.dagger}"
  const val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
  const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
  const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
}
