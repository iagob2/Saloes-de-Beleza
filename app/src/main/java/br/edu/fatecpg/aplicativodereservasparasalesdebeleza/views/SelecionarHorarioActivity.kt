package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.adapter.HorarioAdapter
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Agenda
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Cliente
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Salao
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Servico
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivitySelecionarHorarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class SelecionarHorarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelecionarHorarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelecionarHorarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val servico = intent.getParcelableExtra<Servico>("servico")
        val nomeSalao = intent.getStringExtra("nomeSalao")
        val salaoId = intent.getStringExtra("salaoId") // Receber o ID do salão

        if (servico != null && nomeSalao != null) {
            binding.tvTitulo.text = "$nomeSalao - ${servico.nome}"
            binding.tvResumoAgendamento.text = "Resumo: ${Firebase.auth.currentUser?.email}, ${servico.preco}"

            val horariosDisponiveis = servico.horarios.map { "" to it }

            val horarioAdapter = HorarioAdapter(horariosDisponiveis) { (_, horario) ->
                criarAgendamento(horario, servico, nomeSalao, salaoId)
            }
            binding.rvHorariosDisponiveis.layoutManager = LinearLayoutManager(this)
            binding.rvHorariosDisponiveis.adapter = horarioAdapter
        } else {
            Toast.makeText(this, "Erro ao carregar serviço ou salão.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun criarAgendamento(horario: String, servico: Servico, nomeSalao: String, salaoId: String?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = Firebase.firestore
            val clienteRef = db.collection("cliente").document(userId)

            clienteRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val clienteNome = document.getString("nomeCompleto") ?: Firebase.auth.currentUser?.email ?: ""
                        val dataHora = Date() // Ajuste conforme necessário
                        val agenda = Agenda(
                            dataHora = dataHora,
                            cliente = Cliente(clienteNome, userId),
                            salao = Salao(nome = nomeSalao, email = salaoId ?: "", endereco = "", horario = "", diasDeFuncionamento = emptyList(), nota = null, servicos = emptyList()), // Ajustado para ser uma lista de servicos
                            servico = servico.copy(preco = servico.preco.toDouble()) // Garante que o preco é Double
                        )
                        salvarAgendamento(agenda, clienteRef)
                    } else {
                        Toast.makeText(this, "Erro ao recuperar informações do cliente.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao acessar dados do cliente: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun salvarAgendamento(agenda: Agenda, clienteRef: DocumentReference) {
        val agendaMap = mapOf(
            "dataHora" to agenda.dataHora,
            "cliente" to mapOf(
                "nome" to agenda.cliente.nomeCompleto,
                "email" to agenda.cliente.email
            ),
            "salao" to mapOf(
                "nome" to agenda.salao.nome,
                "email" to agenda.salao.email,
                "endereco" to agenda.salao.endereco,
                "horario" to agenda.salao.horario,
                "diasDeFuncionamento" to agenda.salao.diasDeFuncionamento,
                "nota" to agenda.salao.nota,
                "servicos" to agenda.salao.servicos.map { servico ->
                    mapOf(
                        "nome" to servico.nome,
                        "descricao" to servico.descricao,
                        "preco" to servico.preco,
                        "duracao" to servico.duracao,
                        "horarios" to servico.horarios
                    )
                }
            ),
            "servico" to mapOf(
                "nome" to agenda.servico.nome,
                "descricao" to agenda.servico.descricao,
                "preco" to agenda.servico.preco.toDouble(), // Garante que o preco é Double
                "duracao" to agenda.servico.duracao,
                "horarios" to agenda.servico.horarios
            )
        )

        clienteRef.collection("agendamentos").add(agendaMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Agendamento salvo com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao salvar agendamento: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
