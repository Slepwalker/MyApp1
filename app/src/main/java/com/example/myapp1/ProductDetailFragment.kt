package com.example.myapp1

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.example.myapp1.databinding.FragmentProductDetailBinding
import com.example.myapp1.model.CourseItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentProductDetailBinding? = null
    private val b get() = _binding!!
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater,container, false)
        return  b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = arguments?.getParcelable("product", CourseItem::class.java)
        product?.let {
            b.dtlTitle.text = it.title
            b.dtlDescription.text = "Descripcion: ${it.description}"
            b.dtlPrice.text = "S/. ${it.price}"
            b.dtlStock.text = "${it.stock} en stock"
            if (!it.imageUri.isNullOrEmpty()) {
                b.dtlImage.setImageURI(Uri.parse(it.imageUri))
            } else {
                it.imageRes?.let { resId ->
                    b.dtlImage.setImageResource(resId)
                }
            }
        }

        b.btnEdit.setOnClickListener {
            val editFragment = EditProductFragment()
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            editFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, editFragment)
                .addToBackStack(null)
                .commit()
        }

        b.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar producto")
                .setMessage("¿Seguro que deseas eliminar este producto")
                .setPositiveButton("Eliminar") { _, _ ->
                    parentFragmentManager.setFragmentResult(
                        "delete_request",
                        bundleOf("deleteProduct" to product)
                    )
                    parentFragmentManager.popBackStack()
                }
                .setNegativeButton("cancelar",null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}