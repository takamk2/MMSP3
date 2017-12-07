package jp.local.yukichan.mmsp3.controller

import jp.local.yukichan.mmsp3.data.Note
import jp.local.yukichan.mmsp3.manager.SoundManager

class NoteController(private val soundManager: SoundManager) {

    fun play(note: Note) {
        soundManager.play(note)
    }
}
