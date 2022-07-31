package br.dipievil.boardgamescollection.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.dipievil.boardgamescollection.R
import br.dipievil.boardgamescollection.model.Game
import br.dipievil.boardgamescollection.statics.Constants

class GameListAdapter(private val games: List<Game>,
                      private val context: Context,
                      private val callbacks: (Int,String) -> Unit) :
                    RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvTitle : TextView = view.findViewById(R.id.tvTitle)
        var tvStatus : TextView = view.findViewById(R.id.tvStatus)

        var layout: LinearLayout = view.findViewById(R.id.llGameList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.game_list, parent, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]
        holder.tvTitle.text = game.title.toString()
        holder.tvStatus.text = game.status.toString()

        holder.layout.setOnClickListener{
            this.callbacks(position, Constants.EDIT)
        }

        holder.layout.setOnLongClickListener {
            this.callbacks(position, Constants.DELETE)
            true
        }
    }

    override fun getItemCount(): Int {
        return this.games.size
    }


}