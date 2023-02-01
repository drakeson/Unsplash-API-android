package ug.code.unsplash.ui.save

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ug.code.unsplash.R
import ug.code.unsplash.databinding.FragmentSavedBinding
import ug.code.unsplash.util.Constants.Companion.zero
import ug.code.unsplash.util.Status.ERROR
import ug.code.unsplash.util.Status.SUCCESS
import ug.code.unsplash.util.WallpaperClickListener
import ug.code.unsplash.util.gone
import ug.code.unsplash.util.visible

@AndroidEntryPoint
class SavedFragment : Fragment(R.layout.fragment_saved), WallpaperClickListener.WallpaperClick {

    private val binding: FragmentSavedBinding by viewBinding()
    private val viewModel: SavedViewModel by viewModels()
    private val savedAdapter by lazy {
        SavedAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        val sGrid = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        sGrid.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.recyclerSavedWallpaperView.apply {
            layoutManager = sGrid
            adapter = savedAdapter
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(zero, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deletePhotoFromDataBase(savedAdapter.getPhotoByPosition(viewHolder.bindingAdapterPosition))
                    savedAdapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                    viewModel.getAllPhotosFromFavorite()
                }
            }).attachToRecyclerView(this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllPhotosFromFavorite()
    }

    private fun initObservers() {
        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    it.data?.let { it1 -> savedAdapter.updateItems(it1) }
                    showPhotoInFavorite()
                }
                ERROR -> {
                    emptyPhotoInFavorite()
                }
                else -> {}
            }
        }
    }

    private fun emptyPhotoInFavorite() {
        binding.emptyBoxView.root.visible()
        binding.savedTextView.gone()
        binding.recyclerSavedWallpaperView.gone()
    }

    private fun showPhotoInFavorite() {
        binding.emptyBoxView.root.gone()
        binding.savedTextView.visible()
        binding.recyclerSavedWallpaperView.visible()
    }

    override fun onWallpaperClick(idNetworkPhoto: String, idLocalPhoto: Int, urlImageUser: String) {
        Navigation.findNavController(requireView()).navigate(
            SavedFragmentDirections.actionFragmentSavedToActivityDetailImage(
                idNetworkPhoto, idLocalPhoto, urlImageUser
            )
        )
    }
}