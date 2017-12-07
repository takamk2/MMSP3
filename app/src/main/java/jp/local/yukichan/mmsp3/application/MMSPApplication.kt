package jp.local.yukichan.mmsp3.application

import android.app.Application
import jp.local.yukichan.mmsp3.manager.ChordManager
import jp.local.yukichan.mmsp3.manager.NoteManager
import jp.local.yukichan.mmsp3.manager.SoundManager
import jp.local.yukichan.mmsp3.settings.Settings
import timber.log.Timber

class MMSPApplication : Application() {

    lateinit var settings: Settings
    lateinit var noteManager: NoteManager
    lateinit var chordManager: ChordManager
    private lateinit var soundManager: SoundManager

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        settings = Settings(this)
        noteManager = NoteManager(this)
        chordManager = ChordManager(noteManager)
        soundManager = SoundManager(this, noteManager, chordManager)

        // Todo: Settings: octave
        settings.maxOctave = 3
    }
}