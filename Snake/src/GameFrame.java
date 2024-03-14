import javax.swing.JFrame;

public class GameFrame extends JFrame {
    
    GameFrame(){
    	
        this.add(new GamePanel()); // Add an instance of GamePanel to the frame.
        this.setTitle("Snake"); // Set the title of the window to "Snake".
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation to exit.
        this.setResizable(false); // Make the window non-resizable.
        this.pack(); // Cause the window to be sized to fit the preferred size and layouts of its subcomponents.
        this.setVisible(true); // Make the window visible.
        this.setLocationRelativeTo(null); // Center the window on the screen.
        
    }
    
}
