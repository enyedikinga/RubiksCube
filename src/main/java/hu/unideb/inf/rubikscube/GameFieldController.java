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

import hu.unideb.inf.rubikscube.model.Cube;
import hu.unideb.inf.rubikscube.model.Rotation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides functionality for handling the Rubik's cube.
 *
 * @author kinga
 */
public class GameFieldController implements Initializable {

	@FXML
	private Group gameField;
	@FXML
	private AmbientLight ambientLight;
	@FXML
	private AnchorPane gameAnchorPane;

	/**
	 * Size of the viewport where the cube is drawn.
	 */
	public static final int VIEWPORT_SIZE = 640;

	private Cube cube;

	private int cubeSize;
	private double partSize;
	private double stickerSize;
	private double drawingStartPosition;
	private double gap = 1.025;

	private double mouseOldX = 0;
	private double mouseOldY = 0;

	private double rotateOldX = 0;
	private double rotateOldY = 0;
	private double rotateOldZ = 0;

	private Rotate rotateX = new Rotate(30, Rotate.X_AXIS);
	private Rotate rotateY = new Rotate(45, Rotate.Y_AXIS);
	private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

	private double gameFieldScale = 1;

	private DragDirection dragDirection;

	private boolean layerDragged = false;

	private Logger logger = LoggerFactory.getLogger(GameFieldController.class);

	/**
	 * Creates a new {@link GameFieldController} object.
	 */
	public GameFieldController() {

		cube = new Cube(Cube.DEFAULT_SIZE);
		setDrawDataForCube();
		logger.info("New instance of class GameFieldController created");
	}

	/**
	 * This method sets data for drawing the cube in the proper location.
	 *
	 * Calling this method will recalculate where should the cube be drawn. If the size of the cube is changed or there are any changes in the view environment, this method should be called.
	 */
	public void setDrawDataForCube() {
		cubeSize = cube.getCubeSize();
		partSize = VIEWPORT_SIZE / (2 * cubeSize);
		stickerSize = (partSize * 0.95);
		drawingStartPosition = (VIEWPORT_SIZE / 2)
				- ((cubeSize * (partSize * gap)) / 2)
				+ partSize / 2;

		rotateX.setPivotX(VIEWPORT_SIZE / 2);
		rotateX.setPivotY(VIEWPORT_SIZE / 2);
		rotateX.setPivotZ(VIEWPORT_SIZE / 2);

		rotateY.setPivotX(VIEWPORT_SIZE / 2);
		rotateY.setPivotY(VIEWPORT_SIZE / 2);
		rotateY.setPivotZ(VIEWPORT_SIZE / 2);

		rotateZ.setPivotX(VIEWPORT_SIZE / 2);
		rotateZ.setPivotY(VIEWPORT_SIZE / 2);
		rotateZ.setPivotZ(VIEWPORT_SIZE / 2);

		logger.info("Data for drawing calculated");
		logger.debug("One piece's size is {}", partSize);
		logger.debug("One sticker's size is {}", stickerSize);
		logger.debug("Viewport size is {}x{}", VIEWPORT_SIZE, VIEWPORT_SIZE);
		logger.debug("Start position of drawing the cube is {}", drawingStartPosition);
	}

