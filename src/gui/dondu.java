package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class dondu extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel message;

    public dondu(String messaj)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        message.setText(messaj);

        pack();
        setVisible(true);
    }

    private void onOK() {
        // add your code here
        dispose();
    }
}
