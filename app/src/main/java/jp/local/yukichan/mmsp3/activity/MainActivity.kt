package jp.local.yukichan.mmsp3.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import jp.local.yukichan.mmsp3.R
import jp.local.yukichan.mmsp3.data.Chord
import jp.local.yukichan.mmsp3.data.Note
import jp.local.yukichan.mmsp3.data.Scale
import jp.local.yukichan.mmsp3.event.SequenceItemEvent
import jp.local.yukichan.mmsp3.extension.app
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var lvNoteAdapter: MainActivity.LvNoteAdapter
    private lateinit var lvChordAdapter: MainActivity.LvChordAdapter
    private lateinit var lvScaleAdapter: MainActivity.LvScaleAdapter
//    private lateinit var lvSelectedAdapter: MainActivity.LvSelectedNoteAdapter

    private val compositDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        setContentView(R.layout.activity_main)

        val noteList = app().noteManager.notes.toList()
        lvNoteAdapter = LvNoteAdapter(this)
        lvNoteAdapter.setNoteList(noteList)
        lvNote.adapter = lvNoteAdapter

        val chordList = app().chordManager.chords.toList()
        lvChordAdapter = LvChordAdapter(this)
        lvChordAdapter.setChordList(chordList)
        lvChord.adapter = lvChordAdapter

        val scaleList = app().scaleManager.scales.toList()
        lvScaleAdapter = LvScaleAdapter(this)
        lvScaleAdapter.setScaleList(scaleList)
        lvScale.adapter = lvScaleAdapter

        updateSelectedNote(app().sequence.currentSequenceItem.getUniqueNoteList(app().noteManager))

