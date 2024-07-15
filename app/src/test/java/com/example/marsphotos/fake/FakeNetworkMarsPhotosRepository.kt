package com.example.marsphotos.fake

import com.example.marsphotos.data.MarsPhotoRepository
import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.rules.TestDispatcherRule
import com.example.marsphotos.ui.screens.MarsUiState
import com.example.marsphotos.ui.screens.MarsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FakeNetworkMarsPhotosRepository : MarsPhotoRepository {
    override suspend fun getMarsPhotos(): List<MarsPhoto> {
        return FakeDataSource.photosList
    }
}

class MarsViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun marsViewModel_getMarsPhotos_verifyMarsUiStateSuccess() = runTest {
        var marsViewModel = MarsViewModel(marsPhotoRepository = FakeNetworkMarsPhotosRepository())
        assertEquals(
            MarsUiState.Success(
                FakeDataSource.photosList
            ), marsViewModel.marsUiState
        )
    }
}