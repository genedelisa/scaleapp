package com.rockhoppertech.music.fx.scaleapp;

/*
 * #%L
 * scaleapp-fx
 * %%
 * Copyright (C) 2013 - 2014 Rockhopper Technologies
 * %%
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
 * #L%
 */

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.PatternFactory;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.fx.cmn.GrandStaff;
import com.rockhoppertech.music.fx.cmn.InputStaff;
import com.rockhoppertech.music.fx.cmn.InputStaffDialog;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class ScaleAppController {
    private static final Logger logger = LoggerFactory
            .getLogger(ScaleAppController.class);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label durationLabel;

    @FXML
    private Label nOctLabel;

    @FXML
    private CheckBox mirrorCB;

    @FXML
    private GrandStaff grandStaff;

    @FXML
    private GrandStaff patternStaff;

    @FXML
    private InputStaff inputStaff;

    @FXML
    private ListView<int[]> patternListView;

    @FXML
    private Button inputButton;

    private InputStaffDialog dialog = new InputStaffDialog();

    @FXML
    void popupAction(ActionEvent event) {
        dialog.show();
    }

    @FXML
    private CheckBox patternReverseCB;

    @FXML
    private CheckBox patternSpreadCB;

    @FXML
    private CheckBox patternUpAndDownCB;

    @FXML
    private CheckBox patternUsePCCB;

    @FXML
    private Slider pitchSlider;

    @FXML
    private CheckBox playAsChordCB;

    @FXML
    private AnchorPane root;

    @FXML
    private ListView<Scale> scaleListView;

    @FXML
    private ListView<MIDITrack> trackList;

    @FXML
    private TextField sliderText;

    @FXML
    private Slider nOctavesSlider;

    @FXML
    private CheckBox scaleUpAndDownCB;

    @FXML
    private Slider durationSlider;

    @FXML
    private TextField startBeatText;

    @FXML
    void chooseInstrumentAction(ActionEvent event) {
        // TODO pop up a dialog
    }

    @FXML
    void pitchTextAction(ActionEvent event) {
    }

    @FXML
    void exitAction(ActionEvent event) {
    }

    @FXML
    void patternCBAction(ActionEvent event) {
    }

    @FXML
    void playAction(ActionEvent event) {
        model.getMIDITrack().play();
    }

    private ScaleAppModel model;
    private IntegerProperty pitchProperty = new SimpleIntegerProperty(60);

    @FXML
    void initialize() {
        assert mirrorCB != null : "fx:id=\"mirrorCB\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert patternListView != null : "fx:id=\"patternListView\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert patternReverseCB != null : "fx:id=\"patternReverseCB\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert patternSpreadCB != null : "fx:id=\"patternSpreadCB\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert patternUpAndDownCB != null : "fx:id=\"patternUpAndDownCB\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert patternUsePCCB != null : "fx:id=\"patternUsePCCB\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert pitchSlider != null : "fx:id=\"pitchSlider\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert playAsChordCB != null : "fx:id=\"playAsChordCB\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert scaleListView != null : "fx:id=\"scaleListView\" was not injected: check your FXML file 'scaleapp.fxml'.";
        assert trackList != null : "fx:id=\"trackList\" was not injected: check your FXML file 'scaleapp.fxml'.";

        this.model = new ScaleAppModel();
        grandStaff.setFontSize(24d);
        grandStaff.drawShapes();

        pitchProperty.bindBidirectional(pitchSlider.valueProperty());
        model.pitchProperty().bind(pitchProperty);
        pitchSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue, Number newValue) {
                if (newValue == null) {
                    sliderText.setText("");
                    return;
                }
                sliderText.setText(PitchFormat.getInstance().format(
                        newValue.intValue()));
                dialog.setPitch(newValue.intValue());
            }
        });

        dialog.pitchProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0,
                    Number arg1, Number newPitch) {
                pitchSlider.setValue(newPitch.intValue());
                // sliderText.setText(PitchFormat
                // .midiNumberToString(newPitch.intValue()));
            }
        });

        model.upAndDownProperty()
                .bind(this.scaleUpAndDownCB.selectedProperty());
        model.nOctavesProperty().bind(this.nOctavesSlider.valueProperty());

        // model.durationProperty().bind(this.durationSlider.valueProperty());
        model.durationProperty().bind(
                Bindings.divide(this.durationSlider.valueProperty(), 16d));

        StringExpression durfmt = Bindings.format("%3.3f",
                Bindings.divide(this.durationSlider.valueProperty(), 16d));
        durationLabel.textProperty().bind(durfmt);

        durationSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Number> observableValue,
                            Number oldValue, Number newValue) {
                        if (newValue == null) {
                            durationLabel.setText("");
                            return;
                        }

                        if (!durationSlider.isValueChanging()) {
                            model.refresh();
                        }
                    }
                });

        this.scaleListView.setItems(model.getScaleListData());

        scaleListView.setCellFactory(new Callback<ListView<Scale>,
                ListCell<Scale>>() {
                    @Override
                    public ListCell<Scale> call(ListView<Scale> list) {
                        return new ScaleListCell();
                    }
                });

        scaleListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Scale>() {

                    @Override
                    public void changed(ObservableValue<? extends Scale> arg0,
                            Scale oldScale, Scale newScale) {
                        model.setSelectedScale(newScale);
                    }
                });

        model.midiTrackProperty().addListener(new ChangeListener<MIDITrack>() {
            @Override
            public void changed(ObservableValue<? extends MIDITrack> arg0,
                    MIDITrack arg1, MIDITrack newTrack) {

                logger.debug("midi track property changed {}", newTrack);
                grandStaff.setTrack(newTrack);
                grandStaff.drawShapes();
            }
        });

        patternListView.setCellFactory(new Callback<ListView<int[]>,
                ListCell<int[]>>() {
                    @Override
                    public ListCell<int[]> call(ListView<int[]> list) {
                        return new PatternListCell();
                    }
                });
        updatePatternList();

        patternListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<int[]>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends int[]> observable,
                            int[] oldValue, int[] newValue) {
                        // pattern = newValue;
                        // patternCalculate();
                        // patternTextField.setText(ArrayUtils.toString(pattern));
                    }
                });

        setupDragonDrop();

        trackList.setCellFactory(new Callback<ListView<MIDITrack>,
                ListCell<MIDITrack>>() {
                    @Override
                    public ListCell<MIDITrack> call(ListView<MIDITrack> list) {
                        return new TrackListCell();
                    }
                });

        trackList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<MIDITrack>() {

                    @Override
                    public void changed(
                            ObservableValue<? extends MIDITrack> observable,
                            MIDITrack oldValue, MIDITrack newValue) {

                        model.setMIDITrack(newValue);
                    }
                });

    }

    void updatePatternList() {
        int limit = 7;
        int size = 6;
        List<int[]> patterns = null;

        if (this.model.getMIDITrack() != null) {
            size = model.getMIDITrack().size();
            patterns = PatternFactory.getPatterns(size, size);
        } else {
            patterns = PatternFactory.getPatterns(limit, size);
        }

        ObservableList<int[]> items = FXCollections.observableArrayList();
        this.patternListView.setItems(items);
        for (int[] a : patterns) {
            items.add(a);
        }
    }

    /**
     * Make the array readable as a String.
     * 
     * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
     * 
     */
    static class PatternListCell extends ListCell<int[]> {
        @Override
        public void updateItem(int[] item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < item.length; i++) {
                    sb.append(item[i]).append(' ');
                }
                this.setText(sb.toString());
            }
        }
    }

    /**
     * Make the track readable as a String.
     * 
     * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
     * 
     */
    static class TrackListCell extends ListCell<MIDITrack> {
        @Override
        public void updateItem(MIDITrack item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                this.setText(item.getName() + "-" + System.currentTimeMillis());
            }
        }
    }

    /**
     * Make the Scale readable as a String.
     * 
     * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
     * 
     */
    static class ScaleListCell extends ListCell<Scale> {
        @Override
        public void updateItem(Scale item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                this.setText(item.getName());
            }
        }
    }

    protected void setupDragonDrop() {
        // Image image = new Image(getClass().getResourceAsStream(
        // "/images/rocky-32-trans.png"));

        grandStaff.setOnDragDetected(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent me) {

                Dragboard db = grandStaff.startDragAndDrop(TransferMode.ANY);

                // javafx 8
                // db.setDragView(dragImageView);

                ClipboardContent content = new ClipboardContent();
                MIDITrack track = model.getMIDITrack();
                content.put(midiTrackDataFormat, track);
                db.setContent(content);
                me.consume();
            }
        });

        grandStaff.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent e) {
                e.consume();
            }
        });

        // Parent root = grandStaff.getScene().getRoot();
        // stage.getScene().getRoot();

        if (root != null) {
            root.setOnDragOver(new EventHandler<DragEvent>() {
                public void handle(DragEvent e) {

                    e.consume();
                }
            });
        }

        trackList.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /*
                 * data is dragged over the target; accept it only if it is not
                 * dragged from the same node and if it has MIDITrack data
                 */
                if (event.getGestureSource() != trackList &&
                        event.getDragboard().hasContent(midiTrackDataFormat)) {
                    logger.debug("drag over");
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                // Don't consume the event. Let the layers below process the
                // DragOver event as well so that the
                // translucent container image will follow the cursor.
                // event.consume();
            }
        });

        trackList.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                /* show to the user that it is an actual gesture target */
                logger.debug("drag entered");
                if (event.getGestureSource() != trackList &&
                        event.getDragboard().hasContent(midiTrackDataFormat)) {
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setRadius(5.0);
                    dropShadow.setOffsetX(3.0);
                    dropShadow.setOffsetY(3.0);
                    dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
                    trackList.setEffect(dropShadow);
                }
                event.consume();
            }
        });

        trackList.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                trackList.setEffect(null);
                event.consume();
            }
        });

        trackList.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasContent(midiTrackDataFormat)) {
                    MIDITrack track = (MIDITrack) db
                            .getContent(midiTrackDataFormat);
                    trackList.getItems().add(track);
                    success = true;

                }
                /*
                 * let the source know whether the data was successfully
                 * transferred and used
                 */
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    /**
     * For Dragon drop.
     * <p>
     * The string HAS to be in this format, otherwise on OSX, you'll get
     * "Cannot set data for an invalid UTI"
     */
    private static final DataFormat midiTrackDataFormat = new DataFormat(
            "com.rockhoppertech.music.midi.js.MIDITrack");

}
