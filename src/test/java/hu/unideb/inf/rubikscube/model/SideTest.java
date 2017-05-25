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
public class SideTest {

	private Side side;

	public SideTest() {
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
	 * Test of rotateMainSideClockwise method, of class Side.
	 */
	@Test
	public void testRotateMainSideClockwise() {
		Side side = new Side(3, StickerColor.WHITE, 'U');
		side.setColorAt(0, 0, StickerColor.YELLOW);
		side.rotateMainSideClockwise();

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (i == 0 && j == 2) {
					assertEquals(side.getColorAt(i, j), StickerColor.YELLOW);
				} else {
					assertEquals(side.getColorAt(i, j), StickerColor.WHITE);
				}
			}
		}
	}

	/**
	 * Test of rotateMainSideCounterClockwise method, of class Side.
	 */
	@Test
	public void testRotateMainSideCounterClockwise() {
		Side side = new Side(3, StickerColor.WHITE, 'U');
		side.setColorAt(0, 0, StickerColor.YELLOW);
		side.rotateMainSideCounterClockwise();

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (i == 2 && j == 0) {
					assertEquals(side.getColorAt(i, j), StickerColor.YELLOW);
				} else {
					assertEquals(side.getColorAt(i, j), StickerColor.WHITE);
				}
			}
		}
	}

	/**
	 * Test of getPosition method, of class Side.
	 */
	@Test
	public void testGetPosition() {
		Side side = new Side(3, StickerColor.BLUE, 'B');
		assertEquals(side.getPosition(), 'B');
		side = new Side(3, StickerColor.BLUE, 'F');
		assertEquals(side.getPosition(), 'F');
	}

	/**
	 * Test of getColorAt method, of class Side.
	 */
	@Test
	public void testGetColorAt() {
		Side side = new Side(5, StickerColor.GREEN, 'B');
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				assertEquals(side.getColorAt(i, j), StickerColor.GREEN);
			}
		}
		side.setColorAt(2, 2, StickerColor.WHITE);
		assertEquals(StickerColor.WHITE, side.getColorAt(2, 2));

		assertEquals(null, side.getColorAt(5, 5));
	}

	/**
	 * Test of setColorAt method, of class Side.
	 */
	@Test
	public void testSetColorAt() {
		Side side = new Side(2, StickerColor.YELLOW, 'D');
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 2; ++j) {
				side.setColorAt(i, j, StickerColor.RED);
				assertEquals(StickerColor.RED, side.getColorAt(i, j));
			}
		}

		side.setColorAt(1, 1, StickerColor.WHITE);
		assertEquals(StickerColor.WHITE, side.getColorAt(1, 1));

		side.setColorAt(2, 2, StickerColor.YELLOW);
	}
}