	/**
	 * This method builds up the actual cube to display.
	 *
	 * @return A {@code Group} that contains the built cube.
	 */
	public Group buildCube() {

		gameField = new Group();

		PhongMaterial blackMaterial = new PhongMaterial();
		blackMaterial.setDiffuseColor(Color.BLACK);
		blackMaterial.setSpecularColor(Color.WHITE);
		Box piece;

		for (int i = 0; i < cubeSize; ++i) {
			for (int j = 0; j < cubeSize; ++j) {
				for (int k = 0; k < cubeSize; ++k) {

					//Only outer cubes needed
					if ((i == 0 || i == cubeSize - 1)
							|| (j == 0 || j == cubeSize - 1)
							|| (k == 0 || k == cubeSize - 1)) {

						piece = new Box(partSize, partSize, partSize);

						piece.setMaterial(blackMaterial);
						piece.setTranslateX(drawingStartPosition + i * (partSize * gap));
						piece.setTranslateY(drawingStartPosition + j * (partSize * gap));
						piece.setTranslateZ(drawingStartPosition + k * (partSize * gap));

						gameField.getChildren().add(piece);
					}
				}
			}
		}

		for (int i = 0; i < Cube.NUMBER_OF_SIDES; ++i) {
			for (int j = 0; j < cubeSize; ++j) {
				for (int k = 0; k < cubeSize; ++k) {

					PhongMaterial stickerMaterial = new PhongMaterial();

					//Onto which side should the sticker be translated
					Box sticker = new Box(0, 0, 0);
					switch (cube.getSides()[i].getPosition()) {
						case 'U':
							sticker = new Box(stickerSize, 2, stickerSize);

							sticker.setTranslateX(drawingStartPosition + k * (partSize * gap));
							sticker.setTranslateY(drawingStartPosition - ((partSize * gap) / 2));
							sticker.setTranslateZ(drawingStartPosition + (cubeSize - 1) * (partSize * gap)
									- j * (partSize * gap));

							break;
						case 'F':
							sticker = new Box(stickerSize, stickerSize, 2);

							sticker.setTranslateX(drawingStartPosition + k * (partSize * gap));
							sticker.setTranslateY(drawingStartPosition + j * (partSize * gap));
							sticker.setTranslateZ(drawingStartPosition - ((partSize * gap) / 2));

							break;
						case 'R':
							sticker = new Box(2, stickerSize, stickerSize);

							sticker.setTranslateX(drawingStartPosition + (cubeSize - 1) * (partSize * gap)
									+ ((partSize * gap) / 2));
							sticker.setTranslateY(drawingStartPosition + j * (partSize * gap));
							sticker.setTranslateZ(drawingStartPosition + k * (partSize * gap));

							break;
						case 'D':
							sticker = new Box(stickerSize, 2, stickerSize);

							sticker.setTranslateX(drawingStartPosition + k * (partSize * gap));
							sticker.setTranslateY(drawingStartPosition + (cubeSize - 1) * (partSize * gap)
									+ ((partSize * gap) / 2));
							sticker.setTranslateZ(drawingStartPosition + j * (partSize * gap));

							break;
						case 'L':
							sticker = new Box(2, stickerSize, stickerSize);

							sticker.setTranslateX(drawingStartPosition
									- ((partSize * gap) / 2));
							sticker.setTranslateY(drawingStartPosition + j * (partSize * gap));
							sticker.setTranslateZ(drawingStartPosition + (cubeSize - 1) * (partSize * gap)
									- k * (partSize * gap));
							break;
						case 'B':
							sticker = new Box(stickerSize, stickerSize, 2);

							sticker.setTranslateX(drawingStartPosition + (cubeSize - 1) * (partSize * gap)
									- k * (partSize * gap));
							sticker.setTranslateY(drawingStartPosition + j * (partSize * gap));
							sticker.setTranslateZ(drawingStartPosition + (cubeSize - 1) * (partSize * gap)
									+ ((partSize * gap) / 2));
							break;
					}

					stickerMaterial = setMatriceColor(i, j, k);
					sticker.setMaterial(stickerMaterial);
					gameField.getChildren().add(sticker);
				}
			}
		}

		logger.info("Group of the cube created");
		return gameField;
	}

	/**
	 * Returns a {@link javafx.scene.paint.PhongMaterial} object that has the proper coloring corresponding to the sticker on the cube specified by the parameters.
	 *
	 * @param sideIndex The index of the side. Its value must be in the interval [0, {@link hu.unideb.inf.rubikscube.model.Cube#NUMBER_OF_SIDES}[.
	 * @param j The row index on the side
	 * @param k The column index on the side
	 * @return A {@link javafx.scene.paint.PhongMaterial} object that is colored corresponding to the specified sticker on the cube.
	 */
	public PhongMaterial setMatriceColor(int sideIndex, int j, int k) {

		PhongMaterial stickerMaterial = new PhongMaterial();
		try {
			switch (cube.getSides()[sideIndex].getColorAt(j, k)) {
				case RED:
					stickerMaterial.setDiffuseColor(Color.RED);
					stickerMaterial.setSpecularColor(Color.RED);
					break;
				case WHITE:
					stickerMaterial.setDiffuseColor(Color.WHITE);
					stickerMaterial.setSpecularColor(Color.WHITE);
					break;
				case GREEN:
					stickerMaterial.setDiffuseColor(Color.GREEN);
					stickerMaterial.setSpecularColor(Color.GREEN);
					break;
				case ORANGE:
					stickerMaterial.setDiffuseColor(Color.DARKORANGE);
					stickerMaterial.setSpecularColor(Color.DARKORANGE);
					break;
				case YELLOW:
					stickerMaterial.setDiffuseColor(Color.YELLOW);
					stickerMaterial.setSpecularColor(Color.YELLOW);
					break;
				case BLUE:
					stickerMaterial.setDiffuseColor(Color.BLUE);
					stickerMaterial.setSpecularColor(Color.BLUE);
					break;
				default:
					logger.error("Could not find the color at indices {} {}", j, k);
					break;
			}
		} catch (ArrayIndexOutOfBoundsException exception) {
			logger.error("Invalid side index {}", sideIndex);
			logger.error("{}", exception.getMessage());
		}
		return stickerMaterial;
	}

