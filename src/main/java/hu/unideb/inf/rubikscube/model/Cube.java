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
package hu.unideb.inf.rubikscube.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a Rubik's cube with modifiable size.
 *
 * <p>
 * A Rubik's cube can have several states regarding to the arrangement of the colors on its sides. The <i>default state</i> is when there is only one color on each side so that the cube is solved, otherwise it is scrambled.</p>
 * <p>
 * For example, you can create a 3x3 cube with its default state like this:</p>
 * <pre>
 *	Cube cube = new Cube(3);
 * </pre>
 * <p>
 * Or if you want to set an existing cube to its default state:</p>
 * <pre>
 *	cube.resetSides();
 * </pre>
 * <p>
 * To scramble the cube to a random state, a {@link Scrambler} needs to be specified for the cube with the actual size of the cube, and then needs to be applied to the cube:</p>
 * <pre>
 *	Scrambler scrambler = new Scrambler(3);
 *	cube.scramble(scrambler);
 * </pre>
 * <p>
 * The size of the cube means how many layers the cube consists of.</p>
 *
 * @author kinga
 */
public class Cube {

	/**
	 * The number of the sides of a cube, is always {@value}.
	 */
	public static final int NUMBER_OF_SIDES = 6;
	/**
	 * The default size of the cube is {@value}.
	 */
	public static final int DEFAULT_SIZE = 3;

	private int cubeSize;
	private Side[] sides = new Side[NUMBER_OF_SIDES];

	private static Logger logger = LoggerFactory.getLogger(Cube.class);

	/**
	 * This constructor creates a cube with a size {@code cubeSize} with its default state.
	 *
	 * @param cubeSize The size of the cube
	 */
	public Cube(int cubeSize) {
		if (cubeSize >= 2) {
			this.cubeSize = cubeSize;
			logger.info("Cube created with cubesize {}", cubeSize);
		} else {
			logger.error("Invalid cubesize {}, setting to DEFAULT_SIZE {}", cubeSize, DEFAULT_SIZE);
			this.cubeSize = DEFAULT_SIZE;
		}
		resetSides();
	}

	/**
	 * Sets the cube to its default state.
	 */
	public void resetSides() {

		sides[0] = new Side(cubeSize, StickerColor.RED, 'U');
		sides[1] = new Side(cubeSize, StickerColor.WHITE, 'F');
		sides[2] = new Side(cubeSize, StickerColor.ORANGE, 'D');
		sides[3] = new Side(cubeSize, StickerColor.YELLOW, 'B');
		sides[4] = new Side(cubeSize, StickerColor.GREEN, 'R');
		sides[5] = new Side(cubeSize, StickerColor.BLUE, 'L');

		logger.info("Cube's sides reset to default state");
	}

