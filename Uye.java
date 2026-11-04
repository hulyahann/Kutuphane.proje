import java.util.ArrayList;
import java.util.List;

public abstract class Uye {
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

    public boolean limitDoldumu() {
        return oduncKitaplar.size() >= getMaxKitapSayisi();
    }

    public void kitapEkle(Kitap k) { 
        oduncKitaplar.add(k); 
    }
    
    public void kitapCikar(Kitap k) { 
        oduncKitaplar.remove(k); 
    }
public int getId() {
    return id;
}

public String getAd() {
    return ad;
}

    public List<Kitap> getOdunckitaplar() {
        return oduncKitaplar;
    }
}
