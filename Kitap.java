public class Kitap {
private final String isbn;
    private final String baslik;
    private final String yazar;
    private boolean oduncte;

    public Kitap(String isbn, String baslik, String yazar) {
        this.isbn = isbn;
        this.baslik = baslik;
        this.yazar = yazar;
        this.oduncte = false;
    }

    public String getIsbn() { return isbn; }
    public String getBaslik() { return baslik; }
    public String getYazar() { return yazar; }
    public boolean isOduncte() { return oduncte; }
    public void setOduncte(boolean oduncte) { this.oduncte = oduncte; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s)",
                isbn, baslik, yazar, oduncte ? "ÖDÜNÇTE" : "MÜSAİT");
    }
}

