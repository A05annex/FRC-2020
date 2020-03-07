package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;

/*
 * This sets the collector position using the Talon SRX PID loop to control position. The PID will correct for any inertial
 * and/or collision disturbances to the bucket height
 */
public class CollectorPidPosition extends CommandBase {

  private final ArmSubsystem m_armSubsystem = ArmSubsystem.getInstance();
  private final Constants.ArmPosition m_armPosition;
  boolean m_isFinished;

  /**
   * @param armPosition
   */
  public CollectorPidPosition(Constants.ArmPosition armPosition) {
   m_armPosition = armPosition;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_armSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_isFinished = false;
  }

  // Runs once to set the position.
  @Override
  public void execute() {
    m_armSubsystem.setPosition(m_armPosition);
    m_isFinished = true;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}

