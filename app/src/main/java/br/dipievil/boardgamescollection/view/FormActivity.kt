package br.dipievil.boardgamescollection.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.dipievil.boardgamescollection.R
import br.dipievil.boardgamescollection.model.Game
import br.dipievil.boardgamescollection.repository.DbHandler
import br.dipievil.boardgamescollection.statics.Constants

class FormActivity : AppCompatActivity() {

    private lateinit var etTitle : EditText
    private lateinit var etMinPlayers : EditText
    private lateinit var etMaxPlayers : EditText
    private lateinit var etDuration: EditText
    private lateinit var etStatus : EditText

    private lateinit var btnAdd: Button

    private lateinit var btnCancelar: Button

    var action: String? = ""
    var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        etTitle = findViewById(R.id.etTitle)
        etMinPlayers =findViewById(R.id.etMinPlayers)
        etMaxPlayers = findViewById(R.id.etMaxPlayers)
        etDuration = findViewById(R.id.etDuration)
        etStatus = findViewById(R.id.etStatus)

        btnAdd = findViewById(R.id.btnAdd)
        btnCancelar = findViewById(R.id.btnCancelar)

        action = intent.getStringExtra(Constants.ACTION)

        if(action.equals(Constants.INSERT)){
            game = Game()
        } else {
            val idGame = intent.getIntExtra(Constants.GAMEID,0)
            game = DbHandler(this,null).getGameById((idGame))
            if(game == null){
                finish()
            } else {
                loadForm()
            }
        }

        btnAdd.setOnClickListener{
            save()
        }

        btnCancelar.setOnClickListener{
            finish()
        }
    }

    private fun loadForm() {
        etTitle.setText(game?.title.toString())
        etMinPlayers.setText(game?.minPlayers.toString())
        etMaxPlayers.setText(game?.maxPlayers.toString())
        etDuration.setText(game?.duration.toString())
        etStatus.setText(game?.status.toString())
    }

    fun save(){
        game?.title = etTitle.text.toString()

        game?.status = etStatus.text.toString()

        if(etMinPlayers.text.toString().isEmpty()){
            game?.minPlayers = 1
        } else{
            game?.minPlayers = etMinPlayers.text.toString().toInt()
        }

        if(etMaxPlayers.text.toString().isEmpty()){
            game?.maxPlayers = 1
        } else{
            game?.maxPlayers = etMaxPlayers.text.toString().toInt()
        }

        if(etDuration.text.toString().isEmpty()){
            game?.duration = 0
        } else{
            game?.duration = etDuration.text.toString().toInt()
        }

        val dbHandler = DbHandler(this, null)
        if(action.equals(Constants.INSERT)){
            dbHandler.addGame(game!!)
            etTitle.text.clear()
            etMinPlayers.text.clear()
            etMaxPlayers.text.clear()
            etDuration.text.clear()
            etStatus.text.clear()
        } else {
            dbHandler.updateGame(game!!)
        }
        Toast.makeText(this, getString(R.string.msgGameSaved), Toast.LENGTH_SHORT).show()
        finish()
    }
}