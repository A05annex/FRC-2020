package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;


public class BumpArmKi extends CommandBase {

  private final double m_delta;

  public BumpArmKi(double delta) {
    m_delta = delta;
  }

  @Override
  public boolean isFinished() {
    Constants.ARM_Ki += m_delta;
    return true;
  }
}
