package jp.local.yukichan.mmsp3.data

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.subjects.PublishSubject
import jp.local.yukichan.mmsp3.event.SequenceItemEvent
import jp.local.yukichan.mmsp3.manager.NoteManager

data class SequenceItem(@SerializedName("id") val id: Int) {

    @SerializedName("scale")
    var scale: Scale? = null
        set(value) {
            field = value
            val sequenceItemEvent = SequenceItemEvent(SequenceItemEvent.Type.ChangeScale)
            sequenceItemEvent.sequenceItem = this
            subject.onNext(sequenceItemEvent)
        }

    @SerializedName("chord")
    var chord: Chord? = null
        set(value) {
            field = value
            val sequenceItemEvent = SequenceItemEvent(SequenceItemEvent.Type.ChangeChord)
            sequenceItemEvent.sequenceItem = this
            subject.onNext(sequenceItemEvent)
        }

    @SerializedName("selectedNotes")
    private var selectedNotes: Notes = Notes(id)

    private val subject: PublishSubject<SequenceItemEvent> = PublishSubject.create()
    private val observable: ConnectableObservable<SequenceItemEvent>

    init {
        observable = subject.publish()
    }

    fun containsNote(note: Note): Boolean {
        return selectedNotes.values.contains(note)
    }

    fun addNote(note: Note) {
        if (selectedNotes.plus(note)) {
            val sequenceItemEvent = SequenceItemEvent(SequenceItemEvent.Type.AddNote)
            sequenceItemEvent.sequenceItem = this
            subject.onNext(sequenceItemEvent)
        }
    }

    fun removeNote(note: Note) {
        if (selectedNotes.minus(note)) {
            val sequenceItemEvent = SequenceItemEvent(SequenceItemEvent.Type.RemoveNote)
            sequenceItemEvent.sequenceItem = this
            subject.onNext(sequenceItemEvent)
        }
    }

    fun getNoteList(): List<Note> {
        return selectedNotes.values.toList()
    }

    fun getUniqueNoteList(noteManager: NoteManager): List<Note> {
        return selectedNotes.values
                .map { RootNote(it.noteNo) }
                .toSet()
                .map { noteManager.getNote(it.noteNo, 2)!! } // TODO: octaveをRootNoteと合わせる
                .sortedBy { it.noteNo }
    }

    fun getNoteIds(): Set<Int> {
        return selectedNotes.getNoteIds()
    }

    fun changes(): Observable<SequenceItemEvent> {
        return observable.refCount()
    }

}
