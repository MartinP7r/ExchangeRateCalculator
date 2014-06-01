package firstTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Swing03 implements ActionListener
{
  public static void main(String args[])
  {
    Swing03 app01 = new Swing03();
  }

  public Swing03()
  {
    JMenuBar menuBar01 = new JMenuBar();

    JMenu menu01 = new JMenu("File");

    JMenuItem tempItem = new JMenuItem("New");
    tempItem.addActionListener(this);
    menu01.add(tempItem);
    menu01.addSeparator();

    tempItem = new JMenuItem("Quit");
    tempItem.addActionListener(this);
    menu01.add(tempItem);

    menuBar01.add(menu01);

    JFrame frame01 = new JFrame();
    frame01.setTitle("Playing With Menus");
    frame01.setSize(400,400);
    frame01.setLocation(300,100);
    frame01.setJMenuBar(menuBar01);
   
    frame01.pack();
    frame01.setVisible(true);
  }

    public void actionPerformed(ActionEvent e)
    {
      if (e.getActionCommand() == "Quit")
        System.exit(0);
    }
}