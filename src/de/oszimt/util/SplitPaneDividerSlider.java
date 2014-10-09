/* 
 * Copyright 2014 Jens Deters.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.oszimt.util;

import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 *
 * @author Jens Deters
 */
public class SplitPaneDividerSlider {

    public enum Direction {

        UP, DOWN, LEFT, RIGHT;
    }

    private final Direction direction;
    private final SplitPane splitPane;
    private final int dividerIndex;
    private BooleanProperty aimContentVisibleProperty;
    private DoubleProperty lastDividerPositionProperty;
    private DoubleProperty currentDividerPositionProperty;
    private Region content;
    private double contentInitialMinWidth;
    private double contentInitialMinHeight;
    private Transition slideTransition;
    private Duration cycleDuration;
    private SplitPane.Divider dividerToMove;

    public SplitPaneDividerSlider(SplitPane splitPane, int dividerIndex, Direction direction) {
        this(splitPane, dividerIndex, direction, Duration.millis(7000.0));
    }

    public SplitPaneDividerSlider(SplitPane splitPane,
            int dividerIndex,
            Direction direction,
            Duration cycleDuration) {
        this.direction = direction;
        this.splitPane = splitPane;
        this.dividerIndex = dividerIndex;
        this.cycleDuration = cycleDuration;
        init();
    }

    private void init() {
        slideTransition = new SlideTransition(cycleDuration);

        // figure out right splitpane content
        switch (direction) {
            case LEFT:
            case UP:
                content = (Region) splitPane.getItems().get(dividerIndex);
                break;
            case RIGHT:
            case DOWN:
                content = (Region) splitPane.getItems().get(dividerIndex + 1);
                break;
        }
        contentInitialMinHeight = content.getMinHeight();
        contentInitialMinWidth = content.getMinWidth();
        dividerToMove = splitPane.getDividers().get(dividerIndex);

        aimContentVisibleProperty().addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                // store divider position before transition:
                setLastDividerPosition(splitPane.getDividers().get(dividerIndex).getPosition());
                // "arm" current divider position before transition:
                setCurrentDividerPosition(getLastDividerPosition());
            }
            content.setMinSize(0.0, 0.0);
            slideTransition.play();
        });
    }

    private void restoreContentSize() {
        content.setMinHeight(contentInitialMinHeight);
        content.setMinWidth(contentInitialMinWidth);
        setCurrentDividerPosition(getLastDividerPosition());
    }

    public BooleanProperty aimContentVisibleProperty() {
        if (aimContentVisibleProperty == null) {
            aimContentVisibleProperty = new SimpleBooleanProperty(true);
        }
        return aimContentVisibleProperty;
    }

    public void setAimContentVisible(boolean aimContentVisible) {
        aimContentVisibleProperty().set(aimContentVisible);
    }

    public boolean isAimContentVisible() {
        return aimContentVisibleProperty().get();
    }

    public DoubleProperty lastDividerPositionProperty() {
        if (lastDividerPositionProperty == null) {
            lastDividerPositionProperty = new SimpleDoubleProperty();
        }
        return lastDividerPositionProperty;
    }

    public double getLastDividerPosition() {
        return lastDividerPositionProperty().get();
    }

    public void setLastDividerPosition(double lastDividerPosition) {
        lastDividerPositionProperty().set(lastDividerPosition);
    }

    public DoubleProperty currentDividerPositionProperty() {
        if (currentDividerPositionProperty == null) {
            currentDividerPositionProperty = new SimpleDoubleProperty();
        }
        return currentDividerPositionProperty;
    }

    public double getCurrentDividerPosition() {
        return currentDividerPositionProperty().get();
    }

    public void setCurrentDividerPosition(double currentDividerPosition) {
        currentDividerPositionProperty().set(currentDividerPosition);
        dividerToMove.setPosition(currentDividerPosition);
    }

    private class SlideTransition extends Transition {

        public SlideTransition(final Duration cycleDuration) {
            setCycleDuration(cycleDuration);
        }

        @Override
        protected void interpolate(double d) {
            switch (direction) {
                case LEFT:
                case UP:
                    // intent to slide in content:  
                    if (isAimContentVisible()) {
                        if ((getCurrentDividerPosition() + d) <= getLastDividerPosition()) {
                            setCurrentDividerPosition(getCurrentDividerPosition() + d);
                        } else { //DONE
                            restoreContentSize();
                            stop();
                        }
                    } // intent to slide out content:  
                    else {
                        if (getCurrentDividerPosition() > 0.0) {
                            setCurrentDividerPosition(getCurrentDividerPosition() - d);
                        } else { //DONE
                            setCurrentDividerPosition(0.0);
                            stop();
                        }
                    }
                    break;
                case RIGHT:
                case DOWN:
                    // intent to slide in content:  
                    if (isAimContentVisible()) {
                        if ((getCurrentDividerPosition() - d) >= getLastDividerPosition()) {
                            setCurrentDividerPosition(getCurrentDividerPosition() - d);
                        } else { //DONE
                            restoreContentSize();
                            stop();
                        }
                    } // intent to slide out content:  
                    else {
                        if (getCurrentDividerPosition() < 1.0) {
                            setCurrentDividerPosition(getCurrentDividerPosition() + d);
                        } else {//DONE
                            setCurrentDividerPosition(1.0);
                            stop();
                        }
                    }
                    break;
            }
        }
    }
}
