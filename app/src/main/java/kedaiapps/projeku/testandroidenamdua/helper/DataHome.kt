package kedaiapps.projeku.testandroidenamdua.helper

class DataHome(
    val icon: Int = 0,
    val title: String = ""
) {
    fun getData() : List<DataHome> {
        val data : MutableList<DataHome> = ArrayList()
//        data.add(DataHome(R.drawable.h_profil, "Profil"))
//        data.add(DataHome(R.drawable.h_sekre, "Sekretariat"))
//        data.add(DataHome(R.drawable.h_kegiatan, "Kegiatan"))
//        data.add(DataHome(R.drawable.h_galeri, "Galeri"))
//        data.add(DataHome(R.drawable.h_pengumuman, "Pengumuman"))
//        data.add(DataHome(R.drawable.h_warta, "Warta/Buletin"))
//        data.add(DataHome(R.drawable.h_kontak, "Kontak Kami"))
//        data.add(DataHome(R.drawable.h_berita, "Berita"))
        return data
    }
}