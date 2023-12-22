package kedaiapps.projeku.testandroidenamdua.helper

class DataAkun (
    val icon: Int = 0,
    val title: String = ""
) {

    fun getData() : List<DataAkun> {
        val data : MutableList<DataAkun> = ArrayList()
//        data.add(DataAkun(R.drawable.ic_akun_shop, "Data Toko"))
//        data.add(DataAkun(R.drawable.ic_akun_phone, "Ubah Nomor Ponsel"))
//        data.add(DataAkun(R.drawable.ic_akun_lock, "Ubah Password"))
//        data.add(DataAkun(R.drawable.ic_akun_book, "Syarat dan ketentuan"))
//        data.add(DataAkun(R.drawable.ic_akun_privacy, "Kebijakan privasi"))
        return data
    }
}