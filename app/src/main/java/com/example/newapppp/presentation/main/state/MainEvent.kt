package com.example.newapppp.presentation.main.state

import com.example.newapppp.presentation.abstracts.BaseEvent

interface MainEvent: BaseEvent {

    object OpenDrawer: MainEvent

    object CloseDrawer: MainEvent

}