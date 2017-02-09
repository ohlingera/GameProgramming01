package JavaPaint.paintApp;

import JavaPaint.paintUtil.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * Paint is the main driver class for the Java Paint application.  Java Paint lets users select among 4 drawing tools and 4
 * different colors to draw on the canvas.
 * 
 * @author Al Ohlinger with starter code from the text by Timothy Wright
 */
public class Paint extends JFrame implements Runnable {
   
    private BufferStrategy bs; // Used to help run the game
    private volatile boolean running; // If the game is running or not
    private Thread gameThread; // The main thread for the game
    private Canvas canvas; // The canvas the user draws on
    private RelativeMouseInput mouse; // The mouse used to draw with
    private KeyboardInput keyboard; // Get input from the keyboard
    private Graphics g; // The graphics component used to render the drawing to the screen
    private int colorIndex = 0; // Which color we're currently using represented as an int
    private Color color; // Which color we're currently using
    private final Color[] COLORS = {Color.BLUE, Color.RED, Color.GREEN, Color.BLACK}; // Array of all colors user can draw with
    private final MyButton[] buttons = new MyButton[8]; // Array of buttons used to switch between drawing tools and colors
    private int currentTool = 0; // The drawing tool the user is currently using
    private boolean currentlyDrawing = false; // Whether or not the user is currently drawing something on the canvas
    private final DrawLine drawLine; // The main class of the line drawing tool
    private final DrawRect drawRect; // The main class of the rectangle drawing tool
    private final DrawPolyLine drawPolyLine; // The main class of the PolyLine drawing tool
    private final FreeDraw freeDraw; // The main class of the free drawing tool
   
    /**
     * Constructor, creates instances of the 4 different drawing tools.
     */
    public Paint() {
        drawLine = new DrawLine();
        drawRect = new DrawRect();
        drawPolyLine = new DrawPolyLine();
        freeDraw = new FreeDraw();  
    }
   
    /**
     * In this method, we set up everything we'll need to run the application, including, the canvas, panel, buttons, mouse and
     * keyboard listeners, and just the GUI in general.  This is only called once and isn't where we run our game.
     */
    protected void createAndShowGUI() {
        // Set up the canvas
        canvas = new Canvas();
        canvas.setSize(1280, 720);
        canvas.setBackground(Color.WHITE);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas);

