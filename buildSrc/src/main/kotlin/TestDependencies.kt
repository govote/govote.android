object TestDependencies {
  val junit = "junit:junit:${Versions.junit}"
  val junit5 = listOf(
    "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}",
    "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
  )

  val mockito = "org.mockito:mockito-core:${Versions.mockito}"
  val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockito}"

  val hamcrestAll = "org.hamcrest:hamcrest-all:${Versions.hamcrest}"

  val requestMatcher = arrayOf(
    "br.com.concretesolutions:requestmatcher:${Versions.requestmatcher}"
    , "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
    , "com.jayway.jsonpath:json-path-assert:${Versions.jsonpathassert}"
  )

  val testButler = "com.linkedin.testbutler:test-butler-library:${Versions.testButler}"
  val testButlerApk = "com.linkedin.testbutler:test-butler-app:${Versions.testButler}@apk"

  val kotlinTest = listOf(
    "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}",
    "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
  )

  val testOrchestrator = "androidx.test:orchestrator:${Versions.orchestrator}"

  val androidTest = arrayOf(
    "androidx.test:core:${Versions.testCore}"
    , "androidx.test:runner:${Versions.testCore}"
    , "androidx.test:rules:${Versions.testCore}"
    , "androidx.test.ext:junit:${Versions.testCore}"
    , "androidx.test.ext:truth:${Versions.testCore}"
    , "com.google.truth:truth:${Versions.googleTruth}"
    , "androidx.fragment:fragment-testing:${Versions.fragmentTest}"
    , "androidx.test.espresso:espresso-core:${Versions.espresso}"
    , "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    , "androidx.test.espresso:espresso-accessibility:${Versions.espresso}"
    , "androidx.test.espresso.idling:idling-concurrent:${Versions.espresso}"
    , "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    , "androidx.test.espresso:espresso-web:${Versions.espresso}"
    , "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"
  )

  val supportTestRunner = "androidx.test:runner:${Versions.testCore}"
  val testRules = "androidx.test:rules:${Versions.testCore}"
  val testEspressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
  val testEspressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
  val testEspressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
  val testEspressoWeb = "androidx.test.espresso:espresso-web:${Versions.espresso}"
}
