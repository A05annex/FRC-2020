package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Port7Subsystem;


public class Port7Command extends CommandBase {

  private final Port7Subsystem m_Port7Subsystem = Port7Subsystem.getInstance();

  public Port7Command() {
    addRequirements(m_Port7Subsystem);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    m_Port7Subsystem.setPower(Constants.PORT7_SPEED);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {

  }
}
