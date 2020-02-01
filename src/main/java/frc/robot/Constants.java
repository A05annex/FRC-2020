/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorMatch;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class MotorControllers {
        public static int
                BIG_WHEEL = 9;
    }

    public static final class ColorTargets {
        public static Color
            BLUE_TARGET = ColorMatch.makeColor(0.143, 0.427, 0.429), // order is red, green, blue
            GREEN_TARGET = ColorMatch.makeColor(0.197, 0.561, 0.240),
            RED_TARGET = ColorMatch.makeColor(0.4, 0.4, 0.2),
            YELLOW_TARGET = ColorMatch.makeColor(0.361, 0.524, 0.113);
    }

}
