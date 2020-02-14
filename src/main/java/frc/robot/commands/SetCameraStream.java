/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;

public class SetCameraStream extends CommandBase {

  public static final int LIMELIGHT_STREAM = 0;
  public static final int SECONDARY_STREAM = 1;
  public static final int SIDE_BY_SIDE = 2;

  Limelight m_limelight;
  int m_mode;

  /**
   * Set the camera stream to the limelight camera, the secondary camera, or both.
   *
   * @param mode the mode to set it to, should be a constant from this file.
   */
  public SetCameraStream(Limelight limelight, int mode) {
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
        m_limelight.setLimelightStream();
        break;
      case 1:
        m_limelight.setSecondaryStream();
        break;
      case 2:
        m_limelight.setSideBySideStream();
        break;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
