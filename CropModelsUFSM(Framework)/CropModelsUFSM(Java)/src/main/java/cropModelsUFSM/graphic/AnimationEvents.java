package cropModelsUFSM.graphic;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


public class AnimationEvents {


    public static void fade (Duration duration, int scycles, Node node,
                             double from, double to)
    {
        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(from);
        fade.setToValue(to);
        fade.setCycleCount(scycles);
        fade.setAutoReverse(true);
        fade.play();
    }

    public static void move (Duration duration, int scycles, Node node,
                              double x, double y)
    {

    }


    public static void setDataRange (Duration seconds, int scycles, Label label,
                                     Shape dataShape, Color from, Color to,
                                     String text) {
        FillTransition reFill = 
            new FillTransition(seconds, dataShape, from, to);
        reFill.setCycleCount(scycles);
        reFill.setAutoReverse(true);
        reFill.setInterpolator(Interpolator.EASE_BOTH);
        reFill.play();
        fade(seconds, scycles, label, 1, 0);
        label.setText(text);
        fade(seconds, scycles, label, 0, 1);
    }

    private static FillTransition reFill;

    public static void setWarning (Shape dataShape, Color from, Color to) {
        if (reFill == null) reFill =new FillTransition(Duration.seconds(1),
                                dataShape, from, to);
        if(!reFill.getStatus().equals(Animation.Status.RUNNING)) {
            try {
                ScaleTransition scale =
                        new ScaleTransition(Duration.seconds(0.1), dataShape);
                scale.setCycleCount(1);
                scale.setAutoReverse(true);
                scale.setFromX(0.0);
                scale.setToX(1.0);
                scale.setFromY(0.0);
                scale.setToY(1.0);
                scale.play();

                FillTransition reFill =
                        new FillTransition(Duration.seconds(1.0),
                                dataShape, from, to);
                reFill.setCycleCount(FillTransition.INDEFINITE);
                reFill.setAutoReverse(true);
                reFill.setInterpolator(Interpolator.EASE_BOTH);
                reFill.play();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        } else{
            reFill.pause();
        }
    }

    public static void simulationSucess (Shape dataShape, ImageView okImv,
                                         ImageView playImv, Color from,
                                         Color to){
        RotateTransition okRotate, playRotate;
        FadeTransition okFade, playFade;
        FillTransition colorChange;
        PauseTransition pause;
        SequentialTransition sequence;
        ParallelTransition all;

        okFade = new FadeTransition(Duration.seconds(1), okImv);
        okFade.setCycleCount(1);
        okFade.setFromValue(0); okFade.setToValue(1);

        playFade = new FadeTransition(Duration.seconds(1),playImv);
        playFade.setCycleCount(1);
        playFade.setFromValue(1); playFade.setToValue(0);

        okRotate = new RotateTransition(Duration.seconds(1), okImv);
        okRotate.setCycleCount(1);
        okRotate.setByAngle(1080);

        playRotate = new RotateTransition(Duration.seconds(1), playImv);
        playRotate.setCycleCount(1);
        playRotate.setByAngle(1080);

        colorChange = new FillTransition(Duration.seconds(1),
                dataShape, from, to);
        colorChange.setCycleCount(1);
        colorChange.setAutoReverse(true);

        pause = new PauseTransition(Duration.seconds(1));
        pause.setCycleCount(1);

        all = new ParallelTransition(okFade, playFade, okRotate, playRotate,
                colorChange);
        all.setAutoReverse(true);
        all.setInterpolator(Interpolator.EASE_BOTH);
        sequence = new SequentialTransition(all,pause);
        sequence.setAutoReverse(true);
        sequence.setCycleCount(2);
        sequence.play();
    }
}
