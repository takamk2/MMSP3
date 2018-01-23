package jp.local.yukichan.mmsp3.controller

import jp.local.yukichan.mmsp3.data.Chord
import jp.local.yukichan.mmsp3.data.Scale
import jp.local.yukichan.mmsp3.manager.SoundManager

class ScaleController(private val soundManager: SoundManager) {

    fun play(scale: Scale) {
        soundManager.play(scale)
    }
}