	@FXML
	private void mousePressedOnGameField(MouseEvent event) {

		if (!layerDragged) {
			mouseOldX = event.getSceneX();
			mouseOldY = event.getSceneY();

			logger.debug("Mouse coordinates are x:{}, y:{}",
					mouseOldX, mouseOldY);
		}
	}

	@FXML
	private void mouseDraggedOnGameField(MouseEvent event) {

		if (!layerDragged) {
			rotateX.setAngle(rotateX.getAngle() - (event.getSceneY() - mouseOldY));
			rotateY.setAngle(rotateY.getAngle() + (event.getSceneX() - mouseOldX));
			rotateZ.setAngle(rotateZ.getAngle()
					- (event.getSceneY() - mouseOldY) + (event.getSceneX() - mouseOldX));
			mouseOldX = event.getSceneX();
			mouseOldY = event.getSceneY();

			logger.debug("Mouse coordinates are x:{}, y:{}",
					mouseOldX, mouseOldY);
		}

	}

	@FXML
	private void mouseDragReleasedOnGameField(MouseEvent event) {
		layerDragged = false;
	}

	@FXML
	private void mouseDragDetectedOnGameField(MouseEvent event) {
		gameAnchorPane.startFullDrag();
	}

	@FXML
	private void handleScrollEvent(ScrollEvent event) {

		double zoomFactor = 1.0;
		double deltaY = event.getDeltaY();
		if (deltaY < 0 && gameFieldScale > 0.5) {
			zoomFactor = 0.95;
		} else if (deltaY > 0) {
			if (cube.getCubeSize() > 15 && gameFieldScale < 1.8) {
				zoomFactor = 1.05;
			} else if (cube.getCubeSize() <= 15 && gameFieldScale < 1.5) {
				zoomFactor = 1.05;
			}
		}

		logger.info("Scale amount is {}", gameFieldScale);

		gameField.setScaleX(gameFieldScale * zoomFactor);
		gameField.setScaleY(gameFieldScale * zoomFactor);
		gameField.setScaleZ(gameFieldScale * zoomFactor);
		gameFieldScale = gameField.getScaleX();
	}

	/**
	 * This method sets the mouse action functions for the specified {@code gameField}.
	 *
	 * If the cube is recreated when a layer is rotated, calling this method is necessary.
	 *
	 * @param gameField The {@link javafx.scene.Group} that contains the cube.
	 */
	public void setMouseActions(Group gameField) {

		gameField.setOnMousePressed((MouseEvent event) -> {

			layerDragged = true;
			rotateOldX = event.getX();
			rotateOldY = event.getY();
			rotateOldZ = event.getZ();

			layerDragged = false;
		});

		gameField.setOnDragDetected(event -> gameField.startFullDrag());

		gameField.setOnMouseDragged((MouseEvent event) -> {

			layerDragged = true;

			double x = event.getX();
			double y = event.getY();
			double z = event.getZ();

			int indexI = -1;
			int indexK = -1;
			int indexJ = -1;

			double startOfCube = drawingStartPosition - (partSize / 2);
			double endOfCube = startOfCube + cubeSize * (partSize * gap);

			//Additional accuracy needed because of double's
			double ac = 1.0;

			for (int i = 0; i < cubeSize; ++i) {
				for (int j = 0; j < cubeSize; ++j) {
					for (int k = 0; k < cubeSize; ++k) {

						if (rotateOldX >= startOfCube + i * (partSize * gap) - ac
								&& rotateOldX <= startOfCube + (i + 1) * (partSize * gap) + ac) {
							indexI = i;
						}
						if (rotateOldY >= startOfCube + j * (partSize * gap) - ac
								&& rotateOldY <= startOfCube + (j + 1) * (partSize * gap) + ac) {
							indexJ = j;
						}
						if (rotateOldZ >= startOfCube + k * (partSize * gap) - ac
								&& rotateOldZ <= startOfCube + (k + 1) * (partSize * gap) + ac) {
							indexK = k;
						}
					}
				}
			}

			char sidePosition = getSelectedSide(x, y, z);
			Rotation layerRotation = getLayerRotation(sidePosition, indexI, indexJ, indexK);
			Point3D drag = new Point3D(x, y, z).subtract(rotateOldX, rotateOldY, rotateOldZ);
			logger.debug("Dragging amount is {}", drag.magnitude());
			if (drag.magnitude() > 30) {
				cube.rotate(layerRotation);

				setupGameField();
				rotateOldX = 0;
				rotateOldY = 0;
				rotateOldZ = 0;

				logger.debug("Corner indices at dragging point i:{}, j:{}, k:{}",
						indexI, indexJ, indexK);
				logger.debug("Dragging direction of the layer is {}", dragDirection);
				logger.debug("Dragging of the layer occured on side {}", sidePosition);
				logger.debug("Noticed rotation {}", layerRotation);
			}

		});

		gameField.setOnMouseDragReleased(event -> {
			layerDragged = false;
		});

		gameField.setOnScroll((ScrollEvent event) -> handleScrollEvent(event));
	}

