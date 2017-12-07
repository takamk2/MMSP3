package jp.local.yukichan.mmsp3.settings

import android.content.Context
import android.preference.PreferenceManager

class Settings(context: Context) {

    companion object {
        const val PREF_KEY_MAX_OCTAVE = "max_octave"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    var maxOctave
        get() = prefs.getInt(PREF_KEY_MAX_OCTAVE, 0)
        set(value) {
            prefs.edit().putInt(PREF_KEY_MAX_OCTAVE, value).apply()
        }
}