package com.example.ecommerce.features.product.presentation.screen.product

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.ecommerce.core.constants.Page
import com.example.ecommerce.core.constants.PerPage
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.core.utils.detectScrollEnd
import com.example.ecommerce.databinding.FragmentProductBinding
import com.example.ecommerce.features.product.presentation.screen.product.adapter.ProductAdapter
import com.example.ecommerce.features.product.presentation.screen.product.adapter.ProductShimmerAdapter
import com.example.ecommerce.features.product.presentation.screen.product.adapter.SearchAdapter
import com.example.ecommerce.features.product.presentation.screen.product.item.ProductShimmerItem
import com.example.ecommerce.features.product.presentation.screen.product_details.ProductDetails
import com.example.ecommerce.features.product.presentation.viewmodel.ProductSearchViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.ProductViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productShimmerAdapter: ProductShimmerAdapter
    private lateinit var productShimmerRecyclerView: RecyclerView
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var productShimmerLayout: ShimmerFrameLayout
    private var screenOrientation: Int = 0
    private val productSearchViewModel: ProductSearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenOrientation = resources.configuration.orientation
    }

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }


    private fun initView() {
        recyclerView = binding.productRecyclerView
        productShimmerLayout = binding.productShimmerFrameLayout
        productShimmerRecyclerView = binding.productShimmerInclude.productShimmerRecyclerView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProductRecycleView()
        initShimmerRecycleView()
        fetchData()
        getProduct()
        uiState()
        detectScrollEnd(recyclerView, 3)
        collectProduct()
        productAdapter.refresh()
        productSearchViewModel.searchQuery.asLiveData().observe(viewLifecycleOwner) { query ->
            searchAdapter.updateQuery(query)
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        productAdapter.refresh()
    }

    private fun initProductRecycleView() {
        searchAdapter = SearchAdapter { query ->

            productSearchViewModel.updateSearchQuery(query)

        }
        productAdapter = ProductAdapter { product ->
            val productDetails = productDetails(product)
            val action = ProductFragmentDirections.actionProductFragmentToProductDetailsFragment(
                productDetails
            )
            findNavController().navigate(action)

        }
        initRecycleViewLayoutManger()

        recyclerView.adapter = ConcatAdapter(searchAdapter, productAdapter)
    }

    private fun initRecycleViewLayoutManger() {
        val spanCount = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) spanCount else 1
            }

        }
        recyclerView.layoutManager = gridLayoutManager


    }

    private fun productDetails(product: ProductWithAllDetails): ProductDetails {
        return ProductDetails(
            productId = product.product.productIdJson.toString(),
            productName = product.product.name,
            productPrice = product.product.price,
            productDescription = product.product.description,
            category = product.categories.joinToString { it.categoryName },
            image = product.images.joinToString { it.imageUrl },
            stock = product.product.statusStock,
            rating = product.product.ratingCount.toDouble()
        )
    }

    private fun fetchData() {
        productViewModel.fetchProductsFromRemote(page = Page, perPage = PerPage)
    }

    private fun shimmerProducts(): List<ProductShimmerItem> {
        val products = listOf(
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
            ProductShimmerItem(
                "",
                ""
            ),
        )
        return products
    }

    private fun uiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.productState.collect { state ->
                    productUiState(state)
                }
            }
        }
    }

    private fun productUiState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                if (state.source == "fetchProducts") {
                    shimmerStartWhenLoading()
                }
            }

            is UiState.Success -> {
                if (state.source == "fetchProducts") {
                    productAdapter.refresh()
                    shimmerStopWhenDataSuccess()


                }
            }

            is UiState.Error -> {
                if (state.source == "fetchProducts") {
                    shimmerStopWhenDataSuccess()
                }
            }

        }
    }

    private fun initShimmerRecycleView() {
        productShimmerAdapter = ProductShimmerAdapter(shimmerProducts())
        productShimmerRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        productShimmerRecyclerView.adapter = productShimmerAdapter
    }

    private fun getProduct() {
        lifecycleScope.launch {
            productViewModel.productsPaged.collectLatest { paging ->
                productAdapter.refresh()
                productAdapter.submitData(paging)
            }


        }
    }

    private fun shimmerStopWhenDataSuccess() {
        productShimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        recyclerView.visibility = View.VISIBLE
    }

    private fun shimmerStartWhenLoading() {
        productShimmerLayout.apply {
            visibility = View.VISIBLE // Ensure the shimmer is visible
            startShimmer()            // Start the shimmer animation
        }
        recyclerView.visibility = View.GONE
    }

    private fun collectProduct() {
        lifecycleScope.launch {
            productSearchViewModel.product.collectLatest { pagingData ->
                productAdapter.submitData(pagingData)
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        WorkManager.getInstance(requireContext()).cancelAllWork()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}