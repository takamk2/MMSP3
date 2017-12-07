package jp.local.yukichan.mmsp3.controller

import jp.local.yukichan.mmsp3.data.Chord
import jp.local.yukichan.mmsp3.manager.SoundManager

class ChordController(private val soundManager: SoundManager) {

    fun play(chord: Chord) {
        soundManager.play(chord)
    }
}
