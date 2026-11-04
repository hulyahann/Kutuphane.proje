public class OgrenciUye extends Uye {
    public OgrenciUye(int id, String ad) { super(id, ad); }

    @Override
    public int getMaxKitapSayisi() { return 3; }

    @Override
    public String getUyeTuru() { return "Öğrenci"; }
}

