package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.SpinnerLift;


public class LiftCylinderControl extends CommandBase {

  public static final boolean EXTENDED = true;
  public static final boolean RETRACTED = false;

  public static final int LOWER_CYLINDER = 0;
  public static final int UPPER_CYLINDER = 1;

  private final LiftSubsystem m_liftSubsystem;
  private final SpinnerLift m_spinnerSolenoid;
  private final int m_cylinder;
  private final boolean m_extend;
  private boolean m_isFinished = false;

  public LiftCylinderControl(LiftSubsystem liftSubsystem, SpinnerLift spinnerSolenoid,
                             int cylinder, boolean extend) {
    m_liftSubsystem = liftSubsystem;
    m_spinnerSolenoid = spinnerSolenoid;
    m_cylinder = cylinder;
    m_extend = extend;
    addRequirements(m_liftSubsystem);
    if ((m_cylinder == LOWER_CYLINDER) && (m_extend == EXTENDED)) {
      addRequirements(m_spinnerSolenoid);
    }
  }

  @Override
  public void initialize() {
    m_isFinished = false;
  }

  @Override
  public void execute() {
    if (m_cylinder == LOWER_CYLINDER) {
      if (m_extend == EXTENDED) {
        m_liftSubsystem.extendLower();
        m_spinnerSolenoid.spinner_up();
      } else {
        m_liftSubsystem.retractLower();
      }
    } else if (m_cylinder == UPPER_CYLINDER) {
      if (m_extend == EXTENDED) {
        m_liftSubsystem.extendUpper();
      } else {
        m_liftSubsystem.retractUpper();
      }
    }
    m_isFinished = true;
  }

  @Override
  public boolean isFinished() {
    return m_isFinished;
  }

  @Override
  public void end(boolean interrupted) {

  }
}
