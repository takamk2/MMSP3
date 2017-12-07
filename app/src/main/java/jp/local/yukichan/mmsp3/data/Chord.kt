package jp.local.yukichan.mmsp3.data

import com.google.gson.annotations.SerializedName
import jp.local.yukichan.mmsp3.enum.ChordType
import jp.local.yukichan.mmsp3.enum.NoteType
import timber.log.Timber

data class Chord(
        @SerializedName("id") val id: Int,
        @SerializedName("baseNote") val baseNote: Note,
        @SerializedName("notes") val notes: Set<Note>) {

    val dispName: String
        get() {
            Timber.d("DEBUG:: noteType=${NoteType.get(baseNote)} chordType=${ChordType.get(this)}")
            return NoteType.get(baseNote).dispName + ChordType.get(this).dispName
        }
}
