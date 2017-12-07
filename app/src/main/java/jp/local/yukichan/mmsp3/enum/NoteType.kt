package jp.local.yukichan.mmsp3.enum

import com.google.gson.annotations.SerializedName
import jp.local.yukichan.mmsp3.data.Note

enum class NoteType(@SerializedName("id") val id: Int,
                    @SerializedName("noteNo") val noteNo: Int,
                    @SerializedName("dispName") val dispName: String,
                    @SerializedName("symbol") val symbol: Symbol,
                    @SerializedName("isKeyOfScale") val isKeyOfScale: Boolean,
                    @SerializedName("relationId") val relationId: Int? = null) {

    C(0, 0, "C", Symbol.Natural, true),
    Cs(1, 1, "C♯", Symbol.Shape, false, 2),
    Df(2, 1, "D♭", Symbol.Flat, true, 1),
    D(3, 2, "D", Symbol.Natural, true),
    Ds(4, 3, "D♯", Symbol.Shape, false, 5),
    Ef(5, 3, "E♭", Symbol.Flat, true, 4),
    E(6, 4, "E", Symbol.Natural, true),
    F(7, 5, "F", Symbol.Natural, true),
    Fs(8, 6, "F♯", Symbol.Shape, true, 9),
    Gf(9, 6, "G♭", Symbol.Flat, false, 8),
    G(10, 7, "G", Symbol.Natural, true),
    Gs(11, 8, "G♯", Symbol.Shape, false, 12),
    Af(12, 8, "A♭", Symbol.Flat, true, 11),
    A(13, 9, "A", Symbol.Natural, true),
    As(14, 10, "A♯", Symbol.Shape, false, 15),
    Bf(15, 10, "B♭", Symbol.Flat, true, 14),
    B(16, 11, "B", Symbol.Natural, true),
    ;

    enum class Symbol {
        Natural,
        Shape,
        Flat,
        ;
    }

    companion object {
        fun get(note: Note): NoteType {
            return values().find { it.noteNo == note.noteNo && it.symbol != Symbol.Flat }!!
        }
    }
}
