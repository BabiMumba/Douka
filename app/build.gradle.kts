plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "cd.bmduka.com"
    compileSdk = 34

    defaultConfig {
        applicationId = "cd.bmduka.com"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.storage)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    // implementation(libs.firebase.ml.vision)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //taille ssd et ssp

    implementation ("com.intuit.sdp:sdp-android:1.0.6")//cette bibliothèque permet de gérer les tailles des éléments de l'interface utilisateur
    implementation ("com.intuit.ssp:ssp-android:1.0.6")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.tbuonomo:dotsindicator:5.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("androidx.transition:transition:1.4.1")
    implementation ("com.google.maps:google-maps-services:0.1.3")

}