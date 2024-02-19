plugins {
    id("com.android.application")

}
repositories {
    google()
    mavenCentral()
    @Suppress("DEPRECATION")
    jcenter()
    maven { "https://jitpack.io" }
}


android {
    namespace = "com.example.sch_agro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sch_agro"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }


    buildFeatures{
        viewBinding = true
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.CanHub:Android-Image-Cropper:2.8.+")
   // implementation ("com.softhearted.edmodo:android-image-cropper:2.8.0")

    //crop image
    //api("com.theartofdev.edmodo:android-image-cropper:2.8.0")

    //loading image in image view
   // implementation("com.square.picasso:picasso:2.8.0")

}