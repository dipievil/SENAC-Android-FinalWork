package br.dipievil.boardgamescollection.model

data class Game (
    var id : Int = 0,
    var title : String? = null,
    var minPlayers : Int = 0,
    var maxPlayers : Int = 0,
    var duration : Int = 0,
    var status : String? = null,
    var removed : Boolean = false
)