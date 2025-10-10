package com.example.myapp1

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapp1.databinding.FragmentEditProductBinding
import com.example.myapp1.model.CourseItem
import androidx.fragment.app.setFragmentResult
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProductFragment : Fragment() {

    private var _b: FragmentEditProductBinding? = null
    private val b get() = _b!!
    private var imageUri: Uri? = null
    private var productToEdit: CourseItem? = null
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            b.imgProducto.setImageURI(it)
            b.imgProducto.tag = it.toString()
        }
    }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _b = FragmentEditProductBinding.inflate(inflater,container,false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productToEdit = arguments?.getParcelable("product", CourseItem::class.java)
        productToEdit?.let {
            b.edtProducto.setText(it.title)
            b.edtDescripcion.setText(it.description)
            b.edtPrecio.setText(it.price)
            b.edtCantidad.setText(it.stock)

            if (it.imageUri != null) {
                imageUri = Uri.parse(it.imageUri)
                b.imgProducto.setImageURI(imageUri)
                b.imgProducto.tag = it.imageUri
            } else if (it.imageRes != null) {
                b.imgProducto.setImageResource(it.imageRes)
            }
        }
        b.imgProducto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        b.btnGuardar.setOnClickListener {
            val id = productToEdit?.id?: UUID.randomUUID().toString()
            val title = b.edtProducto.text?.toString().orEmpty().trim()
            val description = b.edtDescripcion.text.toString().orEmpty().trim()
            val price = b.edtPrecio.text.toString().orEmpty().trim()
            val stock = b.edtCantidad.text.toString().orEmpty().trim()
            val imageUriString = b.imgProducto.tag as? String ?: productToEdit?.imageUri
            val imageResValue = productToEdit?.imageRes

            if (title.isEmpty() || description.isEmpty() || price.isEmpty() || stock.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updateItem = CourseItem(
                id = id,
                imageRes = imageResValue,
                title = title,
                description = description,
                price = price,
                stock = stock,
                imageUri = imageUriString
            )
            val result = Bundle().apply {
                putParcelable("updatedProduct", updateItem)
            }
            parentFragmentManager.setFragmentResult("update_request", result)
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}