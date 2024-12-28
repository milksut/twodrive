package islemler;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class dosya_islemleri
{

    private static void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    deleteDirectoryRecursively(entry); // Recursively delete contents
                }
            }
        }
        Files.delete(path); // Delete the directory or file itself
    }

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
                    if(!Files.exists(target))
                    {
                        Files.createDirectory(target);
                    }
                    try(DirectoryStream<Path> stream = Files.newDirectoryStream(source))
                    {
                        LinkedHashMap<Path,Path> child_files = new LinkedHashMap<>();
                        stream.forEach(path ->{
                            child_files.put(path, target.resolve(source.relativize(path)));
                        });
                        yedekle(child_files);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.COPY_ATTRIBUTES);

                    System.out.println("File copied successfully: " + source + " -> " + target);
                }
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


    //------------------------------------------------------------------------------------------
    private static class takip_runnable extends Thread
    {
       private AtomicBoolean working;
       private Path source,target;

       private final LinkedHashMap<Path,Path> child_files = new LinkedHashMap<>();
       private file_tracker child_tracker;

       public takip_runnable(AtomicBoolean working,Path source,Path target)
       {
           this.working = working;
           this.source=source;
           this.target=target;


           try (DirectoryStream<Path> stream = Files.newDirectoryStream(source))
           {
               stream.forEach(path ->
               {
                   if (Files.isDirectory(path))
                   {
                       child_files.put(path, target.resolve(source.relativize(path)));
                   }
               });
           }
           catch (Exception e)
           {
               throw new RuntimeException(e);
           }

           child_tracker = new file_tracker(child_files);
       }

        @Override
        public void run()
        {
            System.out.println("started_working, watching: " + source);
            try(WatchService watcher = FileSystems.getDefault().newWatchService())
            {
                source.register(watcher,StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
                while(!interrupted())
                {
                    WatchKey key = watcher.take();
                    if(key==null){
                        System.out.println("key is null!!!");
                        continue;}

                    working.set(true);
                    ArrayList<WatchEvent<?>> events = new ArrayList<>(key.pollEvents());
                    key.reset();

                    for(WatchEvent<?> event : events)
                    {
                        if(interrupted())
                        {
                            throw new InterruptedException();
                        }
                        Path path = (Path) event.context();
                        Path target_path = target.resolve(path);
                        path = source.resolve(path);

                        System.out.println("an event at :"+path + "! event kind is: " + event.kind());
                        System.out.println("affected files: " + event.context());

                        if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
                        {
                            if(!Files.exists(path)){continue;}
                            if(Files.isDirectory(path))
                            {
                                Files.createDirectory(target_path);
                                child_tracker.add_thread(path,target_path);
                            }
                            else
                            {
                                kopyala_runnable temp = new kopyala_runnable(path,target_path);
                                temp.run();
                            }
                        }
                        else if(event.kind() == StandardWatchEventKinds.ENTRY_DELETE)
                        {
                            if(!Files.exists(target_path)){continue;}
                            if(child_tracker!=null && child_tracker.map.containsKey(path))
                            {
                                AtomicBoolean temp = child_tracker.map.get(path);
                                child_tracker.threads.get(temp).interrupt();

                                child_tracker.map.remove(path);
                                child_tracker.threads.remove(temp);
                            }
                            deleteDirectoryRecursively(target_path);
                        }
                        else if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
                        {
                            if(!Files.exists(path) || Files.isDirectory(path)){continue;}
                            kopyala_runnable temp = new kopyala_runnable(path,target_path);
                            temp.run();
                        }

                    }

                    working.set(false);
                    if(interrupted())
                    {
                        throw new InterruptedException();
                    }
                   Thread.sleep(5000);
                }
            }
            catch(InterruptedException e)
            {
                if(Files.exists(source))
                {e.printStackTrace();}
                else {
                    System.out.println("my file is deleted, i deleting my self : " + source);
                }
                if(child_tracker!=null)
                {
                    child_tracker.close_threads();
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

        }
    }

    public static class file_tracker
    {
        LinkedHashMap<Path,Path> tracked_files;
        LinkedHashMap<AtomicBoolean,Thread> threads = new LinkedHashMap<>();
        HashMap<Path,AtomicBoolean> map = new HashMap<>();

        public file_tracker(LinkedHashMap<Path, Path> paths)
        {
            this.tracked_files = paths;
            tracked_files.forEach((source,target)->
            {
                AtomicBoolean is_working = new AtomicBoolean(false);
                Thread temp = new takip_runnable(is_working,source,target);
                temp.start();
                threads.put(is_working,temp);
                map.put(source,is_working);
            });
        }

        public void add_thread(Path source, Path target)
        {
            tracked_files.put(source,target);
            AtomicBoolean is_working = new AtomicBoolean(false);
            Thread temp = new Thread(new takip_runnable(is_working,source,target));
            temp.start();
            threads.put(is_working,temp);
            map.put(source,is_working);
        }

        public void close_threads()
        {
            try
            {
                for(var entry:threads.entrySet())
                {
                    while(!entry.getKey().get());
                    entry.getValue().interrupt();
                    entry.getValue().join();
                }
                map.clear();
                threads.clear();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }
    }

    //----------------------------------------------------------------------------------------
    public static class dosya_esitle extends Thread
    {
        Path source,target;
        ArrayList<Thread> sub_threads = new ArrayList<>();
        public dosya_esitle(Path source, Path target)
        {
            this.source =source;
            this.target = target;
        }

        @Override
        public void run()
        {
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(source))
            {
                stream.forEach(path ->{
                    try
                    {
                        Path target_path = target.resolve(source.relativize(path));
                        if(Files.isDirectory(path))
                        {
                            if(!Files.exists(target_path))
                            {
                                Files.createDirectories(target_path);
                            }
                            Thread temp = new dosya_esitle(path,target_path);
                            temp.start();
                            sub_threads.add(temp);
                        }
                        else
                        {
                            if(!Files.exists(target_path))
                            {
                                Files.copy(path, target_path, StandardCopyOption.COPY_ATTRIBUTES);
                            }
                            else
                            {
                                BasicFileAttributes sourceAttrs = Files.readAttributes(path, BasicFileAttributes.class);
                                BasicFileAttributes targetAttrs = Files.readAttributes(target_path, BasicFileAttributes.class);
                                if (sourceAttrs.lastModifiedTime().compareTo(targetAttrs.lastModifiedTime()) > 0)
                                {
                                    Files.copy(path, target_path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            try(DirectoryStream<Path> stream = Files.newDirectoryStream(target))
            {
                stream.forEach(path -> {
                    try
                    {
                        Path target_path = source.resolve(target.relativize(path));
                        if(!Files.exists(target_path))
                        {
                            if(Files.isDirectory(path))
                            {
                                deleteDirectoryRecursively(path);
                            }
                            else
                            {
                                Files.delete(path);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            sub_threads.forEach(thread ->
            {
                try
                {thread.join();}
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    //---------------------------------------------------------------------------------------------
    public static double get_folder_size(Path directory)
    {
        if(!Files.isDirectory(directory))
        {
            System.out.println("this is not a directory! path: " +directory);
            return 0;
        }
        try
        {
            final double[] size = {0};

            Files.walkFileTree(directory, new SimpleFileVisitor<>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    size[0] += attrs.size(); // Add file size
                    return FileVisitResult.CONTINUE;
                }
            });

            return size[0]/ 1048576;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
