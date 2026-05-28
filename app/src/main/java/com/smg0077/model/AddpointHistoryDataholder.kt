package com.smg0077.model

data class AddpointHistoryDataholder(
    var amount: String,
    var txn_id: String,
    var payment_method: String,
    var insert_date: String,
    var point_status: String,
    var deposit_type: String,
    var reject_remark: String
)

