import java.awt.Color;

import edu.cymbal.graphics.CanvasAPI;
import edu.cymbal.graphics.Graphics;
import edu.cymbal.graphics.PainterAPI;

/**
 * Draws an aquarium with sea turtles, crabs, rocks, seaweed, bubbles
 * and a sandy sea floor.
 *
 * The canvas is 800 wide and 600 tall. (0, 0) is the center.
 * x goes from -400 (left) to 400 (right).
 * y goes from -300 (bottom) to 300 (top).
 */
public class Aquarium {

    // The painter is used by every method, so we store it here.
    static PainterAPI painter;

    // Colors
    static Color WATER        = new Color(160, 210, 235);
    static Color SAND         = new Color(225, 198, 145);
    static Color SAND_DARK    = new Color(185, 155, 100);
    static Color SAND_LIGHT   = new Color(245, 225, 180);
    static Color SHELL_GREEN  = new Color(25, 120, 95);
    static Color FLIPPER      = new Color(60, 175, 120);
    static Color SHELL_BLUE   = new Color(35, 90, 140);
    static Color HEX_BORDER   = new Color(160, 225, 160);
    static Color DARK         = new Color(15, 25, 30);
    static Color ORANGE       = new Color(240, 110, 60);
    static Color TEAL         = new Color(30, 160, 190);
    static Color NAVY         = new Color(25, 45, 90);
    static Color TAN          = new Color(235, 200, 140);
    static Color CREAM        = new Color(250, 245, 235);
    static Color ROCK_LIGHT   = new Color(195, 205, 220);
    static Color ROCK_MID     = new Color(145, 155, 170);
    static Color ROCK_DARK    = new Color(100, 110, 125);
    static Color ROCK_EDGE    = new Color(70, 75, 85);
    static Color SEAWEED      = new Color(40, 140, 70);
    static Color SEAWEED_DARK = new Color(25, 95, 45);
    static Color BUBBLE       = new Color(220, 242, 255);
    static Color BUBBLE_EDGE  = new Color(100, 170, 215);

    public static void main(String[] args) {
        CanvasAPI canvas = Graphics.createCanvas(800, 600, "Aquarium");
        canvas.setBackgroundColor(WATER);
        painter = canvas.getPainter();
        painter.setSpeed(0); // zero means as fast as possible
        painter.setPenWidth(1.5f);

        // Things in the back are drawn first, so things in front cover them.
        drawSeaFloor();

        drawDecoration("seaweed", -340, -280, 55);
        drawDecoration("seaweed", 40, -285, 45);
        drawDecoration("seaweed", 360, -280, 50);
        drawDecoration("rock", -230, -240, 55);
        drawDecoration("rock", 290, -250, 38);
        drawDecoration("rock", -60, -262, 30);

        // angle: 0 = facing straight up,
        // positive = tilted to the left, negative = tilted to the right
        drawTurtle(-150, 60, 70, 15);    // big turtle, tilted left
        drawTurtle(250, 150, 35, -20);   // small turtle, tilted right
        drawCrab(160, -200, 60, -8);     // big crab, tilted a little right
        drawCrab(-120, -190, 35, 12);    // small crab, tilted a little left

        drawDecoration("bubbles", -170, 200, 20);
        drawDecoration("bubbles", -250, -170, 14);
    }

  

    /**
     * Moves the painter to (x, y) without drawing a line.
     */
    public static void jumpTo(double x, double y) {
        painter.liftPen();
        painter.moveTo(x, y);
        painter.lowerPen();
    }

    /**
     * changes the angles by x and y so it's easier to draw creatures at different angles
     * I chose to use math radians and cosine and sine to calculate the new x and y coordinates after rotation
    */
    public static double turnX(double dx, double dy, double angle) {
        double radians = Math.toRadians(angle);
        return dx * Math.cos(radians) - dy * Math.sin(radians);
    }

    public static double turnY(double dx, double dy, double angle) {
        double radians = Math.toRadians(angle);
        return dx * Math.sin(radians) + dy * Math.cos(radians);
    }

