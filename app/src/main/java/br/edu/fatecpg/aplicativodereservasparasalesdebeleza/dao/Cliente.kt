package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

data class Cliente(
    val email: String = "",
    val nomeCompleto: String = "",
    val agendamentos: List<Agenda> = emptyList() // Adicionando campo para agendamentos
) {
    constructor() : this("", "", emptyList())
}
