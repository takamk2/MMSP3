package jp.local.yukichan.mmsp3.manager

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import jp.local.yukichan.mmsp3.controller.ChordController
import jp.local.yukichan.mmsp3.controller.NoteController
import jp.local.yukichan.mmsp3.data.Chord
import jp.local.yukichan.mmsp3.data.Note
import timber.log.Timber

class SoundManager(private val context: Context,
                   private val noteManager: NoteManager,
                   private val chordManager: ChordManager) {

    private var soundPool: SoundPool? = null

    private val soundMap = mutableMapOf<Note, Int>()

    init {
        initialize()
    }

    private fun initialize() {
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        soundPool = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(16)
                .build()
        if (soundPool == null) {
            throw IllegalStateException("soundPool")
        }

        soundPool!!.setOnLoadCompleteListener { soundPool, sampleId, status ->
            Timber.d("soundPool=$soundPool sampleId=$sampleId status=$status")
        }

        noteManager.notes.sortedBy { it.id }.forEach {
            val descriptor = context.assets.openFd(noteManager.getFilePath(it))
            soundMap[it] = soundPool!!.load(descriptor, 1)
        }

        noteManager.noteController = NoteController(this)
        chordManager.chordController = ChordController(this)
    }

    fun play(note: Note, leftVolume: Float = 1f, rightVolume: Float = 1f) {
        if (!soundMap.contains(note)) {
            Timber.e("There is no soundId")
            return
        }
        soundPool!!.play(soundMap[note]!!, leftVolume, rightVolume, 1, 0, 0f)
    }

    fun play(chord: Chord,
             baseLeftVolume: Float = 1f,
             baseRightVolume: Float = 1f,
             chordLeftVolume: Float = 0.8f,
             chordRightVolume: Float = 0.8f) {
        chord.notes.sortedBy { it.id }.forEachIndexed { index, note ->
            if (index == 0) {
                play(note, baseLeftVolume, baseRightVolume)
            } else {
                play(note, chordLeftVolume, chordRightVolume)
            }
        }
    }
}
