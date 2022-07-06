package Paint;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends Application {

    final public String[] shapes = {"Rubber", "Pencil", "Sqare", "Oval", "Triangle", "Hexagon", "Custom"};
    double x = 0;
    double y = 0;
    public String[] type = {"Fill", "Empty"};
    int valueOutput;
    int timesPressed = 0;
    double[] posX = new double[30];
    double[] posY = new double[30];

    @Override
    public void start(Stage stage) {

        FlowPane root = new FlowPane();

        HBox topBox = new HBox();
        topBox.setPrefSize(640, 50);
        topBox.setAlignment(Pos.CENTER);
        topBox.setSpacing(5);

        VBox leftBox = new VBox();
        leftBox.setPrefSize(70, 430);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setSpacing(20);

        //============================================================

        List<Canvas> canvasList = new ArrayList<>();

        canvasList.add(new Canvas(590, 440));

        Canvas canvas = new Canvas(590, 440);

        //============================================================

        List<String> canvasNames = new ArrayList<>();

        Collections.addAll(canvasNames, shapes);

        //============================================================

        ListView<String> canvasListView = new ListView<>();
        canvasListView.setPrefHeight(215);
        canvasListView.getItems().addAll(canvasNames);
        leftBox.getChildren().addAll(canvasListView);

        //============================================================

        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(590, 420);
        stackPane.getChildren().addAll(canvas);

        Button button = new Button("New File");

        //============================================================

        TextField textField = new TextField();
        textField.setPrefWidth(50);
        Button buttonForText = new Button("Sumbit");

        buttonForText.setOnAction(e -> {
            try {
                valueOutput = Integer.parseInt(textField.getText());

            } catch (NumberFormatException tmp) {
                System.out.println("Only numbers");
            }

            System.out.println(valueOutput);
        });

        //=============================================================

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);

        //=============================================================

        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spinner.setValueFactory(valueFactory);
        spinner.setMaxWidth(60);
        valueFactory.setValue(5);

        //=============================================================

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 590, 440);
        gc.setFill(colorPicker.getValue());
        gc.setStroke(Color.BLACK);

        //============================================================

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(shapes);
        choiceBox.setValue(shapes[1]);


        //============================================================

        ChoiceBox<String> fillBox = new ChoiceBox<>();
        fillBox.getItems().addAll(type);
        fillBox.setValue(type[0]);

        //============================================================

        button.setOnAction(e -> {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, 590, 440);
            timesPressed = 0;
            gc.setFill(colorPicker.getValue());
        });

        colorPicker.setOnAction(e -> {
            gc.setFill(colorPicker.getValue());
            gc.setStroke(colorPicker.getValue());
        });

        spinner.valueProperty().addListener(e -> {
            double value = spinner.getValue();
            gc.setLineWidth(value);
        });

        //===========================================================

        canvas.setOnMousePressed(a -> {

            if (choiceBox.getValue().equals(shapes[1])) {
                double size = spinner.getValue();
                gc.fillOval(a.getX(), a.getY(), size, size);
            }
            if (choiceBox.getValue().equals(shapes[2]) || choiceBox.getValue().equals(shapes[3])) {
                x = a.getX();
                y = a.getY();
            }
            if (choiceBox.getValue().equals(shapes[4])) {
                if (timesPressed != 3) {
                    posX[timesPressed] = a.getX();
                    posY[timesPressed] = a.getY();
                    timesPressed++;
                }
            }
            if (choiceBox.getValue().equals(shapes[5])) {
                if (timesPressed != 6) {
                    posX[timesPressed] = a.getX();
                    posY[timesPressed] = a.getY();
                    timesPressed++;
                }
            }
            if (choiceBox.getValue().equals(shapes[6])) {
                if (timesPressed != valueOutput) {

                    posX[timesPressed] = a.getX();
                    posY[timesPressed] = a.getY();
                    timesPressed++;
                    System.out.println(timesPressed);
                }
            }
        });

        canvas.setOnMouseDragged(b -> {
            if (choiceBox.getValue().equals(shapes[1])) {
                double size = spinner.getValue();
                gc.fillOval(b.getX(), b.getY(), size, size);
            }
            if (choiceBox.getValue().equals(shapes[0])) {
                double size = spinner.getValue();
                gc.setFill(Color.WHITE);
                gc.fillRect(b.getX(), b.getY(), size, size);
                gc.setFill(colorPicker.getValue());
            }
        });

        canvas.setOnMouseReleased(c -> {

            if (choiceBox.getValue().equals(shapes[2])) {
                double sizeX = c.getX() - x;
                double sizeY = c.getY() - y;
                if (fillBox.getValue().equals(type[0])) {
                    gc.fillRect(x, y, sizeX, sizeY);
                } else {
                    gc.strokeRect(x, y, sizeX, sizeY);
                }
                x = 0;
                y = 0;
            } else if (choiceBox.getValue().equals(shapes[3])) {
                double sizeX = c.getX() - x;
                double sizeY = c.getY() - y;
                if (fillBox.getValue().equals(type[0])) {
                    gc.fillOval(x, y, sizeX, sizeY);
                } else {
                    gc.strokeOval(x, y, sizeX, sizeY);
                }
                x = 0;
                y = 0;
            } else if (choiceBox.getValue().equals(shapes[4])) {
                if (timesPressed == 3) {
                    if (fillBox.getValue().equals(type[0])) {
                        gc.fillPolygon(posX, posY, 3);
                    } else {
                        gc.strokePolygon(posX, posY, 3);
                    }

                    timesPressed = 0;
                }
            } else if (choiceBox.getValue().equals(shapes[5])) {
                if (timesPressed == 6) {
                    if (fillBox.getValue().equals(type[0])) {
                        gc.fillPolygon(posX, posY, 6);
                    } else {
                        gc.strokePolygon(posX, posY, 6);
                    }

                    timesPressed = 0;
                }
            } else if (choiceBox.getValue().equals(shapes[6])) {
                if (timesPressed == valueOutput) {
                    if (fillBox.getValue().equals(type[0])) {
                        gc.fillPolygon(posX, posY, valueOutput);
                    } else {
                        gc.strokePolygon(posX, posY, valueOutput);
                    }
                    timesPressed = 0;
                }
            }
        });

        //=============================================================

        Button save = new Button("Save");
        save.setOnAction(e -> {
            FileChooser savefile = new FileChooser();
            savefile.setTitle("Save File");
            File file = savefile.showSaveDialog(stage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error!");
                }
            }
        });

        //============================================================

        topBox.getChildren().addAll(fillBox, colorPicker, spinner, button, choiceBox, textField, buttonForText, save);
        root.getChildren().addAll(topBox, leftBox, stackPane);

        Scene scene = new Scene(root, 660, 480, Color.WHITE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
