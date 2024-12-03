package br.edu.fatecpg.aplicativodereservasparasalesdebeleza.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.databinding.ActivityCadastroBinding
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Cliente
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Salao
import br.edu.fatecpg.aplicativodereservasparasalesdebeleza.dao.Servico
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CadastroActivity : AppCompatActivity() {
    private val TAG = "CadastroActivity"
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    private val listaServicos = mutableListOf<Servico>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val db = Firebase.firestore

        binding.textView.setOnClickListener { finish() }

        binding.rbSalao.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) showSalaoFields() else hideSalaoFields()
        }

        binding.rbCliente.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) hideSalaoFields() else showSalaoFields()
        }

        binding.btnServico.setOnClickListener {
            val intent = Intent(this, NovoServicoActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_NOVO_SERVICO)
        }

        binding.btnEntrar.setOnClickListener {
            val nomeCompleto = binding.editNome.text.toString()
            val email = binding.editEmailCad.text.toString()
            val senha = binding.editSenhaCad.text.toString()

            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User created successfully")
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        if (binding.rbSalao.isChecked) {
                            Log.d(TAG, "Registering a new salon")
                            val horarioFuncionamento = binding.etHorarioFuncionamento.text.toString()
                            val diasFuncionamento = mutableListOf<String>()

                            if (binding.cbSegunda.isChecked) diasFuncionamento.add("Segunda")
                            if (binding.cbTerca.isChecked) diasFuncionamento.add("Terça")
                            if (binding.cbQuarta.isChecked) diasFuncionamento.add("Quarta")
                            if (binding.cbQuinta.isChecked) diasFuncionamento.add("Quinta")
                            if (binding.cbSexta.isChecked) diasFuncionamento.add("Sexta")
                            if (binding.cbSabado.isChecked) diasFuncionamento.add("Sábado")
                            if (binding.cbDomingo.isChecked) diasFuncionamento.add("Domingo")

                            val salao = Salao(
                                nome = nomeCompleto,
                                email = email,
                                horario = horarioFuncionamento,
                                diasDeFuncionamento = diasFuncionamento,
                                nota = 4.5
                            )
                            db.collection("saloes").document(userId).set(salao)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Salon added successfully: $userId")
                                    salvarServicos(db, userId)
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Error adding salon: ${e.message}")
                                    Toast.makeText(this, "Erro ao cadastrar salão", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            val cliente = Cliente(nomeCompleto = nomeCompleto, email = email)
                            db.collection("cliente").document(userId).set(cliente)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Customer registered successfully")
                                    Toast.makeText(this, "Cadastro de cliente realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Error registering customer: ${e.message}")
                                    Toast.makeText(this, "Erro ao cadastrar cliente", Toast.LENGTH_SHORT).show()
                                }
                        }

                        clearForm()
                    } else {
                        Log.e(TAG, "Error creating user: ${task.exception?.message}")
                        Toast.makeText(this, "Erro ao realizar cadastro.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun salvarServicos(db: FirebaseFirestore, salaoId: String) {
        val salaoRef = db.collection("saloes").document(salaoId)
        for (servico in listaServicos) {
            Log.d(TAG, "Saving service: ${servico.nome}")
            salaoRef.collection("servicos").add(servico)
                .addOnSuccessListener {
                    Log.d(TAG, "Serviço ${servico.nome} adicionado com sucesso!")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Erro ao adicionar serviço: ${exception.message}")
                    Toast.makeText(this, "Erro ao adicionar serviço: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun clearForm() {
        binding.editNome.text.clear()
        binding.editEmailCad.text.clear()
        binding.editSenhaCad.text.clear()
        binding.etHorarioFuncionamento.text.clear()
        binding.rgTipoUsuario.clearCheck()
        binding.cbSegunda.isChecked = false
        binding.cbTerca.isChecked = false
        binding.cbQuarta.isChecked = false
        binding.cbQuinta.isChecked = false
        binding.cbSexta.isChecked = false
        binding.cbSabado.isChecked = false
        binding.cbDomingo.isChecked = false

        hideSalaoFields()
    }

    private fun showSalaoFields() {
        binding.etHorarioFuncionamento.visibility = View.VISIBLE
        binding.btnServico.visibility = View.VISIBLE
        binding.tvEscolhaDias.visibility = View.VISIBLE
        binding.gridLayoutDiasSemana.visibility = View.VISIBLE
    }

    private fun hideSalaoFields() {
        binding.etHorarioFuncionamento.visibility = View.GONE
        binding.btnServico.visibility = View.GONE
        binding.tvEscolhaDias.visibility = View.GONE
        binding.gridLayoutDiasSemana.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NOVO_SERVICO && resultCode == Activity.RESULT_OK) {
            val novosServicos = data?.getParcelableArrayListExtra<Servico>("lista_servicos")
            if (novosServicos != null) {
                listaServicos.clear()
                listaServicos.addAll(novosServicos)
                Log.d(TAG, "Serviços recebidos: ${novosServicos.size}")
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_NOVO_SERVICO = 1
    }
}