    //Draws a filled triangle.//
    public static void drawTriangle(double x, double y, double angle,
                                    double dx1, double dy1,
                                    double dx2, double dy2,
                                    double dx3, double dy3,
                                    Color fill, Color outline) {
        painter.setPenColor(outline);
        jumpTo(x + turnX(dx1, dy1, angle), y + turnY(dx1, dy1, angle));
        painter.startFill(fill);
        painter.moveTo(x + turnX(dx2, dy2, angle), y + turnY(dx2, dy2, angle));
        painter.moveTo(x + turnX(dx3, dy3, angle), y + turnY(dx3, dy3, angle));
        painter.moveTo(x + turnX(dx1, dy1, angle), y + turnY(dx1, dy1, angle));
        painter.finishFill();
    }

    /**
     * Draws a filled rectangle. (x, y) is the bottom-left corner.
     */
    public static void drawRectangle(double x, double y, double width, double height,
                                     Color fill, Color outline) {
        painter.setPenColor(outline);
        jumpTo(x, y);
        painter.startFill(fill);
        painter.moveTo(x + width, y);
        painter.moveTo(x + width, y + height);
        painter.moveTo(x, y + height);
        painter.moveTo(x, y);
        painter.finishFill();
    }

    /**
     * Draws a filled circle with its center at (centerX, centerY).
     * painter.circle() starts drawing from the bottom of the circle,
     * so we jump to the bottom first.
     */
    public static void drawCircle(double centerX, double centerY, double radius,
                                  Color fill, Color outline) {
        painter.setPenColor(outline);
        jumpTo(centerX, centerY - radius);
        painter.startFill(fill);
        painter.circle(radius);
        painter.finishFill();
    }

    /**
     * Draws a filled hexagon (6 sides) with its center at (centerX, centerY).
     * radius is the distance from the center to each corner.
     * The loop visits the 6 corners, which are 60 degrees apart.
     */
    public static void drawHexagon(double centerX, double centerY, double radius,
                                   double angle, Color fill, Color outline) {
        painter.setPenColor(outline);
        jumpTo(centerX + turnX(radius, 0, angle), centerY + turnY(radius, 0, angle));
        painter.startFill(fill);
        for (int i = 1; i <= 6; i++) {
            double cornerAngle = angle + i * 60;
            painter.moveTo(centerX + turnX(radius, 0, cornerAngle),
                           centerY + turnY(radius, 0, cornerAngle));
        }
        painter.finishFill();
    }

