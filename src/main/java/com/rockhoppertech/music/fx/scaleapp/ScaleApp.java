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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class ScaleApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(ScaleApp.class);
    private ScaleAppModel model;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        logger.info("Starting JavaFX");
        this.model = new ScaleAppModel();
        String fxmlFile = "/fxml/scaleapp.fxml";
        logger.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(
                fxmlFile));
        // AppController controller = (SymChordsController) loader
        // .getController();

        logger.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add("/styles/styles.css");
        scene.getAccelerators()
                .put(
                        new KeyCodeCombination(KeyCode.Q,
                                KeyCombination.SHORTCUT_DOWN),
                        new Runnable() {
                            @Override
                            public void run() {
                                logger.debug("leaving via accelerator");
                                System.exit(0);
                            }
                        }
                );

        stage.setTitle("Scales");
        // http://docs.oracle.com/javafx/2/api/javafx/stage/Stage.html#getIcons()
        stage.getIcons().addAll(
                new Image("images/icon128.png"),
                new Image("images/icon32.png"),
                new Image("images/icon48.png"),
                new Image("images/icon64.png"),
                new Image("images/icon72.png"),
                new Image("images/icon96.png"));
        stage
                .getIcons()
                .add(
                        new Image(
                                "http://files.softicons.com/download/application-icons/itunes-10-replacement-icons-by-hakeem-derres/png/32x32/iTunes%2010%20Blue.png"));

        stage.setScene(scene);
        // controller.setStage(stage);
        stage.show();
    }

}
