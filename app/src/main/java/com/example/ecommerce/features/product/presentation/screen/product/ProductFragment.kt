package com.example.ecommerce.features.product.presentation.screen.product

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.query
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.cart.presentation.BottomSpacingDecoration
import com.example.ecommerce.features.product.presentation.screen.product.adapter.ProductAdapter
import com.example.ecommerce.features.product.presentation.screen.product.adapter.ProductShimmerAdapter
import com.example.ecommerce.features.product.presentation.screen.product.adapter.SearchAdapter
import com.example.ecommerce.features.product.presentation.screen.product.item.ProductShimmerItem
import com.example.ecommerce.features.product.presentation.screen.product_details.ProductDetails
import com.example.ecommerce.features.product.presentation.viewmodel.DetectScrollEndViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.ProductSearchViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.ProductViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max

@AndroidEntryPoint
class ProductFragment : Fragment() {
   // private lateinit var productSearch: SearchView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productShimmerAdapter: ProductShimmerAdapter
    private lateinit var productShimmerRecyclerView: RecyclerView
    private lateinit var productLinearLayout: LinearLayout
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var productShimmerLayout: ShimmerFrameLayout
    private lateinit var swipeRefreshProductLayout: SwipeRefreshLayout
    private lateinit var detectViewModel: DetectScrollEndViewModel
    private  var screenOrientation :Int = 0
    private val productSearchViewModel: ProductSearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenOrientation = resources.configuration.orientation
           // initRecycleViewLayoutManger(screenOrientation)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        initView(view)
        onSwipeRefreshListener()
        return view
    }


    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.productRecyclerView)
        productShimmerLayout = view.findViewById(R.id.productShimmerFrameLayout)
        swipeRefreshProductLayout = view.findViewById(R.id.productSwipeRefreshViewLayout)
        productShimmerRecyclerView = view.findViewById(R.id.productShimmerRecyclerView)
       // productSearch = view.findViewById(R.id.searchProduct)
        productLinearLayout = view.findViewById(R.id.productLinearLayout)
        detectViewModel =
            ViewModelProvider(requireActivity())[DetectScrollEndViewModel::class.java]


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProductRecycleView()
        initShimmerRecycleView()
        fetchData()
        getProduct()
        uiState()
       // detectScrollEnd()
        detectScrollEnd2()
        //setupSearchView()
        searchProduct()
        productAdapter.refresh()

    }


    private fun initProductRecycleView() {
        val searchAdapter = SearchAdapter { query ->
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
        gridLayoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
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
        productViewModel.fetchProductsFromRemote(page = 1, perPage = 20)
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
        productLinearLayout.visibility = View.VISIBLE
    }

    private fun shimmerStartWhenLoading() {
        productShimmerLayout.apply {
            visibility = View.VISIBLE // Ensure the shimmer is visible
            startShimmer()            // Start the shimmer animation
        }
        productLinearLayout.apply {
            visibility = View.GONE
        }
    }

    private fun onSwipeRefreshListener() {
        swipeRefreshProductLayout.setOnRefreshListener {
            lifecycleScope.launch {
                //delay(1000)
                //addressViewModel.checkUpdateAddress()

                swipeRefreshProductLayout.isRefreshing = false


            }
        }
    }

//    private fun setupSearchView() {
//
//        productSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let {
//                    //Log.e("sds", it)
//                    productSearchViewModel.updateSearchQuery(it)
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let { productSearchViewModel.updateSearchQuery(it) }
//                return true
//            }
//        })
//        productSearch.setOnClickListener {
//            productSearch.isIconified = false
//        }
//
//    }

    private fun searchProduct() {
        lifecycleScope.launch {
            productSearchViewModel.product.collectLatest { pagingData ->
                productAdapter.submitData(pagingData)
            }
        }
    }

    private fun detectScrollEnd() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? GridLayoutManager
                layoutManager?.let {
                    val visibleItemCount = it.childCount
                    val totalItemCount = it.itemCount
                    val pastVisibleItemCount = it.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisibleItemCount) >= totalItemCount && dy > 0) {
                        detectViewModel.updateDetectScroll(true)
                    } else {
                        detectViewModel.updateDetectScroll(false)
                    }
                }
            }
        })
    }
    private fun detectScrollEnd2() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && dy > 0) {
                    recyclerView.post {
                        val lastView = recyclerView.findViewHolderForAdapterPosition(totalItemCount - 1)?.itemView
                        lastView?.let {
                            val lastItemHeight = it.height / 3
                            println(lastItemHeight)
                            removeItemDecoration(recyclerView)

                            recyclerView.addItemDecoration(BottomSpacingDecoration(lastItemHeight))
                            recyclerView.clipToPadding = false
                        }
                    }
                }
            }
        })
    }
    private fun removeItemDecoration(recyclerView: RecyclerView) {
        for (i in 0 until recyclerView.itemDecorationCount) {
            val decoration = recyclerView.getItemDecorationAt(i)
            if (decoration is BottomSpacingDecoration) {
                recyclerView.removeItemDecoration(decoration)
                break
            }
        }
    }


}