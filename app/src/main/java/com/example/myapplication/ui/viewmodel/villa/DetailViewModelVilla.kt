package com.example.myapplication.ui.viewmodel.villa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.myapplication.model.Villa
import com.example.myapplication.repository.VillaRepository
import com.example.myapplication.ui.view.villa.DestinasiDetailVilla
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailUiStateVilla{
    data class Success(val villa: Villa) : DetailUiStateVilla()
    object Error : DetailUiStateVilla()
    object Loading : DetailUiStateVilla()
}

class DetailViewModelVilla(
    savedStateHandle: SavedStateHandle,
    private val villa:VillaRepository): ViewModel(){
    var villaUiState : DetailUiStateVilla by mutableStateOf(DetailUiStateVilla.Loading)
        private set
    private val _id_villa: String = checkNotNull(savedStateHandle[DestinasiDetailVilla.IdVilla])

    init {
        getVillabyId()
    }

    fun getVillabyId(){
        viewModelScope.launch {
            villaUiState = DetailUiStateVilla.Loading
            villaUiState = try {
                DetailUiStateVilla.Success(villa.getVillaById(_id_villa))
            } catch (e: IOException) {
                DetailUiStateVilla.Error
            } catch (e: HttpException) {
                DetailUiStateVilla.Error
            }
        }
    }
    fun deleteVilla(id_Villa: Int){
        viewModelScope.launch {
            try {
                villa.deleteVilla(id_Villa.toString())

            }catch (e:IOException){
                HomeVillaUiState.Error
            }catch (e:HttpException){
                HomeVillaUiState.Error
            }
        }
    }
}

fun Villa.toDetailUiEvent(): InsertUiEvent {
    return InsertUiEvent(
        id_villa = id_villa,
        nama_villa = nama_villa,
        alamat = alamat,
        kamar_tersedia = kamar_tersedia
    )
}