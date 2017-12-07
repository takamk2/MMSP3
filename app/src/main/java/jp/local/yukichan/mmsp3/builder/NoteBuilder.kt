package jp.local.yukichan.mmsp3.builder

import jp.local.yukichan.mmsp3.data.Note

class NoteBuilder(var id: Int? = null,
                  var noteNo: Int? = null,
                  var octave: Int? = null) {

    fun build(): Note {
        return Note(id!!, noteNo!!, octave!!)
    }
}
