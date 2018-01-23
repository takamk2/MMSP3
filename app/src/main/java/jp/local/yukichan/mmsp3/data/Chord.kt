package jp.local.yukichan.mmsp3.data

import com.google.gson.annotations.SerializedName
import jp.local.yukichan.mmsp3.enum.ChordType
import jp.local.yukichan.mmsp3.enum.NoteType
import jp.local.yukichan.mmsp3.manager.NoteManager
import timber.log.Timber

data class Chord(
        @SerializedName("id") val id: Int,
        @SerializedName("baseNote") val baseNote: Note,
        @SerializedName("notes") val notes: Set<Note>) {

    val dispName: String
        get() {
            return NoteType.get(baseNote).dispName + ChordType.get(this).dispName
        }

    fun dispName(symbol: NoteType.Symbol): String {
        return NoteType.get(baseNote, symbol).dispName + ChordType.get(this).dispName
    }

    fun getUniqueNotes(noteManager: NoteManager): Set<Note?> {
        return notes.map { noteManager.getNote(it.noteNo, 2) }.toSet()
    }

    fun getNoteNos(): Set<Int> {
        return notes.map { it.noteNo }.toSet()
    }
}
