package jp.local.yukichan.mmsp3.data

import com.google.gson.annotations.SerializedName
import jp.local.yukichan.mmsp3.enum.NoteType
import jp.local.yukichan.mmsp3.enum.ScaleType
import jp.local.yukichan.mmsp3.manager.NoteManager
import timber.log.Timber

data class Scale(
        @SerializedName("id") val id: Int,
        @SerializedName("baseNote") val baseNote: Note,
        @SerializedName("notes") val notes: Set<Note>,
        @SerializedName("symbol") val symbol: NoteType.Symbol) {

    val dispName: String
        get() {
            return NoteType.get(baseNote, symbol).dispName + " " + ScaleType.get(this).dispName
        }

    fun getUniqueNotes(noteManager: NoteManager): Set<Note?> {
        return notes.map { noteManager.getNote(it.noteNo, 2) }.toSet()
    }

    fun getNoteIds(): Set<Int> {
        return notes.map { it.noteNo }.toSet()
    }
}