//        lvSelectedAdapter = LvSelectedNoteAdapter(this)
//        lvSelectedAdapter.setNoteList(
//                app().sequence.currentSequenceItem.getUniqueNoteList(app().noteManager))
//        lvSelectedNote.adapter = lvSelectedAdapter

        // ---------
    }

    override fun onStart() {
        super.onStart()
        compositDisposable.add(app().sequence.currentSequenceItem.changes().subscribe {
            when(it.type) {
                SequenceItemEvent.Type.AddNote,
                SequenceItemEvent.Type.RemoveNote -> {
                    updateSelectedNote(app().sequence.currentSequenceItem.getUniqueNoteList(app().noteManager))
//                    lvSelectedAdapter.setNoteList(
//                            app().sequence.currentSequenceItem.getUniqueNoteList(app().noteManager))
//                    lvSelectedAdapter.notifyDataSetChanged()
                }
                SequenceItemEvent.Type.ChangeScale -> {
                    if (it.sequenceItem != null && it.sequenceItem!!.scale != null) {
                        tvSelectedScale.text = it.sequenceItem!!.scale!!.dispName
                        tvSelectedChord.text = null
                        // TODO: 絞り込みを行うクラス(
                    }
                }
                SequenceItemEvent.Type.ChangeChord -> {
                    if (it.sequenceItem != null && it.sequenceItem!!.chord != null) {
                        tvSelectedChord.text = it.sequenceItem!!.chord!!.dispName
                    }
                }
            }
        })

        app().explore.changes().subscribe {
            lvScaleAdapter.setScaleList(app().explore.getCandidateScales())
            lvScaleAdapter.notifyDataSetChanged()
        }

        app().explore.changes().subscribe {
            lvChordAdapter.setChordList(app().explore.getCandidateChords())
            lvChordAdapter.notifyDataSetChanged()
        }
    }

    override fun onStop() {
        compositDisposable.clear()
        super.onStop()
    }

    private fun updateSelectedNote(uniqueNoteList: List<Note>) {
        lvSelectedNote.removeAllViews()
        uniqueNoteList.forEach {
            val view = View.inflate(this, R.layout.item_lv_selected_note, null)
            val tvName = view.findViewById<TextView>(R.id.tvName)
            tvName.text = it.dispName
            lvSelectedNote.addView(view)
        }
    }

    // ---------------------------------------------------------------------------------------------
    private class LvNoteAdapter(val context: Context) : BaseAdapter() {

        val noteList = mutableListOf<Note>()

        private val noteManager = context.app().noteManager
        private val sequence = context.app().sequence

        private var inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.item_lv_note, parent, false)
            val tvName = view.findViewById<TextView>(R.id.tvName)
            val tvOctave = view.findViewById<TextView>(R.id.tvOctave)

            val note = getItem(position)
            tvName.text = note.dispName
            tvOctave.text = note.octave.toString()

            updateColor(view!!, sequence.currentSequenceItem.containsNote(note))

            RxView.clicks(view).subscribe {
                noteManager.noteController?.play(note)
            }
            RxView.longClicks(view).subscribe {
                val sequenceItem = sequence.currentSequenceItem
                if (sequenceItem.containsNote(note)) {
                    sequenceItem.removeNote(note)
                    Toast.makeText(context, "remove note=$note", Toast.LENGTH_SHORT).show()
                    updateColor(view!!, false)
                } else {
                    sequenceItem.addNote(note)
                    Toast.makeText(context, "add note=$note", Toast.LENGTH_SHORT).show()
                    updateColor(view!!, true)
                }
            }
            return view
        }

        private fun updateColor(view: View, isSelected: Boolean) {
            val color = if (isSelected) {
                context.resources.getColor(R.color.itemSelected, context.theme)
            } else {
                context.resources.getColor(R.color.itemNormal, context.theme)
            }
            view.setBackgroundColor(color)

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

        private val chordManager = context.app().chordManager
        private val sequence = context.app().sequence

        private var inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.item_lv_chord, parent, false)
            val tvName = view.findViewById<TextView>(R.id.tvName)

            val chord = getItem(position)
            if (sequence.currentSequenceItem.scale == null) {
                tvName.text = chord.dispName
            } else {
                tvName.text = chord.dispName(sequence.currentSequenceItem.scale!!.symbol)
            }

            RxView.clicks(view).subscribe {
                chordManager.chordController?.play(chord)
            }
            RxView.longClicks(view).subscribe {
                sequence.currentSequenceItem.chord = chord
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

    // ---------------------------------------------------------------------------------------------
    private class LvScaleAdapter(val context: Context) : BaseAdapter() {

        val scaleList = mutableListOf<Scale>()

        private val scaleManager = context.app().scaleManager
        private val sequence = context.app().sequence

        private var inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.item_lv_scale, parent, false)
            val tvName = view.findViewById<TextView>(R.id.tvName)

            val scale = getItem(position)
            tvName.text = scale.dispName

            RxView.clicks(view).subscribe {
                scaleManager.scaleController?.play(scale)
            }
            RxView.longClicks(view).subscribe {
                sequence.currentSequenceItem.scale = scale
            }
            return view
        }

        override fun getItem(position: Int): Scale {
            return scaleList[position]
        }

        override fun getItemId(id: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return scaleList.size
        }

        fun setScaleList(data: List<Scale>) {
            scaleList.clear()
            scaleList.addAll(data)
        }
    }

    // ---------------------------------------------------------------------------------------------
//    private class LvSelectedNoteAdapter(val context: Context) : BaseAdapter() {
//
//        val noteList = mutableListOf<Note>()
//
//        private val sequence = context.app().sequence
//
//        private var inflater: LayoutInflater =
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            val view = convertView ?: inflater.inflate(R.layout.item_lv_selected_note, parent, false)
//            val tvName = view.findViewById<TextView>(R.id.tvName)
//
//            val note = getItem(position)
//            tvName.text = note.dispName
//
//            return view
//        }
//
//        private fun updateColor(view: View, isSelected: Boolean) {
//            val color = if (isSelected) {
//                context.resources.getColor(R.color.itemSelected, context.theme)
//            } else {
//                context.resources.getColor(R.color.itemNormal, context.theme)
//            }
//            view.setBackgroundColor(color)
//
//        }
//
//        override fun getItem(position: Int): Note {
//            return noteList[position]
//        }
//
//        override fun getItemId(id: Int): Long {
//            return 0
//        }
//
//        override fun getCount(): Int {
//            return noteList.size
//        }
//
//        fun setNoteList(data: List<Note>) {
//            noteList.clear()
//            noteList.addAll(data)
//        }
//    }
}
