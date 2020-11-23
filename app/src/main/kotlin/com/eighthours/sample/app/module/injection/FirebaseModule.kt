package com.eighthours.sample.app.module.injection

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module
import java.io.ByteArrayInputStream

val FirebaseModule = module {
    single { initFirebase() }
    single { FirebaseAuth.getInstance(get()) }
}

private fun initFirebase(): FirebaseApp {
    val credentials = if (System.getenv("GOOGLE_APPLICATION_CREDENTIALS") != null) {
        GoogleCredentials.getApplicationDefault()
    } else {
        System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON")?.let {
            GoogleCredentials.fromStream(ByteArrayInputStream(it.toByteArray()))
        } ?: error("GOOGLE_APPLICATION_CREDENTIALS not set.")
    }

    val databaseUrl = System.getenv("FIREBASE_DATABASE_URL")
        ?: error("FIREBASE_DATABASE_URL not set.")

    val options = FirebaseOptions.Builder()
        .setCredentials(credentials)
        .setDatabaseUrl(databaseUrl)
        .build()

    return FirebaseApp.initializeApp(options)
}
