/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;

public class SetLimelightMode extends CommandBase {

  public static int DRIVER_MODE = 0;
  public static int VISION_MODE = 1;

  Limelight m_limelight;
  int m_mode;

  /**
   * Set the mode of the Limelight to driver or vision.
   *
   * @param mode the mode to set it to, should be a constant from this file.
   */
  public SetLimelightMode(Limelight limelight, int mode) {
    m_limelight = limelight;
    addRequirements(m_limelight);

    m_mode = mode;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    switch (m_mode) {
      case 0:
        m_limelight.setDriveCamera();
        break;
      case 1:
        m_limelight.setVisionCamera();
        break;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
