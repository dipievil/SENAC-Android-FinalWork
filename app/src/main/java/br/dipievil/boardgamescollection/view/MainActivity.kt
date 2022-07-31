package br.dipievil.boardgamescollection.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.dipievil.boardgamescollection.R
import br.dipievil.boardgamescollection.adapter.GameListAdapter
import br.dipievil.boardgamescollection.model.Game
import br.dipievil.boardgamescollection.repository.DbHandler
import br.dipievil.boardgamescollection.statics.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var rvGames : RecyclerView
    private lateinit var btnAdd : FloatingActionButton

    private var listAdapter : GameListAdapter? = null
    private var linearLayoutManager : LinearLayoutManager? = null

    private var games : List<Game> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGames = findViewById(R.id.rvGames)
        btnAdd = findViewById(R.id.btnAddGame)

        btnAdd.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            intent.putExtra(Constants.ACTION,Constants.INSERT)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val dbHandler = DbHandler(this,null)
        games = dbHandler.getGames()

        listAdapter = GameListAdapter(games,this){ position, action ->
            if(action == Constants.EDIT){
                val intent = Intent(this, FormActivity::class.java)
                intent.putExtra(Constants.ACTION,Constants.EDIT)
                intent.putExtra(Constants.GAMEID,games[position].id)
                startActivity(intent)
            } else {
                if(dbHandler.deleteGame(games[position].id)){
                    games = dbHandler.getGames()
                    listAdapter?.notifyDataSetChanged()
                }
            }
        }

        linearLayoutManager = LinearLayoutManager(this)
        rvGames.layoutManager = linearLayoutManager
        rvGames.adapter = listAdapter
    }
}