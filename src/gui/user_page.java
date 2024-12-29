package gui;

import entities.Kullanici;
import entities.Paylasilan_dosya;
import islemler.Kullanici_islemleri;
import islemler.dosya_islemleri;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class user_page extends JFrame
{
    private JLabel current_user_name;
    private JLabel change_user_name;
    private JLabel password_change;
    private JLabel file_upload;
    private JLabel teammate_add;
    private JLabel file_share;
    private JTextField new_name;
    private JButton değiştirButton;
    private JButton istekYollaButton;
    private JButton dosyaSeçButton;
    private JButton takımÜyesiYapButton;
    private JButton düzenleButton;
    private JTextField team_member_input;
    private JComboBox team_mates;
    private JComboBox uploaded_files;
    private JButton notifications;
    private JComboBox uploaded_files_share;
    private JButton dosyayıPaylaşButton;
    private JPanel panel1;
    private JButton indirButton;
    private JButton çıkışYapButton;

    private class file_warper
    {
        private String sahip;
        private int alıcı;
        private String file;
        private boolean is_owner;

        file_warper(String sahip, int alıcı, String file, boolean is_owner)
        {
            this.sahip = sahip;
            this.alıcı = alıcı;
            this.file = file;
            this.is_owner = is_owner;
        }

        @Override
        public String toString()
        {
            if(is_owner)
            {
                return file;
            }
            else
            {
                return "<html>(" + sahip+" adlı kullanıcının dosylarından)<br>" + file + "</html>";
            }
        }
    }

    user_page(Kullanici current_user)
    {
        setContentPane(panel1);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700,400));

        if(current_user.getChanging_password())
        {
            new change_password(current_user);
        }

        current_user.getDosyalar().forEach(x->
        {
            uploaded_files_share.addItem(new file_warper(null,-1,x,true));
            uploaded_files.addItem(new file_warper(null,-1,x,true));
        });
        current_user.getPaylasilan_dosyalar().forEach(x->
        {
            if(x.getPaylasimci() == current_user.getId()){return;}
            String sender = Kullanici_islemleri.get_from_map(x.getPaylasimci());
            if(sender==null)
            {
                System.out.println("Dosyayı paylaşmış olan kullanıcı bulunamadı! Kullanıcı id: " + x.getPaylasimci()
                        +"\n Paylaşım Siliniyor! Paylaşılan dosya ismi: " + x.getPaylasilan_dosya());
                return;
            }

            uploaded_files.addItem(new file_warper(sender,x.getAlici(),x.getPaylasilan_dosya(),false));

        });
        current_user.getTakim_uyeleri().forEach(x->team_mates.addItem(Kullanici_islemleri.get_from_map(x)));

        çıkışYapButton.addActionListener(_->
        {
            new login_register();
            dispose();
        });

        current_user_name.setText("Hoşgeldin " + current_user.getKullanici_adi());

        notifications.addActionListener(_->new bildrimler(current_user.getBildirimler()));

        değiştirButton.addActionListener(_->
        {
            String new_name_text = new_name.getText().trim();
            if(new_name_text.isEmpty())
            {
                new dondu("Lütfen geçerli bir kullanıcı adı giriniz!");
                return;
            }
            if(!Kullanici_islemleri.is_username_unique(new_name_text))
            {
                new dondu("Bu kullanıcı adı çoktan alınmış!");
                return;
            }
            if(!Kullanici_islemleri.change_username(current_user,new_name_text))
            {
                new dondu("Kullancı adı değiştrilirken bir sıkıntı oluştu!");
                return;
            }
            else
            {
                new dondu("Kullanıcı adı başarıyla değiştirildi!");
                current_user_name.setText("Hoşgeldin " + current_user.getKullanici_adi());
            }
        });

        istekYollaButton.addActionListener(_->{
            Boolean temp = Kullanici_islemleri.send_notification(current_user.getKullanici_adi() +
                            " Şifresini değiştirme isteği yolladı!", current_user.getId(),1,true);
            new dondu(temp ? "şifre değiştime isteği başarıyla gönderildi!" : "istek gönderilirken bir hata oluştu !");
        });

        dosyaSeçButton.addActionListener(_->
        {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = fileChooser.getSelectedFile();
                String dondu = Kullanici_islemleri.upload_file(selectedFile,current_user);
                if(dondu.startsWith("Dosya yükleme başarılı!"))
                {
                    uploaded_files.addItem(selectedFile.getName());
                    uploaded_files_share.addItem(selectedFile.getName());
                }
                new dondu(dondu);
            }
        });

        takımÜyesiYapButton.addActionListener(_->
        {
            String user_name = team_member_input.getText().trim();
            if(user_name.isEmpty())
            {
                new dondu("Lütfen geçerli bir kullanıcı adı giriniz!");
                return;
            }
            if(Kullanici_islemleri.is_username_unique(user_name) || user_name.equals("altay gök"))
            {
                new dondu("Bu isimde bir kullanıcı yok!");
            }

            int temp_id = Kullanici_islemleri.get_from_map(user_name);

            if(Kullanici_islemleri.send_notification(current_user.getKullanici_adi()+" sizi takım üyesi olarak ekledi!",
                    current_user.getId(),temp_id,true))
            {
                current_user.getTakim_uyeleri().add(temp_id);
                Kullanici_islemleri.kullanici_kaydet(current_user,false);
                team_mates.addItem(user_name);
                new dondu(user_name + " adlı kişi başarıyla takımarkadaşı olarak eklendi");
                return;
            }
            else
            {
                new dondu(user_name + " adlı kişiyi arkadaş eklemeye çalışrıken bir hata ile karşılaşıldı!");
            }
        });

        dosyayıPaylaşButton.addActionListener(_->
        {
            String shared_file = (String) uploaded_files_share.getSelectedItem();
            String receiver_name = (String) team_mates.getSelectedItem();
            int receiver_id = Kullanici_islemleri.get_from_map(receiver_name);

            if(receiver_id<=1)
            {
                new dondu("Bu isimde bir kullanıcı bulunmadı! Kullanıcı ismi: " + receiver_name);
                return;
            }

            Kullanici receiver = Kullanici_islemleri.get_kullanici(receiver_id);

            if(receiver == null)
            {
                new dondu("Kullanıcı erişilirken bir sorunla karşılaşıldı! Kullanıcı ismi: " +  receiver_name);
                return;
            }

            Paylasilan_dosya temp = new Paylasilan_dosya(current_user.getId(),receiver_id,shared_file);

            current_user.getPaylasilan_dosyalar().add(temp);

            receiver.getPaylasilan_dosyalar().add(temp);
            receiver.getBildirimler().add(current_user.getKullanici_adi() + " kullanıcısı sizinle " + shared_file
                    + " adlı dosyayı paylaştı! Tarih: " + LocalDateTime.now());

            Kullanici_islemleri.kullanici_kaydet(current_user,false);
            Kullanici_islemleri.kullanici_kaydet(receiver,false);

            new dondu(shared_file+" ismindeki dosya " + receiver_name + " adlı kullanıcı ile paylaşıldı!");

        });

        indirButton.addActionListener(_->
        {
            file_warper temp = (file_warper) uploaded_files.getSelectedItem();

            if(temp == null)
            {
                new dondu("Lütfen indirilecek dosyayı seçniz!");
                return;
            }

            JFileChooser folderChooser = new JFileChooser();
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = folderChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    Path downland_folder = Path.of(folderChooser.getSelectedFile().getAbsolutePath());

                    if(Files.exists(downland_folder.resolve(temp.file)))
                    {
                        int x = 1;
                        while(Files.exists(downland_folder.resolve("("+x+")" + temp.file)))
                        {
                            x++;
                        }
                        downland_folder = downland_folder.resolve("("+x+")"+ temp.file);
                    }


                    Path selected_file = Path.of(System.getProperty("user.dir") + "\\files\\"
                            + (temp.is_owner ? current_user.getId() : Kullanici_islemleri.get_from_map(temp.sahip))
                            + "\\" + temp.file);

                    LinkedHashMap<Path,Path> temp2 = new LinkedHashMap<>(1);
                    temp2.put(selected_file,downland_folder);

                    String dondu = dosya_islemleri.yedekle(temp2);

                    if(dondu.equals("successful"))
                    {
                        new dondu(temp.file + " adlı belge " + downland_folder + " konumuna indirildi!");
                    }
                    else
                    {
                        new dondu("Dosya indirilirken bir hata oluştu !" + dondu);
                    }
                }
                catch (Exception e)
                {
                    new dondu("Dosya indrilirken bir hata oluştu!");
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });

        düzenleButton.addActionListener(_->
        {
            file_warper temp = (file_warper) uploaded_files.getSelectedItem();
            if(temp == null)
            {
                new dondu("Lütfen düzenlenecek dosyayı seçniz!");
                return;
            }
            File selected_file = new File(System.getProperty("user.dir") + "\\files\\"
                    + (temp.is_owner ? current_user.getId() : Kullanici_islemleri.get_from_map(temp.sahip))
                    + "\\" + temp.file);

            if (Desktop.isDesktopSupported())
            {
                try
                {
                    Desktop.getDesktop().open(selected_file);
                    Kullanici_islemleri.send_notification(current_user.getKullanici_adi() + " adlı kullanıcı "
                            + temp.file + "adlı dosyayı düzenledi!",current_user.getId(),
                            temp.is_owner ? temp.alıcı : Kullanici_islemleri.get_from_map(temp.sahip),false);
                    return;
                }
                catch (IOException ex)
                {
                    new dondu("Dosya açılırken bir hata oluştu!");
                    throw new RuntimeException(ex);
                }
            }
            else
            {
                new dondu("Dosyayı açacak bir uygulama bulunamadı");
            }

        });

    }
}