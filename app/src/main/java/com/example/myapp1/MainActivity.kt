package com.example.myapp1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp1.databinding.ActivityMainBinding
import com.example.myapp1.model.CourseItem
import com.example.myapp1.ui.theme.adapter.CourseAdapter

class MainActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_CLIENT_NAME = "extra_student_name"
        const val EXTRA_CLIENT_EMAIL = "extra_student_email"
    }

    private lateinit var b: ActivityMainBinding
    private lateinit var adapter: CourseAdapter
    private val items = mutableListOf<CourseItem>()
    private val addProductLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val data = result.data!!
            val product = data.getStringExtra("product") ?: return@registerForActivityResult
            val description = data.getStringExtra("description") ?: ""
            val price = data.getStringExtra("price") ?: ""
            val stock = data.getStringExtra("stock") ?: ""
            val imageUri = data.getStringExtra("imageUri")

            val newItem = CourseItem(
                title = product,
                description = description,
                price = price,
                stock = stock,
                imageUri = imageUri
            )
            items.add(newItem)
            adapter.notifyItemInserted(items.size - 1)

            Toast.makeText(this, "Producto agregado con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        val name = intent.getStringExtra(EXTRA_CLIENT_NAME).orEmpty()
        val email = intent.getStringExtra(EXTRA_CLIENT_EMAIL).orEmpty()

        b.tvWelcome.text = "Bienvenido, $name"
        b.tvEmail.text = email

        items.addAll(
            listOf(
            CourseItem("1",R.drawable.prod_cuaderno, "Cuaderno A4 tapa dura rayado","Cuaderno de tapa dura, ideal para clases o apuntes","5.00", "30"),
            CourseItem("2",R.drawable.prod_lapiz,"Lápiz grafito HB","Lápiz tecnico para escritura y dibujo técnico","0.50","100"),
            CourseItem("3",R.drawable.prod_lapicero,"Lapicero Azul Pilot", "Lapicero de Tinta Azul con agarre ergonómico", "3.50","23"),
            CourseItem("4",R.drawable.prod_marcador, "Marcadores permanentes", "Set de marcadores (Negro, Rojo, Azul, Verde)", "6.00","46"),
            CourseItem("5",R.drawable.prod_resaltadores, "Resaltadores Stabilo", "Resaltadores de coloeres (Amarillo, Rosa, Verde, Celeste, Naranja", "7.50","53"),
            CourseItem("6",R.drawable.prod_goma, "Goma de Borrar","Goma de Borrar Blanca", "0.50","102"),
            CourseItem("7",R.drawable.prod_regla, "Regla plástica 30cm", "Regla Transparente con medidas en cm y mm", "1.00","60"),
            CourseItem("8",R.drawable.prod_carpetas, "Carpeta Anillada tamaño A4", "Carpeta plástica o de cartón prensado con anillos metálicos", "2.00","45"),
            CourseItem("9",R.drawable.prod_goma, "Goma líquida 250gr", "Goma líquida, pega rápido y duradero, no tóxico", "3.50","38"),
            CourseItem("10",R.drawable.prod_tijera, "Tijera escolar 13cm", "Tijera segura para niños, con agarre ergónomico", "2.60","47")
            )
        )
        adapter = CourseAdapter(items){ item ->
            val fragment = ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("product",item)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit()
        }

        b.recyclerCourses.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter

        }

        b.fabAdd.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            addProductLauncher.launch(intent)
        }

        supportFragmentManager.setFragmentResultListener("delete_request",this){ _, bundle ->
            val productToDelete = bundle.getParcelable("deleteProduct", CourseItem::class.java)
            productToDelete?.let { item ->
                val index = items.indexOfFirst{it.id == item.id}
                if (index != -1){
                    items.removeAt(index)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Producto eliminado con éxito", Toast.LENGTH_SHORT).show()
                }
            }
        }

        supportFragmentManager.setFragmentResultListener("update_request",this){ _, bundle ->
            val updatedProduct = bundle.getParcelable("updatedProduct", CourseItem::class.java)
            updatedProduct?.let { newItem ->
                val index = items.indexOfFirst{it.id == newItem.id}
                if (index != -1){
                    items[index] = newItem
                    adapter.notifyItemChanged(index)
                    Toast.makeText(this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "No se encontró el producto a actualizar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}