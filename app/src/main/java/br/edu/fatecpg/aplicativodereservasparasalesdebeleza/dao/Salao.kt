package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

data class Salao(
    val email: String,
    val nomeCompleto: String,
    val horario: String,
    val diasDeFuncionamento: List<String>,
    val servicos: String,
    val endereco: String? = null,
    val nota: Double? = null
)