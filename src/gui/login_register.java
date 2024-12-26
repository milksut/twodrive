package gui;

import entities.Kullanici;

import javax.swing.*;
import java.awt.*;

public class login_register extends JFrame
{
    private JTextField user_nameTextField;
    private JPanel panel1;
    private JPasswordField passwordPasswordField;
    private JButton register;
    private JButton login;

    public login_register()
    {
        setContentPane(panel1);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(250,150));

        register.addActionListener(_ ->
        {
            //TODO:
        });

        login.addActionListener(_->{
            //TODO:
        });
    }

}
