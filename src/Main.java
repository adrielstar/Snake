import helper.Helper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

   public enum Direction {
       UP, DOWN, LEFT, RIGHT
   }

    private Direction direction = Direction.RIGHT;
    private boolean moved = false;
    private boolean running = false;

    private Timeline timeline = new Timeline();

    private ObservableList<Node> snake;

    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(Helper.APP_W, Helper.APP_H);

        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        Rectangle food = new Rectangle(Helper.BLOCK_SIZE, Helper.BLOCK_SIZE);
        food.setFill(Color.BLUE);
        food.setTranslateX((int)(Math.random() * Helper.APP_W-Helper.BLOCK_SIZE) / Helper.BLOCK_SIZE * Helper.BLOCK_SIZE);
        food.setTranslateY((int)(Math.random() * Helper.APP_H-Helper.BLOCK_SIZE) / Helper.BLOCK_SIZE * Helper.BLOCK_SIZE);

        KeyFrame frame = new KeyFrame(Duration.seconds(0.2), event -> {
            if (!running)
                return;

            boolean toRemove = snake.size() > 1;

            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

            double tailX = tail.getTranslateX();
            double tailY = tail.getTranslateY();

            switch (direction){
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - Helper.BLOCK_SIZE);
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() + Helper.BLOCK_SIZE);
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX() - Helper.BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX() + Helper.BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
            }

            moved = true;

            if (toRemove)
                snake.add(0, tail);

            for (Node rect : snake) {
                if (rect != tail && tail.getTranslateX() == rect.getTranslateX() && tail.getTranslateY() == rect.getTranslateY()){
                    restartGame();
                    break;
                }
            }

            if (tail.getTranslateX() < 0 || tail.getTranslateX() >= Helper.APP_W || tail.getTranslateY() < 0 || tail.getTranslateY() >= Helper.APP_H){
                restartGame();
            }

            if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()){
                food.setTranslateX((int)(Math.random() * Helper.APP_H-Helper.BLOCK_SIZE) / Helper.BLOCK_SIZE * Helper.BLOCK_SIZE);
                food.setTranslateY((int)(Math.random() * Helper.APP_H-Helper.BLOCK_SIZE) / Helper.BLOCK_SIZE * Helper.BLOCK_SIZE);
                
                Rectangle rect = new Rectangle(Helper.BLOCK_SIZE, Helper.BLOCK_SIZE);
                rect.setTranslateX(tailX);
                rect.setTranslateY(tailY);
                
                snake.add(rect);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        
        root.getChildren().addAll(food, snakeBody);
        return root;
    }

    private void restartGame() {
        stopGame();
        startGame();
    }

    private void startGame() {
        direction = Direction.RIGHT;
        Rectangle head = new Rectangle(Helper.BLOCK_SIZE, Helper.BLOCK_SIZE);
        snake.add(head);
        timeline.play();
        running = true;
    }

    private void stopGame() {
        running =false;
        timeline.stop();
        snake.clear();
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            if (!moved)
                return;

                switch (event.getCode()) {
                    case UP:
                        if (direction != Direction.DOWN)
                            direction = Direction.UP;
                        break;
                    case DOWN:
                        if (direction != Direction.UP)
                            direction = Direction.DOWN;
                        break;
                    case  LEFT:
                        if (direction != Direction.RIGHT)
                            direction = Direction.LEFT;
                        break;
                    case RIGHT:
                        if (direction != Direction.LEFT)
                            direction = Direction.RIGHT;
                        break;
                }
            moved = false;
        });
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
