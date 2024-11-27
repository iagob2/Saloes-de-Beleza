package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import HistoricoAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Agenda
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityHistoricoServicoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivitySelecionarServicoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoricoServicoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoServicoBinding
    private lateinit var historicoAdapter: HistoricoAdapter
    private val historicoList = mutableListOf<Agenda>()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoServicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historicoAdapter = HistoricoAdapter(historicoList)
        binding.rvHistoricoServicos.apply {
            layoutManager = LinearLayoutManager(this@HistoricoServicoActivity)
            adapter = historicoAdapter
        }

        loadAgendamentos()

        binding.txtVoltar.setOnClickListener {
            val intent = Intent(this, ActivitySelecionarServicoBinding::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadAgendamentos() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("cliente")
                .document(userId)
                .collection("agendamentos")
                .get()
                .addOnSuccessListener { result ->
                    val newList = result.map { it.toObject(Agenda::class.java) }
                    historicoAdapter.updateList(newList) // Atualizar lista no adapter com os agendamentos
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao carregar agendamentos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show()
        }
    }
}
