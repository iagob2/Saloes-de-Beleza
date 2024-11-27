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

        if (servico != null && nomeSalao != null) {
            binding.tvTitulo.text = "$nomeSalao - ${servico.nome}"
            binding.tvResumoAgendamento.text = "Resumo: ${Firebase.auth.currentUser?.email}, ${servico.preco}"

            val horariosDisponiveis = servico.horarios.map { "" to it }

            val horarioAdapter = HorarioAdapter(horariosDisponiveis) { (_, horario) ->
                criarAgendamento(horario, servico, nomeSalao)
            }
            binding.rvHorariosDisponiveis.layoutManager = LinearLayoutManager(this)
            binding.rvHorariosDisponiveis.adapter = horarioAdapter
        } else {
            Toast.makeText(this, "Erro ao carregar serviço ou salão.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun criarAgendamento(horario: String, servico: Servico, nomeSalao: String) {
        val dataHora = Date() // Ajuste conforme necessário

        val agenda = Agenda(
            dataHora = dataHora,
            cliente = Cliente(Firebase.auth.currentUser?.email ?: "", ""),
            salao = Salao(nomeSalao, "", "", emptyList(), "", null, null),
            servico = servico
        )
        salvarAgendamento(agenda)
    }

    private fun salvarAgendamento(agenda: Agenda) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = Firebase.firestore
            val clienteRef = db.collection("cliente").document(userId)

            clienteRef.collection("agendamentos").add(agenda)
                .addOnSuccessListener {
                    Toast.makeText(this, "Agendamento realizado com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao salvar agendamento: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show()
        }
    }
}
