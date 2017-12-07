package jp.local.yukichan.mmsp3.manager

import jp.local.yukichan.mmsp3.builder.ChordBuilder
import jp.local.yukichan.mmsp3.controller.ChordController
import jp.local.yukichan.mmsp3.data.Chord
import jp.local.yukichan.mmsp3.data.RootNote
import jp.local.yukichan.mmsp3.enum.ChordType
import jp.local.yukichan.mmsp3.enum.NoteType

class ChordManager(private val noteManager: NoteManager) {

    var chordController: ChordController? = null

    val chords = mutableSetOf<Chord>()

    init {
        initialize()
    }

    // TODO: 必要になったらコメントを外す
//    fun getChord(baseNote: Note, notes: Set<Note>): Chord? {
//        return chords.find { it.baseNote == baseNote && it.notes == notes }
//    }

    private fun initialize() {
        chords.clear()

        val builder = ChordBuilder(noteManager)
        NoteType.values().forEach { noteType ->
            ChordType.values().forEach { chordType ->
                builder.id = chords.size
                builder.rootNote = RootNote(noteType.noteNo)
                builder.chordType = chordType
                chords.add(builder.build())
            }
        }
    }
}

