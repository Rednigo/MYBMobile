package com.example.myb.interfaces

import android.content.Context

interface UIUpdater {
    fun runOnUIThread(action: () -> Unit)
    fun getContext(): Context
    abstract fun Gson(): Any
    //abstract fun parseJsonToIncomes(response: String): List<Income>
}
