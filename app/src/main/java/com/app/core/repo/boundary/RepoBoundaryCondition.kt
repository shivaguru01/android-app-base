package com.app.core.repo.boundary

import com.app.core.model.RecyclerItem
import com.app.core.model.RecyclerItemAlbum
import com.app.core.repo.local.LocalRepo
import com.app.core.repo.remote.RemoteRepo
import javax.inject.Inject

class RepoBoundaryCondition
@Inject
constructor(
    override var localRepo: LocalRepo,
    override var remoteRepo: RemoteRepo,
    override var networkPageSize: Int = 50
) : BaseBoundaryCondition<RecyclerItem>() {

    override fun loadData() {

    }

}