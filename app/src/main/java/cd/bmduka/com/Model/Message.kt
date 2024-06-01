package cd.babi.chatal.models

import java.util.Date

data class Message(
    var Isender: Boolean,
    var Isreceiver: Boolean,
    var message: String = "",
    var messageId: String = "",
    var time: Long = Date().time,
    var seen: Boolean = false
)
