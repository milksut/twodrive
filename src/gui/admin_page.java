package gui;

import entities.Kullanici;
import islemler.Kullanici_islemleri;

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
                    Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\files\\Logs.txt"));
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
