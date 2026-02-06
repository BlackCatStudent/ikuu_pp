package com.ikuuvpn.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IkuuVpnApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}