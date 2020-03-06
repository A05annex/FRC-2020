package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;


public class BumpArmKp extends CommandBase {

  private final double m_delta;

  public BumpArmKp(double delta) {
    m_delta = delta;
  }

  @Override
  public boolean isFinished() {
    Constants.ARM_Kp += m_delta;
    return true;
  }
}