	/**
	 * This method builds up the field of the game.
	 *
	 * Calling this method is necessary if a rotation of a layer occurred so that the cube must be rebuilt.
	 */
	public void setupGameField() {

		if (gameAnchorPane.getChildren().contains(gameField)) {
			gameAnchorPane.getChildren().remove(gameField);
		}

		setDrawDataForCube();
		gameField = buildCube();
		gameField.getChildren().add(ambientLight);
		gameField.setDepthTest(DepthTest.ENABLE);
		gameField.getTransforms().addAll(rotateX, rotateY, rotateZ);
		gameField.setScaleX(gameFieldScale);
		gameField.setScaleY(gameFieldScale);
		gameField.setScaleZ(gameFieldScale);
		gameAnchorPane.getChildren().add(gameField);
		setMouseActions(gameField);

		logger.info("Gamefield creation successful");
	}

	/**
	 * {@inheritDoc}.
	 *
	 * @param url {@inheritDoc}
	 * @param rb {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ambientLight.setColor(Color.WHITE);
		setupGameField();
	}

	/**
	 * Returns the number of the layer at the specified {@code index}.
	 *
	 * Indexing the pieces of the cube starts in the most left-up-front corner and grows into the other directions. Based on this type of indexing, this method transforms the specified {@code index} to a layer number on the cube which absolute value can be used for identifying the layers among the same type of rotations. Note that this method will return negative values too, so modifying to its absolute value is required by using the value at identifying the layer among the same type of rotations. Positive values will be returned if the specified {@code index} is closer to the origin of the indexing, negative ones if it's further from the origin.
	 *
	 * @param index The index of the piece used on the cube
	 * @return The number of the layer with side distinction, so that negative values can be returned too
	 * @see Rotation
	 */
	public int getLayerNumber(int index) {

		int layerNumber = index;
		//When size of the cube is odd:
		if (cubeSize % 2 == 1) {
			layerNumber -= (cubeSize / 2);
			if (cubeSize == 3) {
				return layerNumber * -1;
			}
			if (layerNumber < 0) {
				layerNumber *= -1;
			}
			if (layerNumber != 0) {
				layerNumber = ((cubeSize / 2) + 1) - layerNumber;
			}
			if (index > cubeSize / 2) {
				layerNumber *= -1;
			}
		} //When size of the cube is even:
		else {
			layerNumber -= (cubeSize / 2);
			if (layerNumber >= 0) {
				layerNumber += 1;
			}
			if (cubeSize == 2) {
				return layerNumber * -1;
			}
			if (layerNumber < 0) {
				layerNumber *= -1;
			}
			layerNumber = ((cubeSize / 2) + 1) - layerNumber;
			if (index >= cubeSize / 2) {
				layerNumber *= -1;
			}
		}

		return layerNumber;
	}

