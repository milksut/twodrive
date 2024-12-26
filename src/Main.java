import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import gui.login_register;
import islemler.dosya_islemleri;

public class Main
{
    public static void main(String[] args)
    {

        new login_register();


        LinkedHashMap<Path,Path> watching = new LinkedHashMap<>(1);
        Path source_path = Paths.get("C:\\Users\\altay\\Desktop\\source");
        Path target_path = Paths.get("C:\\Users\\altay\\Desktop\\target");
        watching.put(source_path, target_path);
        Thread temp = new dosya_islemleri.dosya_esitle(source_path, target_path);
        temp.start();
        try {
            temp.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        dosya_islemleri.file_tracker tracker = new dosya_islemleri.file_tracker(watching);
        System.out.println("Hello world!");
    }
}