package kedaiapps.projeku.testandroidenamdua.helper

class DataLocation (
    val title: String = ""
) {
    fun getData() : List<DataLocation> {
        val data : MutableList<DataLocation> = ArrayList()
        data.add(DataLocation("New York City"))
        data.add(DataLocation("Kuala Lumpur"))
        data.add(DataLocation("Singapure"))
        return data
    }
}