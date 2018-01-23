package jp.local.yukichan.mmsp3.builder

import jp.local.yukichan.mmsp3.data.Note
import jp.local.yukichan.mmsp3.data.RootNote
import jp.local.yukichan.mmsp3.data.Scale
import jp.local.yukichan.mmsp3.enum.Degree
import jp.local.yukichan.mmsp3.enum.NoteType
import jp.local.yukichan.mmsp3.enum.ScaleType
import jp.local.yukichan.mmsp3.manager.NoteManager
import timber.log.Timber

class ScaleBuilder(private val noteManager: NoteManager,
                   var id: Int? = null,
                   var rootNote: RootNote? = null,
                   var scaleType: ScaleType? = null,
                   var symbol: NoteType.Symbol? = null) {

    fun build(): Scale {
        val baseNote = noteManager.getNote(rootNote!!.noteNo, rootNote!!.baseOctave)
        val notes = mutableSetOf<Note>()
        scaleType!!.constitution.forEach {
            var noteNo = rootNote!!.noteNo + it.noteNo
            var octave = rootNote!!.octave
            if (noteNo >= Degree.Octave.noteNo) {
                noteNo -= Degree.Octave.noteNo
                octave++
            }
            notes.add(noteManager.getNote(noteNo, octave)!!)
        }
        val scale = Scale(id!!, baseNote!!, notes, symbol!!)
        return scale
    }
}

