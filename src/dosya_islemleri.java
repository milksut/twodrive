import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class dosya_islemleri
{
    private static class kopyala_runnable implements Runnable
    {
        Path source,target;
        kopyala_runnable(Path source, Path target){this.source = source;this.target=target;}

        @Override
        public void run()
        {

            try
            {
                if(Files.isDirectory(source))
                {
                    throw new RuntimeException("Source can't be a directory: " + source);
                }
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES);

                System.out.println("File copied successfully: " + source + " -> " + target);
            }
            catch (IOException e)
            {
                System.err.println("Error copying file: " + source + " -> " + target);
                e.printStackTrace();
            }
        }
    }

    public static String yedekle(LinkedHashMap<Path,Path> paths)
    {
        try
        {
            ArrayList<Thread> threads = new ArrayList<>(paths.size());
            paths.forEach((source, target) ->
            {
                try
                {
                    Thread temp = new Thread(new kopyala_runnable(source,target));
                    temp.start();
                    threads.add(temp);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

            });
            threads.forEach(thread ->
            {
                try
                {
                    thread.join();
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
        return "successful";
    }

    private static class takip_runnable implements Runnable
    {
       public AtomicBoolean working = new AtomicBoolean(false);

        @Override
        public void run()
        {
            //TODO:
        }
    }

    public class file_tracker
    {
        LinkedHashMap<Path,Path> tracked_files;
        ArrayList<Thread> threads;

        file_tracker(LinkedHashMap<Path,Path> paths)
        {
            this.tracked_files = paths;
            //TODO:
        }

        public void close_threads()
        {
            //TODO:
        }

    }
}
