package jp.local.yukichan.mmsp3.explore

import android.content.Context
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import jp.local.yukichan.mmsp3.data.Chord
import jp.local.yukichan.mmsp3.data.Note
import jp.local.yukichan.mmsp3.data.Scale
import jp.local.yukichan.mmsp3.data.SequenceItem
import jp.local.yukichan.mmsp3.enum.ChordType
import jp.local.yukichan.mmsp3.enum.Degree
import jp.local.yukichan.mmsp3.event.SequenceItemEvent
import jp.local.yukichan.mmsp3.extension.app
import timber.log.Timber

/**
 * Created by takamk2 on 18/01/22.
 *
 * The Edit Fragment of Base Class.
 */
class Explore(val context: Context) {

    private var sequenceItem: SequenceItem? = null

    private var candidateScales: MutableList<Scale> = mutableListOf()

    private var candidateChords: MutableList<Chord> = mutableListOf()

    private val compositDisposable = CompositeDisposable()

    private val consumer: Consumer<SequenceItemEvent> = Consumer {
        updateCandidateScales(sequenceItem)
        updateCandidateChords(sequenceItem)
        subject.onNext(it)
    }

    private fun updateCandidateScales(sequenceItem: SequenceItem?) {
        val scaleManager = context.app().scaleManager
        var scales = setOf<Scale>()

        candidateScales.clear()
        scales = scales.plus(scaleManager.scales)

        if (sequenceItem == null) {
            candidateScales.addAll(scales.sortedBy { it.id }.toList())
            return
        }

        val noteIds = sequenceItem.getNoteIds()

        var tmpScales = setOf<Scale>()
        scales.forEach {
            if (it.getNoteIds().containsAll(noteIds)) {
                tmpScales = tmpScales.plus(it)
            }
        }
        scales = tmpScales.toSet()

        candidateScales.addAll(scales.sortedBy { it.id }.toList())
    }

    private fun updateCandidateChords(sequenceItem: SequenceItem?) {
        val chordManager = context.app().chordManager
        var chords = setOf<Chord>()

        var noteNos: Set<Int>
        var tmpChords: Set<Chord>

        candidateChords.clear()

        // sequenceItemがなかったら全部表示
        if (sequenceItem == null) {
            chords = chords.plus(chordManager.chords)
            candidateChords.addAll(chords.sortedBy { it.id }.toList())
            return
        }

        if (sequenceItem.scale != null) {
            // scaleのroot
            sequenceItem.scale!!.notes.sortedBy { it.id }.forEach { note ->
                ChordType.values().forEach { chordType ->
                    val notes = mutableSetOf<Int>()
                    chordType.constitution.forEach { degree ->
                        val n = (note.noteNo + degree.noteNo + Degree.Octave.noteNo) % Degree.Octave.noteNo
                        notes.add(n)
                    }
                    val chord = chordManager.getChord(note.noteNo, notes)
                    chord?.let { chords = chords.plus(it) }
                }
            }

            // scaleのnoteによる絞り込み
            noteNos = sequenceItem.scale!!.getNoteIds()
            noteNos = Degree.values().map { it.noteNo }.toSet().minus(noteNos) // 含まないnote(avoid)
            tmpChords = setOf()
            chords.forEach { chord ->
                for (noteNo in noteNos) {
                    if (chord.getNoteNos().contains(noteNo)) {
                        return@forEach
                    }
                }
                tmpChords = tmpChords.plus(chord)
            }
            tmpChords = chords.minus(tmpChords)
            chords = chords.minus(tmpChords) // 反転
        }

        // selectedNoteによる絞り込み
        noteNos = sequenceItem.getNoteIds()
        tmpChords = setOf()
        chords.forEach {
            if (it.getNoteNos().containsAll(noteNos)) {
                tmpChords = tmpChords.plus(it)
            }
        }
        tmpChords = chords.minus(tmpChords)
        chords = chords.minus(tmpChords) // 反転

        candidateChords.addAll(chords.sortedBy { it.id }.toList())
    }

    private fun getAllScales(): Collection<Scale> {
        return context.app().scaleManager.scales
    }

    private val subject: PublishSubject<SequenceItemEvent> = PublishSubject.create()
    private val observable: ConnectableObservable<SequenceItemEvent>

    init {
        observable = subject.publish()
    }

    fun setSequenceItem(sequenceItem: SequenceItem?) {
        compositDisposable.clear()

        if (sequenceItem != null) {
            compositDisposable.add(sequenceItem.changes()
                    .subscribeOn(Schedulers.computation())
                    .subscribe(consumer))
        }

        this.sequenceItem = sequenceItem
    }

    fun getCandidateScales(): List<Scale> {
        return candidateScales
    }

    fun getCandidateChords(): List<Chord> {
        return candidateChords
    }

    fun changes(): Observable<SequenceItemEvent> {
        return observable.refCount()
    }
}
