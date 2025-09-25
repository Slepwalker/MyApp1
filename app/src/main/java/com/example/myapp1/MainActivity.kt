package com.example.myapp1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enlaza con el XML:
        setContentView(R.layout.activity_main)

        //Referencias
        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtTelefono = findViewById<EditText>(R.id.edtTelefono)
        val edtProducto = findViewById<EditText>(R.id.edtProducto)
        val edtCantidad = findViewById<EditText>(R.id.edtCantidad)
        val edtDircliente = findViewById<EditText>(R.id.edtDircliente)

        //Referencia al FloatingActionButton
        val fabEnviar = findViewById<FloatingActionButton>(R.id.fabEnviar)

        fabEnviar.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val telefono = edtTelefono.text.toString()
            val producto = edtProducto.text.toString()
            val cantidad = edtCantidad.text.toString()
            val direccion = edtDircliente.text.toString()

            // Mostrar un resumen en Toast
            val mensaje =
                "Cliente: $nombre\nTel: $telefono\nProducto: $producto\nCantidad: $cantidad\nDir: $direccion"
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

            //validacion
            fabEnviar.setOnClickListener {
                val nombre = edtNombre.text.toString().trim()
                val telefono = edtTelefono.text.toString().trim()
                val producto = edtProducto.text.toString().trim()
                val cantidad = edtCantidad.text.toString().trim()
                val direccion = edtDircliente.text.toString().trim()
                when {
                    nombre.isEmpty() -> {
                        edtNombre.error = "Ingrese su nombre"
                        edtNombre.requestFocus()
                    }

                    telefono.isEmpty() -> {
                        edtTelefono.error = "Ingrese su teléfono"
                        edtTelefono.requestFocus()
                    }

                    !telefono.matches(Regex("^[0-9]{6,9}\$")) -> {
                        edtTelefono.error = "Teléfono no válido"
                        edtTelefono.requestFocus()
                    }

                    producto.isEmpty() -> {
                        edtProducto.error = "Ingrese producto"
                        edtProducto.requestFocus()
                    }

                    cantidad.isEmpty() -> {
                        edtCantidad.error = "Ingrese cantidad"
                        edtCantidad.requestFocus()
                    }

                    cantidad.toIntOrNull() == null || cantidad.toInt() <= 0 -> {
                        edtCantidad.error = "Cantidad no válida"
                        edtCantidad.requestFocus()
                    }

                    direccion.isEmpty() -> {
                        edtDircliente.error = "Ingrese dirección"
                        edtDircliente.requestFocus()
                    }

                    else -> {
                        // Todo válido → muestra el mensaje
                        val mensaje = """
                Cliente: $nombre
                Tel: $telefono
                Producto: $producto
                Cantidad: $cantidad
                Dir: $direccion
            """.trimIndent()

                        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}