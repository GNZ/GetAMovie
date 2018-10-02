package com.gnz.getamovie.application.extensions


import android.app.Activity
import android.content.Context
import android.content.Intent


inline fun <reified T : Activity> Context.startActivity() {
    val intent = newIntent<T>(this)
    startActivity(intent)
}

inline fun <reified T : Any> Context.startActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent)
}

inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)