package jp.local.yukichan.mmsp3.event

import jp.local.yukichan.mmsp3.data.SequenceItem

/**
 * Created by takamk2 on 18/01/03.
 *
 * The Edit Fragment of Base Class.
 */
class SequenceItemEvent(val type: Type) {

    var sequenceItem: SequenceItem? = null

    enum class Type {
        ChangeScale,
        ChangeChord,
        AddNote,
        RemoveNote,
        ;
    }
}