package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityCadastroBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Cliente
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Salao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val db = Firebase.firestore

        binding.rbSalao.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etHorarioFuncionamento.visibility = View.VISIBLE
                binding.edtServico.visibility = View.VISIBLE
                binding.tvEscolhaDias.visibility = View.VISIBLE
                binding.gridLayoutDiasSemana.visibility = View.VISIBLE
            }
        }

        binding.rbCliente.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etHorarioFuncionamento.visibility = View.GONE
                binding.edtServico.visibility = View.GONE
                binding.tvEscolhaDias.visibility = View.GONE
                binding.gridLayoutDiasSemana.visibility = View.GONE
            }
        }

        binding.btnEntrar.setOnClickListener {
            val nomeCompleto = binding.editNome.text.toString()
            val email = binding.editEmailCad.text.toString()
            val senha = binding.editSenhaCad.text.toString()

            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        if (binding.rbSalao.isChecked) {
                            val horarioFuncionamento = binding.etHorarioFuncionamento.text.toString()
                            val diasFuncionamento = mutableListOf<String>()

                            if (binding.cbSegunda.isChecked) diasFuncionamento.add("Segunda")
                            if (binding.cbTerca.isChecked) diasFuncionamento.add("Terça")
                            if (binding.cbQuarta.isChecked) diasFuncionamento.add("Quarta")
                            if (binding.cbQuinta.isChecked) diasFuncionamento.add("Quinta")
                            if (binding.cbSexta.isChecked) diasFuncionamento.add("Sexta")
                            if (binding.cbSabado.isChecked) diasFuncionamento.add("Sábado")
                            if (binding.cbDomingo.isChecked) diasFuncionamento.add("Domingo")

                            val servicos = binding.edtServico.text.toString()
                            val salao = Salao(
                                email,
                                nomeCompleto,
                                horarioFuncionamento,
                                diasFuncionamento,
                                servicos,
                                endereco = null,
                                nota = null
                            )
                            db.collection("saloes").document(userId).set(salao)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Cadastro de salão realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Erro ao cadastrar salão", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            val cliente = Cliente(email, nomeCompleto)
                            db.collection("cliente").document(userId).set(cliente)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Cadastro de cliente realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Erro ao cadastrar cliente", Toast.LENGTH_SHORT).show()
                                }
                        }

                        binding.editNome.text.clear()
                        binding.editEmailCad.text.clear()
                        binding.editSenhaCad.text.clear()
                        binding.etHorarioFuncionamento.text.clear()
                        binding.edtServico.text.clear()
                        binding.rgTipoUsuario.clearCheck()
                        binding.cbSegunda.isChecked = false
                        binding.cbTerca.isChecked = false
                        binding.cbQuarta.isChecked = false
                        binding.cbQuinta.isChecked = false
                        binding.cbSexta.isChecked = false
                        binding.cbSabado.isChecked = false
                        binding.cbDomingo.isChecked = false

                        binding.etHorarioFuncionamento.visibility = View.GONE
                        binding.edtServico.visibility = View.GONE
                        binding.tvEscolhaDias.visibility = View.GONE
                        binding.gridLayoutDiasSemana.visibility = View.GONE
                    } else {
                        Toast.makeText(this, "Erro ao realizar cadastro.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
