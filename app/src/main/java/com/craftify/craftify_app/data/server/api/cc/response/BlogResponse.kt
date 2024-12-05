import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetAllBlogResponseItem(
	@field:SerializedName("header_image")
	val headerImage: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
) : Parcelable

@Parcelize
data class CreatedAt(
	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
) : Parcelable

typealias GetAllBlogResponse = List<GetAllBlogResponseItem>