    /**
     * Draws a sea turtle seen from above. Every part is placed as an
     * offset from the center of the shell, then turned by angle.
     *
     * @param x     center of the shell
     * @param y     center of the shell
     * @param size  about half the height of the shell
     * @param angle degrees to tilt the turtle (0 = head straight up)
     */
    public static void drawTurtle(double x, double y, double size, double angle) {
        // Flippers: the loop runs twice, once for the right side (side = 1)
        // and once for the left side (side = -1).
        for (int side = 1; side >= -1; side = side - 2) {
            // Front flipper: two light triangles on top, two dark ones underneath
            drawTriangle(x, y, angle,
                         side * 0.6 * size,  0.7 * size,
                         side * 1.45 * size, 1.1 * size,
                         side * 0.7 * size,  0.55 * size, FLIPPER, DARK);
            drawTriangle(x, y, angle,
                         side * 1.45 * size, 1.1 * size,
                         side * 1.6 * size,  0.95 * size,
                         side * 0.7 * size,  0.55 * size, FLIPPER, DARK);
            drawTriangle(x, y, angle,
                         side * 0.7 * size,  0.55 * size,
                         side * 1.6 * size,  0.95 * size,
                         side * 0.8 * size,  0.35 * size, SHELL_GREEN, DARK);
            drawTriangle(x, y, angle,
                         side * 1.6 * size,  0.95 * size,
                         side * 1.2 * size,  0.6 * size,
                         side * 0.8 * size,  0.35 * size, SHELL_GREEN, DARK);

            // Back flipper: two triangles
            drawTriangle(x, y, angle,
                         side * 0.5 * size,  -0.8 * size,
                         side * 0.85 * size, -0.95 * size,
                         side * 1.0 * size,  -1.3 * size, FLIPPER, DARK);
            drawTriangle(x, y, angle,
                         side * 0.5 * size,  -0.8 * size,
                         side * 1.0 * size,  -1.3 * size,
                         side * 0.35 * size, -1.0 * size, FLIPPER, DARK);
        }

        // Tail
        drawTriangle(x, y, angle,
                     -0.12 * size, -1.05 * size,
                     0.12 * size,  -1.05 * size,
                     0,            -1.4 * size, FLIPPER, DARK);

        // Head and eyes
        drawHexagon(x + turnX(0, 1.32 * size, angle), y + turnY(0, 1.32 * size, angle),
                    0.28 * size, angle, FLIPPER, DARK);
        for (int side = 1; side >= -1; side = side - 2) {
            double eyeX = side * 0.12 * size;
            double eyeY = 1.38 * size;
            drawCircle(x + turnX(eyeX, eyeY, angle), y + turnY(eyeX, eyeY, angle),
                       0.05 * size, DARK, DARK);
        }

        // Shell: an oval made of 12 points.
        // Each pair of numbers is one point (right, up) from the center of the shell.
        double[] shellX = {0, 0.55, 0.85, 0.92, 0.78, 0.42, 0, -0.42, -0.78, -0.92, -0.85, -0.55};
        double[] shellY = {1.12, 0.97, 0.55, -0.05, -0.65, -1.0, -1.12, -1.0, -0.65, -0.05, 0.55, 0.97};
        painter.setPenColor(DARK);
        jumpTo(x + turnX(0, 1.12 * size, angle), y + turnY(0, 1.12 * size, angle));
        painter.startFill(SHELL_GREEN);
        for (int i = 1; i <= shellX.length; i++) {
            int p = i % shellX.length; // the last step goes back to point 0 to close the oval
            double dx = shellX[p] * size;
            double dy = shellY[p] * size;
            painter.moveTo(x + turnX(dx, dy, angle), y + turnY(dx, dy, angle));
        }
        painter.finishFill();

        // Hexagons on the shell: a column of 3 down the middle and 2 on each side.
        double hex = 0.27 * size;
        for (int i = -1; i <= 1; i++) {
            double dy = i * 0.52 * size;
            drawHexagon(x + turnX(0, dy, angle), y + turnY(0, dy, angle),
                        hex, angle, SHELL_BLUE, HEX_BORDER);
        }
        for (int side = 1; side >= -1; side = side - 2) {
            for (int j = 1; j >= -1; j = j - 2) {
                double dx = side * 0.45 * size;
                double dy = j * 0.26 * size;
                drawHexagon(x + turnX(dx, dy, angle), y + turnY(dx, dy, angle),
                            hex, angle, SHELL_BLUE, HEX_BORDER);
            }
        }

        // Spine: a line down the middle of the shell
        painter.setPenColor(DARK);
        jumpTo(x + turnX(0, 1.12 * size, angle), y + turnY(0, 1.12 * size, angle));
        painter.moveTo(x + turnX(0, -1.12 * size, angle), y + turnY(0, -1.12 * size, angle));
    }

