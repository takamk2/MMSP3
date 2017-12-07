package jp.local.yukichan.mmsp3.enum

import com.google.gson.annotations.SerializedName
import jp.local.yukichan.mmsp3.data.Chord

import jp.local.yukichan.mmsp3.enum.Degree.*

enum class ChordType(
        @SerializedName("id") val id: Int,
        @SerializedName("dispName") val dispName: String,
        @SerializedName("constitution") val constitution: Set<Degree>,
        @SerializedName("tensions") val tensions: Set<Degree>) {

    Major(0, "", setOf(Root, M3, P5), setOf(M2, aug4)),
    MajorSixth(1, "M6", setOf(Root, M3, P5, M6), setOf(M2, aug4)),
    MajorSeventh(2, "M7", setOf(Root, M3, P5, M7), setOf(M2, aug4)),
    Seventh(3, "7", setOf(Root, M3, P5, m7), setOf(m2, M2, m3, aug4, m6, M6)),
    MajorFlatFive(4, "-5", setOf(Root, M3, dim5), setOf()),
    MajorSeventhFlatFive(5, "M7-5", setOf(Root, M3, dim5, M7), setOf()),
    Minor(6, "m", setOf(Root, m3, P5), setOf(M2, P4)),
    MinorMajorSixth(7, "mM6", setOf(Root, m3, P5, M6), setOf(M2, P4)),
    MinorMajorSeventh(8, "mM7", setOf(Root, m3, P5, M7), setOf(M2, P4)),
    MinorSeventh(9, "m7", setOf(Root, m3, P5, m7), setOf(M2, P4, M6)),
    MinorSeventhFlatFive(10, "m7-5", setOf(Root, m3, dim5, m7), setOf(M2, P4, m6)),
    Diminish(11, "dim", setOf(Root, m3, dim5), setOf(M2, P4, m6)),
    DiminishSeventh(12, "dim7", setOf(Root, m3, dim5, M6), setOf()),
    ;

    companion object {
        fun get(chord: Chord): ChordType {
            val degrees = mutableSetOf<Degree>()
            val baseNoteNo = chord.baseNote.noteNo
            val chordList = chord.notes.toList()
            val advance = chordList[0].noteNo > chordList[1].noteNo
            chordList.forEach {
                if (advance) {
                    var degree = Degree.get(Degree.Octave.noteNo - baseNoteNo + it.noteNo)!!
                    if (degree == Octave) {
                        degree = Root
                    }
                    degrees.add(degree)
                } else {
                    val noteNo = (it.noteNo - baseNoteNo + Octave.noteNo) % Octave.noteNo
                    degrees.add(Degree.get(noteNo)!!)
                }
            }
            return values().find { it.constitution == degrees }!!
        }
    }
}
