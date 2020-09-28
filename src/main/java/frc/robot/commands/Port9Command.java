package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Port9Subsystem;


public class Port9Command extends CommandBase {

  private final Port9Subsystem m_Port9Subsystem = Port9Subsystem.getInstance();

  public Port9Command() {
    addRequirements(m_Port9Subsystem);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    m_Port9Subsystem.setPower(Constants.PORT9_SPEED);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {

  }
}