    /**
     * Draws a crab seen from above, built out of colorful triangles.
     *
     * @param x     center of the body
     * @param y     center of the body
     * @param size  about half the width of the body
     * @param angle degrees to tilt the crab (0 = claws straight up)
     */
    public static void drawCrab(double x, double y, double size, double angle) {
        for (int side = 1; side >= -1; side = side - 2) {
            // Three legs on this side. Each leg has an upper and a lower part.
            for (int i = 0; i < 3; i++) {
                double legY = 0.15 - i * 0.28; // each leg is a bit lower than the last
                drawTriangle(x, y, angle,
                             side * 0.7 * size, (legY + 0.07) * size,
                             side * 0.7 * size, (legY - 0.07) * size,
                             side * 1.3 * size, (legY + 0.25) * size, NAVY, CREAM);
                drawTriangle(x, y, angle,
                             side * 1.25 * size, (legY + 0.25) * size,
                             side * 1.35 * size, (legY + 0.25) * size,
                             side * 1.55 * size, (legY - 0.3) * size, ORANGE, CREAM);
            }

            // Arm
            drawTriangle(x, y, angle,
                         side * 0.45 * size, 0.5 * size,
                         side * 0.6 * size,  0.45 * size,
                         side * 0.9 * size,  0.95 * size, TEAL, CREAM);
            // Claw: two pincers
            drawTriangle(x, y, angle,
                         side * 0.75 * size, 0.95 * size,
                         side * 0.95 * size, 1.55 * size,
                         side * 1.15 * size, 1.25 * size, ORANGE, CREAM);
            drawTriangle(x, y, angle,
                         side * 0.9 * size,  0.9 * size,
                         side * 1.35 * size, 1.15 * size,
                         side * 1.1 * size,  0.85 * size, NAVY, CREAM);

            // Eye stalk (a thin triangle) and eye
            drawTriangle(x, y, angle,
                         side * 0.13 * size, 0.6 * size,
                         side * 0.21 * size, 0.6 * size,
                         side * 0.17 * size, 1.0 * size, NAVY, NAVY);
            double eyeX = side * 0.17 * size;
            double eyeY = 1.0 * size;
            drawCircle(x + turnX(eyeX, eyeY, angle), y + turnY(eyeX, eyeY, angle),
                       0.08 * size, NAVY, CREAM);
        }

        // Body: 8 triangles that all meet at the middle point (0, 0.05)
        double my = 0.05 * size;
        drawTriangle(x, y, angle, 0, my, -0.9 * size, 0.2 * size,  -0.6 * size, 0.62 * size, ORANGE, CREAM);
        drawTriangle(x, y, angle, 0, my, -0.6 * size, 0.62 * size, 0,           0.72 * size, TEAL, CREAM);
        drawTriangle(x, y, angle, 0, my, 0,           0.72 * size, 0.6 * size,  0.62 * size, NAVY, CREAM);
        drawTriangle(x, y, angle, 0, my, 0.6 * size,  0.62 * size, 0.9 * size,  0.2 * size,  TAN, CREAM);
        drawTriangle(x, y, angle, 0, my, 0.9 * size,  0.2 * size,  0.6 * size,  -0.5 * size, ORANGE, CREAM);
        drawTriangle(x, y, angle, 0, my, 0.6 * size,  -0.5 * size, 0,           -0.68 * size, TEAL, CREAM);
        drawTriangle(x, y, angle, 0, my, 0,           -0.68 * size, -0.6 * size, -0.5 * size, NAVY, CREAM);
        drawTriangle(x, y, angle, 0, my, -0.6 * size, -0.5 * size, -0.9 * size, 0.2 * size,  TAN, CREAM);
    }

    // ==================== DECORATION METHODS ====================

    /**
     * Draws a decoration of the given type at (x, y).
     *
     * @param type "rock", "seaweed" or "bubbles"
     */
    public static void drawDecoration(String type, double x, double y, double size) {
        if (type.equals("rock")) {
            drawRock(x, y, size);
        } else if (type.equals("seaweed")) {
            drawSeaweed(x, y, size);
        } else if (type.equals("bubbles")) {
            drawBubbles(x, y, size);
        } else {
            System.out.println("Unknown decoration: " + type);
        }
    }

