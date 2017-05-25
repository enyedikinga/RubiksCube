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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import hu.unideb.inf.rubikscube.model.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;

/**
 * This class provides functionality for handling main events in the game.
 *
 * This class controls special events like saving the state of the game.
 *
 * @author kinga
 */
public class MenuFieldController implements Initializable {

	@FXML
	private Button scrambleButton;
	@FXML
	private Button resetButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button loadButton;
	@FXML
	private ComboBox cubeSizeSetter;
	@FXML
	private TextField cubeSizeTextField;

	private GameFieldController gameFieldController;
	private File defaultDirectory;
	private File tmpDir;

	private Logger logger = LoggerFactory.getLogger(MenuFieldController.class);

	/**
	 * Creates a {@link MenuFieldController} object.
	 *
	 * @param gameFieldController The {@link GameFieldController} to use
	 */
	public MenuFieldController(GameFieldController gameFieldController) {

		this.gameFieldController = gameFieldController;
		defaultDirectory = new File(System.getProperty("user.home")
				+ System.getProperty("file.separator")
				+ "rubikscubeSavedGames"
				+ System.getProperty("file.separator"));

		String defaultDirPath = defaultDirectory.getPath() + System.getProperty("file.separator");
		logger.info("Default dir path {}", defaultDirPath);

		tmpDir = new File(System.getProperty("java.io.tmpdir")
				+ System.getProperty("file.separator")
				+ "rubikscubeTempData");
		tmpDir.deleteOnExit();
		tmpDir.mkdir();

		logger.info("New instance of class MenuFieldController created");
		logger.info("Default directory for storing appdata is {}", defaultDirectory);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<Integer> cubeSizes = new ArrayList<>();
		for (int i = 2; i <= 25; ++i) {
			cubeSizes.add(i);
		}
		cubeSizeSetter.getItems().addAll(cubeSizes);
		cubeSizeSetter.setValue(Cube.DEFAULT_SIZE);
		cubeSizeTextField.setEffect(new DropShadow());
	}

	@FXML
	private void setCubeSize(ActionEvent event) {
		int cubeSize = gameFieldController.getCube().getCubeSize();
		int selectedCubeSize = (Integer) cubeSizeSetter.getSelectionModel().getSelectedItem();
		if (selectedCubeSize != cubeSize) {

			File tmpFile = new File(tmpDir.getPath()
					+ System.getProperty("file.separator")
					+ cubeSize
					+ "cube.json");
			logger.info("Temporarly saving cube {} state to {}", cubeSize, tmpFile);
			saveTo(tmpFile);
			tmpFile.deleteOnExit();

			logger.info("Cubesize set to {}", selectedCubeSize);

			File loadFile = new File(tmpDir.getPath()
					+ System.getProperty("file.separator")
					+ selectedCubeSize
					+ "cube.json");
			if (loadFile.exists()) {
				logger.info("Found previously started gameplay for cubesize {}", selectedCubeSize);
				loadFrom(loadFile);
			} else {
				logger.info("Found no previously started gameplay for cube {}, creating new one",
						selectedCubeSize);
				gameFieldController.setCube(new Cube(selectedCubeSize));
				gameFieldController.setupGameField();
			}
		}
	}

	@FXML
	private void resetCube(ActionEvent event) {

		logger.info("Reset button clicked");
		gameFieldController.getCube().resetSides();
		gameFieldController.setupGameField();
	}

	@FXML
	private void scrambleCube() {

		logger.info("Scramble button clicked");
		Scrambler s = new Scrambler(gameFieldController.getCube().getCubeSize());
		gameFieldController.getCube().resetSides();
		gameFieldController.getCube().scramble(s);
		gameFieldController.setupGameField();
	}

	@FXML
	private void saveGame(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		logger.info("{}", defaultDirectory);
		defaultDirectory.mkdir();

		fileChooser.setInitialDirectory(defaultDirectory);
		fileChooser.setInitialFileName("*.json");
		fileChooser.setTitle("Save File");

		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Json Files", "*.json"),
				new ExtensionFilter("All Files", "*"));

		Button sourceButton = (Button) event.getSource();
		Stage stage = (Stage) (sourceButton.getScene().getWindow());

		File selectedFile = fileChooser.showSaveDialog(stage);
		if (selectedFile != null) {
			saveTo(selectedFile);
		}
	}

	private void saveTo(File selectedFile) {
		logger.info("Creatable file to save is {} ", selectedFile);

		Gson gson = new GsonBuilder().create();
		try (Writer writer
				= new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(selectedFile), "utf-8"))) {
			gson.toJson(gameFieldController.getCube(), writer);
			logger.info("Successfully saved file {}", selectedFile);
		} catch (UnsupportedEncodingException exception) {
			logger.error("Error at saving file {}", selectedFile);
			logger.error("{}", exception.getMessage());
		} catch (FileNotFoundException exception) {
			logger.error("Error at saving file {}", selectedFile);
			logger.error("{}", exception.getMessage());
		} catch (IOException exception) {
			logger.error("Error at saving file {}", selectedFile);
			logger.error("{}", exception.getMessage());
		}
	}

	@FXML
	private void loadGame(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		logger.info("{}", defaultDirectory);
		defaultDirectory.mkdir();

		fileChooser.setTitle("Load File");
		fileChooser.setInitialDirectory(defaultDirectory);
		fileChooser.setInitialFileName("*.json");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Json Files", "*.json"),
				new ExtensionFilter("All Files", "*"));

		Button sourceButton = (Button) event.getSource();
		Stage stage = (Stage) (sourceButton.getScene().getWindow());

		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {

			//Save temporarly the current gameplay before loading
			int cubeSize = gameFieldController.getCube().getCubeSize();
			File tmpFile = new File(tmpDir.getPath()
					+ System.getProperty("file.separator")
					+ cubeSize
					+ "cube.json");
			logger.info("Temporarly saving cube {} state to {}", cubeSize, tmpFile);
			saveTo(tmpFile);
			tmpFile.deleteOnExit();

			loadFrom(selectedFile);
		}
	}

	private void loadFrom(File selectedFile) {
		logger.info("Selected file to load is {} ", selectedFile);
		Gson gson = new GsonBuilder().create();
		try (Reader reader
				= new BufferedReader(
						new InputStreamReader(
								new FileInputStream(selectedFile), "utf-8"))) {
			Cube loadedCube = gson.fromJson(reader, gameFieldController.getCube().getClass());
			logger.info("Successfully read from file {}", selectedFile);
			if (loadedCube != null) {
				gameFieldController.setCube(loadedCube);
				gameFieldController.setupGameField();
				cubeSizeSetter.getSelectionModel().select(new Integer(loadedCube.getCubeSize()));
			} else {
				logger.error("Could not load cube from {}", selectedFile);
			}
		} catch (UnsupportedEncodingException exception) {
			logger.error("Error at reading from file {}", selectedFile);
			logger.error("{}", exception.getMessage());
		} catch (FileNotFoundException exception) {
			logger.error("Error at reading from file {}", selectedFile);
			logger.error("{}", exception.getMessage());
		} catch (IOException exception) {
			logger.error("Error at reading from file {}", selectedFile);
			logger.error("{}", exception.getMessage());
		}
	}
}
