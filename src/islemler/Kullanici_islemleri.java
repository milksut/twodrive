package islemler;

import entities.Kullanici;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;

public class Kullanici_islemleri
{
    private static HashMap<String,Integer> user_names_ids;
    private static HashMap<Integer,String> user_names_ids_reverse;
    private static final String working_directory = System.getProperty("user.dir") + "\\files";

    static
    {
        System.out.println("yaratıldım");

        try(ObjectInputStream user_name_reader = new ObjectInputStream(new FileInputStream(working_directory + "\\user_names_ids.bin")))
        {
            Kullanici_islemleri.user_names_ids = (HashMap<String,Integer>) user_name_reader.readObject();
            user_names_ids_reverse = HashMap.newHashMap(user_names_ids.size());
            user_names_ids.forEach((name,id)->
            {
                user_names_ids_reverse.put(id,name);
            });
            user_name_reader.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();

            Kullanici_islemleri.user_names_ids = new HashMap<String,Integer>();
            user_names_ids_reverse = new HashMap<>();

            if(!Files.exists(Path.of(working_directory)))
            {
                try {
                    Files.createDirectories(Path.of(working_directory));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            kullanici_kaydet(new Kullanici("altay gök", "altay123", true),true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static boolean is_username_unique(String user_name)
    {
        return !user_names_ids.containsKey(user_name);
    }
    public static int get_next_id()
    {
        if(user_names_ids.isEmpty()){return 1;}
        else{return Collections.max(user_names_ids.values()) + 1;}
    }
    public static boolean kullanici_kaydet(Kullanici kullanici,boolean is_new)
    {
        try
        {
            ObjectOutputStream writer;
            if(is_new)
            {
                if (!is_username_unique(kullanici.getKullanici_adi())) {
                    System.out.println("bu isimde kullanici zaten var! kullanici ismi: " + kullanici.getKullanici_adi());
                    return false;
                }
                if (Files.exists(Path.of(working_directory + "\\" + kullanici.getId()))) {
                    System.out.println("bu isimde kullanıcı olmamasına rağmen, bu id ye sahip bir dosya var! kullanıc adı: " + kullanici.getKullanici_adi() +
                            " kullanıcı id: " + kullanici.getId());
                    return false;
                }

                user_names_ids.put(kullanici.getKullanici_adi(), kullanici.getId());
                user_names_ids_reverse.put(kullanici.getId(),kullanici.getKullanici_adi());
                writer = new ObjectOutputStream(new FileOutputStream(working_directory + "\\user_names_ids.bin"));
                writer.writeObject(user_names_ids);
                writer.close();

                Files.createDirectory(Path.of(working_directory+"\\"+kullanici.getId()));
            }

            writer = new ObjectOutputStream(new FileOutputStream(working_directory+"\\"+kullanici.getId() + "\\user_info.bin"));
            writer.writeObject(kullanici);
            writer.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Kullanici get_kullanici(Integer user_id)
    {
        try
        {
            if(!Files.exists(Path.of(working_directory+"\\"+user_id)))
            {
                System.out.println("no such user exsist! user_id: " + user_id);
                return null;
            }
            ObjectInputStream reader = new ObjectInputStream(new FileInputStream(working_directory+"\\"+user_id + "\\user_info.bin"));
            Kullanici temp = (Kullanici) reader.readObject();
            reader.close();
            return temp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static Kullanici get_kullanici(String user_name)
    {
        if(!user_names_ids.containsKey(user_name))
        {
            System.out.println("no such user name! user name: " + user_name);
            return null;
        }
        return get_kullanici(user_names_ids.get(user_name));
    }
    public static boolean change_username(Kullanici kullanici,String new_name)
    {
        if(!is_username_unique(new_name))
        {
            System.out.println("Bu isimde bir kullanıcı var zaten!");
            return false;
        }
        user_names_ids.remove(kullanici.getKullanici_adi(),kullanici.getId());
        user_names_ids.put(new_name, kullanici.getId());

        user_names_ids_reverse.put(kullanici.getId(), new_name);

        kullanici.setKullanici_adi(new_name);
        kullanici_kaydet(kullanici,false);
        return true;
    }
}
