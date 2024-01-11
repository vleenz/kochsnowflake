
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.event.*;

/**
 * @author Vincent Lee
 */
public class App extends JPanel implements MouseWheelListener {
    private int order = 0;
    private static double scaleFactor = 1.0;
    private Point2D mousePt;
    private Point2D.Double shapePosition = new Point2D.Double(400, 400);
    private int panelWidth;
    private int panelHeight;
    private int invert = 1;
    private int full = 1;

    public App() {

        shapePosition.x = panelWidth / 2;
        shapePosition.y = panelHeight / 2;
        // adds a mouseWheelListener
        addMouseWheelListener(this);

        // adds an orderlist
        String[] orders = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        JComboBox<String> orderList = new JComboBox<>(orders);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Order: "));
        panel.add(orderList);
        orderList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                order = Integer.parseInt((String) orderList.getSelectedItem());
                order = order - 1;
                repaint();
            }
        });

        add(panel);

        // adds a button to reset the zoom level
        JButton resetButton = new JButton("Reset Zoom level");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaleFactor = 1;
                repaint();
            }
        });
        add(resetButton);

        // adds a button to reset the whole snowflake
        JButton resetButtonFull = new JButton("Reset all");
        resetButtonFull.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                order = 0;
                shapePosition.x = getWidth() / 2;
                shapePosition.y = getHeight() / 2;
                scaleFactor = 1;
                invert = 1;
                full = 1;
                repaint();
            }
        });
        add(resetButtonFull);

        // adds a button to draw the snowflake inverted
        JButton invertButton = new JButton("Invert");
        invertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                invert = invert * -1;
                repaint();
            }
        });
        add(invertButton);

        // adds a button to draw the snowflake both inverted and normal
        JButton crazyButton = new JButton("Inverted Full");
        crazyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                full = full * -1;
                repaint();
            }
        });
        add(crazyButton);

        // gets the point where the mouse is clicked and dragged
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                mousePt = null;

            }

            public void mouseDragged(MouseEvent e) {
                mousePt = e.getPoint();
            }
        });

        // using the point from where the mouse is clicked and dragged, set the shape
        // position by calculating the difference between the intial mouse clicked point
        // and the position where it is dragged,
        // and shifting the position of the snowflake by that amount
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point2D p = e.getPoint();
                double dx = p.getX() - mousePt.getX();
                double dy = p.getY() - mousePt.getY();
                shapePosition.setLocation(shapePosition.getX() + dx, shapePosition.getY() + dy);
                mousePt = p;
                repaint();
            }
        });

        // used to adjust the position of the snowflake when the size of the window is
        // changed. This is done by calculating the difference between the old and new
        // dimensions of the window, and shifting the position of the snowflake by that
        // amount.
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int prevPanelWidth = panelWidth;
                int prevPanelHeight = panelHeight;
                panelWidth = getWidth();
                panelHeight = getHeight();

                double dx = (panelWidth - prevPanelWidth) / 2.0;
                double dy = (panelHeight - prevPanelHeight) / 2.0;

                shapePosition.setLocation(shapePosition.getX() + dx, shapePosition.getY()
                        + dy);

                repaint();
            }
        });

    }

    /**
     * Paint component where the snowflake is drawn on to the panel
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Redraws the snowflake according to panel size
        // Calculates the difference in width and height between the current panel
        // size and the previous panel size
        double dx = (getWidth() - panelWidth) / 2.0;
        double dy = (getHeight() - panelHeight) / 2.0;

        // Shifts the position of the snowflake by the difference in width and height
        // between the current and previous panel sizes
        shapePosition.setLocation(shapePosition.getX() + dx, shapePosition.getY() + dy);

        // Updates the panel width and height to the current panel size
        panelWidth = getWidth();
        panelHeight = getHeight();

        // Calculates the length of a side of the equilateral triangle,
        // which is proportional to the average of
        // the panel width and height, and is scaled by the current zoom level
        double sideLength = ((getWidth() + getHeight()) / 4) * scaleFactor;

        // Finds the points to draw the 3 lines of the equilateral triangle
        double x1 = (shapePosition.x - sideLength / 2);
        double y1 = (shapePosition.y + sideLength / 2);
        double x2 = (shapePosition.x + sideLength / 2);
        double y2 = (shapePosition.y + sideLength / 2);
        double x3 = (x1 + sideLength * Math.cos(Math.PI / 3));
        double y3 = (y1 + sideLength * Math.sin(Math.PI / 3 * -1));

        // calls the splitline method for each of the lines.
        splitLine(g2d, order, x1, y1, x2, y2, invert);
        splitLine(g2d, order, x2, y2, x3, y3, invert);
        splitLine(g2d, order, x3, y3, x1, y1, invert);

        // if the full flag is set to -1, call the split line method again for each of
        // the lines, with the negative angles too.
        if (full == -1) {
            splitLine(g2d, order, x1, y1, x2, y2, -invert);
            splitLine(g2d, order, x2, y2, x3, y3, -invert);
            splitLine(g2d, order, x3, y3, x1, y1, -invert);
        }

    }

    /**
     * Recursive method that splits a line in to 4 new line segments.
     * 
     * @param g      the Graphics component
     * @param order  the order of recursion
     * @param x1     starting x coordinate of a line
     * @param y1     starting y coordinate of a line
     * @param x2     finishing x coordinate of a line
     * @param y2     finishing y coordinate of a line
     * @param invert a flag to indicate whether to invert the angle or not
     */
    private void splitLine(Graphics g, int order, double x1, double y1, double x2, double y2, int invert) {
        Graphics2D g2d = (Graphics2D) g;
        // Base case: order equals 0. Draw a straight line between (x1, y1) and (x2,
        // y2).
        if (order == 0) {
            g2d.draw(new Line2D.Double(x1, y1, x2, y2));

        }
        // Recursive case: order is greater than 0. Split the line into four segments
        // and call the splitLine() method on each one.
        else {
            // Calculate the distance between (x1, y1) and (x2, y2) using the distance
            // formula.
            double lineDistance = Point2D.distance(x1, y1, x2, y2);
            // Calculate the length of each segment
            double segLength = lineDistance / 3;
            // Calculate the coordinates for the two internal points (x3, y3) and (x4, y4).
            double x3 = x1 + (segLength * (x2 - x1) / lineDistance);
            double y3 = y1 + (segLength * (y2 - y1) / lineDistance);
            double x4 = x1 + (2 * segLength * (x2 - x1) / lineDistance);
            double y4 = y1 + (2 * segLength * (y2 - y1) / lineDistance);

            // Calculate the angle for the third point (x5, y5) using the coordinates of the
            // internal points.
            double angle = Math.atan2(y4 - y3, x4 - x3) + Math.PI / 3 * invert;

            // Calculate the coordinates of the third point (x5, y5) using the angle and the
            // length of the segment.
            double x5 = x3 + segLength * Math.cos(angle);
            double y5 = y3 + segLength * Math.sin(angle);

            // Recursively call the splitLine() method on each segment.
            splitLine(g, order - 1, x1, y1, x3, y3, invert);
            splitLine(g, order - 1, x4, y4, x2, y2, invert);
            splitLine(g, order - 1, x5, y5, x4, y4, invert);
            splitLine(g, order - 1, x3, y3, x5, y5, invert);
        }
    }

    /**
     * Creates a JFrame to adds to it the components created in the App constructor.
     * 
     * @param args Command-line arguments for the application
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Koch Snowflake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 800));
        frame.add(new App());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /**
     * Method that increases or decreases the scaleFactor when the mouse wheel is
     * moved and triggers a repaint of the snowflake
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            scaleFactor *= 1.01;
        }

        if (notches > 0) {
            scaleFactor /= 1.01;
        }

        revalidate();
        repaint();
    }
}
