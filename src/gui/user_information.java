package gui;

import entities.Kullanici;
import islemler.Kullanici_islemleri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class user_information extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel User_name;
    private JLabel password_show;
    private JSpinner file_size;
    private JButton değiştirButton;
    private JButton button;
    private JButton dosyalarıGörButton;
    private JButton şifresiniDeğiştirmesineIzinVerButton;

    public user_information(Kullanici user,Kullanici admin)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        User_name.setText(user.getKullanici_adi());

        password_show.setText(user.getSifre());

        if(admin.getTakim_uyeleri().contains(user.getId()))
        {
            şifresiniDeğiştirmesineIzinVerButton.addActionListener(_->
            {
                user.setChanging_password(true);
                user.getBildirimler().add("Admin şifre değiştirme isteğinizi kabul etti!" + " Tarih: " + LocalDateTime.now());
                admin.getTakim_uyeleri().remove(user.getId());

                Kullanici_islemleri.kullanici_kaydet(user,false);
                Kullanici_islemleri.kullanici_kaydet(admin,false);

                new dondu(user.getKullanici_adi()+" adlı kullanıcının şifre değiştirme isteği Kabul edildi!");
                şifresiniDeğiştirmesineIzinVerButton.setEnabled(false);
            });
        }
        else{şifresiniDeğiştirmesineIzinVerButton.setEnabled(false);}

        file_size.setValue(user.getMax_file_size());

        değiştirButton.addActionListener(_->
        {
            double x = (double) file_size.getValue();
            double target_size = Kullanici_islemleri.get_user_folder_size(user);

            if(x<target_size)
            {
                new dondu(String.format("Kullanıcının maksimum dosya boyutunu şuanda kullanılandan daha küçük yapamassınız! \n" +
                        "Kullanııcnın şu anki dosya boyutu: %.2f MB",target_size));
                return;
            }

            user.setMax_file_size(x);
            user.getBildirimler().add(String.format("Maksimum dosya boyutunuz admin tarafından  %.2f MB yapıldı! \n" +
                    "Kalan yeriniz:  %.2f MB Tarih: " + LocalDateTime.now(),x,x-target_size));

            Kullanici_islemleri.kullanici_kaydet(user,false);

            new dondu(String.format(user.getKullanici_adi() + " adlı kullanıcının maksimum dosya boyutu %.2f MB olarak değiştirildi!",x));
        });

        button.addActionListener(_->
        {
            new bildrimler(user.getPaylasilan_dosyalar().stream().map(x->
            {
                return "Gönderen :" + Kullanici_islemleri.get_from_map(x.getPaylasimci()) +
                        "<br> Alan :" + Kullanici_islemleri.get_from_map(x.getAlici()) +
                        "<br> Dosya ismi :" + x.getPaylasilan_dosya() + "<br>";
            }).collect(Collectors.toList()));
        });

        dosyalarıGörButton.addActionListener(_->
        {
            new bildrimler(user.getDosyalar());
        });

        pack();
        setVisible(true);
        setMinimumSize(new Dimension(500,300));
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void createUIComponents() {
       file_size = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    }
}
