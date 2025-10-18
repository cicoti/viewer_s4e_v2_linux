package com.s4etech.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MensagemUtils {

    private static Window resolveOwner(Component source) {
        // Garante que temos a janela top-level (JFrame/JDialog) como owner
        return source != null ? SwingUtilities.getWindowAncestor(source) : null;
    }

    /** Centraliza o dialog no MESMO monitor do componente origem. */
    private static void showDialog(Component source, String title, String message,
                                   int messageType, int optionType, Runnable afterShow) {
        JOptionPane pane = new JOptionPane(message, messageType, optionType);
        Window owner = resolveOwner(source);
        JDialog dialog = pane.createDialog(owner, title);
        dialog.setModal(true);

        // Pega a tela (monitor) do componente origem — se null, cai no owner
        Component ref = (source != null ? source :
                        owner != null ? owner : dialog);

        // Calcula o centro no bounds do monitor do componente de referência
        Rectangle screen = ref.getGraphicsConfiguration().getBounds();
        dialog.pack();
        Dimension d = dialog.getSize();
        int x = screen.x + (screen.width  - d.width)  / 2;
        int y = screen.y + (screen.height - d.height) / 2;
        dialog.setLocation(x, y);

        dialog.setAlwaysOnTop(true); // ajuda a não ficar “perdido” atrás
        dialog.setVisible(true);

        if (afterShow != null) afterShow.run();
    }

    public static void showInfo(Component source, String message, String title) {
        showDialog(source, title, message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null);
    }

    public static void showError(Component source, String message, String title) {
        showDialog(source, title, message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null);
    }

    public static void showWarning(Component source, String message, String title) {
        showDialog(source, title, message, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null);
    }

    public static boolean showConfirm(Component source, String message, String title) {
        final boolean[] result = new boolean[1];
        JOptionPane pane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        Window owner = resolveOwner(source);
        JDialog dialog = pane.createDialog(owner, title);

        Component ref = (source != null ? source :
                        owner != null ? owner : dialog);

        Rectangle screen = ref.getGraphicsConfiguration().getBounds();
        dialog.pack();
        Dimension d = dialog.getSize();
        int x = screen.x + (screen.width  - d.width)  / 2;
        int y = screen.y + (screen.height - d.height) / 2;
        dialog.setLocation(x, y);

        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setVisible(true);

        Object value = pane.getValue();
        result[0] = (value != null && (int) value == JOptionPane.YES_OPTION);
        return result[0];
    }
}
