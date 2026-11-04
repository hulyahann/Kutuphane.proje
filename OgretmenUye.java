public class OgretmenUye extends Uye {
    public OgretmenUye(int id, String ad) { super(id, ad); }

    @Override
    public int getMaxKitapSayisi() { return 5; }

    @Override
    public String getUyeTuru() { return "Öğretmen"; }
}
    

