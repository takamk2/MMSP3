package jp.local.yukichan.mmsp3.extension

import android.content.Context
import jp.local.yukichan.mmsp3.application.MMSPApplication

fun Context.app(): MMSPApplication {
    return applicationContext as MMSPApplication
}