        // Set up the panel to hold the buttons
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new FlowLayout());
        selectionPanel.setPreferredSize(new Dimension(50, 370));
        getContentPane().add(selectionPanel, BorderLayout.WEST);

        // Set up GUI
        setTitle("Java Paint");
        setIgnoreRepaint(true);
        pack();
        setVisible(true);

        // Add key listeners
        keyboard = new KeyboardInput();
        canvas.addKeyListener(keyboard);
        
        // Add mouse listeners
        mouse = new RelativeMouseInput(getContentPane());
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addMouseWheelListener(mouse);
        selectionPanel.addMouseListener(mouse);
        selectionPanel.addMouseMotionListener(mouse);
        selectionPanel.addMouseWheelListener(mouse);
        
        // We'll create a custom cursor by drawing on the canvas, but we still need the regular cursor for the panel with the buttons.
        disableCursor();
        selectionPanel.setCursor(Cursor.getDefaultCursor());
      
        // Create the buttons to switch tools and colors
        for (int i = 0; i < 8; i++) {
            // The buttons have a custom image on them which is made in the MyButton class and added here.
            MyButton button = new MyButton("", i);
            button.setPreferredSize(new Dimension(40, 40));
            selectionPanel.add(button);
            buttons[i] = button;

            // Add the actions to the buttons
            final int buttonIndex = i;
            button.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) { 
                    if (buttonIndex < 4) {
                        currentTool = buttonIndex;
                    }
                    else {
                        colorIndex = buttonIndex - 4;
                    }
                } 
            } );
        }
      
        // More setting up
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        canvas.requestFocus();

        gameThread = new Thread(this);
        gameThread.start();
   }
   
    /**
     * This method is unchanged other than taking out the frameRate line since we didn't use that.
     */
    @Override
    public void run() {
        running = true;
        while( running ) {
           gameLoop();
        }
    }
    
    /**
     * This method is unchanged other than switching the order of the calls to renderFrame and processInput.
     */
    private void gameLoop() {
        renderFrame();
        processInput();
        sleep( 10L );
    }

    /**
     * This method is unchanged other than declaring Graphics g as a global variable and not passing it to the render method.
     */
    private void renderFrame() {
        do {
            do {
                g = null;
                try {
                    g = bs.getDrawGraphics();
                    g.clearRect(0, 0, getWidth(), getHeight());
                    render();
                } finally {
                    if(g != null) {
                       g.dispose();
                    }
                }
            } while(bs.contentsRestored());
            bs.show();
        } while(bs.contentsLost());
    }
   
    /**
     * This method is unchanged.
     * 
     * @param sleep 
     */
    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch(InterruptedException ex) { }
    }
   
    /**
     * In this method we get the user input from both the keyboard and the mouse.
     */
    private void processInput() {
        //Poll the keyboard and mouse
        keyboard.poll();
        mouse.poll();
     
        /* If we're not in the middle of drawing something and the user scrolls down with the mouse wheel, we cycle through the
           available colors */
        if (!currentlyDrawing && mouse.getNotches() >= 0) {
            colorIndex += mouse.getNotches();
            colorIndex = Math.abs(colorIndex % 4);
            color = COLORS[colorIndex];
            g.setColor(color);
        }
        
        /* If we're not in the middle of drawing something and the user scholls up with the mouse wheel, we cycle through the drawing
           tools */
        if (!currentlyDrawing && mouse.getNotches() <= 0) {
            currentTool -= mouse.getNotches();
            currentTool = Math.abs(currentTool % 4);
        }
        
        // Depending on the tool selected, we process any clicking with the mouse buttons in the appropriate tool's class
        if (currentTool == 0) {
            // We also set whether we're currently drawing with that tool
            currentlyDrawing = drawLine.processInput(mouse, color);
        }
        else if (currentTool == 1) {
            currentlyDrawing = drawRect.processInput(mouse, color);
        }
        else if (currentTool == 2) {
            currentlyDrawing = drawPolyLine.processInput(mouse, color);
        }
        else {
            currentlyDrawing = freeDraw.processInput(mouse, color);
        }
        
        // If the user presses C, we clear all lines from all tools and set all currently drawing checkers to false
        if (keyboard.keyDownOnce(KeyEvent.VK_C)) {
            drawLine.lines.clear();
            drawLine.isSecondPoint = false;
            freeDraw.lines.clear();
            freeDraw.drawingLine = false;
            drawPolyLine.lines.clear();
            drawPolyLine.drawingLine = false;
            drawRect.rects.clear();
            drawRect.isSecondPoint = false;
            currentlyDrawing = false;
        }
    }
    /**
     * In this method, we draw everything that's volatile to the screen.
     */
    private void render() {
        // Call the render method for all tools and draw their lines/shapes to the screen.
        freeDraw.render(g, mouse);
        drawLine.render(g, mouse, color);
        drawRect.render(g, mouse, color);
        drawPolyLine.render(g, mouse, color);
        
        // Clear the currently selected buttons
        for (int i = 0; i < 8; i++) {
            buttons[i].getModel().setPressed(false);
            buttons[i].setBackground(null);
        }
        /* Set the currently selected color and tool buttons to be pressed. Also changed the background to make it clear they're
           pressed. */
        buttons[colorIndex + 4].getModel().setPressed(true);
        buttons[colorIndex + 4].setBackground(Color.CYAN);
        buttons[currentTool].getModel().setPressed(true);
        buttons[currentTool].setBackground(Color.CYAN);
        
        // Draw the custom crosshair cursor to the screen at the mouse's position.
        g.setColor(color);
        g.drawLine(mouse.getPosition().x - 10, mouse.getPosition().y, mouse.getPosition().x + 10, mouse.getPosition().y);
        g.drawLine(mouse.getPosition().x, mouse.getPosition().y - 10, mouse.getPosition().x, mouse.getPosition().y + 10);
    }
   
    /**
     * This method is unchanged.
     */
    private void disableCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0,0);
        String name = "CanBeAnything";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }
   
    /**
     * This method is unchanged.
     */
    protected void onWindowClosing() {
        try {
            running = false;
            gameThread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
   
    /**
     * This method is unchanged.
     * 
     * @param args
     */
    public static void main(String[] args) {
        final Paint app = new Paint();
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                app.onWindowClosing();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app.createAndShowGUI();
            }
        });
    }
}