import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

public class Main
{
    public static void main(String[] args)
    {
        LinkedHashMap<Path,Path> copys = new LinkedHashMap<>(5);

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("C:\\Users\\altay\\Desktop\\source")))
        {
            stream.forEach(path ->
                    copys.put(path,Path.of(path.toString().replace("source","target"))));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        dosya_islemleri.yedekle(copys);
        System.out.println("Hello world!");
    }
}