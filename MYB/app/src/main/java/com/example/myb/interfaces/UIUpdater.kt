package com.example.myb.interfaces

import android.content.Context

interface UIUpdater {
    fun runOnUIThread(action: () -> Unit)
    fun getContext(): Context
}
