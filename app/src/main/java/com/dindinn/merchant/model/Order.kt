package com.dindinn.merchant.model

class Order {
    var id: Int? = null
    var quantity: Int? = null
    lateinit var addon: List<Addon>
    var alerted_at: String? = null
    var created_at: String? = null
    var expired_at: String? = null
    var title: String? = null


}