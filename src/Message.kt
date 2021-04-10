data class Message(val date: String? = null, val time: String? = null, val sender: String? = null, var message: String? = null){
    override fun toString(): String {
        return "$date $time $sender: $message"
    }
}

