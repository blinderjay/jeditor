/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeditor.effect.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * some problem
 * @author blinderjay
 */
public class SplitDividerSlider {

    public enum Direction {
        UP, Down, Left, Right;
    }
    private final SplitPane splitPane;
    private SplitPane.Divider divider;
    private Region content;
    private DoubleProperty staticDivider;
    private DoubleProperty dynamicDivider;
    private BooleanProperty staticStatusProperty;
    private SlideTranstion slideplay;
    private int flag;

    public SplitDividerSlider(
            SplitPane splitPane,
            int dividerindex,
            Direction direction,
            double staticDivider,
            double dynamicDivider) {
        this(splitPane, dividerindex, direction, staticDivider, dynamicDivider, Duration.millis(2048));
    }

    public SplitDividerSlider(
            SplitPane splitPane,
            int dividerindex,
            Direction direction,
            double staticDivider,
            double dynamicDivider,
            Duration cycleDuration) {

        this.splitPane = splitPane;
        this.divider = splitPane.getDividers().get(dividerindex);
        switch (direction) {
            case Left:
            case UP:
                flag = 0;
                break;
            case Down:
            case Right:
                flag = 1;
                break;
        }
        this.content = (Region) splitPane.getItems().get(dividerindex + flag);
        this.staticDivider = new SimpleDoubleProperty(staticDivider);
        this.dynamicDivider = new SimpleDoubleProperty(dynamicDivider);

        this.staticStatusProperty = new SimpleBooleanProperty(true);
        this.slideplay = new SlideTranstion(cycleDuration);
        staticStatusProperty.addListener((observable, oldValue, newValue) -> {
                this.slideplay.play();
        });
    }

    public void setStaticStatus(boolean bo) {
        staticStatusProperty.set(bo);
    }

    private class SlideTranstion extends Transition {

        public SlideTranstion(final Duration cycleDuration) {
            setCycleDuration(cycleDuration);
            this.setInterpolator(Interpolator.EASE_OUT);
        }

        @Override
        protected void interpolate(double frac) {

//            if (dynamicDivider.get() < staticDivider.get()) {
//                if (dynamicDivider.get() + frac < staticDivider.get()) {
//                    divider.setPosition(staticStatusProperty.get() == true
//                            ? dynamicDivider.get() + frac
//                            : staticDivider.get() - frac);
//                } else {
//                    stop();
//                }
            if (dynamicDivider.get() < staticDivider.get()) {
                if (0.7+ frac < 1) {
                    divider.setPosition(staticStatusProperty.get() == true
                            ? 0.7+ frac
                            : 1 - frac);
                } else {
                    stop();
                }
            } else {
                if (dynamicDivider.get() - frac > staticDivider.get()) {
                    divider.setPosition(staticStatusProperty.get() == true
                            ? dynamicDivider.get() - frac
                            : staticDivider.get() + frac);
                } else {
                    stop();
                }
            }

        }
    }

}
