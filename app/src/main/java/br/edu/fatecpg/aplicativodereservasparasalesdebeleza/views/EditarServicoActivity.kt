package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityEditarServicoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.models.HistoricoServico
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class EditarServicoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarServicoBinding
    private val db = Firebase.firestore
    private lateinit var historico: HistoricoServico

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarServicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historico = intent.getParcelableExtra("HISTORICO_SERVICO") ?: return

        // Preenche os campos com os dados existentes
        binding.edtNomeServico.setText(historico.servico.nome)
        binding.edtDescricaoServico.setText(historico.servico.descricao)
        binding.edtPrecoServico.setText(historico.servico.preco.toString())
        binding.edtDuracaoServico.setText(historico.servico.duracao)
        binding.edtHorariosDisponiveis.setText(historico.servico.horarios.joinToString(", "))

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = historico.dataHora?.toDate()?.let { dateFormat.format(it) } ?: "Data inválida"

        binding.txtDataHora.text = formattedDate // Certifique-se de que o ID txtDataHora está correto no layout XML

        binding.btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }
    }

    private fun salvarAlteracoes() {
        val nome = binding.edtNomeServico.text.toString()
        val descricao = binding.edtDescricaoServico.text.toString()
        val preco = binding.edtPrecoServico.text.toString().toDoubleOrNull() ?: return
        val duracao = binding.edtDuracaoServico.text.toString()
        val horarios = binding.edtHorariosDisponiveis.text.toString().split(",").map { it.trim() }

        val servicoAtualizado = historico.servico.copy(
            nome = nome,
            descricao = descricao,
            preco = preco,
            duracao = duracao,
            horarios = horarios
        )

        val salaoEmail = historico.salao.email

        db.collection("cliente")
            .get()
            .addOnSuccessListener { clients ->
                for (client in clients) {
                    val clienteId = client.id
                    db.collection("cliente")
                        .document(clienteId)
                        .collection("agendamentos")
                        .whereEqualTo("salao.email", salaoEmail)
                        .get()
                        .addOnSuccessListener { agendamentos ->
                            for (agendamento in agendamentos) {
                                val agendamentoId = agendamento.id
                                val agenda = agendamento.toObject(HistoricoServico::class.java)
                                if (agenda == historico) {
                                    db.collection("cliente")
                                        .document(clienteId)
                                        .collection("agendamentos")
                                        .document(agendamentoId)
                                        .update(mapOf(
                                            "servico" to servicoAtualizado
                                        ))
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Agendamento atualizado com sucesso", Toast.LENGTH_SHORT).show()
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this, "Erro ao atualizar agendamento: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    break
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao encontrar agendamentos: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao encontrar clientes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
