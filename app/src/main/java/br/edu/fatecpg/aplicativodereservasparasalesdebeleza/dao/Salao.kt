package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

data class Salao(
    val email: String,
    val nomeCompleto: String,
    val horarioFuncionamento: String,
    val diasFuncionamento: List<String>,
    val servicosOferecidos: String
)