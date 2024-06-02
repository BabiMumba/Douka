package cd.babi.chatal.models

import java.util.Date

data class Message(
    //var senderId: String = "",
  //  var receiverId: String = "",
    var message: String = "",
    //var messageId: String = "",
    //var time: Long = Date().time,
    //var seen: Boolean = false,
    var isReceived: Boolean = true
)
