package com.example.myapp1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp1.databinding.ActivityMainBinding
import com.example.myapp1.model.AppDatabase
import com.example.myapp1.model.CourseItem
import com.example.myapp1.ui.theme.adapter.CourseAdapter
import kotlinx.coroutines.launch

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

        setSupportActionBar(b.toolbarMain)
        supportActionBar?.title = ""

        val name = intent.getStringExtra(EXTRA_CLIENT_NAME).orEmpty()
        val email = intent.getStringExtra(EXTRA_CLIENT_EMAIL).orEmpty()

        b.tvWelcome.text = "Bienvenido, $name"
        b.tvEmail.text = email

        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(this@MainActivity).courseItemDao()

            if(dao.getAll().isEmpty()) {
                val listaProduct = listOf(
                    CourseItem(
                        imageRes = R.drawable.prod_cuaderno,
                        title = "Cuaderno A4 tapa dura rayado",
                        description = "Cuaderno de tapa dura, ideal para clases o apuntes",
                        price = "5.00",
                        stock = "30"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_lapiz,
                        title = "Lápiz grafito HB",
                        description = "Lápiz tecnico para escritura y dibujo técnico",
                        price = "0.50",
                        stock = "100"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_lapicero,
                        title = "Lapicero Azul Pilot",
                        description = "Lapicero de Tinta Azul con agarre ergonómico",
                        price = "3.50",
                        stock = "23"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_marcador,
                        title = "Marcadores permanentes",
                        description = "Set de marcadores (Negro, Rojo, Azul, Verde)",
                        price = "6.00",
                        stock = "46"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_resaltadores,
                        title = "Resaltadores Stabilo",
                        description = "Resaltadores de coloeres (Amarillo, Rosa, Verde, Celeste, Naranja",
                        price = "7.50",
                        stock = "53"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_goma,
                        title = "Goma de Borrar",
                        description = "Goma de Borrar Blanca",
                        price = "0.50",
                        stock = "102"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_regla,
                        title = "Regla plástica 30cm",
                        description = "Regla Transparente con medidas en cm y mm",
                        price = "1.00",
                        stock = "60"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_carpetas,
                        title = "Carpeta Anillada tamaño A4",
                        description = "Carpeta plástica o de cartón prensado con anillos metálicos",
                        price = "2.00",
                        stock = "45"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_goma,
                        title = "Goma líquida 250gr",
                        description = "Goma líquida, pega rápido y duradero, no tóxico",
                        price = "3.50",
                        stock = "38"
                    ),
                    CourseItem(
                        imageRes = R.drawable.prod_tijera,
                        title = "Tijera escolar 13cm",
                        description = "Tijera segura para niños, con agarre ergónomico",
                        price = "2.60",
                        stock = "47"
                    )
                )
                listaProduct.forEach { dao.insert(it) }
            }
            val productos = dao.getAll()
            items.clear()
            items.addAll(productos)
            adapter.notifyDataSetChanged()
        }
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
                lifecycleScope.launch {
                    val dao = AppDatabase.getDatabase(this@MainActivity).courseItemDao()
                    dao.delete(item)
                    val index = items.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        items.removeAt(index)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@MainActivity, "Producto eliminado con éxito", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        supportFragmentManager.setFragmentResultListener("update_request",this){ _, bundle ->
            val updatedProduct = bundle.getParcelable("updatedProduct", CourseItem::class.java)
            updatedProduct?.let { newItem ->
                lifecycleScope.launch {
                    val dao = AppDatabase.getDatabase(this@MainActivity).courseItemDao()
                    dao.update(newItem)
                    val index = items.indexOfFirst { it.id == newItem.id }
                    if (index != -1) {
                        items[index] = newItem
                        adapter.notifyItemChanged(index)
                        Toast.makeText(
                            this@MainActivity,
                            "Producto actualizado con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "No se encontró el producto a actualizar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCourseItems()
    }

    private fun loadCourseItems() {
        val dao = AppDatabase.getDatabase(this).courseItemDao()
        lifecycleScope.launch {
            val productos = dao.getAll()
            items.clear()
            items.addAll(productos)
            adapter.notifyDataSetChanged()
        }
    }
}