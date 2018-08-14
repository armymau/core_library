package core_kt.connection.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GenericResponse(@SerializedName("status") val status: Int = -1,
                           @SerializedName("status_description") val status_description: String = "",
                           @SerializedName("data") val data: Gson? = null,
                           @SerializedName("load_time") val load_time: String = "",
                           @SerializedName("error") val error: ErrorObject? = null,
                           @SerializedName("message") val message: MessageObject? = null,
                           @SerializedName("user_token") val user_token: String = "") : Serializable

data class MessageObject(@SerializedName("msg") val msg: String = "",
                         @SerializedName("description") val description: String = "") : Serializable

data class ErrorObject(@SerializedName("message") val message: String = "",
                       @SerializedName("description") val description: String = "") : Serializable