package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao

data class Salao(
    val email: String = "",
    val nomeCompleto: String = "",
    val horario: String = "",
    val diasDeFuncionamento: List<String> = emptyList(),
    val servicos: String = "",
    val endereco: String? = null,
    val nota: Double? = null
) {
    // Construtor padrão sem argumentos necessário para Firestore
    constructor() : this("", "", "", emptyList(), "", null, null)
}
