/*
 * Copyright 2017 Faculty of Informatics, University of Debrecen.
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
package hu.unideb.inf.rubikscube;

import java.net.URL;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main class of this application.
 *
 * @author kinga
 */
public class MainApp extends Application {

	private GameFieldController gameFieldController;
	private MenuFieldController menuFieldController;

	@Override
	public void start(Stage primaryStage) throws Exception {

		gameFieldController = new GameFieldController();

		String resourcePath = System.getProperty("file.separator")
				+ "fxml"
				+ System.getProperty("file.separator")
				+ "menuField.fxml";
		URL location = getClass().getResource(resourcePath);
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		fxmlLoader.setLocation(location);
		menuFieldController = new MenuFieldController(gameFieldController);
		fxmlLoader.setController(menuFieldController);
		Pane menuFieldPane = fxmlLoader.load();
		menuFieldPane.autosize();
		Image menuBackground = new Image(
				fxmlLoader.getClassLoader().getResourceAsStream("menuBackground.png"));
		Background background = new Background(new BackgroundImage(menuBackground, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
		menuFieldPane.setBackground(background);

		resourcePath = System.getProperty("file.separator")
				+ "fxml"
				+ System.getProperty("file.separator")
				+ "gameField.fxml";
		location = getClass().getResource(resourcePath);
		fxmlLoader = new FXMLLoader(location);
		fxmlLoader.setLocation(location);
		fxmlLoader.setController(gameFieldController);
		Pane gameFieldPane = fxmlLoader.load();
		gameFieldPane.autosize();
		gameFieldPane.setBackground(new Background(new BackgroundFill(Color.POWDERBLUE,
				CornerRadii.EMPTY, Insets.EMPTY)));

		GridPane mainPane = new GridPane();
		mainPane.add(gameFieldPane, 0, 0);
		mainPane.add(menuFieldPane, 1, 0);
		mainPane.setMinSize(gameFieldPane.getWidth() + menuFieldPane.getWidth(),
				gameFieldPane.getHeight());
		mainPane.setDepthTest(DepthTest.DISABLE);

		Scene scene = new Scene(mainPane, gameFieldPane.getWidth() + menuFieldPane.getWidth(),
				gameFieldPane.getHeight(), true, SceneAntialiasing.BALANCED);
		PerspectiveCamera camera = new PerspectiveCamera(false);
		scene.setCamera(camera);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Rubik's cube");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * The main() method of the class.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
