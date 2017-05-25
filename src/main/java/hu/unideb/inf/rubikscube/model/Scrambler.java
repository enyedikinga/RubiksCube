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
 * This class represents a scramble that a Rubik's cube can be scrambled with.
 *
 * <p>
 * To start solving a Rubik's cube, the default, solved state of the cube must be changed to a state that has an acceptable amount of disorder so that the user can start solving the cube. This class represents such a scramble that can cause such disorder on the cube.</p>
 *
 * <p>
 * A scramble is an actual sequence of rotations which can be applied to a cube causing a disorder. Each cube with different size needs a different amount of scrambling to get an acceptable amount of disorder.</p>
 *
 * @see Rotation
 * @author kinga
 */
public class Scrambler {

	private Logger logger = LoggerFactory.getLogger(Scrambler.class);

	private Rotation[] scramble;
	private int targetCubeSize;

	/**
	 * Creates a {@link Scrambler} object for a cube with a size {@code targetCubeSize}.
	 *
	 * The created object will only be able to generate proper scramble for cubes those size is {@code targetCubeSize}. Because different sized cubes need different amount of scrambling, the length of the generated scramble will vary on the {@code targetCubeSize}.
	 *
	 * @param targetCubeSize The size of the cube that the scramble must be applicable on
	 */
	public Scrambler(int targetCubeSize) {
		this.targetCubeSize = targetCubeSize;
		int scrambleLength = 0;
		switch (targetCubeSize) {
			case 2:
				scrambleLength = 15;
				break;
			case 3:
				scrambleLength = 25;
				break;
			default:
				scrambleLength = targetCubeSize * 20 - 40;
				break;

		}
		scramble = new Rotation[scrambleLength];

		generateNewScramble();
		logger.info("New scrambler created for cubesize {} with a length of {}",
				targetCubeSize, scrambleLength);
	}

	/**
	 * This method generates a new scramble for cubes with a size specified in the constructor.
	 */
	public void generateNewScramble() {

		scramble[0] = Rotation.generateNewRotation(targetCubeSize);
		scramble[1] = Rotation.generateNewDifferentRotation(targetCubeSize, scramble[0]);

		for (int i = 2; i < scramble.length; ++i) {
			Character currentSide;
			Character previousSide;
			do {
				scramble[i] = Rotation.generateNewDifferentRotation(targetCubeSize, scramble[i - 1]);
				currentSide = scramble[i].getSidePosition();
				previousSide = scramble[i - 2].getSidePosition();
			} while (currentSide.equals(previousSide));
		}
		logger.info("Generated new scramble {}", scramble);
	}

	/**
	 * Returns the scramble.
	 *
	 * @return The scramble
	 */
	public Rotation[] getScramble() {
		return scramble;
	}

	/**
	 * Returns the string representation of this {@link Scrambler}.
	 *
	 * The string representation of a {@link Scrambler} object is sequence of {@link Rotation} objects separated by spaces.
	 *
	 * @return The string representation of this scramble
	 */
	@Override
	public String toString() {
		String theScramble = scramble[0].getName();
		for (int i = 1; i < scramble.length; ++i) {
			theScramble += " " + scramble[i].getName();
		}

		return theScramble;
	}
}
