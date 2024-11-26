package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import HistoricoServicoActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.adapter.SelecaoSaloesAdapter
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Salao
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivitySelecaoSaloesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SelecaoSaloesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelecaoSaloesBinding
    private lateinit var salaoList: MutableList<Salao>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelecaoSaloesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa a lista de salões
        salaoList = mutableListOf()

        // Configura RecyclerView
        binding.recyclerViewSaloes.layoutManager = LinearLayoutManager(this)

        binding.btnAgenda.setOnClickListener {
            val intent = Intent(this,HistoricoServicoActivity::class.java)
            startActivity(intent)
        }

        // Busca dados do Firestore
        val db = Firebase.firestore
        db.collection("saloes").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val salao = document.toObject(Salao::class.java)
                    salaoList.add(salao)
                }
                // Configura o RecyclerView com os dados recebidos
                binding.recyclerViewSaloes.adapter = SelecaoSaloesAdapter(this, salaoList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao carregar salões: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
