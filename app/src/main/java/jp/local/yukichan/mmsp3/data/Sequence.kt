package jp.local.yukichan.mmsp3.data

import com.google.gson.annotations.SerializedName
import jp.local.yukichan.mmsp3.explore.Explore

data class Sequence(@SerializedName("id") val id: Int, private val explore: Explore) {
    @SerializedName("sequenceItems") val sequenceItems = mutableListOf<SequenceItem>()
    @SerializedName("currentSequenceItemPosition") var currentSequenceItemPosition = -1

    val currentSequenceItem: SequenceItem
        get() {
            return sequenceItems[currentSequenceItemPosition]
        }

    init {
        createNewSequenceItemForNext()
    }

    fun createNewSequenceItemForNext() {
        currentSequenceItemPosition += 1
        sequenceItems.add(currentSequenceItemPosition, SequenceItem(getLatestSequenceItemId() + 1))
        explore.setSequenceItem(currentSequenceItem)
    }

    private fun getLatestSequenceItemId(): Int {
        if (sequenceItems.isEmpty()) {
            return -1
        }
        return sequenceItems.sortedBy { it.id }.last().id
    }
}
