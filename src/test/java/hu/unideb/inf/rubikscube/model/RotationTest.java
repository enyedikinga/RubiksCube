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
public class RotationTest {

	public RotationTest() {
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
	 * Test of getRotationType method, of class Rotation.
	 */
	@Test
	public void testGetRotationType() {
		for (int i = 0; i < Rotation.BASIC_ROTATIONS.size(); ++i) {
			String rotationType = Rotation.BASIC_ROTATIONS.get(i).getName().substring(1);
			assertEquals(rotationType, Rotation.BASIC_ROTATIONS.get(i).getRotationType());
		}
		Rotation rotation = new Rotation(12, "R");
		assertEquals("R", rotation.getRotationType());
		rotation = new Rotation(0, "E'");
		assertEquals("E'", rotation.getRotationType());
		rotation = new Rotation(0, "M2");
		assertEquals("M2", rotation.getRotationType());
		rotation = new Rotation(2, "F'");
		assertEquals("F'", rotation.getRotationType());
		rotation = new Rotation(1, "B2");
		assertEquals("B2", rotation.getRotationType());
	}

	/**
	 * Test of getLayerNumber method, of class Rotation.
	 */
	@Test
	public void testGetLayerNumber() {
		//In basic rotations, layer number is 1.
		for (Rotation rotation : Rotation.BASIC_ROTATIONS) {
			assertEquals(1, rotation.getLayerNumber());
		}
		Rotation rotation = new Rotation(0, "S");
		assertEquals(0, rotation.getLayerNumber());
		for (int i = 1; i < 13; ++i) {
			rotation = new Rotation(i, "L");
			assertEquals(i, rotation.getLayerNumber());
		}
	}

	/**
	 * Test of getSidePosition method, of class Rotation.
	 */
	@Test
	public void testGetSidePosition() {
		Rotation rotation = new Rotation(2, "L'");
		assertEquals('L', rotation.getSidePosition());
		rotation = new Rotation(0, "E'");
		assertEquals('E', rotation.getSidePosition());
		rotation = new Rotation(10, "B2");
		assertEquals('B', rotation.getSidePosition());
		rotation = new Rotation(0, "S2");
		assertEquals('S', rotation.getSidePosition());
		rotation = new Rotation(1, "B'");
		assertEquals('B', rotation.getSidePosition());
		rotation = new Rotation(5, "F2");
		assertEquals('F', rotation.getSidePosition());

	}

	/**
	 * Test of getName method, of class Rotation.
	 */
	@Test
	public void testGetName() {
		Rotation rotation = new Rotation(4, "D'");
		assertEquals("4D'", rotation.getName());
		rotation = new Rotation(1, "L2");
		assertEquals("1L2", rotation.getName());
		rotation = new Rotation(11, "R");
		assertEquals("11R", rotation.getName());
		rotation = new Rotation(0, "S2");
		assertEquals("0S2", rotation.getName());
		rotation = new Rotation(2, "D2");
		assertEquals("2D2", rotation.getName());
	}

	/**
	 * Test of generateNewRotation method, of class Rotation.
	 */
	@Test
	public void testGenerateNewRotation() {
		for (int i = 2; i <= 25; ++i) {
			Rotation rotation = Rotation.generateNewRotation(i);
			if (rotation.getLayerNumber() > i / 2) {
				fail("Cube " + i + " has no layernumber " + rotation.getLayerNumber());
			}
			Rotation basicRotation = new Rotation(1, rotation.getRotationType());
			boolean isBaseRotation = false;
			for (Rotation baseRot : Rotation.BASIC_ROTATIONS) {
				if (baseRot.getRotationType().equals(basicRotation.getRotationType())) {
					isBaseRotation = true;
					break;
				}
			}
			if (!isBaseRotation) {
				fail("Invalid rotationType in generated rotation " + rotation);
			}
		}
	}

	/**
	 * Test of generateNewDifferentRotation method, of class Rotation.
	 */
	@Test
	public void testGenerateNewDifferentRotation() {
		for (int i = 2; i <= 25; ++i) {
			Rotation rotation1 = Rotation.generateNewRotation(i);
			Rotation rotation2 = Rotation.generateNewDifferentRotation(i, rotation1);
			if (rotation1.getLayerNumber() > i / 2) {
				fail("Cube " + i + " has no layernumber " + rotation1.getLayerNumber());
			}
			Rotation basicRotation1 = new Rotation(1, rotation1.getRotationType());
			boolean isBaseRotation = false;
			for (Rotation baseRot : Rotation.BASIC_ROTATIONS) {
				if (baseRot.getRotationType().equals(basicRotation1.getRotationType())) {
					isBaseRotation = true;
					break;
				}
			}
			if (!isBaseRotation) {
				fail("Invalid rotationType in generated rotation " + rotation1);
			}

			if (rotation2.getLayerNumber() > i / 2) {
				fail("Cube " + i + " has no layernumber " + rotation1.getLayerNumber());
			}
			Rotation basicRotation2 = new Rotation(1, rotation2.getRotationType());
			isBaseRotation = false;
			for (Rotation baseRot : Rotation.BASIC_ROTATIONS) {
				if (baseRot.getRotationType().equals(basicRotation2.getRotationType())) {
					isBaseRotation = true;
					break;
				}
			}
			if (!isBaseRotation) {
				fail("Invalid rotationType in generated rotation " + rotation2);
			}

			if (rotation1.getName().equals(rotation2.getName())) {
				fail("Same rotation generated two times, first rotation: " + rotation1
						+ "second rotation: " + rotation2);
			}

		}
	}

}
