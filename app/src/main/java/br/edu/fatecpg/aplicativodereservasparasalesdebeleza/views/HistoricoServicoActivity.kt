import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityHistoricoServicoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivitySelecionarServicoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.models.HistoricoServico
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoricoServicoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoServicoBinding
    private lateinit var historicoAdapter: HistoricoAdapter
    private val historicoList = mutableListOf<HistoricoServico>()
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


        loadHistorico()

        binding.txtVoltar.setOnClickListener {
            val intent = Intent(this, ActivitySelecionarServicoBinding::class.java)
            startActivity(intent)
            finish()
        }


        binding.btnFiltro.setOnClickListener {
            val novoHistorico = HistoricoServico(
                nomeServico = "Corte de Cabelo",
                dataHora = "24/11/2024 - 15:00",
                status = "Concluído",
                salao = "Beleza Suprema",
                valor = "R$ 50,00"
            )
            saveHistorico(novoHistorico)
        }
    }

    private fun saveHistorico(historico: HistoricoServico) {
        db.collection("historico_servicos")
            .add(historico)
            .addOnSuccessListener {
                Toast.makeText(this, "Histórico salvo com sucesso!", Toast.LENGTH_SHORT).show()
                historicoAdapter.addItem(historico) // Adicionar item à lista visível
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar histórico: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadHistorico() {
        db.collection("historico_servicos")
            .get()
            .addOnSuccessListener { result ->
                val newList = result.map { it.toObject(HistoricoServico::class.java) }
                historicoAdapter.updateList(newList) // Atualizar lista no adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar histórico: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
