/*
 * Copyright Rockhopper Technologies, Inc.
 */
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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public final class ScaleAppModel {
    private static Logger logger = LoggerFactory.getLogger(ScaleAppModel.class);

    private ObservableList<Scale> data;
    private ListProperty<Scale> scaleListProperty;

    private Scale selectedScale;
    private ObjectProperty<Scale> selectedScaleProperty;

    private ObjectProperty<MIDITrack> midiTrackProperty;

    private IntegerProperty pitchProperty = new SimpleIntegerProperty(60);
    private IntegerProperty nOctavesProperty = new SimpleIntegerProperty(1);
    private BooleanProperty upAndDownProperty = new SimpleBooleanProperty(false);
    private DoubleProperty startBeatProperty = new SimpleDoubleProperty(1d);
    private DoubleProperty durationProperty = new SimpleDoubleProperty(1d);

    public ScaleAppModel() {
        this.data = FXCollections.observableArrayList();
        this.data.addAll(ScaleFactory.getAll());
        this.scaleListProperty = new SimpleListProperty<>(this, "scales",
                this.data);

        midiTrackProperty = new SimpleObjectProperty<MIDITrack>(new MIDITrack());
    }

    DoubleProperty startBeatProperty() {
        return startBeatProperty;
    }

    DoubleProperty durationProperty() {
        return durationProperty;
    }

    public void setDuration(double d) {
        this.durationProperty.set(d);
    }

    public void setStartBeat(double d) {
        this.startBeatProperty.set(d);
    }

    public double getDuration() {
        return this.durationProperty.get();
    }

    public double getStartBeat() {
        return this.startBeatProperty.get();
    }

    BooleanProperty upAndDownProperty() {
        return upAndDownProperty;
    }

    public void setUpAndDown(boolean b) {
        this.upAndDownProperty.set(b);
    }

    public boolean isUpAndDown() {
        return this.upAndDownProperty.get();
    }

    public void setNOctaves(int n) {
        this.nOctavesProperty.set(n);
    }

    public int getNOctaves() {
        return this.nOctavesProperty.get();
    }

    IntegerProperty nOctavesProperty() {
        return nOctavesProperty;
    }

    public IntegerProperty pitchProperty() {
        return pitchProperty;
    }

    public ObservableList<Scale> getScaleListData() {
        return scaleListProperty.get();
    }

    public void setScaleListData(ObservableList<Scale> value) {
        scaleListProperty.set(value);
    }

    public ListProperty<Scale> scaleListProperty() {
        return scaleListProperty;
    }

    int getPitch() {
        return this.pitchProperty.get();
    }

    public void setSelectedScale(Scale newScale) {
        this.selectedScale = newScale;
        MIDITrack track = ScaleFactory.createMIDITrack(
                newScale,
                getPitch(),
                getStartBeat(),
                getDuration(),
                isUpAndDown(),
                getNOctaves());
        this.setMIDITrack(track);
    }

    public ObjectProperty<MIDITrack> midiTrackProperty() {
        return midiTrackProperty;
    }

    public MIDITrack getMIDITrack() {
        return this.midiTrackProperty().get();
    }

    public void setMIDITrack(MIDITrack track) {
        this.midiTrackProperty().set(track);
    }
}
