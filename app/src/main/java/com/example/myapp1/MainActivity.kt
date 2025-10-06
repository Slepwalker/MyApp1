package com.example.myapp1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp1.databinding.ActivityMainBinding
import com.example.myapp1.model.CourseItem
import com.example.myapp1.ui.theme.adapter.CourseAdapter

class MainActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_STUDENT_NAME = "extra_student_name"
        const val EXTRA_STUDENT_EMAIL = "extra_student_email"
    }

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        val name = intent.getStringExtra(EXTRA_STUDENT_NAME).orEmpty()
        val email = intent.getStringExtra(EXTRA_STUDENT_EMAIL).orEmpty()

        b.tvWelcome.text = "Bienvenido, $name"
        b.tvEmail.text = email

        val courses = listOf(
            CourseItem(R.mipmap.ic_launcher_round, "Cuaderno A4 tapa dura rayado","Cuaderno de tapa dura, ideal para clases o apuntes","5.00"),
            CourseItem(R.mipmap.ic_launcher_round,"Lápiz grafito HB","Lápiz tecnico para escritura y dibujo técnico","0.50"),
            CourseItem(R.mipmap.ic_launcher_round,"Lapicero Azul Pilot", "Lapicero de Tinta Azul con agarre ergonómico", "3.50"),
            CourseItem(R.mipmap.ic_launcher_round, "Marcadores permanentes", "Set de marcadores (Negro, Rojo, Azul, Verde)", "6.00"),
            CourseItem(R.mipmap.ic_launcher_round, "Resaltadores Stabilo", "Resaltadores de coloeres (Amarillo, Rosa, Verde, Celeste, Naranja", "7.50"),
            CourseItem(R.mipmap.ic_launcher_round, "Goma de Borrar","Goma de Borrar Blanca", "0.50"),
            CourseItem(R.mipmap.ic_launcher_round, "Regla plástica 30cm", "Regla Transparente con medidas en cm y mm", "1.00"),
            CourseItem(R.mipmap.ic_launcher_round, "Carpeta Anillada tamaño A4", "Carpeta plástica o de cartón prensado con anillos metálicos", "2.00"),
            CourseItem(R.mipmap.ic_launcher_round, "Goma líquida 250gr", "Goma líquida, pega rápido y duradero, no tóxico", "3.50"),
            CourseItem(R.mipmap.ic_launcher_round, "Tijera escolar 13cm", "Tijera segura para niños, con agarre ergónomico", "2.60")
        )

        b.recyclerCourses.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CourseAdapter(courses){ item ->
                Toast.makeText(this@MainActivity, "Curso: ${item.title}", Toast.LENGTH_SHORT).show()
            }
        }
        b.fabAdd.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
    }
}