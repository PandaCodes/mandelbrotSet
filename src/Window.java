import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window extends MouseAdapter {
    private Graphics2D g2d;
    private Dimension size;
    private BufferedImage currentStage;
    private Mondelbrot mondelbrot;
    private double x = -2;
    private double y = -2;
    private double d;
    private int loop = 10;
    private Point old = new Point();
    private int multiplier = 10;

    Window(Dimension screenSize) {
        size = screenSize;
        JPanel window = new JPanel();
        window.setFocusable(true);
        window.setSize(screenSize);
        JFrame frame = new JFrame();
        frame.setFocusable(true);
        frame.setSize(screenSize);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(window);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        g2d = (Graphics2D) window.getGraphics();
        window.addMouseMotionListener(this);
        window.addMouseListener(this);
        window.addMouseWheelListener(this);
        currentStage = new BufferedImage(size.width, size.height,BufferedImage.TYPE_INT_RGB);
        mondelbrot = new Mondelbrot( size );
        d = 4.0/size.height;
    }

    public void draw (int m[][]) {
        Graphics2D  bg2d = (Graphics2D)currentStage.getGraphics();
        //double r, g, b;
        bg2d.fillRect(0,0,size.width, size.height);
        for (int i = 0; i < size.width; i++ )
            for (int j = 0; j < size.height; j++) {
                //if (m[i][j] < 600) {
                    /*g = m[i][j]*m[i][j]*256.0/1000000;
                    b = m[i][j]*m[i][j]*m[i][j]*256.0/1000000000;
                    r = m[i][j]*256.0/1000;
                    /*System.out.print(r);
                    System.out.print(" ");
                    System.out.print(g);
                    System.out.print(" ");
                    System.out.println(b); */
                    int n = m[i][j];
                    Color color =
                        new Color((float)(0.1098+(n*0.00329)*0.45),
                                (float)(0.18824 + (n*0.00329)*0.6166),
                                (float)(0.29412+(n*0.00329)*0.3));
                    bg2d.setColor(color);//16777215 - белый
                    bg2d.drawLine(i,j,i,j);
                //}
            }
        g2d.drawImage(currentStage, null, 0, 0);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        g2d.drawImage(currentStage, null, 0, 0);
        int h = size.height/loop;
        int w = size.width/loop;
        g2d.drawRect(e.getX() - h/2, e.getY()- w/2, h, w);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        old = e.getPoint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        multiplier += e.getWheelRotation();
        draw(mondelbrot.lastMatrix());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {

            String fName;
            File file = null;
            int i = 0;
            do {
                fName = "image" + i + ".bmp";
                file = new File(fName);
                i++;
            } while (file.exists());

            try
            {
                ImageIO.write(currentStage, "png", new File(fName));
                System.out.println("Success!");
            }

            catch (IOException ex)
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            return;
        }
        if (old.x == e.getX() && old.y == e.getY()) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                x = x + (e.getX() - size.width/loop/2)*d;
                y = y + (e.getY() - size.height/loop/2)*d;
                d = d/loop;

            } else if (e.getButton() == MouseEvent.BUTTON3)
            {
                d = d*loop;
                x = x - (size.width - size.width/loop)*d/2;
                y = y - (size.height - size.height/loop)*d/2;
            }
        } else {
            x -= (e.getX() - old.getX())*d;
            y -= (e.getY() - old.getY())*d;
        }
        System.out.print("click\n");
        draw(mondelbrot.createMatrix(x,y, d));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public static void main(String[] args) {
        Dimension size = new Dimension(500, 600);
        Window wnd = new Window( size );

        /*for (int i = 1; i<2000; i++)
        {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }   */
        wnd.draw(wnd.mondelbrot.createMatrix(wnd.x,wnd.y, wnd.d));
    }
}
