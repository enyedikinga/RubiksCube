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
/**
 * This component is for representing a Rubik's cube.
 *
 * In this package all the components of a Rubik's cube can be found. For the representation of the cube
 * itself the {@link Cube} class is responsible, which has {@link Side} objects where the color values are stored as {@link StickerColor} enums. To start playing the {@link Scrambler} class provides functionality to scramble the cube, and with the {@link Rotation} class the user's rotations on the cube are represented.
 */
package hu.unideb.inf.rubikscube.model;
