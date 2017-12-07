package jp.local.yukichan.mmsp3.builder

import jp.local.yukichan.mmsp3.data.Chord
import jp.local.yukichan.mmsp3.data.Note
import jp.local.yukichan.mmsp3.data.RootNote
import jp.local.yukichan.mmsp3.enum.ChordType
import jp.local.yukichan.mmsp3.enum.Degree
import jp.local.yukichan.mmsp3.manager.NoteManager

class ChordBuilder(private val noteManager: NoteManager,
                   var id: Int? = null,
                   var rootNote: RootNote? = null,
                   var chordType: ChordType? = null) {

    fun build(): Chord {
        val baseNote = noteManager.getNote(rootNote!!.noteNo, rootNote!!.baseOctave)
        val notes = mutableSetOf<Note>()
        notes.add(noteManager.getNote(rootNote!!.noteNo, rootNote!!.baseOctave)!!)
        chordType!!.constitution.forEach {
            val noteNo =
                    (rootNote!!.noteNo + it.noteNo + Degree.Octave.noteNo) % Degree.Octave.noteNo
            notes.add(noteManager.getNote(noteNo, rootNote!!.octave)!!)
        }
        return Chord(id!!, baseNote!!, notes)
    }
}