    /**
     * Draws a gray rock made of triangles. Lighter triangles on top
     * and darker ones on the right make it look lit from above.
     */
    public static void drawRock(double x, double y, double size) {
        double mx = 0.1 * size; // middle point where all triangles meet
        double my = 0.1 * size;
        drawTriangle(x, y, 0, mx, my, -0.8 * size, 0.4 * size,  0.1 * size, 1.0 * size,  ROCK_LIGHT, ROCK_EDGE);
        drawTriangle(x, y, 0, mx, my, 0.1 * size,  1.0 * size,  0.9 * size, 0.3 * size,  ROCK_LIGHT, ROCK_EDGE);
        drawTriangle(x, y, 0, mx, my, 0.9 * size,  0.3 * size,  0.7 * size, -0.8 * size, ROCK_DARK, ROCK_EDGE);
        drawTriangle(x, y, 0, mx, my, 0.7 * size,  -0.8 * size, -0.9 * size, -0.7 * size, ROCK_MID, ROCK_EDGE);
        drawTriangle(x, y, 0, mx, my, -0.9 * size, -0.7 * size, -0.8 * size, 0.4 * size, ROCK_MID, ROCK_EDGE);
    }

    /**
     * Draws seaweed: a thin stem with leaves that switch sides as they go up.
     * (x, y) is the bottom of the stem.
     */
    public static void drawSeaweed(double x, double y, double size) {
        drawRectangle(x - 0.04 * size, y, 0.08 * size, 2.4 * size, SEAWEED, SEAWEED_DARK);

        for (int i = 0; i < 6; i++) {
            double leafY = (0.15 + i * 0.37) * size;
            // even leaves go right, odd leaves go left
            int side = 1;
            if (i % 2 == 1) {
                side = -1;
            }
            drawTriangle(x, y, 0,
                         0,                  leafY,
                         side * 0.5 * size,  leafY + 0.35 * size,
                         0,                  leafY + 0.22 * size, SEAWEED, SEAWEED_DARK);
        }
    }

    /**
     * Draws 5 bubbles rising up. Each bubble is bigger than the one below it.
     * (x, y) is where the bottom bubble goes.
     */
    public static void drawBubbles(double x, double y, double size) {
        for (int i = 0; i < 5; i++) {
            double radius = size * (0.15 + 0.07 * i);
            // zig-zag: even bubbles on the left, odd bubbles on the right
            double bubbleX = x;
            if (i % 2 == 1) {
                bubbleX = x + 0.4 * size;
            }
            double bubbleY = y + i * size * 1.1;
            drawCircle(bubbleX, bubbleY, radius, BUBBLE, BUBBLE_EDGE);
        }
    }

    /**
     * Draws the sandy sea floor: a sand bank with a bumpy top,
     * hundreds of tiny grains, and a few pebbles.
     */
    public static void drawSeaFloor() {
        // Bumps along the top so the edge looks wavy.
        // They are drawn first, so the sand bank covers their bottom halves.
        // The outline is the same color as the sand, so no lines show inside the sand.
        for (int x = -400; x <= 400; x = x + 60) {
            drawCircle(x, -225, 35, SAND, SAND);
        }

        // The main sand bank across the bottom of the canvas
        drawRectangle(-400, -300, 800, 75, SAND, SAND);

        // Grains: tiny dots in random spots, in two different sand colors
        painter.setPenWidth(3);
        for (int i = 0; i < 700; i++) {
            double grainX = -400 + Math.random() * 800; // anywhere from -400 to 400
            double grainY = -300 + Math.random() * 75;  // anywhere from -300 to -225
            if (i % 2 == 0) {
                painter.setPenColor(SAND_DARK);
            } else {
                painter.setPenColor(SAND_LIGHT);
            }
            jumpTo(grainX, grainY);
            painter.moveTo(grainX + 2, grainY); // a very short line looks like a dot
        }
        painter.setPenWidth(1.5f);

        // Pebbles
        for (int i = 0; i < 12; i++) {
            double pebbleX = -390 + Math.random() * 780;
            double pebbleY = -290 + Math.random() * 60;
            drawCircle(pebbleX, pebbleY, 3 + Math.random() * 3, ROCK_MID, ROCK_DARK);
        }
    }
}
