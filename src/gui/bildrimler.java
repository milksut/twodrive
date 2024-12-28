package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class bildrimler extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JScrollPane notifications;
    private JList notifications_list;

    public bildrimler(List<String> kullanici_bildirimler) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String notification : kullanici_bildirimler)
        {
            listModel.addElement("<html>" + notification.replace("\n","<br>") + "</html>");
        }

        notifications_list.setModel(listModel);

        buttonOK.addActionListener(_-> onOK());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        contentPane.registerKeyboardAction(_-> onOK()
                , KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setVisible(true);
    }

    private void onOK() {
        dispose();
    }

}
