package jp.local.yukichan.mmsp3.data

import com.google.gson.annotations.SerializedName

data class Notes(@SerializedName("id") val id: Int) {

    @SerializedName("values")
    var values: Set<Note> = setOf()

    fun plus(note: Note): Boolean {
        if (!values.contains(note)) {
            values = values.plus(note)
            return true
        }
        return false
    }

    fun minus(note: Note): Boolean {
        if (values.contains(note)) {
            values = values.minus(note)
            return true
        }
        return false
    }

    fun getNoteIds(): Set<Int> {
        return values.map { it.noteNo }.toSet()
    }
}

