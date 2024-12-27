package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import islemler.Kullanici_islemleri;
import islemler.encoder;

public class Kullanici implements Serializable
{
    private final int id = Kullanici_islemleri.get_next_id();

    private String kullanici_adi;

    private String sifre;

    private String tuz;

    private boolean is_admin = false;

    private List<Paylasilan_dosya> paylasilan_dosyalar = new ArrayList<>();

    private List<Integer> takim_uyeleri = new ArrayList<>();

    private List<String> dosyalar = new ArrayList<>();

    private List<String> bildirimler = new ArrayList<>();

    private Boolean changing_password = false;


    public Kullanici(String kullanici_adi, String sifre, Boolean is_admin)
    {
        setKullanici_adi(kullanici_adi);
        setSifre(sifre);
        setIs_admin(is_admin);
    }


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

    public void setSifre(String sifre){
        setTuz(encoder.generateSalt());
        try
        {
            this.sifre = encoder.encodePassword(sifre, getTuz());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTuz() {
        return tuz;
    }

    public void setTuz(String tuz) {
        this.tuz = tuz;
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

    public List<Integer> getTakim_uyeleri()
    {
        return takim_uyeleri;
    }

    public void setTakim_uyeleri(List<Integer> takim_uyeleri) {
        this.takim_uyeleri = takim_uyeleri;
    }

    public List<String> getDosyalar() {
        return dosyalar;
    }

    public void setDosyalar(List<String> dosyalar) {
        this.dosyalar = dosyalar;
    }

    public List<String> getBildirimler()
    {
        return bildirimler;
    }

    public void setBildirimler(List<String> bildirimler)
    {
        this.bildirimler = bildirimler;
    }

    public Boolean getChanging_password() {
        return changing_password;
    }

    public void setChanging_password(Boolean changing_password) {
        this.changing_password = changing_password;
    }
}
