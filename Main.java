
    
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

// ===================== ÜYE (SOYUT SINIF) =====================
abstract class Uye {
    private final int id;
    private final String ad;
    private final List<Kitap> oduncKitaplar = new ArrayList<>();

    public Uye(int id, String ad) {
        this.id = id;
        this.ad = ad;
    }

    // Alt sınıflar kendi limitini belirler (polimorfizm)
    public abstract int getMaxKitapSayisi();
    public abstract String getUyeTuru();

    public boolean limitDolduMu() {
        return oduncKitaplar.size() >= getMaxKitapSayisi();
    }

    public void kitapEkle(Kitap k) { oduncKitaplar.add(k); }
    public void kitapCikar(Kitap k) { oduncKitaplar.remove(k); }

    public int getId() { return id; }
    public String getAd() { return ad; }
    public List<Kitap> getOduncKitaplar() { return oduncKitaplar; }

    @Override
    public String toString() {
        return String.format("#%d %s (%s) - Ödünç kitap: %d/%d",
                id, ad, getUyeTuru(), oduncKitaplar.size(), getMaxKitapSayisi());
    }
}

class OgrenciUye extends Uye {
    public OgrenciUye(int id, String ad) { super(id, ad); }

    @Override
    public int getMaxKitapSayisi() { return 3; }

    @Override
    public String getUyeTuru() { return "Öğrenci"; }
}

class OgretmenUye extends Uye {
    public OgretmenUye(int id, String ad) { super(id, ad); }

    @Override
    public int getMaxKitapSayisi() { return 5; }

    @Override
    public String getUyeTuru() { return "Öğretmen"; }
}

// ===================== KÜTÜPHANE =====================
class Kutuphane {
    private final Map<String, Kitap> kitaplar = new HashMap<>();
    private final Map<Integer, Uye> uyeler = new HashMap<>();
    private int sonrakiUyeId = 1;

    public boolean kitapEkle(String isbn, String baslik, String yazar) {
        if (kitaplar.containsKey(isbn)) return false;
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

// ===================== ANA PROGRAM =====================
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Kutuphane kutuphane = new Kutuphane();

    public static void main(String[] args) {
        ornekVeriYukle();

        boolean calisiyor = true;
        while (calisiyor) {
            menuYazdir();
            int secim = sayiOku("Seçiminiz: ");
            System.out.println();

            switch (secim) {
                case 1 -> kitapEkle();
                case 2 -> uyeEkle();
                case 3 -> kutuphane.kitaplariListele();
                case 4 -> kutuphane.uyeleriListele();
                case 5 -> oduncVer();
                case 6 -> iadeAl();
                case 7 -> kitapAra();
                case 0 -> {
                    System.out.println("Çıkış yapılıyor...");
                    calisiyor = false;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void menuYazdir() {
        System.out.println("\n===== KÜTÜPHANE YÖNETİM SİSTEMİ =====");
        System.out.println("1 - Kitap ekle");
        System.out.println("2 - Üye ekle");
        System.out.println("3 - Kitapları listele");
        System.out.println("4 - Üyeleri listele");
        System.out.println("5 - Ödünç ver");
        System.out.println("6 - İade al");
        System.out.println("7 - Kitap ara");
        System.out.println("0 - Çıkış");
    }

    private static void kitapEkle() {
        String isbn = metinOku("ISBN: ");
        String baslik = metinOku("Başlık: ");
        String yazar = metinOku("Yazar: ");
        if (kutuphane.kitapEkle(isbn, baslik, yazar)) {
            System.out.println("Kitap eklendi.");
        } else {
            System.out.println("Hata: Bu ISBN zaten kayıtlı.");
        }
    }

    private static void uyeEkle() {
        String ad = metinOku("Üye adı: ");
        String tur = metinOku("Tür (1: Öğrenci, 2: Öğretmen): ");
        Uye uye = kutuphane.uyeEkle(ad, tur.equals("2"));
        System.out.println("Üye eklendi: " + uye);
    }

    private static void oduncVer() {
        int uyeId = sayiOku("Üye ID: ");
        String isbn = metinOku("Kitap ISBN: ");
        System.out.println(kutuphane.oduncVer(uyeId, isbn));
    }

    private static void iadeAl() {
        int uyeId = sayiOku("Üye ID: ");
        String isbn = metinOku("Kitap ISBN: ");
        System.out.println(kutuphane.iadeAl(uyeId, isbn));
    }

    private static void kitapAra() {
        String aranan = metinOku("Aranacak kelime (başlık/yazar): ");
        List<Kitap> sonuc = kutuphane.kitapAra(aranan);
        if (sonuc.isEmpty()) {
            System.out.println("Sonuç bulunamadı.");
        } else {
            sonuc.forEach(System.out::println);
        }
    }

    // Yardımcı metotlar
    private static String metinOku(String mesaj) {
        System.out.print(mesaj);
        return sc.nextLine().trim();
    }

    private static int sayiOku(String mesaj) {
        while (true) {
            try {
                System.out.print(mesaj);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Lütfen geçerli bir sayı girin.");
            }
        }
    }

    private static void ornekVeriYukle() {
        kutuphane.kitapEkle("111", "Sefiller", "Victor Hugo");
        kutuphane.kitapEkle("222", "Suç ve Ceza", "Dostoyevski");
        kutuphane.kitapEkle("333", "Clean Code", "Robert C. Martin");
        kutuphane.kitapEkle("444", "Effective Java", "Joshua Bloch");
        kutuphane.uyeEkle("Ayşe Yılmaz", false);
        kutuphane.uyeEkle("Mehmet Demir", true);
    }
}