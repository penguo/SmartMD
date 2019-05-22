package com.penguodev.mdeditor

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.penguodev.mdeditor.components.MdComponent
import com.penguodev.mdeditor.components.TextComponent
import com.penguodev.mdeditor.databinding.ItemComponentEditBinding
import com.penguodev.mdeditor.databinding.ItemComponentTextBinding

//, int defStyleAttr, int defStyleRes
class MdEditor @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    var adapter: MdEditorAdapter? = MdEditorAdapter(this)

    @SuppressLint("InflateParams")
    fun getTextComponentView(mdComponent: TextComponent) =
        DataBindingUtil.inflate<ItemComponentTextBinding>(
            LayoutInflater.from(context),
            R.layout.item_component_text,
            null,
            false
        ).apply { item = mdComponent }

    val editView = DataBindingUtil.inflate<ItemComponentEditBinding>(
        LayoutInflater.from(context),
        R.layout.item_component_edit,
        null,
        false
    ).apply {
        adapter?.initEditText(editText)
    }

    fun notifyItemAdded(index: Int) {
        val item = adapter?.getItemList()?.get(index)
        addView(
            when (item) {
                is TextComponent -> {
                    getTextComponentView(item).root
                }
                else -> return
            }, index
        )
    }

    fun notifyItemRemoved(index: Int) {
        removeViewAt(index)
    }

    fun notifyDataSetChanged() {
        removeAllViews()
        adapter?.getItemList()?.forEachIndexed { index, mdComponent ->
            addView(
                when (mdComponent) {
                    is TextComponent -> {
                        if (index == adapter?.getCurrentPosition()) {
                            editView.apply { item = mdComponent }
                        } else {
                            getTextComponentView(mdComponent)
                        }.root
                    }
                    else -> return
                }, index
            )
        }
    }

}

open class MdEditorAdapter(private val mdEditor: MdEditor) {
    private val itemList = mutableListOf<MdComponent>()
    private var currentPosition = 0
    private val mdTextWatcher = MdTextWatcher()

    init {
        itemList.add(TextComponent("Hello", null, null))
        itemList.add(TextComponent("23232", null, null))
        itemList.add(TextComponent("5555", null, null))
        itemList.add(TextComponent("Bye!", null, null))
    }

    fun initEditText(editText: EditText) {
        editText.addTextChangedListener(mdTextWatcher)
        editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.selectionStart == 0) {
                onDelPressed()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    fun getItemList(): List<MdComponent> = itemList

    fun getCurrentPosition(): Int = currentPosition

    fun getItem(index: Int): MdComponent {
        return itemList[index].apply {
            if (index == currentPosition) {
                when (this) {
                    is TextComponent -> {
                        text = mdEditor.editView.editText.text.toString()
                    }
                }
            }
        }
    }

    fun addItem(index: Int, mdComponent: MdComponent) {
        itemList.add(index, mdComponent)
        mdEditor.notifyItemAdded(index)
    }

    fun removeItem(index: Int) {
        itemList.removeAt(index)
        mdEditor.notifyItemRemoved(index)
    }

    // inner logic
    private fun enter() {
        val texts = mdEditor.editView.editText.text.toString().split("\n")
        texts.forEachIndexed { index, s ->
            if (index != texts.size - 1) {
                addItem(currentPosition, TextComponent(s, null, null))
                currentPosition++
            } else {
                setCurrentPosition(TextComponent(s, null, null), 0)
            }
        }
    }

    fun setCurrentPosition(item: MdComponent, selection: Int?) {
        itemList[currentPosition] = item
        when (item) {
            is TextComponent -> {
                mdEditor.editView.run {
                    this.item = item
                    this.executePendingBindings()
                    editText.setSelection(selection ?: item.text.length)
                }
            }
        }
    }

    private fun onDelPressed() {
        if (mdEditor.editView.editText.selectionStart == 0 && currentPosition > 0) {
            val currentItem = getItem(currentPosition)
            val prevItem = getItem(currentPosition - 1)
            when {
                currentItem is TextComponent
                        && prevItem is TextComponent -> {
                    val keepSelection = prevItem.text.length
                    prevItem.text += currentItem.text
                    mdEditor.notifyItemRemoved(currentPosition - 1)
                    setCurrentPosition(prevItem, keepSelection)
                    currentPosition--
                }
            }
        }
    }

    inner class MdTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s?.contains("\n") == true) {
                enter()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}
