package jp.local.yukichan.mmsp3.data

import com.google.gson.annotations.SerializedName
import jp.local.yukichan.mmsp3.enum.NoteType

data class Note(
        @SerializedName("id") val id: Int,
        @SerializedName("noteNo") val noteNo: Int,
        @SerializedName("octave") val octave: Int = 2) {

    val dispName: String
        get() {
            return NoteType.get(this).dispName
        }
}