	/**
	 * Returns a rotation that fits the specified parameters.
	 *
	 * Based on the {@code sidePosition}, the actual side where the user started dragging on the cube, the appropriate index will be used with the dragging direction specified by {@link #getSelectedSide(double, double, double)}. This method creates the right rotation from all these data.
	 *
	 * @param sidePosition The side where the user started dragging on the cube
	 * @param indexI The x-axis index of a piece on the cube
	 * @param indexJ The y-axis index of a piece on the cube
	 * @param indexK The z-axis index of a piece on the cube
	 * @return A {@link hu.unideb.inf.rubikscube.model.Rotation} built from the given parameters
	 */
	public Rotation getLayerRotation(char sidePosition, int indexI, int indexJ, int indexK) {

		Rotation layerRotation = null;
		int layerNumber = 0;
		switch (sidePosition) {
			case 'U':
				switch (dragDirection) {
					case RIGHT:
						layerNumber = getLayerNumber(indexK);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "S");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "F");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "B'");
						}
						break;
					case UP:
						layerNumber = getLayerNumber(indexI);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "M'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "L'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "R");
						}
						break;
					case LEFT:
						layerNumber = getLayerNumber(indexK);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "S'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "F'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "B");
						}
						break;
					case DOWN:
						layerNumber = getLayerNumber(indexI);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "M");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "L");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "R'");
						}
						break;
				}
				break;

			case 'F':
				switch (dragDirection) {
					case RIGHT:
						layerNumber = getLayerNumber(indexJ);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "E'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "U'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "D");
						}
						break;
					case UP:
						layerNumber = getLayerNumber(indexI);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "M'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "L'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "R");
						}
						break;
					case LEFT:
						layerNumber = getLayerNumber(indexJ);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "E");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "U");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "D'");
						}
						break;
					case DOWN:
						layerNumber = getLayerNumber(indexI);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "M");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "L");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "R'");
						}
						break;
				}
				break;

			case 'D':
				switch (dragDirection) {
					case RIGHT:
						layerNumber = getLayerNumber(indexK);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "S'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "F'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "B");
						}
						break;
					case UP:
						layerNumber = getLayerNumber(indexI);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "M'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "L'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "R");
						}
						break;
					case LEFT:
						layerNumber = getLayerNumber(indexK);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "S");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "F");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "B'");
						}
						break;
					case DOWN:
						layerNumber = getLayerNumber(indexI);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "M");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "L");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "R'");
						}
						break;
				}
				break;
			case 'B':
				switch (dragDirection) {
					case RIGHT:
						layerNumber = getLayerNumber(indexJ);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "E'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "U'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "D");
						}
						break;
					case UP:
						layerNumber = getLayerNumber(indexI);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "M");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "L");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "R'");
						}
						break;
					case LEFT:
						layerNumber = getLayerNumber(indexJ);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "E");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "U");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "D'");
						}
						break;
					case DOWN:
						layerNumber = getLayerNumber(indexI);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "M'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "L'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "R");
						}
						break;
				}
				break;
			case 'R':
				switch (dragDirection) {
					case RIGHT:
						layerNumber = getLayerNumber(indexJ);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "E'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "U'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "D");
						}
						break;
					case UP:
						layerNumber = getLayerNumber(indexK);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "S'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "F'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "B");
						}
						break;
					case LEFT:
						layerNumber = getLayerNumber(indexJ);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "E");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "U");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "D'");
						}
						break;
					case DOWN:
						layerNumber = getLayerNumber(indexK);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "S");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "F");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "B'");
						}
						break;
				}
				break;
			case 'L':
				switch (dragDirection) {
					case RIGHT:
						layerNumber = getLayerNumber(indexJ);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "E'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "U'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "D");
						}
						break;
					case UP:
						layerNumber = getLayerNumber(indexK);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "S");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "F");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "B'");
						}
						break;
					case LEFT:
						layerNumber = getLayerNumber(indexJ);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "E");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "U");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "D'");
						}
						break;
					case DOWN:
						layerNumber = getLayerNumber(indexK);
						if (layerNumber == 0) {
							layerRotation = new Rotation(layerNumber, "S'");
						} else if (layerNumber > 0) {
							layerRotation = new Rotation(layerNumber, "F'");
						} else if (layerNumber < 0) {
							layerRotation = new Rotation(Math.abs(layerNumber), "B");
						}
						break;
				}
				break;
		}

		return layerRotation;
	}

	/**
	 * Returns the side position where the user started dragging on the cube, and sets the direction of its dragging.
	 *
	 * Note that parameters {@code x, y, z} are the actual position of the mouse, not the position of where the dragging started.
	 *
	 * @param x The present x coordinate of the mouse in the coordinate system of the cube
	 * @param y The present y coordinate of the mouse in the coordinate system of the cube
	 * @param z The present z coordinate of the mouse in the coordinate system of the cube
	 * @return The side position where the dragging started or a null character if the side position couldn't be detected
	 */
	public char getSelectedSide(double x, double y, double z) {

		//Dragging direction
		Point3D dir = new Point3D(x, y, z).subtract(rotateOldX, rotateOldY, rotateOldZ);

		double startOfCube = drawingStartPosition - (partSize / 2);
		double endOfCube = startOfCube + cubeSize * (partSize * gap);
		//Additional accuracy needed because of double's
		double ac = 1.0;
		if (cubeSize == 2 || cubeSize == 3) {
			ac = 3.0;
		}

		if (rotateOldX <= startOfCube + ac) {
			dragDirection = Math.abs(dir.getZ()) > Math.abs(dir.getY()) ? DragDirection.LEFT : DragDirection.DOWN;
			if (dragDirection == DragDirection.LEFT && dir.getZ() < 0) {
				dragDirection = DragDirection.RIGHT;
			}
			if (dragDirection == DragDirection.DOWN && dir.getY() < 0) {
				dragDirection = DragDirection.UP;
			}

			return 'L';
		}
		if (rotateOldX >= endOfCube - ac) {
			dragDirection = Math.abs(dir.getZ()) > Math.abs(dir.getY()) ? DragDirection.RIGHT : DragDirection.DOWN;
			if (dragDirection == DragDirection.RIGHT && dir.getZ() < 0) {
				dragDirection = DragDirection.LEFT;
			}
			if (dragDirection == DragDirection.DOWN && dir.getY() < 0) {
				dragDirection = DragDirection.UP;
			}

			return 'R';
		}
		if (rotateOldY <= startOfCube + ac) {
			dragDirection = Math.abs(dir.getX()) > Math.abs(dir.getZ()) ? DragDirection.RIGHT : DragDirection.UP;
			if (dragDirection == DragDirection.RIGHT && dir.getX() < 0) {
				dragDirection = DragDirection.LEFT;
			}
			if (dragDirection == DragDirection.UP && dir.getZ() < 0) {
				dragDirection = DragDirection.DOWN;
			}
			return 'U';
		}
		if (rotateOldY >= endOfCube - ac) {
			dragDirection = Math.abs(dir.getX()) > Math.abs(dir.getZ()) ? DragDirection.RIGHT : DragDirection.DOWN;
			if (dragDirection == DragDirection.RIGHT && dir.getX() < 0) {
				dragDirection = DragDirection.LEFT;
			}
			if (dragDirection == DragDirection.DOWN && dir.getZ() < 0) {
				dragDirection = DragDirection.UP;
			}
			return 'D';
		}
		if (rotateOldZ <= startOfCube + ac) {
			dragDirection = Math.abs(dir.getX()) > Math.abs(dir.getY()) ? DragDirection.RIGHT : DragDirection.DOWN;
			if (dragDirection == DragDirection.RIGHT && dir.getX() < 0) {
				dragDirection = DragDirection.LEFT;
			}
			if (dragDirection == DragDirection.DOWN && dir.getY() < 0) {
				dragDirection = DragDirection.UP;
			}
			return 'F';
		}
		if (rotateOldZ >= startOfCube - ac) {
			dragDirection = Math.abs(dir.getX()) > Math.abs(dir.getY()) ? DragDirection.LEFT : DragDirection.DOWN;
			if (dragDirection == DragDirection.LEFT && dir.getX() < 0) {
				dragDirection = DragDirection.RIGHT;
			}
			if (dragDirection == DragDirection.DOWN && dir.getY() < 0) {
				dragDirection = DragDirection.UP;
			}

			return 'B';
		}

		return '\0';
	}

	/**
	 * Returns the {@code Group} that contains the cube.
	 *
	 * @return A {@code Group} that contains the cube
	 */
	public Group getGameField() {
		return gameField;
	}

	/**
	 * Returns the {@link hu.unideb.inf.rubikscube.model.Cube} used in the game.
	 *
	 * @return The {@link hu.unideb.inf.rubikscube.model.Cube} used in the game
	 */
	public Cube getCube() {
		return cube;
	}

	/**
	 * Sets the {@link hu.unideb.inf.rubikscube.model.Cube} used in the game.
	 *
	 * @param cube The new cube to use in the game
	 */
	public void setCube(Cube cube) {
		this.cube = cube;
	}

	/**
	 * Sets the {@link javafx.scene.Group} that contains the cube.
	 *
	 * @param gameField The {@link javafx.scene.Group} that contains the cube
	 */
	public void setGameField(Group gameField) {
		this.gameField = gameField;
	}
}
