import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kutuphane {
    private final Map<String, Kitap> kitaplar = new HashMap<>();
    private final Map<Integer, Uye> uyeler = new HashMap<>();
    private int sonrakiUyeId = 1;

    public boolean kitapEkle(String isbn, String baslik, String yazar) {
        if (    kitaplar.containsKey(isbn)) return false;
        kitaplar.put(isbn, new Kitap(isbn, baslik, yazar));
        return true;
    }

    public Uye uyeEkle(String ad, boolean ogretmenMi) {
        Uye uye = ogretmenMi
                ? new OgretmenUye(sonrakiUyeId, ad)
                : new OgrenciUye(sonrakiUyeId, ad);
        uyeler.put(sonrakiUyeId, uye);
        sonrakiUyeId++;
        return uye;
    }

    public String oduncVer(int uyeId, String isbn) {
        Uye uye = uyeler.get(uyeId);
        Kitap kitap = kitaplar.get(isbn);

        if (uye == null) return "Hata: Üye bulunamadı.";
        if (kitap == null) return "Hata: Kitap bulunamadı.";
        if (kitap.isOduncte()) return "Hata: Kitap şu an başkasında.";
        if (uye.limitDolduMu()) return "Hata: Ödünç alma limitine ulaşıldı.";

        kitap.setOduncte(true);
        uye.kitapEkle(kitap);
        return "Başarılı: '" + kitap.getBaslik() + "' " + uye.getAd() + " adlı üyeye verildi.";
    }

    public String iadeAl(int uyeId, String isbn) {
        Uye uye = uyeler.get(uyeId);
        Kitap kitap = kitaplar.get(isbn);

        if (uye == null) return "Hata: Üye bulunamadı.";
        if (kitap == null) return "Hata: Kitap bulunamadı.";
        if (!uye.getOduncKitaplar().contains(kitap)) return "Hata: Bu kitap bu üyede değil.";

        kitap.setOduncte(false);
        uye.kitapCikar(kitap);
        return "Başarılı: '" + kitap.getBaslik() + "' iade alındı.";
    }

    public List<Kitap> kitapAra(String aranan) {
        List<Kitap> sonuc = new ArrayList<>();
        String q = aranan.toLowerCase();
        for (Kitap k : kitaplar.values()) {
            if (k.getBaslik().toLowerCase().contains(q)
                    || k.getYazar().toLowerCase().contains(q)) {
                sonuc.add(k);
            }
        }
        return sonuc;
    }

    public void kitaplariListele() {
        if (kitaplar.isEmpty()) {
            System.out.println("Kayıtlı kitap yok.");
            return;
        }
        kitaplar.values().forEach(System.out::println);
    }

    public void uyeleriListele() {
        if (uyeler.isEmpty()) {
            System.out.println("Kayıtlı üye yok.");
            return;
        }
        uyeler.values().forEach(System.out::println);
    }
}