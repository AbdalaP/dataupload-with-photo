plugins {
    id("com.android.application")
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
    implementation(files("libs\\opencsv-2.4.jar"))
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.vanniktech:android-image-cropper:4.5.0")
    implementation ("org.apache.poi:poi:4.0.0")
    implementation ("org.apache.poi:poi-ooxml:4.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    //implementation("androidx.recyclerview:recyclerview:1.3.2")

}