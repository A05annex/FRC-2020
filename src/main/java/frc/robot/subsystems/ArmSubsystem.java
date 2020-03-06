package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ArmSubsystem extends SubsystemBase {

  /**
   * The Singleton instance of this CollectorSubsystem. External classes should
   * use the {@link #getInstance()} method to get the instance.
   */
  private final static ArmSubsystem INSTANCE = new ArmSubsystem();
  private TalonSRX m_position = new TalonSRX(Constants.MotorControllers.COLLECTOR_POSITION);
  private double m_targetPosition;

  /**
   * Creates a new instance of this CollectorSubsystem.
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

  /**
   * Returns the Singleton instance of this CollectorSubsystem. This static method
   * should be used -- {@code CollectorSubsystem.getInstance();} -- by external
   * classes, rather than the constructor to get the instance of this class.
   */
  public static ArmSubsystem getInstance() {
    return INSTANCE;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    setupTalonPID();
  }

  /**
   * Gets the current encoder position
   * @return
   */
  public double getPosition() {
    return m_position.getSelectedSensorPosition();
  }

  /**
   * Gets the current power, which is useful when you want to find the power that will balance the weight of the arm during
   * calibration of the manual move.
   * @return
   */
  public double getPositionPower() {
    return m_position.getMotorOutputPercent();
  }

  /**
   * Set the power for moving the arm position. This method looks at the current position and adds power to offset the
   * weight of the collector so the collector does not move if the requested power is 0.0;
   * @param power
   */
  public void setPositionPower(double power) {
    if (power != 0.0) {
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
      m_position.set(ControlMode.PercentOutput, power);
    }
  }

  public void setupTalonPID() {
    m_position.config_kP(0, Constants.ARM_Kp);
    m_position.config_kI(0, Constants.ARM_Ki);
    m_position.config_IntegralZone(0,3000); // don't start integral until you are within about 1.5"
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

  public double getTargetPosition() {
    return m_targetPosition;
  }
}

