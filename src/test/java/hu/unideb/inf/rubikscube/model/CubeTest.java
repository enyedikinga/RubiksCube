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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kinga
 */
public class CubeTest {

	public CubeTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of resetSides method, of class Cube.
	 */
	@Test
	public void testResetSides() {
		Cube cube = new Cube(Cube.DEFAULT_SIZE);
		Scrambler scrambler = new Scrambler(Cube.DEFAULT_SIZE);
		cube.scramble(scrambler);
		cube.resetSides();
		for (int i = 0; i < Cube.NUMBER_OF_SIDES; ++i) {
			StickerColor color = cube.getSides()[i].getColorAt(0, 0);
			for (int j = 0; j < cube.getCubeSize(); ++j) {
				for (int k = 0; k < cube.getCubeSize(); ++k) {
					if (cube.getSides()[i].getColorAt(j, k) != color) {
						fail("Side nr " + i + " was not reset correctly.");
					}
				}
			}
		}
	}

	/**
	 * Test of rotate method, of class Cube.
	 */
	@Test
	public void testRotate() {
		Cube cube = new Cube(17);
		Rotation rotation = new Rotation(5, "R2");
		cube.rotate(rotation);
		for (int i = 0; i < 17; ++i) {
			for (int j = 0; j < 17; ++j) {
				if (j == 12) {
					assertEquals(StickerColor.ORANGE, cube.getSideAt('U').getColorAt(i, j));
				}
			}
		}

		for (int i = 3; i <= 25; i += 2) {
			cube = new Cube(i);
			rotation = new Rotation(0, "M2");
			cube.rotate(rotation);
			for (int j = 0; j < i; ++j) {
				for (int k = 0; k < i; ++k) {
					if (k == (i / 2)) {
						assertEquals("cube " + i, StickerColor.ORANGE, cube.getSideAt('U').getColorAt(j, k));
						assertEquals(StickerColor.YELLOW, cube.getSideAt('F').getColorAt(j, k));
						assertEquals(StickerColor.RED, cube.getSideAt('D').getColorAt(j, k));
						assertEquals(StickerColor.WHITE, cube.getSideAt('B').getColorAt(j, k));
					}
				}
			}
			cube.resetSides();
			rotation = new Rotation(0, "E'");
			cube.rotate(rotation);
			for (int j = 0; j < i; ++j) {
				for (int k = 0; k < i; ++k) {
					if (j == (i / 2)) {
						assertEquals("cube " + i, StickerColor.BLUE, cube.getSideAt('F').getColorAt(j, k));
						assertEquals(StickerColor.WHITE, cube.getSideAt('R').getColorAt(j, k));
						assertEquals(StickerColor.GREEN, cube.getSideAt('B').getColorAt(j, k));
						assertEquals(StickerColor.YELLOW, cube.getSideAt('L').getColorAt(j, k));
					}
				}
			}
		}
	}

	/**
	 * Test of scramble method, of class Cube.
	 */
	@Test
	public void testScramble() {
		//For the special case of a 2x2 cube:
		int passCount2Cube = 0;

		for (int i = 2; i <= 25; ++i) {
			Cube cube = new Cube(i);
			Scrambler scrambler = new Scrambler(i);
			cube.scramble(scrambler);

			for (Side side : cube.getSides()) {
				ArrayList<StickerColor> foundColors = new ArrayList<>();
				for (int j = 0; j < i; ++j) {
					for (int k = 0; k < i; ++k) {
						StickerColor color = side.getColorAt(j, k);
						if (!foundColors.contains(color)) {
							foundColors.add(color);
						}
					}
				}

				if (i > 10) {
					if (foundColors.size() < 6) {
						fail("One side doesn't contain at least 5 colors on cube " + i);
					}
				} else if (i > 2) {
					if (foundColors.size() < 3) {
						fail("One side doesn't contain at least 2 colors on cube " + i);
					}
				} else {
					if (foundColors.size() >= 2) {
						passCount2Cube++;
					}
				}
			}
		}
		if (passCount2Cube < 4) {
			fail("At least 3 sides don't contain at least 2 colors on cube 2");
		}
	}

	/**
	 * Test of getSideAt method, of class Cube.
	 */
	@Test
	public void testGetSideAt() {
		Cube cube = new Cube(9);
		for (int i = 0; i < Cube.NUMBER_OF_SIDES; ++i) {
			char position = cube.getSides()[i].getPosition();

			if (!(cube.getSideAt(position).equals(cube.getSides()[i]))) {
				fail("Not returned the same side");
			}
		}
	}

	/**
	 * Test of getSides method, of class Cube.
	 */
	@Test
	public void testGetSides() {
		for (int i = 2; i <= 25; ++i) {
			Cube cube = new Cube(i);
			assertEquals(cube.getSides().length, Cube.NUMBER_OF_SIDES);
		}
	}

	/**
	 * Test of getCubeSize method, of class Cube.
	 */
	@Test
	public void testGetCubeSize() {
		for (int i = 2; i <= 25; ++i) {
			Cube cube = new Cube(i);
			assertEquals(cube.getCubeSize(), i);
		}
	}

	/**
	 * Test of toString method, of class Cube.
	 */
	@Test
	public void testToString() {

	}

}
