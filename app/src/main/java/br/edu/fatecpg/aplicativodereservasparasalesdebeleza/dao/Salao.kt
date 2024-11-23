package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

data class Salao(
    val email: String,
    val nomeCompleto: String,
    val horario: String,
    val diasDeFuncionamento: List<String>,
    val servicos: String,
    val endereco: String,  // Adicionando a propriedade 'endereco'
    val nota: Double // Adicionando a propriedade 'nota'
)