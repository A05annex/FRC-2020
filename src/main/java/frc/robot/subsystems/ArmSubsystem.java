package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ArmSubsystem extends SubsystemBase {


  private TalonSRX m_position = new TalonSRX(Constants.MotorControllers.COLLECTOR_POSITION);
  private double m_lastPower = 0.0;

  /**
   * Creates a new instance of this ArmSubsystem.
   * This constructor is private since this class is a Singleton. External classes
   * should use the {@link #getInstance()} method to get the instance.
   */
  private ArmSubsystem() {
    m_position.configFactoryDefault();
    m_position.setNeutralMode(NeutralMode.Coast);
    m_position.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    initializeArmEncoder();
    m_position.setSensorPhase(true);

  }

  public void initializeArmEncoder() {
    m_position.setSelectedSensorPosition((int) Constants.ArmPosition.START_POSITION.POSITION,
        0, 10);
  }


  public double getPosition() {
    return m_position.getSelectedSensorPosition();
  }

  public double getPositionPower() {
    return m_lastPower;
  }

  public void setPositionPower(double power) {
    double position = m_position.getSelectedSensorPosition();
    if ((position < 0.0) && (power < 0.0)) {
      power = 0.0;
    } else if (position < 35000.0) {
      power = .2 + power;
    } else if (position < 50000.0) {
      power = .1 + power;
    } else if (position < 60000.0) {
      if (power >= 0.0) {
        power = 0.5;
      } else {
        power = .05 + power;
      }
    }
    if (power != m_lastPower) {
      m_position.set(ControlMode.PercentOutput, power);
      m_lastPower = power;
    }
  }

  //================================================================================================================================
  /**
   * The Singleton instance of this ArmSubsystem. External classes should
   * use the {@link #getInstance()} method to get the instance.
   */
  private static final ArmSubsystem INSTANCE = new ArmSubsystem();

  /**
   * Returns the Singleton instance of this ArmSubsystem. This static method
   * should be used -- {@code ArmSubsystem.getInstance();} -- by external
   * classes, rather than the constructor to get the instance of this class.
   */
  public static ArmSubsystem getInstance() {
    return INSTANCE;
  }
  //================================================================================================================================

}

