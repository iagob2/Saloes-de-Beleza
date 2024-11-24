package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

import java.util.Date

data class Agenda(
    val dataHora: Date = Date(),
    val cliente: Cliente = Cliente("", ""),
    val salao: Salao = Salao("", "", "", emptyList(), "", null, null),
    val servico: Servico = Servico("", "", "", "")
)
