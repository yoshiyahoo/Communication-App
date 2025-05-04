
import java.awt.*;
import javax.swing.*;

public class GUITools 
{
 //Should setup colors automatically for GUI objects.
    public static void ColorGUIComponents(Object object)
    {
         //Colors
         Color backgroundColor = new Color(30, 30, 30);
         Color textFieldColor = new Color(50, 50, 50);
         Color borderColor = new Color(70, 70, 70);
 
        if (object instanceof JTextField) 
        {
            JTextField field = (JTextField) object;
            field.setBackground(textFieldColor);
            field.setForeground(Color.WHITE);
            field.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
            field.setCaretColor(Color.WHITE);
        }
        else if(object instanceof JButton)
        {
            JButton button = (JButton) object;
            button.setBackground(Color.gray);
            button.setForeground(Color.WHITE);
            
            button.addMouseListener(new java.awt.event.MouseAdapter() 
            {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.white);
                button.setForeground(Color.black);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) 
                {
                    button.setBackground(Color.gray);
                    button.setForeground(Color.WHITE);
                }
                });
        }
        else if(object instanceof JPanel)
        {
            JPanel jpanel = (JPanel) object;
            jpanel.setBackground(backgroundColor);
        }
        else if(object instanceof JLabel)
        {
            JLabel jLabel = (JLabel) object;
            jLabel.setForeground(Color.WHITE);
        }
        else if(object instanceof JTextArea)
        {
            JTextArea jTextArea = (JTextArea) object;
            jTextArea.setBackground(textFieldColor);
            jTextArea.setForeground(Color.WHITE);
            jTextArea.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
            jTextArea.setCaretColor(Color.WHITE);
        }
        else if(object instanceof JList)
        {
            JList jList = (JList) object;
            jList.setBackground(textFieldColor);
            jList.setForeground(Color.WHITE);
            jList.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        }
        else
        {
            System.out.println("(" + object.getClass().getSimpleName() + ") Object not supported by auto-coloring.");
        }



    }
    
}
