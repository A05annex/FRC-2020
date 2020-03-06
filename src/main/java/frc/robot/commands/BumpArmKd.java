package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;


public class BumpArmKd extends CommandBase {

  private final double m_delta;

  public BumpArmKd(double delta) {
    m_delta = delta;
  }

  @Override
  public boolean isFinished() {
    Constants.ARM_Kd += m_delta;
    return true;
  }
}
