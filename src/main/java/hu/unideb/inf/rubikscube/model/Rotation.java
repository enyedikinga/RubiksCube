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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a rotation of a layer on a Rubik's cube.
 *
 * <p>
 * Rotating a layer of a Rubik's cube means turning the layer by either 90, -90 or 180 degrees. Each rotation can be represented with two components, {@code layerNumber} and {@code rotationType}.</p>
 * <p>
 * {@code layerNumber} is a non-negative integer which is used for identifying which layer should be rotated from the ones that have the same {@code rotationType}. 1 means the most outer layer, 2 means the layer inward next to the most outer layer, etc... 0 means the most middle layer only if the size of the cube is <i>odd</i>, otherwise there's no layer with {@code layerNumber} 0. A valid {@code layerNumber} is in the interval {@code [0, (x/2)]} if {@code x} is odd, {@code [1, (x/2)]} if {@code x} is even, where {@code x >= 2}, means the size of the cube, and {@code (x/2)} means the integer part of the division.</p>
 * <p>
 * {@code rotationType} is a string that tells on which side should the layer with {@code layerNumber} be rotated, and to which direction should it be rotated. So a {@code rotationType} consists of two components, a side position, and the direction of the rotation.</p>
 * <p>
 * The side position means the location of the side on the cube, which can be up-, down-, right-, left-, front- and back-side, relative to the storage of the data of the cube. These values are represented with a single character U, D, R, L, F, B. There are three more additional place to rotate that can't be bound to any sides. These are the most middle layers on the cubes with an odd size, the middle layer through sides U-F-D-B is M, the middle layer through sides F-R-B-L is E, and the middle layer through sides U-R-B-L is S.</p>
 * <p>
 * The second component, the direction of the rotation can be clockwise (no additional representation), counterclockwise (represented as a single apostrophe, '), and two-times-rotation (represented as a number two, 2). The direction is always interpreted from a special viewing position, as if the actual side would be in front of us.</p>
 * <p>
 * For example, the rotation 2R' on a 4x4x4-sized cube means rotating the second layer from the right side counterclockwise (from a viewing position as if the side would be in front of us), so into the direction of the user. The 3L on a 7x7x7-sized cube means rotating the third layer from the left side clockwise (from a viewing position as if the side would be in front of us), so into the direction of the user.
 * </p>
 *
 * @author kinga
 */
public class Rotation {

	private int layerNumber;
	private String rotationType;

	/**
	 * This list contains all those rotations that can be applied to any kind of cube regardless to its size.
	 *
	 * <p>
	 * This list is used to generate a valid scramble.</p>
	 */
	public static final ArrayList<Rotation> BASIC_ROTATIONS = new ArrayList<>(
			Arrays.asList(
					new Rotation(1, "R"),
					new Rotation(1, "R'"),
					new Rotation(1, "R2"),
					new Rotation(1, "U"),
					new Rotation(1, "U'"),
					new Rotation(1, "U2"),
					new Rotation(1, "F"),
					new Rotation(1, "F'"),
					new Rotation(1, "F2"),
					new Rotation(1, "L"),
					new Rotation(1, "L'"),
					new Rotation(1, "L2"),
					new Rotation(1, "D"),
					new Rotation(1, "D'"),
					new Rotation(1, "D2"),
					new Rotation(1, "B"),
					new Rotation(1, "B'"),
					new Rotation(1, "B2")
			)
	);

	private static Logger logger = LoggerFactory.getLogger(Rotation.class);

	/**
	 * Creates a {@link Rotation} object from the specified {@code layerNumber} and {@code rotationType}.
	 *
	 * @param layerNumber An integer that tells which layer should be rotated from the type {@code rotationType}
	 * @param rotationType A string that tells the type of the rotation
	 * @see Rotation
	 */
	public Rotation(int layerNumber, String rotationType) {
		this.layerNumber = layerNumber;
		this.rotationType = rotationType;
	}

	/**
	 * Returns the type of the rotation.
	 *
	 * @return The type of the rotation.
	 * @see Rotation
	 */
	public String getRotationType() {
		return rotationType;
	}

	/**
	 * Returns the number of the layer in the rotation.
	 *
	 * @return the number of the layer in the rotation
	 * @see Rotation
	 */
	public int getLayerNumber() {
		return layerNumber;
	}

	/**
	 * Returns the side position in the rotation.
	 *
	 * This method returns the side position of the type of the rotation, or those special places of the middle layers that can't be bound to any sides.
	 *
	 * @return the side position in the rotation
	 * @see Rotation
	 */
	public char getSidePosition() {
		return rotationType.charAt(0);
	}

	/**
	 * Returns the name of the rotation.
	 *
	 * The name of the rotation means a string that consists of the number of the layer rotated and the type of the rotation concatenated.
	 *
	 * @return the name of the rotation.
	 */
	public String getName() {
		return layerNumber + rotationType;
	}

	/**
	 * Returns the string representation of this {@link Rotation}.
	 *
	 * The string representation of a {@link Rotation} is the name of the rotation
	 *
	 * @return The string representation of this rotation.
	 * @see #getName()
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Generates a new {@link Rotation} for a {@code targetCubeSize} sized cube.
	 *
	 * This method guarantees that the generated rotation will be applicable on the cube with the specified {@code targetCubeSize}.
	 *
	 * @param targetCubeSize The size of the cube that the rotation must be applicable on
	 * @return A {@link Rotation} that can be applied to a cube with a size {@code targetCubeSize}
	 */
	public static Rotation generateNewRotation(int targetCubeSize) {

		Random random = new Random();
		int newRotationIndex = random.nextInt(BASIC_ROTATIONS.size());
		int maxLayerNumber = targetCubeSize / 2;
		int layerNumber = random.nextInt(maxLayerNumber) + 1;

		Rotation rotation = new Rotation(layerNumber, BASIC_ROTATIONS.get(newRotationIndex).getRotationType());
		logger.info("Generated rotation {} ", rotation);
		return rotation;
	}

	/**
	 * Generates a new {@link Rotation} that is different from {@code previousRotation} for a {@code targetCubeSize} sized cube.
	 *
	 * This method guarantees that the generated rotation will be applicable on the cube with the specified {@code targetCubeSize} and the generated rotation will be different from {@code previousRotation}.
	 *
	 * @param targetCubeSize The size of the cube that the rotation must be applicable on
	 * @param previousRotation The rotation that the new rotation must be different from
	 * @return A {@link Rotation} that can be applied to a cube with a size {@code targetCubeSize} and is different from {@code previousRotation}
	 */
	public static Rotation generateNewDifferentRotation(int targetCubeSize, Rotation previousRotation) {

		Character previous = previousRotation.getSidePosition();
		Rotation nextRotation;
		boolean isSame = true;
		do {
			nextRotation = generateNewRotation(targetCubeSize);
			Character next = nextRotation.getSidePosition();

			if (!previous.equals(next)) {
				isSame = false;
			}
		} while (isSame);

		return nextRotation;
	}

}
