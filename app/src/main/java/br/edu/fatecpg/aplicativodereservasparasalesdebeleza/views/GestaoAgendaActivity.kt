package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.adapter.GestaoAgendaAdapter
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityGestaoAgendaBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.models.HistoricoServico
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GestaoAgendaActivity : AppCompatActivity() {
    private val TAG = "GestaoAgendaActivity"
    private lateinit var binding: ActivityGestaoAgendaBinding
    private lateinit var adapter: GestaoAgendaAdapter
    private val historicoList = mutableListOf<HistoricoServico>()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestaoAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GestaoAgendaAdapter(historicoList) { historico ->
            showEditDeleteDialog(historico)
        }
        binding.rvListaAgendamentos.apply {
            layoutManager = LinearLayoutManager(this@GestaoAgendaActivity)
            adapter = this@GestaoAgendaActivity.adapter
        }

        val salaoLogadoId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        loadAgendamentos(salaoLogadoId)
    }

    private fun loadAgendamentos(salaoLogadoId: String) {
        db.collection("cliente")
            .get()
            .addOnSuccessListener { result ->
                val newList = mutableListOf<HistoricoServico>()
                result.forEach { document ->
                    db.collection("cliente")
                        .document(document.id)
                        .collection("agendamentos")
                        .whereEqualTo("salao.email", salaoLogadoId)
                        .get()
                        .addOnSuccessListener { agendamentos ->
                            for (agendamento in agendamentos) {
                                val historicoServico = agendamento.toObject(HistoricoServico::class.java)
                                newList.add(historicoServico)
                            }
                            adapter.updateList(newList)
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Erro ao carregar agendamentos: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar clientes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEditDeleteDialog(historico: HistoricoServico) {
        val options = arrayOf("Editar", "Deletar")
        AlertDialog.Builder(this)
            .setTitle("Escolha uma opção")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(this, EditarServicoActivity::class.java)
                        intent.putExtra("HISTORICO_SERVICO", historico)
                        startActivity(intent)
                    }
                    1 -> {
                        deleteHistorico(historico)
                    }
                }
            }
            .show()
    }

    private fun deleteHistorico(historico: HistoricoServico) {
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
                                        .delete()
                                        .addOnSuccessListener {
                                            historicoList.remove(historico)
                                            adapter.notifyDataSetChanged()
                                            Toast.makeText(this, "Agendamento deletado com sucesso", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this, "Erro ao deletar agendamento: ${e.message}", Toast.LENGTH_SHORT).show()
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
