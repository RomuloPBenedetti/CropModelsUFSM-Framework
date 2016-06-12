package cropModelsUFSM.graphic;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import cropModelsUFSM.support.Util;

import java.util.ArrayList;
import java.util.List;

import static cropModelsUFSM.support.Util.images;

public class ImageAnimation
{
    private  List<Image> imageList = new ArrayList<>();
    private final  SequentialTransition sequence;
    private final  Move move; private final  Fade fade;
    private  PauseTransition pause;
    private final  ImageView frontImage, backImage;
    int currentImg = 0;

    public ImageAnimation (ImageView frontImage, ImageView backImage)
    {
        
        Util.images.forEach(i -> imageList.add(new Image(i)));
        this.frontImage = frontImage; frontImage.setImage(imageList.get(0));
        this.backImage = backImage; backImage.setImage(imageList.get(1));
        move = new Move (40.0,Duration.seconds(6.0), frontImage, 1, true);
        fade = new Fade (40.0,Duration.seconds(4.0), frontImage, 1, true);
        sequence = new SequentialTransition(move, fade);
        sequence.setCycleCount(1);
    }

    public void play ()
    {
        sequence.play();
        sequence.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentImg+1 < Util.images.size())
                    currentImg++;
                else
                    currentImg = 0;
                frontImage.setImage(imageList.get(currentImg));
                if(currentImg == images.size()-1)
                    backImage.setImage(imageList.get(0));
                else
                    backImage.setImage(imageList.get(currentImg+1));
                sequence.play();
            }
        });
    }

/***********************************************************************
                Classes de transicao especializadas
***********************************************************************/


    class Move extends Transition
    {
        private double x, y;
        private ImageView imv;

        public Move (Double fps, Duration duration, ImageView imv,
                     int cyicles, boolean reverse)
        {
            super(fps);
            setCycleDuration(duration);
            this.imv = imv;
            setCycleCount(cyicles);
            setAutoReverse(reverse);
        }

        @Override
        protected void interpolate(double frac)
        {
            x = 0;
            y = imv.getImage().getHeight()-150.0;
            Rectangle2D originalvp = imv.getViewport();
            double height = originalvp.getHeight();
            double width = originalvp.getWidth();
            double x = originalvp.getMinX();
            Rectangle2D vp =
                    new Rectangle2D(x, frac*y, width, height);
            imv.setViewport(vp);
        }
    }

    class Fade extends Transition
    {

        private ImageView imv;

        public Fade (Double fps, Duration duration, ImageView imv,
                     int cyicles, boolean reverse)
        {
            super(fps);
            setCycleDuration(duration);
            this.imv = imv;
            setCycleCount(cyicles);
            setAutoReverse(reverse);
        }

        @Override
        protected void interpolate(double frac)
        {
            imv.setOpacity(1.0-(1*frac));
        }
    }
}
