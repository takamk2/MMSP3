package jp.local.yukichan.mmsp3.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import jp.local.yukichan.mmsp3.R
import jp.local.yukichan.mmsp3.data.Chord
import jp.local.yukichan.mmsp3.data.Note
import jp.local.yukichan.mmsp3.extension.app
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var lvNoteAdapter: MainActivity.LvNoteAdapter
    private lateinit var lvChordAdapter: MainActivity.LvChordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        setContentView(R.layout.activity_main)

        val noteList = app().noteManager.notes.toList()
        lvNoteAdapter = LvNoteAdapter(this)
        lvNoteAdapter.setNoteList(noteList)
        lvNote.adapter = lvNoteAdapter

        val chordList = app().chordManager.chords.toList()
        Timber.d("DEBUG:: chordList=$chordList")
        lvChordAdapter = LvChordAdapter(this)
        lvChordAdapter.setChordList(chordList)
        lvChord.adapter = lvChordAdapter
    }

    // ---------------------------------------------------------------------------------------------
    private class LvNoteAdapter(context: Context) : BaseAdapter() {

        val noteList = mutableListOf<Note>()

        private var noteManager = context.app().noteManager

        private var inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.item_lv_note, parent, false)
            val tvName = view.findViewById<TextView>(R.id.tvName)
            val tvOctave = view.findViewById<TextView>(R.id.tvOctave)

            val note = getItem(position)
            tvName.text = note.dispName
            tvOctave.text = note.octave.toString()

            RxView.clicks(view).subscribe {
                noteManager.noteController?.play(note)
            }
            return view
        }

        override fun getItem(position: Int): Note {
            return noteList[position]
        }

        override fun getItemId(id: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return noteList.size
        }

        fun setNoteList(data: List<Note>) {
            noteList.clear()
            noteList.addAll(data)
        }
    }

    // ---------------------------------------------------------------------------------------------
    private class LvChordAdapter(context: Context) : BaseAdapter() {

        val chordList = mutableListOf<Chord>()

        private var chordManager = context.app().chordManager

        private var inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.item_lv_chord, parent, false)
            val tvName = view.findViewById<TextView>(R.id.tvName)

            val chord = getItem(position)
            tvName.text = chord.dispName

            RxView.clicks(view).subscribe {
                chordManager.chordController?.play(chord)
            }
            return view
        }

        override fun getItem(position: Int): Chord {
            return chordList[position]
        }

        override fun getItemId(id: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return chordList.size
        }

        fun setChordList(data: List<Chord>) {
            chordList.clear()
            chordList.addAll(data)
        }
    }
}
