object TestDependencies {
  const val junit = "junit:junit:${Versions.junit}"
  
  val junit5 = listOf(
    "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}",
    "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
  )

  const val mockito = "org.mockito:mockito-core:${Versions.mockito}"
  const val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockito}"

  const val hamcrestAll = "org.hamcrest:hamcrest-all:${Versions.hamcrest}"

  val requestMatcher = arrayOf(
    "br.com.concretesolutions:requestmatcher:${Versions.requestmatcher}"
    , "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
    , "com.jayway.jsonpath:json-path-assert:${Versions.jsonpathassert}"
  )

  const val testButler = "com.linkedin.testbutler:test-butler-library:${Versions.testButler}"
  const val testButlerApk = "com.linkedin.testbutler:test-butler-app:${Versions.testButler}@apk"

  val kotlinTest = listOf(
    "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}",
    "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
  )

  const val testOrchestrator = "androidx.test:orchestrator:${Versions.orchestrator}"

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

  const val testCore = "androidx.test:core:${Versions.testCore}"
  const val supportTestRunner = "androidx.test:runner:${Versions.testCore}"
  const val testRules = "androidx.test:rules:${Versions.testCore}"
  const val testEspressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
  const val testEspressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
  const val testEspressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
  const val testEspressoWeb = "androidx.test.espresso:espresso-web:${Versions.espresso}"
  const val testAssertionJUnit = "androidx.test.ext:junit:${Versions.testCore}"
  const val testAssertionGoogleTruth = "androidx.test.ext:truth:${Versions.testCore}"
  const val googleTruth = "com.google.truth:truth:${Versions.googleTruth}"
}
