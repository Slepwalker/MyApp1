package com.example.myapp1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp1.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(){

    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnLogin.setOnClickListener {
            val name = b.etNombre.text?.toString().orEmpty().trim()
            val email = b.etEmail.text?.toString().orEmpty().trim()
            val pass = b.etContrasena.text?.toString().orEmpty()

            val nameErrors = validateName(name)
            val emailErrors = validateEmail(email)
            val passErrors = validatePassword(pass)

            b.tilNombre.error = nameErrors.firstOrNull()
            b.tilEmail.error = emailErrors.firstOrNull()
            b.tilContrasena.error =passErrors.firstOrNull()

            if (nameErrors.isEmpty() && emailErrors.isEmpty() && passErrors.isEmpty()){
                val i = Intent(this, MainActivity::class.java).apply {
                    putExtra(MainActivity.EXTRA_CLIENT_NAME, name)
                    putExtra(MainActivity.EXTRA_CLIENT_EMAIL,email)
                }
                startActivity(i)
            }else{
                Toast.makeText(this, "Corrige los campos y vuelve a intentarlo", Toast.LENGTH_SHORT).show()

                val detail = buildString {
                    if (nameErrors.isNotEmpty()){
                        append("Nombre:\n• "); append(nameErrors.joinToString("\n• ")); append("\n\n")
                    }
                    if (nameErrors.isNotEmpty()){
                        append("Correo:\n• "); append(emailErrors.joinToString("\n• ")); append("\n\n")
                    }
                    if (passErrors.isNotEmpty()){
                        append("Contraseña:\n• "); append(passErrors.joinToString("\n• "))
                    }
                }.trim()

                AlertDialog.Builder(this)
                    .setTitle("Validacion fallida")
                    .setMessage(detail)
                    .setPositiveButton("Entendido", null)
                    .show()
            }
        }

        b.btnRegister.setOnClickListener {
            Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show()
        }

        b.tvForgot.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Recuperar contraseña")
                .setMessage("Ingresa tu correo para recuperar tu contraseña")
                .setPositiveButton("Ok",null)
                .show()
        }

    }

    private  fun validateName(name: String): List<String>{
        val errs = mutableListOf<String>()
        if (name.isBlank()) errs += "El nombre es requerido."
        if (name.length < 3) errs += "El nombre debe tener al menos 3 caracteres."
        if (name.length > 50) errs += "El nombre no debe superar los 50 caracteres."
        if (!name.all { it.isLetter() || it.isWhitespace() }) errs += "Solo se permiten letras y espacios."
        return errs
    }
    private fun validateEmail(email: String): List<String> {
        val errs = mutableListOf<String>()
        if (email.isBlank()) errs += "El correo es requerido."
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) errs += "Formato de correo inválido."
        if (email.length > 254) errs += "El correo no debe superar 254 caracteres."
        val domain = email.substringAfter('@', "")
        val tld = domain.substringAfterLast('.', "")
        if (!domain.contains('.')) errs += "Debe contener un dominio válido (p.ej. ejemplo.com)."
        if (tld.length < 2) errs += "El TLD del dominio debe tener al menos 2 letras."
        return errs
    }

    private fun validatePassword(pass: String): List<String> {
        val errs = mutableListOf<String>()
        if (pass.length < 8) errs += "Debe tener al menos 8 caracteres."
        if (!pass.any { it.isUpperCase() }) errs += "Debe incluir al menos una mayúscula."
        if (!pass.any { it.isLowerCase() }) errs += "Debe incluir al menos una minúscula."
        if (!pass.any { it.isDigit() }) errs += "Debe incluir al menos un número."
        val specials = """!@#${'$'}%^&*()\-_=+\[\]{};:'",.<>?/|\\`~""".toSet()
        if (!pass.any { it in specials }) errs += "Debe incluir al menos un carácter especial."
        if (pass.any { it.isWhitespace() }) errs += "No debe contener espacios."
        return errs
    }
}