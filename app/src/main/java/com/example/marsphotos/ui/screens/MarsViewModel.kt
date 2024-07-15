package com.example.marsphotos.ui.screens

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marsphotos.MarsPhotosApplication
import com.example.marsphotos.data.MarsPhotoRepository
import com.example.marsphotos.model.MarsPhoto
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface
MarsUiState {
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MarsViewModel(private val marsPhotoRepository: MarsPhotoRepository) : ViewModel() {

    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set


    init {
        getMarsPhotos()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getMarsPhotos() {
        viewModelScope.launch {
            try {
                val result = marsPhotoRepository.getMarsPhotos()

                marsUiState = MarsUiState.Success(
                    result
                )
            } catch (e: IOException) {
                MarsUiState.Error
            } catch (e: HttpException) {
                MarsUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MarsPhotosApplication)
                val marsPhotoRepository = application.container.marsPhotosRepository
                MarsViewModel(marsPhotoRepository = marsPhotoRepository)
            }
        }
    }
}