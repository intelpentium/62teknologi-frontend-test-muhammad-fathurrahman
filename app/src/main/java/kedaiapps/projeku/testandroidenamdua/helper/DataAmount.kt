package kedaiapps.projeku.testandroidenamdua.helper

class DataAmount (
    val jml: Int = 0,
    val title: String = ""
) {

    fun getData() : List<DataAmount> {
        val data : MutableList<DataAmount> = ArrayList()
        data.add(DataAmount(25000, "25.000"))
        data.add(DataAmount(50000, "50.000"))
        data.add(DataAmount(100000, "100.000"))
        data.add(DataAmount(500000, "500.000"))
        data.add(DataAmount(1000000, "1.000.000"))
        return data
    }

}