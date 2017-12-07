package jp.local.yukichan.mmsp3.data

import com.google.gson.annotations.SerializedName

data class RootNote(
        @SerializedName("noteNo") val noteNo: Int,
        @SerializedName("octave") val octave: Int = 2,
        @SerializedName("baseOctave") val baseOctave: Int = 0)
