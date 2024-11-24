package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.adapter.SelecionarServicoAdapter
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Servico
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivitySelecionarServicoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SelecionarServicoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelecionarServicoBinding
    private lateinit var servicoList: MutableList<Servico>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelecionarServicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nomeSalao = intent.getStringExtra("nomeSalao")
        binding.tvNomeSalao.text = nomeSalao

        servicoList = mutableListOf()
        binding.rvListaServicos.layoutManager = LinearLayoutManager(this)

        val db = Firebase.firestore
        db.collection("saloes").whereEqualTo("nomeCompleto", nomeSalao).get()
            .addOnSuccessListener { result ->
                if (result.documents.isNotEmpty()) {
                    val salaoDocument = result.documents[0]
                    db.collection("saloes").document(salaoDocument.id).collection("servicos").get()
                        .addOnSuccessListener { servicesResult ->
                            for (document in servicesResult) {
                                val servico = document.toObject(Servico::class.java)
                                servicoList.add(servico)
                            }
                            val adapter = SelecionarServicoAdapter(servicoList) { servico ->
                                // Ao clicar em um serviço, passa os dados para a SelecionarHorarioActivity
                                val intent = Intent(this, SelecionarHorarioActivity::class.java).apply {
                                    putExtra("servico", servico)
                                    putExtra("nomeSalao", nomeSalao)
                                }
                                startActivity(intent)
                            }
                            binding.rvListaServicos.adapter = adapter
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Erro ao carregar serviços: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Salão não encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao carregar serviços: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
