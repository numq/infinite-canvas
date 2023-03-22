package upload

sealed class UploadState<T> private constructor() {
    data class Active<T>(val targetList: MutableList<T>) : UploadState<T>()
    object Empty : UploadState<Nothing>()
}