package Main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Simple Chess");
        //program stops upon termination
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window cannot be resized
        window.setResizable(false);

        //Add GamePanel to the window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();

        //frame displayed in the centre
        window.setLocationRelativeTo(null);
        //so the window is visible
        window.setVisible(true);

        gp.launchGame();
    }
}
