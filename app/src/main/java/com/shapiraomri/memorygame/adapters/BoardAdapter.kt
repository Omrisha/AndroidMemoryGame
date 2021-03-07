package com.shapiraomri.memorygame.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import com.shapiraomri.memorygame.R
import com.shapiraomri.memorygame.models.MemoryCard

public class BoardAdapter (
        private var cards: List<MemoryCard>,
        private val context: Context
        ) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var image: ImageView
    override fun getCount(): Int {
        return cards.count()
    }

    override fun getItem(position: Int): Any {
        return cards[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null){
            convertView = layoutInflater!!.inflate(R.layout.card_item, null)
        }

        image = convertView!!.findViewById(R.id.card_button_image)

        val memoryCard = cards[position]
        if (memoryCard.isMatched) {
            image.alpha = 0.5F
        } else {
            image.alpha = 1.0F
        }
        image.setImageResource(if (memoryCard.isFaceUp) memoryCard.identifier else R.drawable.ic_code)

        return convertView
    }

}