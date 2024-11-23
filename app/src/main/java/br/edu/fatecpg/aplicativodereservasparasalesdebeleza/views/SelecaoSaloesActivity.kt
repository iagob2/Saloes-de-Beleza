package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.R
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Salao

class SelecaoSaloesActivity : AppCompatActivity() {

    private lateinit var recyclerViewSaloes: RecyclerView
    private lateinit var salaoList: List<Salao>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_saloes)

        // Inicializa RecyclerView
        recyclerViewSaloes = findViewById(R.id.recyclerViewSaloes)
        recyclerViewSaloes.layoutManager = LinearLayoutManager(this)

        // Exemplo de lista de salões
        salaoList = listOf(
            Salao("email1@exemplo.com", "Salão 1", "09:00 - 18:00", listOf("Segunda a Sexta"), "Corte, Manicure", "Rua Exemplo, 123", 4.5),
            Salao("email2@exemplo.com", "Salão 2", "10:00 - 19:00", listOf("Segunda a Sábado"), "Corte, Maquiagem", "Av. Exemplo, 456", 4.2)
        )

        // Adapter inline para RecyclerView
        val salaoAdapter = object : RecyclerView.Adapter<SalaoViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaoViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_salao, parent, false)
                return SalaoViewHolder(view)
            }

            override fun onBindViewHolder(holder: SalaoViewHolder, position: Int) {
                val salao = salaoList[position]
                holder.bind(salao) {
                    // Ao clicar em um item da lista, redireciona para a seleção de serviço
                    val intent = Intent(this@SelecaoSaloesActivity, SelecionarServicoActivity::class.java)
                    intent.putExtra("nomeSalao", salao.nomeCompleto)
                    startActivity(intent)
                }
            }

            override fun getItemCount(): Int = salaoList.size
        }

        recyclerViewSaloes.adapter = salaoAdapter
    }

    // ViewHolder para os itens de salão
    inner class SalaoViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvNomeSalao = itemView.findViewById<android.widget.TextView>(R.id.tvNomeSalao)
        private val tvEnderecoSalao = itemView.findViewById<android.widget.TextView>(R.id.tvEnderecoSalao)
        private val tvNotaSalao = itemView.findViewById<android.widget.TextView>(R.id.tvNotaSalao)

        fun bind(salao: Salao, onClick: (Salao) -> Unit) {
            tvNomeSalao.text = salao.nomeCompleto
            tvEnderecoSalao.text = salao.endereco
            tvNotaSalao.text = "Nota: ${salao.nota}"
            itemView.setOnClickListener {
                onClick(salao)
            }
        }
    }
}
