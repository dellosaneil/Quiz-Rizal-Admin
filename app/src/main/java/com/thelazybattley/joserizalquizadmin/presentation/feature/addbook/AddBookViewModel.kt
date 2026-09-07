package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.google.firebase.firestore.FirebaseFirestore
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : BaseViewModel<AddBookState, AddBookActions>(), AddBookCallback {

    override fun handleAction(action: AddBookActions) {
        when (action) {

            else -> {}
        }
    }


}
