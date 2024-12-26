package entities;

import java.util.List;
import islemler.Kullanici_islemleri;

public class Kullanici
{
    private final int id = Kullanici_islemleri.get_next_id();

    private String kullanici_adi;

    private String sifre;

    private boolean is_admin = false;

    private List<Paylasilan_dosya> paylasilan_dosyalar;

    private List<Kullanici> takim_uyeleri;

    private List<String> dosyalar;

    public int getId() {
        return id;
    }
    public String getKullanici_adi() {
        return kullanici_adi;
    }

    public void setKullanici_adi(String kullanici_adi) {
        this.kullanici_adi = kullanici_adi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public List<Paylasilan_dosya> getPaylasilan_dosyalar() {
        return paylasilan_dosyalar;
    }

    public void setPaylasilan_dosyalar(List<Paylasilan_dosya> paylasilan_dosyalar) {
        this.paylasilan_dosyalar = paylasilan_dosyalar;
    }

    public List<Kullanici> getTakim_uyeleri() {
        return takim_uyeleri;
    }

    public void setTakim_uyeleri(List<Kullanici> takim_uyeleri) {
        this.takim_uyeleri = takim_uyeleri;
    }

    public List<String> getDosyalar() {
        return dosyalar;
    }

    public void setDosyalar(List<String> dosyalar) {
        this.dosyalar = dosyalar;
    }
}
