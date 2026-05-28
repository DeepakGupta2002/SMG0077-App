package com.smg0077.model

data class WithdrawTransHistoryDataHolder(
    var request_no: String,
    var request_amount: String,
    var request_status: String,
    var payment_mode: String,
    var remark: String,
    var insert_date: String,
    var paymentReceipt_url: String
)
