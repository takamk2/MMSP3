package jp.local.yukichan.mmsp3.application

import android.app.Application
import jp.local.yukichan.mmsp3.data.Sequence
import jp.local.yukichan.mmsp3.explore.Explore
import jp.local.yukichan.mmsp3.manager.ChordManager
import jp.local.yukichan.mmsp3.manager.NoteManager
import jp.local.yukichan.mmsp3.manager.ScaleManager
import jp.local.yukichan.mmsp3.manager.SoundManager
import jp.local.yukichan.mmsp3.settings.Settings
import timber.log.Timber

class MMSPApplication : Application() {

    lateinit var settings: Settings
    lateinit var noteManager: NoteManager
    lateinit var chordManager: ChordManager
    lateinit var scaleManager: ScaleManager
    lateinit var sequence: Sequence
    lateinit var explore: Explore
    private lateinit var soundManager: SoundManager

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        explore = Explore(this)

        settings = Settings(this)
        noteManager = NoteManager(this)
        chordManager = ChordManager(noteManager)
        scaleManager = ScaleManager(noteManager)
        soundManager = SoundManager(this, noteManager, chordManager, scaleManager)
        sequence = Sequence(0, explore)

        // Todo: Settings: octave
        settings.maxOctave = 3
    }
}