	/**
	 * Applies a {@link Rotation} {@code rotation} to the cube.
	 *
	 * <p>
	 * As a result, one of the layers of the cube will be rotated with the given {@code rotation}.</p>
	 *
	 * @param rotation The rotation to be applied
	 * @see Rotation
	 */
	public void rotate(Rotation rotation) {

		String rotationName = rotation.getName();
		Side mainSide = getSideAt(rotation.getSidePosition());

		StickerColor tempColor;
		int last = cubeSize - 1;
		int counter = 1;

		if (rotationName.endsWith("2")) {
			counter = 2;
		}

		int layerNr = rotation.getLayerNumber() - 1;
		String rotationType = rotation.getRotationType();
		int middleRowCol = cubeSize / 2;

		for (int k = 0; k < counter; ++k) {
			if (layerNr == 0 && mainSide != null) {
				if (rotationType.contains("'")) {
					mainSide.rotateMainSideCounterClockwise();
				} else {
					mainSide.rotateMainSideClockwise();
				}
			}

			switch (rotationType) {
				case "R2":
				case "R":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(i, last - layerNr);

						getSideAt('U').setColorAt(i, last - layerNr,
								getSideAt('F').getColorAt(i, last - layerNr));
						getSideAt('F').setColorAt(i, last - layerNr,
								getSideAt('D').getColorAt(i, last - layerNr));
						getSideAt('D').setColorAt(i, last - layerNr,
								getSideAt('B').getColorAt(last - i, 0 + layerNr));
						getSideAt('B').setColorAt(last - i, 0 + layerNr,
								tempColor);
					}
					break;
				case "R'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(i, last - layerNr);

						getSideAt('U').setColorAt(i, last - layerNr,
								getSideAt('B').getColorAt(last - i, layerNr));
						getSideAt('B').setColorAt(last - i, layerNr,
								getSideAt('D').getColorAt(i, last - layerNr));
						getSideAt('D').setColorAt(i, last - layerNr,
								getSideAt('F').getColorAt(i, last - layerNr));
						getSideAt('F').setColorAt(i, last - layerNr,
								tempColor);
					}
					break;
				case "U2":
				case "U":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('F').getColorAt(layerNr, i);

						getSideAt('F').setColorAt(layerNr, i,
								getSideAt('R').getColorAt(layerNr, i));
						getSideAt('R').setColorAt(layerNr, i,
								getSideAt('B').getColorAt(layerNr, i));
						getSideAt('B').setColorAt(layerNr, i,
								getSideAt('L').getColorAt(layerNr, i));
						getSideAt('L').setColorAt(layerNr, i,
								tempColor);
					}

					break;
				case "U'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('F').getColorAt(layerNr, i);

