import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class GUITools
{
    // Should setup colors automatically for GUI objects.
    public static void ColorGUIComponents(Object object)
    {
        // Colors
        Color backgroundColor = new Color(30, 30, 30);
        Color textFieldColor = new Color(50, 50, 50);
        Color borderColor = new Color(70, 70, 70);

        if (object instanceof JTextField)
        {
            JTextField field = (JTextField) object;
            field.setBackground(textFieldColor);
            field.setForeground(Color.WHITE);
            Border originalBorder = field.getBorder();

            if (originalBorder instanceof javax.swing.border.TitledBorder)
            {
                javax.swing.border.TitledBorder titled = (javax.swing.border.TitledBorder) originalBorder;
                titled.setTitleColor(Color.WHITE);
            }
            else if (originalBorder == null)
            {
                field.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
            }

            field.setCaretColor(Color.WHITE);
        }
        else if (object instanceof JButton)
        {
            JButton button = (JButton) object;
            button.setBackground(Color.GRAY);
            button.setForeground(Color.WHITE);

            button.addMouseListener(new java.awt.event.MouseAdapter()
            {
                public void mouseEntered(java.awt.event.MouseEvent evt)
                {
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                }

                public void mouseExited(java.awt.event.MouseEvent evt)
                {
                    button.setBackground(Color.GRAY);
                    button.setForeground(Color.WHITE);
                }
            });
        }
        else if (object instanceof JPanel)
        {
            JPanel jpanel = (JPanel) object;
            jpanel.setBackground(backgroundColor);
            applyToAllChildren(jpanel);
        }
        else if (object instanceof JLabel)
        {
            JLabel jLabel = (JLabel) object;
            jLabel.setForeground(Color.WHITE);
        }
        else if (object instanceof JTextArea)
        {
            JTextArea jTextArea = (JTextArea) object;
            jTextArea.setBackground(textFieldColor);
            jTextArea.setForeground(Color.WHITE);

            if (jTextArea.getBorder() == null)
            {
                jTextArea.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
            }

            jTextArea.setCaretColor(Color.WHITE);
        }
        else if (object instanceof JList)
        {
            JList<?> jList = (JList<?>) object;
            jList.setBackground(textFieldColor);
            jList.setForeground(Color.WHITE);

            if (jList.getBorder() == null)
            {
                jList.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
            }
        }
        else if (object instanceof JDialog)
        {
            JDialog dialog = (JDialog) object;
            Container content = dialog.getContentPane();
            content.setBackground(backgroundColor);
            applyToAllChildren(content);
        }
        else if (object instanceof JScrollPane)
        {
            JScrollPane scroll = (JScrollPane) object;
            scroll.setBackground(backgroundColor);
            Component view = scroll.getViewport().getView();

            if (view != null)
            {
                ColorGUIComponents(view);
            }
        }
        else
        {
            System.out.println("(" + object.getClass().getSimpleName() + ") Object not supported by auto-coloring.");
        }
    }

    // Helper method to apply color settings to all nested components
    private static void applyToAllChildren(Container container)
    {
        for (Component comp : container.getComponents())
        {
            ColorGUIComponents(comp);

            if (comp instanceof Container)
            {
                applyToAllChildren((Container) comp);
            }
        }
    }
}
