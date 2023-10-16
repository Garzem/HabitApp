package com.example.newapppp.presentation.filter.state

import com.example.newapppp.abstracts.BaseState
import com.example.newapppp.domain.model.Filter

data class FilterState(val filter: Filter?): BaseState
