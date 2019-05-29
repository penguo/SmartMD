package com.penguodev.mdeditor

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.penguodev.mdeditor.components.MdGrammer
import com.penguodev.mdeditor.components.MdComponent
import com.penguodev.mdeditor.databinding.ItemComponentEditBinding
import com.penguodev.mdeditor.databinding.ItemComponentTextBinding

//, int defStyleAttr, int defStyleRes
class MdEditor @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    var adapter: MdEditorAdapter? = MdEditorAdapter(this)
    var editable: Boolean = true

    private var mLifeCycleOwner: LifecycleOwner? = null
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        mLifeCycleOwner = lifecycleOwner
    }

    @SuppressLint("InflateParams")
    fun getTextComponentView(mdComponent: MdComponent) =
        DataBindingUtil.inflate<ItemComponentTextBinding>(
            LayoutInflater.from(context),
            R.layout.item_component_text,
            null,
            false
        ).apply {
            lifecycleOwner = mLifeCycleOwner
            item = mdComponent
            if (editable) {
                this.text.setOnXYClickListener { view, touchX, touchY ->
                    if (touchX == null || touchY == null) return@setOnXYClickListener
                    val position = view.getPreciseOffset(touchX.toInt(), touchY.toInt())
                    val line = this@MdEditor.indexOfChild(this.root)
                    Log.d("Click", "line: $line, posiion: $position")
                    adapter?.setCurrentIndex(this@MdEditor.indexOfChild(this.root), position)
                }
            }
        }

    val editView = DataBindingUtil.inflate<ItemComponentEditBinding>(
        LayoutInflater.from(context),
        R.layout.item_component_edit,
        null,
        false
    ).apply {
        lifecycleOwner = mLifeCycleOwner
        adapter?.initEditText(editText)
    }

    fun getContent(): String = StringBuilder().apply {
        adapter?.getItemList()?.forEachIndexed { index, mdComponent ->
            if (index != 0) append("\n")
            append(mdComponent.text)
        }
    }.toString()

    fun setContent(text: String) {
        adapter?.setContent(text)
    }

    fun notifyItemAdded(index: Int) {
        val item = adapter?.getItem(index)
        addView(
            when (item) {
                is MdComponent -> {
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
                if (index == adapter?.getCurrentIndex() && editable) {
                    editView.apply { item = mdComponent }
                } else {
                    getTextComponentView(mdComponent)
                }.root
                , index
            )
        }
    }

    // TODO
    fun notifyItemRangedAdded(start: Int, end: Int) {
        for (i in start..end) {
            notifyItemAdded(i)
        }
    }

    // TODO
    fun notifyCurrentIndexChanged(oldIndex: Int, newIndex: Int) {
        notifyDataSetChanged()
//        notifyItemRangedAdded()
//        detachViewFromParent(oldIndex)
//        notifyItemAdded(oldIndex)
//        val newItem = adapter?.getItem(newIndex)
//        attachViewToParent(
//            when (newItem) {
//                is MdComponent -> {
//                    editView.apply { item = newItem }
//                }
//                else -> return
//            }.root, newIndex, null
//        )
    }
}

open class MdEditorAdapter(private val mdEditor: MdEditor) {
    private val itemList = mutableListOf<MdComponent>()
    private var currentIndex = 0
    private val mdTextWatcher = MdTextWatcher()

    init {
        itemList.add(MdComponent(""))
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

    fun setContent(text: String) {
        itemList.clear()
        text.split("\n").forEach {
            itemList.add(MdComponent(it))
        }
        mdEditor.notifyDataSetChanged()
    }

    fun getItemList(): List<MdComponent> = itemList

    fun getCurrentIndex(): Int = currentIndex

    fun getItem(index: Int): MdComponent {
        return itemList[index]
    }

    fun addCurrentItemText(frontInput: String, rearInput: String? = null) {
        mdEditor.editView.editText.run {
            val keepSelection = getSelection()
            text.insert(keepSelection.first, frontInput)
            rearInput?.let {
                text.insert(keepSelection.second + frontInput.length, it)
            }
            setSelection(
                keepSelection.first + frontInput.length,
                keepSelection.second + frontInput.length
            )
        }
    }

    fun addCurrentItemTextPrefix(prefix: String) {
        mdEditor.editView.editText.run {
            val keepSelection = getSelection()
            text.insert(0, prefix)
            setSelection(keepSelection.first + prefix.length, keepSelection.second + prefix.length)
        }
    }

    fun removeCurrentItemTextPrefix(prefix: String) {
        mdEditor.editView.editText.run {
            val keepSelection = getSelection()
            text.removePrefix(prefix)
            setSelection(keepSelection.first - prefix.length, keepSelection.second - prefix.length)
        }
    }

    fun updateCurrentIndexItem() {
        val currentItem = getItem(currentIndex)
        currentItem.text = mdEditor.editView.editText.text.toString()
        setItem(currentIndex, currentItem)
    }

    fun setItem(index: Int, item: MdComponent) {
        itemList[index] = item
    }

    fun addItem(index: Int, mdComponent: MdComponent) {
        itemList.add(index, mdComponent)
        mdEditor.notifyItemAdded(index)
    }

    fun removeItem(index: Int) {
        itemList.removeAt(index)
        mdEditor.notifyItemRemoved(index)
    }

    private fun enter() {
        val texts = mdEditor.editView.editText.text.toString().split("\n")
        texts.forEachIndexed { index, s ->
            if (index != texts.size - 1) {
                addItem(currentIndex, MdComponent(s))
                currentIndex++
            } else {
                updateCurrentIndex(MdComponent(s), 0)
            }
        }
    }

    fun updateCurrentIndex(item: MdComponent, selection: Int?) {
        setItem(currentIndex, item)
        mdEditor.editView.run {
            this.item = item
            this.executePendingBindings()
            editText.setSelection(selection ?: item.text.length)
        }
    }

    fun setCurrentIndex(index: Int, selection: Int?) {
        updateCurrentIndexItem()
        val oldIndex = currentIndex
        currentIndex = index
        mdEditor.notifyCurrentIndexChanged(oldIndex, currentIndex)
        val item = getItem(currentIndex)
        mdEditor.editView.run {
            executePendingBindings()
            editText.setSelection(selection ?: item.text.length)
        }
    }

    private fun onDelPressed() {
        if (mdEditor.editView.editText.selectionStart == 0 && currentIndex > 0) {
            val currentItem = getItem(currentIndex)
            val prevItem = getItem(currentIndex - 1)
            updateCurrentIndexItem()
            val keepSelection = prevItem.text.length
            prevItem.text += currentItem.text
            removeItem(currentIndex - 1)
            currentIndex--
            updateCurrentIndex(prevItem, keepSelection)
        }
    }

    inner class MdTextWatcher : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            if (editable == null) return
            if (editable.contains("\n")) {
                enter()
                return
            }
            val before = editable.toString()

            // BOLD
            MdGrammer.values().forEach {
                val match = it.regex.toPattern().matcher(editable)

                var start: Int? = null
                var regexLength = 0
                while (match.find()) {
                    if (start == null) {
                        start = match.start()
                        if (regexLength == 0) {
                            regexLength = match.end() - match.start()
                        }
                    } else {
                        Log.d("match", "$start ~ ${match.end()}")
                        editable.setSpan(it.span, start, match.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        start = null
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}
