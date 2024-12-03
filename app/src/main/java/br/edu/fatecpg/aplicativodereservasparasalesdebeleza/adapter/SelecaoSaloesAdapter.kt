package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Salao
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ItemSalaoBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views.SelecionarServicoActivity

class SelecaoSaloesAdapter(
    private val context: Context,
    private val salaoList: List<Salao>
) : RecyclerView.Adapter<SelecaoSaloesAdapter.SalaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaoViewHolder {
        val binding = ItemSalaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SalaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SalaoViewHolder, position: Int) {
        val salao = salaoList[position]
        holder.bind(salao) {
            // Ao clicar em um item da lista, redireciona para a seleção de serviço
            val intent = Intent(context, SelecionarServicoActivity::class.java).apply {
                putExtra("nomeSalao", salao.nome)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = salaoList.size

    // ViewHolder para os itens de salão
    inner class SalaoViewHolder(private val binding: ItemSalaoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(salao: Salao, onClick: (Salao) -> Unit) {
            binding.tvNomeSalao.text = salao.nome
            binding.tvNotaSalao.text = "Nota: ${salao.nota}"
            binding.root.setOnClickListener {
                onClick(salao)
            }
        }
    }
}
