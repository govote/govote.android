object Dependencies {
  val kotlinStandardLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

  val design = "com.google.android.material:material:${Versions.design}"
  val recyclerView = "androidx.recyclerview:recyclerview:${Versions.androidx}"
  val appcompat = "androidx.appcompat:appcompat:${Versions.androidx}"
  val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
  val navigationFragment = "android.arch.navigation:navigation-fragment-ktx:${Versions.navigation}"
  val navigationUI = "android.arch.navigation:navigation-ui:${Versions.navigation}"

  val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebase_core}"
  val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebase_messaging}"
  val firebaseConfig = "com.google.firebase:firebase-config:${Versions.firebase_remote_config}"

  val facebookLogin = "com.facebook.android:facebook-login:${Versions.facebook_sdk}"
  val playServicesBase = "com.google.android.gms:play-services-base:${Versions.play_services}"
  val gson = "com.google.code.gson:gson:${Versions.gson}"
  val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
  val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
  val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  val retrofitRxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
  val okhttp3 = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
  val timber = "com.jakewharton.timber:timber:${Versions.timber}"
  val fresco = "com.facebook.fresco:fresco:${Versions.fresco}"
  val rootBeer = "com.scottyab:rootbeer-lib:${Versions.rootbeer}"
  val licensesDialog = "de.psdev.licensesdialog:licensesdialog:${Versions.licensesDialog}"
  val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"
  val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
  val leakCanaryNoOp = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakCanary}"
  val leakCanaryFragment = "com.squareup.leakcanary:leakcanary-support-fragment:${Versions.leakCanary}"
  val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"
  val stethoOkHttp3 = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"
  val traceur = "com.tspoon.traceur:traceur:${Versions.traceur}"
  val okhttp3LogInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
  val flipper = "com.facebook.flipper:flipper:${Versions.flipper}"
  val soloader = "com.facebook.soloader:soloader:${Versions.soloader}"

  val archExtensions = "android.arch.lifecycle:extensions:${Versions.arch}"
  val archCompiler = "android.arch.lifecycle:compiler:${Versions.arch}"

  val dagger = "com.google.dagger:dagger-android:${Versions.dagger}"
  val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
  val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
  val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
}

object TestDependencies {
  val junit = "junit:junit:${Versions.junit}"
  val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}"
  val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
  val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
  val mockito = "org.mockito:mockito-core:${Versions.mockito}"
  val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockito}"
  val hamcrestAll = "org.hamcrest:hamcrest-all:${Versions.hamcrest}"
  val requestMatcher = "br.com.concretesolutions:requestmatcher:${Versions.requestmatcher}"
  val okhttp3MockWebserver = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
  val jsonPathAssert = "com.jayway.jsonpath:json-path-assert:${Versions.jsonpathassert}"
  val testButler = "com.linkedin.testbutler:test-butler-library:${Versions.testButler}"

  val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
  val kotlinTestJUnit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"

  val supportTestRunner = "androidx.test:runner:${Versions.testCore}"
  val testOrchestrator = "androidx.test:orchestrator:${Versions.orchestrator}"
  val testRules = "androidx.test:rules:${Versions.testCore}"
  val testEspressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
  val testEspressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
  val testEspressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
  val testEspressoWeb = "androidx.test.espresso:espresso-web:${Versions.espresso}"
}