						getSideAt('F').setColorAt(layerNr, i,
								getSideAt('L').getColorAt(layerNr, i));
						getSideAt('L').setColorAt(layerNr, i,
								getSideAt('B').getColorAt(layerNr, i));
						getSideAt('B').setColorAt(layerNr, i,
								getSideAt('R').getColorAt(layerNr, i));
						getSideAt('R').setColorAt(layerNr, i,
								tempColor);
					}

					break;
				case "F2":
				case "F":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(last - layerNr, i);

						getSideAt('U').setColorAt(last - layerNr, i,
								getSideAt('L').getColorAt(last - i, last - layerNr));
						getSideAt('L').setColorAt(last - i, last - layerNr,
								getSideAt('D').getColorAt(layerNr, last - i));
						getSideAt('D').setColorAt(layerNr, last - i,
								getSideAt('R').getColorAt(i, layerNr));
						getSideAt('R').setColorAt(i, layerNr, tempColor);

					}
					break;
				case "F'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(last - layerNr, i);

						getSideAt('U').setColorAt(last - layerNr, i,
								getSideAt('R').getColorAt(i, layerNr));
						getSideAt('R').setColorAt(i, layerNr,
								getSideAt('D').getColorAt(layerNr, last - i));
						getSideAt('D').setColorAt(layerNr, last - i,
								getSideAt('L').getColorAt(last - i, last - layerNr));
						getSideAt('L').setColorAt(last - i, last - layerNr, tempColor);
					}
					break;
				case "L2":
				case "L":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(i, layerNr);

						getSideAt('U').setColorAt(i, layerNr,
								getSideAt('B').getColorAt(last - i, last - layerNr));
						getSideAt('B').setColorAt(last - i, last - layerNr,
								getSideAt('D').getColorAt(i, layerNr));
						getSideAt('D').setColorAt(i, layerNr,
								getSideAt('F').getColorAt(i, layerNr));
						getSideAt('F').setColorAt(i, layerNr, tempColor);
					}
					break;
				case "L'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(i, layerNr);

						getSideAt('U').setColorAt(i, layerNr,
								getSideAt('F').getColorAt(i, layerNr));
						getSideAt('F').setColorAt(i, layerNr,
								getSideAt('D').getColorAt(i, layerNr));
						getSideAt('D').setColorAt(i, layerNr,
								getSideAt('B').getColorAt(last - i, last - layerNr));
						getSideAt('B').setColorAt(last - i, last - layerNr, tempColor);
					}
					break;
				case "D2":
				case "D":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('F').getColorAt(last - layerNr, i);

						getSideAt('F').setColorAt(last - layerNr, i,
								getSideAt('L').getColorAt(last - layerNr, i));
						getSideAt('L').setColorAt(last - layerNr, i,
								getSideAt('B').getColorAt(last - layerNr, i));
						getSideAt('B').setColorAt(last - layerNr, i,
								getSideAt('R').getColorAt(last - layerNr, i));
						getSideAt('R').setColorAt(last - layerNr, i, tempColor);
					}
					break;
				case "D'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('F').getColorAt(last - layerNr, i);

						getSideAt('F').setColorAt(last - layerNr, i,
								getSideAt('R').getColorAt(last - layerNr, i));
						getSideAt('R').setColorAt(last - layerNr, i,
								getSideAt('B').getColorAt(last - layerNr, i));
						getSideAt('B').setColorAt(last - layerNr, i,
								getSideAt('L').getColorAt(last - layerNr, i));
						getSideAt('L').setColorAt(last - layerNr, i, tempColor);
					}
					break;
				case "B2":
				case "B":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(layerNr, i);

						getSideAt('U').setColorAt(layerNr, i,
								getSideAt('R').getColorAt(i, last - layerNr));
						getSideAt('R').setColorAt(i, last - layerNr,
								getSideAt('D').getColorAt(last - layerNr, last - i));
						getSideAt('D').setColorAt(last - layerNr, last - i,
								getSideAt('L').getColorAt(last - i, layerNr));
						getSideAt('L').setColorAt(last - i, layerNr, tempColor);
					}
					break;
				case "B'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(layerNr, i);

						getSideAt('U').setColorAt(layerNr, i,
								getSideAt('L').getColorAt(last - i, layerNr));
						getSideAt('L').setColorAt(last - i, layerNr,
								getSideAt('D').getColorAt(last - layerNr, last - i));
						getSideAt('D').setColorAt(last - layerNr, last - i,
								getSideAt('R').getColorAt(i, last - layerNr));
						getSideAt('R').setColorAt(i, last - layerNr, tempColor);
					}
					break;
				case "E2":
				case "E":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('F').getColorAt(middleRowCol, i);

						getSideAt('F').setColorAt(middleRowCol, i,
								getSideAt('R').getColorAt(middleRowCol, i));
						getSideAt('R').setColorAt(middleRowCol, i,
								getSideAt('B').getColorAt(middleRowCol, i));
						getSideAt('B').setColorAt(middleRowCol, i,
								getSideAt('L').getColorAt(middleRowCol, i));
						getSideAt('L').setColorAt(middleRowCol, i, tempColor);
					}
					break;
				case "E'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('F').getColorAt(middleRowCol, i);

						getSideAt('F').setColorAt(middleRowCol, i,
								getSideAt('L').getColorAt(middleRowCol, i));
						getSideAt('L').setColorAt(middleRowCol, i,
								getSideAt('B').getColorAt(middleRowCol, i));
						getSideAt('B').setColorAt(middleRowCol, i,
								getSideAt('R').getColorAt(middleRowCol, i));
						getSideAt('R').setColorAt(middleRowCol, i, tempColor);
					}
					break;
				case "M2":
				case "M":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('F').getColorAt(i, middleRowCol);

						getSideAt('F').setColorAt(i, middleRowCol,
								getSideAt('U').getColorAt(i, middleRowCol));
						getSideAt('U').setColorAt(i, middleRowCol,
								getSideAt('B').getColorAt(cubeSize - i - 1, middleRowCol));
						getSideAt('B').setColorAt(cubeSize - i - 1, middleRowCol,
								getSideAt('D').getColorAt(i, middleRowCol));
						getSideAt('D').setColorAt(i, middleRowCol, tempColor);
					}
					break;
				case "M'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('F').getColorAt(i, middleRowCol);

						getSideAt('F').setColorAt(i, middleRowCol,
								getSideAt('D').getColorAt(i, middleRowCol));
						getSideAt('D').setColorAt(i, middleRowCol,
								getSideAt('B').getColorAt(cubeSize - i - 1, middleRowCol));
						getSideAt('B').setColorAt(cubeSize - i - 1, middleRowCol,
								getSideAt('U').getColorAt(i, middleRowCol));
						getSideAt('U').setColorAt(i, middleRowCol, tempColor);
					}
					break;
				case "S2":
				case "S":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(middleRowCol, i);

						getSideAt('U').setColorAt(middleRowCol, i,
								getSideAt('L').getColorAt(cubeSize - i - 1, middleRowCol));
						getSideAt('L').setColorAt(cubeSize - i - 1, middleRowCol,
								getSideAt('D').getColorAt(middleRowCol, cubeSize - i - 1));
						getSideAt('D').setColorAt(middleRowCol, cubeSize - i - 1,
								getSideAt('R').getColorAt(i, middleRowCol));
						getSideAt('R').setColorAt(i, middleRowCol, tempColor);
					}
					break;
				case "S'":
					for (int i = 0; i < cubeSize; ++i) {
						tempColor = getSideAt('U').getColorAt(middleRowCol, i);

						getSideAt('U').setColorAt(middleRowCol, i,
								getSideAt('R').getColorAt(i, middleRowCol));
						getSideAt('R').setColorAt(i, middleRowCol,
								getSideAt('D').getColorAt(middleRowCol, cubeSize - i - 1));
						getSideAt('D').setColorAt(middleRowCol, cubeSize - i - 1,
								getSideAt('L').getColorAt(cubeSize - i - 1, middleRowCol));
						getSideAt('L').setColorAt(cubeSize - i - 1, middleRowCol, tempColor);
					}
					break;
				default:
					logger.error("Invalid rotation type {}", rotationType);
					break;
			}
		}
		logger.info("Cube was rotated with rotation {}", rotationName);
	}

	/**
	 * Scrambles the cube with the specified {@code scrambler}.
	 *
	 * <p>
	 * This method applies a scramble specified with {@code scrambler} to the cube. The given scramble will be applied to the <i>current</i> state of the cube regardless of whether it's already scrambled or solved.</p>
	 *
	 * @param scrambler A {@link Scrambler} that contains the scramble
	 * @see Scrambler
	 */
	public void scramble(Scrambler scrambler) {

		Rotation[] scramble = scrambler.getScramble();

		for (int i = 0; i < scramble.length; ++i) {
			rotate(scramble[i]);
		}
		logger.info("Cube scrambled");
	}

	/**
	 * Returns the side at the specified {@code position}.
	 *
	 * @param position The position of the side
	 * @return The side at {@code position} if the specified {@code position} is valid, otherwise {@code null}
	 */
	public Side getSideAt(char position) {

		for (int i = 0; i < NUMBER_OF_SIDES; ++i) {
			if (sides[i].getPosition() == position) {
				return sides[i];
			}
		}

		logger.debug("Invalid sideposition {}", position);
		return null;
	}

	/**
	 * Returns the sides of the cube.
	 *
	 * @return The sides of the cube
	 */
	public Side[] getSides() {
		return sides;
	}

	/**
	 * Returns the size of the cube.
	 *
	 * @return The size of the cube
	 */
	public int getCubeSize() {
		return cubeSize;
	}

	/**
	 * Returns the string representation of this {@link Cube} object.
	 *
	 * @return The string representation of this cube object
	 */
	@Override
	public String toString() {

		String cubeAsString = "";

		for (int i = 0; i < NUMBER_OF_SIDES; ++i) {
			cubeAsString += sides[i] + "\n";
		}

		return cubeAsString;
	}
}
