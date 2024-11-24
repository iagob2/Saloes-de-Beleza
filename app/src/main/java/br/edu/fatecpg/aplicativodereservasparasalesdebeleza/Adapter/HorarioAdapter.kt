package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ItemHorarioBinding

class HorarioAdapter(
    private val horarioList: List<Pair<String, String>>, // Par (Dia, Horário)
    private val onClick: (Pair<String, String>) -> Unit
) : RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val binding = ItemHorarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val (dia, horario) = horarioList[position]
        holder.bind(dia, horario)
        holder.itemView.setOnClickListener {
            onClick(dia to horario)
        }
    }

    override fun getItemCount(): Int = horarioList.size

    inner class HorarioViewHolder(private val binding: ItemHorarioBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dia: String, horario: String) {
            binding.tvDia.text = dia
            binding.tvHorario.text = horario
            binding.tvDisponibilidade.text = "Disponível"
        }
    }
}
