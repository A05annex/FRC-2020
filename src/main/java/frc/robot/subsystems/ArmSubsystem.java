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
  private double m_targetPosition;

  /**
   * Creates a new instance of this ArmSubsystem.
   * This constructor is private since this class is a Singleton. External classes
   * should use the {@link #getInstance()} method to get the instance.
   */
  private ArmSubsystem() {
    m_position.configFactoryDefault();
    m_position.setNeutralMode(NeutralMode.Coast);
    m_position.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    m_position.setSensorPhase(true);
    initializeArmEncoder();
    setupTalonPID();
  }

  public void initializeArmEncoder() {
    m_position.setSelectedSensorPosition((int) Constants.ArmPosition.START_POSITION.POSITION,
        0, 10);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Gets the current encoder position
   * @return (double) The current encoder position.
   */
  public double getPosition() {
    return m_position.getSelectedSensorPosition();
  }

  /**
   * Gets the current power, which is useful when you want to find the power that will balance the weight of the arm during
   * calibration of the manual move.
   * @return (double) The current arm motor power.
   */
  public double getPositionPower() {
    return m_position.getMotorOutputPercent();
  }

  private void setupTalonPID() {
    m_position.config_kP(0, Constants.ARM_Kp);
    m_position.config_kI(0, Constants.ARM_Ki);
    m_position.config_IntegralZone(0,Constants.ARM_INTEGRAL_ZONE); // don't start integral until you are close
    m_position.config_kD(0, Constants.ARM_Kd);
  }

  /**
   * specify the position you want and let the arm PID loop get you too and hold that position.
   * @param position
   */
  public void setPosition(Constants.ArmPosition position) {
    // here we are asking the PID controller to fold the position - as soon as the driver
    m_targetPosition = position.POSITION;
    m_position.set(ControlMode.Position, position.POSITION);
  }

  public void incrementTargetPosition(double increment) {
    double newTarget = m_targetPosition + increment;
    if (newTarget > Constants.ArmPosition.START_POSITION.POSITION) {
      newTarget = Constants.ArmPosition.START_POSITION.POSITION;
    }
    if (newTarget < Constants.ArmPosition.FLOOR_POSITION.POSITION) {
      newTarget = Constants.ArmPosition.FLOOR_POSITION.POSITION;
    }
    m_targetPosition = newTarget;
    m_position.set(ControlMode.Position, newTarget);
  }

  public double getTargetPosition() {
    return m_targetPosition;
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

