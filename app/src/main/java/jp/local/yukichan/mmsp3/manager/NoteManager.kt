package jp.local.yukichan.mmsp3.manager

import android.content.Context
import jp.local.yukichan.mmsp3.builder.NoteBuilder
import jp.local.yukichan.mmsp3.controller.NoteController
import jp.local.yukichan.mmsp3.data.Note
import jp.local.yukichan.mmsp3.enum.NoteType
import jp.local.yukichan.mmsp3.extension.app

class NoteManager(private val context: Context) {

    var noteController: NoteController? = null

    val notes = mutableSetOf<Note>()

    companion object {
        const val DIR_PATH: String = "sounds/"
        const val FILE_EXTENSION: String = ".wav"
    }

    init {
        initialize()
    }

    fun getNote(noteNo: Int, octave: Int): Note? {
        return notes.find { it.noteNo == noteNo && it.octave == octave }
    }

    private fun initialize() {
        notes.clear()

        val builder = NoteBuilder()
        val maxOctave = context.app().settings.maxOctave
        for (octave in 0..maxOctave) {
            builder.octave = octave
            NoteType.values().filter { it.isKeyOfScale }.forEach {
                builder.id = notes.size
                builder.noteNo = it.noteNo
                notes.add(builder.build())
            }
        }
    }

    fun getFilePath(note: Note): String? {
        val noteType = NoteType.get(note)
        return DIR_PATH + noteType.name.toLowerCase() + note.octave + FILE_EXTENSION
    }
}
