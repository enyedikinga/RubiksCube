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
public class ScramblerTest {

	public ScramblerTest() {
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
	 * Test of generateNewScramble method, of class Scrambler.
	 */
	@Test
	public void testGenerateNewScramble() {
		for (int i = 2; i <= 25; ++i) {
			Scrambler scrambler = new Scrambler(i);
			ArrayList<Character> foundSides = new ArrayList<>();
			for (Rotation rotation : scrambler.getScramble()) {
				if (!(foundSides.contains(rotation.getSidePosition()))) {
					foundSides.add(rotation.getSidePosition());
				}
			}
			if (i > 4) {
				if (foundSides.size() < 6) {
					fail("Not all sides rotated at least once while scrambling cube " + i);
				}
			} else if (i >= 2) {
				if (foundSides.size() < 3) {
					fail("Less then 3 sides rotated at least once while scrambling cube " + i);
				}
			}
		}
	}

	/**
	 * Test of toString method, of class Scrambler.
	 */
	@Test
	public void testToString() {
		for (int j = 2; j <= 25; ++j) {
			Scrambler scrambler = new Scrambler(j);
			String scramble = scrambler.toString();
			int spaces = 0;
			for (int i = 0; i < scramble.length(); ++i) {
				if (scramble.charAt(i) == ' ') {
					spaces++;
				}
			}
			if (spaces + 1 != scrambler.getScramble().length) {
				fail("Wrong scramble form at scramble " + j);
			}
		}
	}

}
