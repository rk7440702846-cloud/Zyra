package com.example.ui.components

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.R

@DrawableRes
fun getDrawableResByName(context: Context, name: String): Int {
    if (name.isBlank()) return R.drawable.ic_launcher_foreground
    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
    return if (id != 0) id else R.drawable.ic_launcher_foreground
}

@Composable
fun rememberDrawableRes(name: String): Int {
    val context = LocalContext.current
    return getDrawableResByName(context, name)
}
