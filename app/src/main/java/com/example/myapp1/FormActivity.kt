package com.example.myapp1

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp1.databinding.ActivityFormBinding
import com.example.myapp1.ui.theme.adapter.CourseAdapter

class FormActivity: AppCompatActivity(){

    private lateinit var b: ActivityFormBinding
    private var imageUri: Uri? =null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri -> if (uri != null) {
            contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
            imageUri = uri
            b.imgProducto.setImageURI(uri)
            b.imgProducto.tag = uri.toString()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFormBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.imgProducto.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            pickImageLauncher.launch("image/*")
        }

        b.btnGuardar.setOnClickListener {

            val product = b.edtProducto.text?.toString().orEmpty().trim()
            val description = b.edtDescripcion.text.toString().orEmpty().trim()
            val price = b.edtPrecio.text.toString().orEmpty().trim()
            val stock = b.edtCantidad.text.toString().orEmpty().trim()
            val imageUri = b.imgProducto.tag as? String

            val productErrors = validateProducto(product)
            val descriptionErrors = validateDescripcion(description)
            val priceErrors = validatePrecio(price)
            val stockErrors = validateCantidad(stock)
            val imageErrors = mutableListOf<String>()
            if (imageUri == null) {
                imageErrors += "Debe seleccionar una imagen del producto"
            }

            b.edtProducto.error = productErrors.firstOrNull()
            b.edtDescripcion.error = descriptionErrors.firstOrNull()
            b.edtPrecio.error = priceErrors.firstOrNull()
            b.edtCantidad.error = stockErrors.firstOrNull()

            if (imageErrors.isNotEmpty()) {
                Toast.makeText(this, imageErrors.first(), Toast.LENGTH_SHORT).show()
            }

            if (productErrors.isEmpty() && descriptionErrors.isEmpty() && priceErrors.isEmpty() && stockErrors.isEmpty() && imageErrors.isEmpty()) {
                val intent = Intent()
                intent.putExtra("product", product)
                intent.putExtra("description", description)
                intent.putExtra("price", price)
                intent.putExtra("stock",stock)
                intent.putExtra("imageUri",imageUri)
                setResult(RESULT_OK, intent)
                finish()
            }

        }
    }

    private fun validateProducto (product: String): List<String>{
        val errs = mutableListOf<String>()
        if (product.isEmpty()) errs += "El nombre del producto es requerido"
        if (product.length < 3) errs += "El nombre del producto debe tener al menos 3 caracteres."
        if (product.length > 50) errs += "El nombre del producto no debe superar los 50 caracteres."
        if (!product.all { it.isLetter() || it.isWhitespace() }) errs += "Solo se permiten letras y espacios."
        return errs
    }
    private fun validateDescripcion (description: String): List<String>{
        val errs = mutableListOf<String>()
        if (description.isEmpty()) errs += "La descripcion del producto es requerido"
        if (description.length < 3) errs += "La descripcion del producto debe tener al menos 3 caracteres."
        if (description.length > 150) errs += "La descripcion del producto no debe superar los 150 caracteres."
        return errs
    }

    private fun validatePrecio (price: String): List<String>{
        val errs = mutableListOf<String>()
        val value = price.toDoubleOrNull()
        if (value == null || value < 0.1) errs += "El precio debe ser un número válido mayor que 0.1."
        if (price.isEmpty()) errs += "El precio es requerido"
        val allowedChars = "0123456789.".toSet()
        if (!price.all { it in allowedChars } || price.count {it == '.'} > 1) errs += "el precio solo puede contener números y punto decimal."
        if (price.any { it.isWhitespace() }) errs += "No debe contener espacios."
        return errs
    }

    private fun validateCantidad (stock: String): List<String>{
        val errs = mutableListOf<String>()
        if (stock.isEmpty()) errs += "La cantidad es requerido"
        if (!stock.all { it.isDigit() }) errs += "Solo se permiten numeros"
        val value = stock.toIntOrNull()
        if (value != null) {
            if (value < 1) errs += "La cantidad minima es 1"
            if (value > 9999) errs += "La cantidad maxima es 9999"
        }else{
            errs += "cantidad invalida"
        }
        if (stock.any(){it.isWhitespace()}) errs += "No debe contener espacios"
        return errs
    }
}