package ru.geekbrains.imageconverter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class MainActivityContract : ActivityResultContract<Unit, Intent>() {
    override fun createIntent(context: Context, input: Unit?): Intent {
        val intent = Intent().apply {
            type = context.getString(R.string.type_image)
            action = Intent.ACTION_OPEN_DOCUMENT
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        val title = context.getString(R.string.select_image)
        return Intent.createChooser(intent, title)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> intent
    }
}