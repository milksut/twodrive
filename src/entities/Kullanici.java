package entities;

import java.util.List;

public class Kullanici
{

    private int id;

    private String kullanici_adi;

    private String sifre;

    private boolean is_admin = false;

    private List<Paylasilan_dosya> paylasilan_dosyalar;

    private List<Kullanici> takim_uyeleri;

    private List<String> dosyalar;
}
