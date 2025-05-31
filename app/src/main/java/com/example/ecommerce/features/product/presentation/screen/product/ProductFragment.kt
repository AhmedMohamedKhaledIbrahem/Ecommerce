package com.example.ecommerce.features.product.presentation.screen.product

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.navigationWithArgs
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.core.utils.checkIsMessageOrResourceId
import com.example.ecommerce.core.utils.detectScrollEnd
import com.example.ecommerce.core.utils.removeItemDecoration
import com.example.ecommerce.databinding.FilterBottomSheetLayoutBinding
import com.example.ecommerce.databinding.FragmentProductBinding
import com.example.ecommerce.features.category.presentation.event.CategoryEvent
import com.example.ecommerce.features.category.presentation.screen.adapter.CategoryAdapter
import com.example.ecommerce.features.category.presentation.viewmodel.CategoryViewModel
import com.example.ecommerce.features.product.presentation.event.ProductEvent
import com.example.ecommerce.features.product.presentation.screen.product.adapter.ProductAdapter
import com.example.ecommerce.features.product.presentation.screen.product.adapter.ProductShimmerAdapter
import com.example.ecommerce.features.product.presentation.screen.product.adapter.SearchAdapter
import com.example.ecommerce.features.product.presentation.screen.product.item.ProductShimmerItem
import com.example.ecommerce.features.product.presentation.screen.product_details.ProductDetails
import com.example.ecommerce.features.product.presentation.viewmodel.ExpandedBottomSheetFilterViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.ProductSearchViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.ProductViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private var bottomSheetJob: Job? = null
    private val binding get() = _binding!!
    var category: List<CategoryEntity> = emptyList()
    var selectedCategoryIds = mutableSetOf<Int>()
    var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productShimmerAdapter: ProductShimmerAdapter
    private lateinit var productShimmerRecyclerView: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var productShimmerLayout: ShimmerFrameLayout
    private var screenOrientation: Int = 0
    private val productViewModel: ProductViewModel by viewModels()
    private val productSearchViewModel: ProductSearchViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var expandedBottomSheetFilterViewModel: ExpandedBottomSheetFilterViewModel
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
        expandedBottomSheetFilterViewModel =
            ViewModelProvider(requireActivity())[ExpandedBottomSheetFilterViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShimmerRecycleView()
        initProductRecycleView()
        fetchCategory()
        fetchProductRemote()
        fetchProductRemoteState()
        fetchProductPaging()
        fetchProductPagingState()
        loadCategory()
        productEvent()
        categoryEvent()
        categoryState()
        searchEvent()
        searchState()
        detectScrollEnd(recyclerView, 3)
        expandBottomSheetFilter()

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        fetchProductPaging()
        fetchProductPagingState()
    }

    private fun expandBottomSheetFilter() {
        bottomSheetJob?.cancel()
        bottomSheetJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                expandedBottomSheetFilterViewModel.expandedFilter.collectLatest { expanded ->
                    if (expanded) {
                        val binding = FilterBottomSheetLayoutBinding.inflate(
                            LayoutInflater.from(requireContext()),
                            null,
                            false
                        )
                        bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
                            setContentView(binding.root)
                            setOnDismissListener {
                                expandedBottomSheetFilterViewModel.setExpandedFilter(false)
                            }
                            show()
                        }

                        val categoryAdapter = CategoryAdapter(
                            categories = category,
                            selectedCategoryIds = selectedCategoryIds,
                            onFilterCategoryClick = { category ->
                                if (selectedCategoryIds.contains(category.id)) {
                                    selectedCategoryIds.remove(category.id)
                                } else {
                                    selectedCategoryIds.add(category.id)

                                }
                                productViewModel.onEvent(
                                    ProductEvent.Input.FilterByCategory(
                                        category = selectedCategoryIds.toList()
                                    )
                                )
                                productViewModel.onEvent(ProductEvent.OnFilterCategoryClick)
                            }
                        )

                        val recyclerView = binding.categoryRecyclerView
                        recyclerView.adapter = categoryAdapter
                        recyclerView.layoutManager =
                            GridLayoutManager(requireContext(), 2)
                    }

                }
            }
        }
    }

    private fun productEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.productPagedEvent.collectLatest { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(
                                view = binding.root,
                                message = event.message
                            )
                        }

                        is UiEvent.Navigation.ProductDetails -> {
                            navigationWithArgs(event, onNavigate = { _, args ->
                                val action =
                                    ProductFragmentDirections.actionProductFragmentToProductDetailsFragment(
                                        args as ProductDetails
                                    )
                                findNavController().navigate(action)
                            })

                        }

                        else -> Unit
                    }

                }
            }
        }
    }

    private fun fetchProductRemote() {
        productViewModel.onEvent(ProductEvent.FetchProductRemote)
    }


    private fun fetchProductRemoteState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.productRemoteState.collect { state ->
                    if (state.isLoading) {
                        shimmerStartWhenLoading()
                    } else {
                        shimmerStopWhenDataSuccess()
                        productViewModel.onEvent(ProductEvent.FetchProductPaging)
                    }
                }
            }
        }
    }

    private fun fetchProductPaging() {
        productViewModel.onEvent(ProductEvent.FetchProductPaging)
    }

    private fun fetchProductPagingState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.productPagedState.collectLatest { state ->
                    if (state.isLoading) {

                    } else {
                        productAdapter.submitData(state.products)
                        selectedCategoryIds = state.category.toMutableSet()

                    }

                }
            }
        }
    }


    private fun searchEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productSearchViewModel.searchEvent.collectLatest { event ->
                    when (event) {

                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(
                                view = binding.root,
                                message = event.message
                            )
                        }

                        else -> Unit
                    }

                }
            }
        }
    }

    private fun searchState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productSearchViewModel.searchState.collectLatest { state ->
                    if (state.isSearching) {

                    } else {
                        binding.productRecyclerView.post {
                            if (view != null && _binding != null) {
                                removeItemDecoration(binding.productRecyclerView)
                            }
                        }
                        binding.productRecyclerView.clipToPadding = true
                        productAdapter.submitData(state.products)

                    }

                }
            }
        }
    }

    private fun fetchCategory() {
        categoryViewModel.onEvent(CategoryEvent.FetchCategory)
    }

    private fun loadCategory() {
        categoryViewModel.onEvent(CategoryEvent.LoadCategory)
    }

    private fun categoryEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.categoryEvent.collectLatest { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            checkIsMessageOrResourceId(
                                event = event,
                                context = requireContext(),
                                root = binding.root
                            )
                        }

                        else -> Unit
                    }

                }
            }
        }
    }

    private fun categoryState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.categoryState.collectLatest { state ->
                    val categories = state.categories
                    if (categories.isNotEmpty()) {
                        category = categories

                    }
                }
            }
        }
    }

    private fun initProductRecycleView() {

        searchAdapter = SearchAdapter { query ->
            productSearchViewModel.onEvent(ProductEvent.Input.SearchQuery(query))
            productSearchViewModel.onEvent(ProductEvent.Searched)
        }
        productAdapter = ProductAdapter { product ->
            val productDetails = productDetails(product)
            productViewModel.onEvent(
                ProductEvent.Input.InputProductDetails(
                    productDetails
                )
            )
            productViewModel.onEvent(ProductEvent.OnClickProductCard)

        }
        initRecycleViewLayoutManger()

        recyclerView.adapter = ConcatAdapter(searchAdapter, productAdapter)
    }

    private fun initRecycleViewLayoutManger() {
        val spanCount =
            if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
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

    private fun initShimmerRecycleView() {
        productShimmerAdapter = ProductShimmerAdapter(shimmerProducts())
        productShimmerRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        productShimmerRecyclerView.adapter = productShimmerAdapter
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
            visibility = View.VISIBLE
            startShimmer()
        }
        recyclerView.visibility = View.GONE
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