package gui;

import entities.Kullanici;
import islemler.Kullanici_islemleri;

import javax.swing.*;
import java.awt.*;

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
    private JTextField textField1;
    private JComboBox team_mates;
    private JComboBox uploaded_files;
    private JButton notifications;
    private JComboBox uploaded_files_share;
    private JButton dosyayıPaylaşButton;
    private JPanel panel1;
    private JButton indirButton;

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

        //TODO:
    }
}