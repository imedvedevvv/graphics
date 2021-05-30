package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;

    private static final double centerX = 500.0;
    private static final double centerY = 400.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    private void drawShapes(GraphicsContext context) {

        context.fillRect(0,0, WIDTH, HEIGHT);
        drawFillCircle(context, 300);
        context.setStroke(Color.BLACK);

        context.setFill(Color.BLUE);
        drawFillCircle(context, 600);
        drawCircle(context, 600);
        drawFillCircle(context, 500);
        drawCircle(context, 500);

        context.setFill(Color.RED);
        drawFillCircle(context, 400);
        drawCircle(context, 400);
        drawFillCircle(context, 300);
        drawCircle(context, 300);

        context.setFill(Color.YELLOW);
        drawFillCircle(context, 200);
        drawCircle(context, 200);
        drawFillCircle(context, 100);
        drawCircle(context, 100);

        drawCross(context, centerX, centerY);
    }

    private void drawCircle(GraphicsContext context, double radius) {
        context.fillOval(centerX - radius/2, centerY-radius/2, radius, radius);
    }

    private void drawFillCircle(GraphicsContext context, double radius) {
        context.strokeOval(centerX - radius/2, centerY - radius/2, radius, radius);
    }

    private void drawCross(GraphicsContext context, double x, double y) {
        context.strokeLine(x - 5, y, x + 5, y);
        context.strokeLine(x, y - 5, x, y + 5);
    }
}