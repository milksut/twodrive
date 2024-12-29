package gui;

import entities.Kullanici;
import islemler.Kullanici_islemleri;
import islemler.my_log_writer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class admin_page extends JFrame {
    private JButton bildirimlerButton;
    private JButton çıkışYapButton;
    private JComboBox select_user;
    private JButton bilgileriniGörButton;
    private JButton logDosyalarınaBakButton;
    private JLabel current_user_name;
    private JPanel panel1;

    admin_page(Kullanici current_user)
    {
        setContentPane(panel1);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(550,300));

        çıkışYapButton.addActionListener(_->
        {
            new login_register();
            dispose();
        });

        current_user_name.setText("Hoşgeldin " + current_user.getKullanici_adi());

        bildirimlerButton.addActionListener(_->new bildrimler(current_user.getBildirimler()));

        Kullanici_islemleri.get_the_map().forEach((user_name, id)->
        {
            select_user.addItem(user_name);
        });

        bilgileriniGörButton.addActionListener(_->
        {
            Kullanici temp = Kullanici_islemleri.get_kullanici((String) select_user.getSelectedItem());
            if(temp == null)
            {
                new dondu("Kullanıcı getirilirken bir sorun oluştu!");
                return;
            }
            new user_information(temp,current_user);
        });

        logDosyalarınaBakButton.addActionListener(_->
        {
            if (Desktop.isDesktopSupported())
            {
                try
                {
                    String working_dir = System.getProperty("user.dir") + "\\files\\";
                    File file1 = new File(working_dir + "takım_üyesi_belirleme_log.txt");
                    File file2 = new File(working_dir + "dosya_paylaşımı_log.txt");
                    File file3 = new File(working_dir + "şifre_değiştirme_istekleri_log.txt");
                    File file4 = new File(working_dir + "şifre_değiştirme_onayları_log.txt");
                    File file5 = new File(working_dir + "giriş_işlemleri_log.txt");

                    if (file1.exists()) {
                        Desktop.getDesktop().open(file1);
                    } else {
                        System.out.println(file1.getName() + " does not exist.");
                    }

                    if (file2.exists()) {
                        Desktop.getDesktop().open(file2);
                    } else {
                        System.out.println(file2.getName() + " does not exist.");
                    }

                    if (file3.exists()) {
                        Desktop.getDesktop().open(file3);
                    } else {
                        System.out.println(file3.getName() + " does not exist.");
                    }

                    if (file4.exists()) {
                        Desktop.getDesktop().open(file4);
                    } else {
                        System.out.println(file4.getName() + " does not exist.");
                    }

                    if (file5.exists()) {
                        Desktop.getDesktop().open(file5);
                    } else {
                        System.out.println(file5.getName() + " does not exist.");
                    }
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
