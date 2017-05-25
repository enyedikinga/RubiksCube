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
 * This class represents a side of a Rubik's cube.
 *
 * A side consists of {@link StickerColor} objects, and has also a position which identifies its place on the cube. Possible values of the position of the side can be {@code U, D, R, L, F, B}. If the size of the side is {@code x}, then the number of colors on it is {@code x * x}, so only colors on the actual side are kept track. A side has no information about its surroundings.
 *
 * @author kinga
 */
public class Side {

	private int sideSize;
	private StickerColor[][] colors;
	private char position;

	private static Logger logger = LoggerFactory.getLogger(Side.class);

	/**
	 * Creates a new {@link Side} object from the specified {@code sideSize}, {@code color} at the specified {@code position}.
	 *
	 * The created side will have the given size {@code sideSize}. The whole side will have the same {@code color} specified. The side will be placed at the given {@code position}.
	 *
	 * @param sideSize The size of the side
	 * @param color The color of the whole side
	 * @param position The position where the side will be placed at
	 * @see Side
	 */
	public Side(int sideSize, StickerColor color, char position) {

		this.position = position;
		this.sideSize = sideSize;
		colors = new StickerColor[sideSize][sideSize];

		for (int i = 0; i < sideSize; ++i) {
			for (int j = 0; j < sideSize; ++j) {
				colors[i][j] = color;
			}
		}

		logger.info("Side created at sideposition {} with color {}", position, color);
	}

	/**
	 * This method rotates the side in clockwise direction.
	 */
	public void rotateMainSideClockwise() {

		//On the side: tanspose, then reverse each row
		StickerColor[][] newColors = new StickerColor[sideSize][sideSize];
		for (int i = 0; i < sideSize; ++i) {
			for (int j = 0; j < sideSize; ++j) {
				newColors[i][j] = colors[j][i];
			}
		}

		for (int i = 0; i < sideSize; ++i) {
			for (int j = 0; j < sideSize; ++j) {
				colors[i][j] = newColors[i][sideSize - j - 1];
			}
		}

		logger.info("Mainside {} rotated clockwise", position);
	}

	/**
	 * This method rotates the side in counterclockwise direction.
	 */
	public void rotateMainSideCounterClockwise() {

		//On the side: transpose, then reverse each cols:
		StickerColor[][] newColors = new StickerColor[sideSize][sideSize];
		for (int i = 0; i < sideSize; ++i) {
			for (int j = 0; j < sideSize; ++j) {
				newColors[i][j] = colors[j][i];
			}
		}

		for (int i = 0; i < sideSize; ++i) {
			for (int j = 0; j < sideSize; ++j) {
				colors[i][j] = newColors[sideSize - i - 1][j];
			}
		}
		logger.info("Mainside {} rotated counterclockwise", position);
	}

	/**
	 * Returns the position of the side on the cube.
	 *
	 * @return The position of the side on the cube
	 */
	public char getPosition() {
		return position;
	}

	/**
	 * Returns the color on the side at the specified indices.
	 *
	 * @param i Row index
	 * @param j Column index
	 * @return The color at indices {@code i,j} on the side or {@code null} if the specified indices are out of bounds.
	 */
	public StickerColor getColorAt(int i, int j) {
		try {
			return colors[i][j];
		} catch (ArrayIndexOutOfBoundsException exception) {
			logger.error("Invalid indices {} {}", i, j);
			logger.error("{}", exception.getMessage());
			return null;
		}
	}

	/**
	 * Sets the color on the side at the specified indices.
	 *
	 * @param i Row index
	 * @param j Column index
	 * @param color The new color to be set
	 */
	public void setColorAt(int i, int j, StickerColor color) {
		try {
			colors[i][j] = color;
		} catch (ArrayIndexOutOfBoundsException exception) {
			logger.error("Invalid indices {} {}", i, j);
			logger.error("{}", exception.getMessage());
		}
	}

	/**
	 * Returns the string representation of this {@link Side} object.
	 *
	 * @return The string representation of this side object.
	 */
	@Override
	public String toString() {

		String side = "";
		for (int i = 0; i < sideSize; ++i) {
			for (int j = 0; j < sideSize; ++j) {
				switch (colors[i][j]) {
					case RED:
						side += "r ";
						break;
					case WHITE:
						side += "w ";
						break;
					case GREEN:
						side += "g ";
						break;
					case ORANGE:
						side += "o ";
						break;
					case YELLOW:
						side += "y ";
						break;
					case BLUE:
						side += "b ";
				}
			}
		}
		return side;
	}
}
