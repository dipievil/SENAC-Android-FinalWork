package br.dipievil.boardgamescollection.repository

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.dipievil.boardgamescollection.model.Game

class DbHandler(
    context: Context?,
    errorHandler: DatabaseErrorHandler?
) : SQLiteOpenHelper(context, NAME, null, VERSION, errorHandler) {

    companion object{
        const val NAME = "BGCOLDB"
        const val VERSION = 1
        const val TABLE = "Games"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = "CREATE TABLE IF NOT EXISTS $TABLE (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "title TEXT, "+
                "minplayers INTEGER, "+
                "maxplayers INTEGER, "+
                "duration INTEGER, "+
                "status TEXT, "+
                "removed INTEGER)"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun addGame(game: Game): Boolean{
        var db = this.writableDatabase

        val values = ContentValues()
        values.put("title", game.title)
        values.put("minPlayers", game.minPlayers)
        values.put("maxPlayers", game.maxPlayers)
        values.put("duration", game.duration)
        values.put("status", game.status)
        values.put("removed", game.removed)
        val returnInsert = db.insert(TABLE,null,values)
        db.close()
        return returnInsert >= 0
    }

    fun updateGame( game: Game): Boolean{
        var db = this.writableDatabase

        val values = ContentValues()
        values.put("title", game.title)
        values.put("minPlayers", game.minPlayers)
        values.put("maxPlayers", game.maxPlayers)
        values.put("duration", game.duration)
        values.put("status", game.status)
        values.put("removed", game.removed)
        val returnInsert = db.update(TABLE,values, "id = "+game.id, null)
        db.close()
        return returnInsert >= 0
    }

    fun deleteGame(idGame: Int) : Boolean{
        var game = getGameById(idGame)
        game?.removed = true
        /*
        var db = this.writableDatabase
        val returnDelete = db.delete(TABLE, "id = "+idGame, null)
        db.close()
         */
        return updateGame(game as Game)
    }

    fun getGames() : ArrayList<Game>{
        var games = ArrayList<Game>()
        var db = this.readableDatabase
        val query = "SELECT * FROM $TABLE WHERE removed == 0 ORDER BY title"
        val cursor = db.rawQuery(query, null)
        if (cursor != null){
            if(cursor.count > 0){
                cursor.moveToFirst()
                do {
                    val id = cursor.getInt(0)
                    val title = cursor.getString(1)
                    val minPlayers = cursor.getInt(2)
                    val maxPlayers = cursor.getInt(3)
                    val duration = cursor.getInt(4)
                    val status = cursor.getString(5)
                    val removed = cursor.getString(6).toBoolean()
                    val book = Game(id, title, minPlayers, maxPlayers, duration, status,removed)
                    games.add(book)
                } while(cursor.moveToNext())
            }
        }
        return games
    }

    fun getGameById(idBook: Int) : Game?{
        var db = this.readableDatabase

        val query = "SELECT * FROM $TABLE WHERE id = $idBook ORDER BY title"
        val cursor = db.rawQuery(query, null)
        if (cursor != null){
            if(cursor.count > 0){
                cursor.moveToFirst()
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val minPlayers = cursor.getInt(2)
                val maxPlayers = cursor.getInt(3)
                val duration = cursor.getInt(4)
                val status = cursor.getString(5)
                val removed = cursor.getString(6).toBoolean()
                val game = Game(id, title, minPlayers, maxPlayers, duration, status, removed)
                return game
            }
        }
        return null
    }
}