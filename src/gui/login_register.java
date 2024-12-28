package gui;

import entities.Kullanici;
import islemler.Kullanici_islemleri;
import islemler.encoder;

import javax.swing.*;
import java.awt.*;

public class login_register extends JFrame
{
    private JTextField user_nameTextField;
    private JPanel panel1;
    private JPasswordField passwordPasswordField;
    private JButton register;
    private JButton login;
    private JLabel output;
    private Kullanici kullanici;

    public login_register()
    {
        setContentPane(panel1);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(550,350));

        register.addActionListener(_ ->
        {
            output.setText("");

            String user_name = user_nameTextField.getText().trim();
            String password = new String(passwordPasswordField.getPassword()).trim();

            if (user_name.isEmpty() || password.isEmpty()) {
                output.setText("Lütfen kullanıcı adı ve şifre giriniz!");
                return;
            }

            if(!Kullanici_islemleri.is_username_unique(user_name))
            {
                output.setText("Bu Kullanıcı Adı Çoktan Alınmış!");
                return;
            }
            if(!Kullanici_islemleri.kullanici_kaydet(new Kullanici(user_name,password,false),true))
            {
                output.setText("Kullanıcı oluşturulurken bir hata oluştu!");
                return;
            }
            output.setText("<html>Kullanıcı başarıyla oluşturuldu!<br>Lütfen Giriş yapınız.</html>");
        });

        login.addActionListener(_->
        {
            output.setText("");

            String user_name = user_nameTextField.getText().trim();
            String password = new String(passwordPasswordField.getPassword()).trim();

            if (user_name.isEmpty() || password.isEmpty()) {
                output.setText("Lütfen kullanıcı adı ve şifre giriniz!");
                return;
            }

            if(Kullanici_islemleri.is_username_unique(user_name))
            {
                output.setText("Kullanıcı adı veya şifre yanlış!");
                return;
            }

            kullanici = Kullanici_islemleri.get_kullanici(user_name);

            try
            {
                if(kullanici==null || !encoder.matches(password,kullanici.getSifre(),kullanici.getTuz()))
                {
                    output.setText("Kullanıcı adı veya şifre yanlış!");
                    return;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            output.setText("Giriş Başarılı! Yönlendriliyor...");

            new user_page(kullanici);

            dispose();

        });
    }

}
