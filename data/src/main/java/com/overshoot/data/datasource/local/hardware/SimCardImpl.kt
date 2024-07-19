package com.overshoot.data.datasource.local.hardware

class SimCardImpl: SimCard {
    override fun getPhoneNumber(): String {
        return "+1 650-555-3434"
    }
}