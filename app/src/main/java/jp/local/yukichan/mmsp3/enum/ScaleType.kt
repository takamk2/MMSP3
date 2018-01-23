package jp.local.yukichan.mmsp3.enum

import com.google.gson.annotations.SerializedName
import jp.local.yukichan.mmsp3.data.Scale

enum class ScaleType(
        @SerializedName("id") val id: Int,
        @SerializedName("dispName") val dispName: String,
        @SerializedName("constitution") val constitution: Set<Degree>) {

    MajorScale(0, "Major scale",
            setOf(Degree.Root, Degree.M2, Degree.M3, Degree.P4, Degree.P5, Degree.M6, Degree.M7)),
    MinorScale(1, "Minor scale",
            setOf(Degree.Root, Degree.M2, Degree.m3, Degree.P4, Degree.P5, Degree.m6, Degree.m7)),
    ;

    companion object {
        fun get(scale: Scale): ScaleType {
            val degrees = mutableSetOf<Degree>()
            val baseNoteNo = scale.baseNote.noteNo
            val scaleList = scale.notes.toList()
            val advance = scaleList[0].noteNo > scaleList[1].noteNo
            scaleList.forEach {
                if (advance) {
                    var degree = Degree.get(Degree.Octave.noteNo - baseNoteNo + it.noteNo)!!
                    if (degree == Degree.Octave) {
                        degree = Degree.Root
                    }
                    degrees.add(degree)
                } else {
                    val noteNo = (it.noteNo - baseNoteNo + Degree.Octave.noteNo) % Degree.Octave.noteNo
                    degrees.add(Degree.get(noteNo)!!)
                }
            }
            return values().find { it.constitution == degrees }!!
        }
    }
}

