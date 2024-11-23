package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.R
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Servico

class SelecionarServicoActivity : AppCompatActivity() {

    private lateinit var tvNomeSalao: TextView
    private lateinit var recyclerViewServicos: RecyclerView
    private lateinit var servicoList: List<Servico>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecionar_servico)

        tvNomeSalao = findViewById(R.id.tvNomeSalao)
        recyclerViewServicos = findViewById(R.id.rvListaServicos)
        recyclerViewServicos.layoutManager = LinearLayoutManager(this)

        // Recebe o nome do salão passado pela Intent
        val nomeSalao = intent.getStringExtra("nomeSalao")
        tvNomeSalao.text = nomeSalao

        // Exemplo de lista de serviços com preço como String e duração como String
        servicoList = listOf(
            Servico("Corte de cabelo", "R$ 30,00", "30min", "Corte de cabelo masculino e feminino"),
            Servico("Manicure", "R$ 20,00", "40min", "Serviço de manicure completo")
        )

        // Adapter inline para RecyclerView
        val servicoAdapter = object : RecyclerView.Adapter<ServicoViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicoViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_servico, parent, false)
                return ServicoViewHolder(view)
            }

            override fun onBindViewHolder(holder: ServicoViewHolder, position: Int) {
                val servico = servicoList[position]
                holder.bind(servico)
            }

            override fun getItemCount(): Int = servicoList.size
        }

        recyclerViewServicos.adapter = servicoAdapter
    }

    // ViewHolder para os itens de serviço
    inner class ServicoViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvNomeServico: TextView = itemView.findViewById(R.id.tvNomeServico)
        private val tvPrecoDuracaoServico: TextView = itemView.findViewById(R.id.tvPrecoDuracaoServico)
        private val tvDescricaoServico: TextView = itemView.findViewById(R.id.tvDescricaoServico)

        fun bind(servico: Servico) {
            tvNomeServico.text = servico.nome
            // Aqui, concatena o preço com a duração
            tvPrecoDuracaoServico.text = "${servico.preco} - ${servico.duracao}"
            tvDescricaoServico.text = servico.descricao
        }
    }

}
