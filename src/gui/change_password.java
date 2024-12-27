package gui;

import entities.Kullanici;
import islemler.Kullanici_islemleri;

import javax.swing.*;
import java.awt.event.*;

public class change_password extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextField new_password;
    private JLabel output;
    private final Kullanici current_user;

    public change_password(Kullanici current_user)
    {
        this.current_user = current_user;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(_->onOK());


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });


        contentPane.registerKeyboardAction(_->onOK(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setVisible(true);
    }

    private void onOK() {
        String new_password_text = new_password.getText().trim();
        output.setText("");
        if(new_password_text.isEmpty())
        {
            output.setText("Lütfen geçerli bir şifre girin!");
            return;
        }
        else
        {
            output.setText("şifreniz değiştiriliyor");
            current_user.setSifre(new_password_text);
            current_user.setChanging_password(false);
            Kullanici_islemleri.kullanici_kaydet(current_user,false);
            dispose();
        }
    }
}
