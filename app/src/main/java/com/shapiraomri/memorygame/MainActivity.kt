package com.shapiraomri.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import com.shapiraomri.memorygame.adapters.BoardAdapter
import com.shapiraomri.memorygame.models.MemoryCard
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "MainActivity"
private const val CARDS_NUMBER = 32
class MainActivity : AppCompatActivity() {
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    private lateinit var mainAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startGameInitializations()

        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            startGameInitializations()
        }
    }

    private fun startGameInitializations() {
        val images = mutableListOf(R.drawable.ic_heart, R.drawable.ic_light, R.drawable.ic_plane, R.drawable.ic_smiley)
        for (index in 2..(CARDS_NUMBER / 4)) {
            images.addAll(mutableListOf(R.drawable.ic_heart, R.drawable.ic_light, R.drawable.ic_plane, R.drawable.ic_smiley))
        }
        images.shuffle()

        cards = images.indices.map { index ->
            MemoryCard(images[index])
        }

        val gridView = findViewById<GridView>(R.id.board_game)
        mainAdapter = BoardAdapter(cards, this)
        gridView.adapter = mainAdapter
        gridView.numColumns = 4
        gridView.setOnItemClickListener { _, _, position, _ ->
            Log.i(TAG, "button clicked!")
            updateModels(position) // update the models
            mainAdapter.notifyDataSetChanged() // update the UI for the game
        }
    }

    private fun updateModels(position: Int) {
        //val card = cards[position]
        val card: MemoryCard = mainAdapter.getItem(position) as MemoryCard
        // Error check
        if (card.isFaceUp) {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show()
            return
        }
        // Three cases
        // 0 cards flipped over => restore the cards + flip over the selected card
        // 1 card flipped over => flip over the selected card + check the images are the same
        // 2 cards flipped over => restore the cards + flip over the selected card
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 selected cards
            restoreCards()
            indexOfSingleSelectedCard = position

        } else {
            // exactly 1 card was selected previously
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched)
                card.isFaceUp = false
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier) {
            Toast.makeText(this, "Match found!", Toast.LENGTH_SHORT).show()
            cards[position1].isMatched = true
            cards[position2].isMatched = true
        }
    }
}