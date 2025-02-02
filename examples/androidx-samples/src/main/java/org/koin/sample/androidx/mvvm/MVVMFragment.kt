package org.koin.sample.androidx.mvvm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.get
import org.koin.android.ext.android.getKoin
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.scope.requireScopeActivity
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.getActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModelForClass
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.sample.android.R
import org.koin.sample.androidx.components.ID
import org.koin.sample.androidx.components.mvvm.ExtSimpleViewModel
import org.koin.sample.androidx.components.mvvm.SavedStateViewModel
import org.koin.sample.androidx.components.mvvm.SimpleViewModel
import org.koin.sample.androidx.components.scope.Session

class MVVMFragment(private val session: Session) : Fragment(R.layout.mvvm_fragment), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()

    val simpleViewModel: SimpleViewModel by viewModel { parametersOf(ID) }

    // Generic KClass Access
    val scopeVm: ExtSimpleViewModel by viewModelForClass(ExtSimpleViewModel::class)
    val extScopeVm: ExtSimpleViewModel by viewModel(named("ext"))

    val shared: SimpleViewModel by activityViewModel()// sharedViewModel { parametersOf(ID) }

    val sharedSaved: SavedStateViewModel by activityViewModel { parametersOf(ID) }
    val saved by viewModel<SavedStateViewModel> { parametersOf(ID) }
    val saved2 by viewModel<SavedStateViewModel> { parametersOf(ID) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shared2 = getActivityViewModel<SimpleViewModel> { parametersOf(ID) }

        checkNotNull(session)

        assert(shared != simpleViewModel)
        assert((requireActivity() as MVVMActivity).simpleViewModel == shared)

        assert((requireActivity() as MVVMActivity).savedVm != saved)
        assert((requireActivity() as MVVMActivity).savedVm != saved2)
        assert(scopeVm.session.id == extScopeVm.session.id)
        assert((requireActivity() as MVVMActivity).savedVm == sharedSaved)

        assert(shared == shared2)

        assert(saved == saved2)

        assert(requireScopeActivity<MVVMActivity>().get<Session>().id == getKoin().getProperty("session_id"))
        assert(scope.get<Session>().id == requireScopeActivity<MVVMActivity>().get<Session>().id)
    }
}
