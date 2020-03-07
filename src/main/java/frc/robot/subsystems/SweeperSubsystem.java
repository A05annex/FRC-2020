package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SweeperSubsystem extends SubsystemBase {

  private final TalonSRX m_sweeper = new TalonSRX(Constants.MotorControllers.COLLECTOR_SWEEPER);
  private double m_lastPower = 0.0;
  /**
   * Creates a new instance of this SweeperSubsystem.
   * This constructor is private since this class is a Singleton. External classes
   * should use the {@link #getInstance()} method to get the instance.
   */
  private SweeperSubsystem() {
    m_sweeper.configFactoryDefault();
    m_sweeper.setNeutralMode(NeutralMode.Brake);
    m_sweeper.set(ControlMode.PercentOutput, 0.0);
  }

  @SuppressWarnings("unused")
  public double getSweeperPower() {
    return m_lastPower;
  }

  public void setSweeperPower(double power) {
    if (power != m_lastPower) {
      m_sweeper.set(ControlMode.PercentOutput, power);
      m_lastPower = power;
    }
  }

  //================================================================================================================================
  /**
   * The Singleton instance of this SweeperSubsystem. External classes should
   * use the {@link #getInstance()} method to get the instance.
   */
  private static final SweeperSubsystem INSTANCE = new SweeperSubsystem();

  /**
   * Returns the Singleton instance of this SweeperSubsystem. This static method
   * should be used -- {@code SweeperSubsystem.getInstance();} -- by external
   * classes, rather than the constructor to get the instance of this class.
   */
  public static SweeperSubsystem getInstance() {
    return INSTANCE;
  }
  //================================================================================================================================

}

