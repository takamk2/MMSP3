package jp.local.yukichan.mmsp3.manager

import jp.local.yukichan.mmsp3.builder.ScaleBuilder
import jp.local.yukichan.mmsp3.controller.ScaleController
import jp.local.yukichan.mmsp3.data.Note
import jp.local.yukichan.mmsp3.data.RootNote
import jp.local.yukichan.mmsp3.data.Scale
import jp.local.yukichan.mmsp3.enum.NoteType
import jp.local.yukichan.mmsp3.enum.ScaleType

class ScaleManager(private val noteManager: NoteManager) {

    var scaleController: ScaleController? = null

    val scales = mutableSetOf<Scale>()

    init {
        initialize()
    }

    // TODO: 必要になったらコメントを外す
//    fun getScale(baseNote: Note, notes: Set<Note>): ChangeScale? {
//        return scales.find { it.baseNote == baseNote && it.notes == notes }
//    }

    private fun initialize() {
        scales.clear()

        val builder = ScaleBuilder(noteManager)
        NoteType.values().filter { it.isKeyOfScale }.forEach { noteType ->
            ScaleType.values().filter { it == ScaleType.MajorScale }.forEach { scaleType ->
                builder.id = scales.size
                builder.rootNote = RootNote(noteType.noteNo)
                builder.scaleType = scaleType
                builder.symbol = noteType.symbol
                scales.add(builder.build())
            }
        }
    }
}

