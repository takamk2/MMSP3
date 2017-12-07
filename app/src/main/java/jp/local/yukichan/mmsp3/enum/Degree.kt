package jp.local.yukichan.mmsp3.enum

import com.google.gson.annotations.SerializedName

enum class Degree(@SerializedName("id") val id: Int,
                  @SerializedName("noteNo") val noteNo: Int,
                  @SerializedName("priority") val priority: Int,
                  @SerializedName("degreeName") val degreeName: String,
                  @SerializedName("tensionName") val tensionName: String,
                  @SerializedName("canUseTetnsion") val canUseTesnsion: Boolean) {

    Root(0, 0, 0, "R", "", false),
    m2(0, 1, 0, "m2", "♭9", true),
    M2(0, 2, 0, "M2", "9", true),
    m3(0, 3, 0, "m3", "♯9", false),
    M3(0, 4, 0, "M3", "", false),
    P4(0, 5, 0, "P4", "11", true),
    aug4(0, 6, 1, "aug4", "♯11", true),
    dim5(0, 6, 0, "dim5", "#11", false),
    P5(0, 7, 0, "P5", "", false),
    aug5(0, 8, 1, "aug5", "♭13", false),
    m6(0, 8, 0, "m6", "♭13", true),
    M6(0, 9, 0, "M6", "13", true),
    m7(0, 10, 0, "m7", "", false),
    M7(0, 11, 0, "M7", "", false),
    Octave(0, 12, 0, "R", "", false),
    ;

    companion object {
        fun get(noteNo: Int, priority: Int = 0): Degree? {
            return values().find { it.noteNo == noteNo && it.priority == priority }
        }
    }
}