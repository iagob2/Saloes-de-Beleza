package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

import java.util.Date

data class Agenda(
    val dataHora: Date,
    val cliente: Cliente,
    val salao: Salao,
    val servico: Servico
)
