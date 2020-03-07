/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.NavX;

public class DriveSubsystem extends SubsystemBase {

  private double m_targetLeftSpeed;
  private double m_targetRightSpeed;
  private Solenoid m_shifter = new Solenoid(Constants.Pneumatics.DRIVE_SHIFTER);
  private TalonSRX m_rightMaster = new TalonSRX(Constants.MotorControllers.DRIVE_RIGHT_MASTER);
  private TalonSRX m_rm2 = new TalonSRX(Constants.MotorControllers.DRIVE_RIGHT_SLAVE_1);
  private TalonSRX m_rm3 = new TalonSRX(Constants.MotorControllers.DRIVE_RIGHT_SLAVE_2);
  private TalonSRX m_leftMaster = new TalonSRX(Constants.MotorControllers.DRIVE_LEFT_MASTER);
  private TalonSRX m_lm2 = new TalonSRX(Constants.MotorControllers.DRIVE_LEFT_SLAVE_1);
  private TalonSRX m_lm3 = new TalonSRX(Constants.MotorControllers.DRIVE_LEFT_SLAVE_2);
  private NavX m_navx = NavX.getInstance();

  /**
   * Creates a new DriveSubsystem.
   */
  public DriveSubsystem() {
    m_shifter.set(false);
    // constructs and configures all six drive motors
    // restore everything to known factory default state
    m_rightMaster.configFactoryDefault();
    m_rm2.configFactoryDefault();
    m_rm3.configFactoryDefault();
    m_leftMaster.configFactoryDefault();
    m_lm2.configFactoryDefault();
    m_lm3.configFactoryDefault();
    // now configure them
    m_rm2.follow(m_rightMaster);
    m_rm3.follow(m_rightMaster);
    m_lm2.follow(m_leftMaster);
    m_lm3.follow(m_leftMaster);
    m_rm2.setInverted(InvertType.FollowMaster);
    m_rm3.setInverted(InvertType.FollowMaster);
    m_lm2.setInverted(InvertType.FollowMaster);
    m_lm3.setInverted(InvertType.FollowMaster);
    setNeutralMode(NeutralMode.Coast);
    m_rightMaster.setInverted(InvertType.InvertMotorOutput);
    m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    // setup the drivetrain constants specific to the robot ...
    setRobot();
    // reset encoders
    resetEncoders();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Set the drive motor power based on an arcade control model of forward and turn speed.
   *
   * @param forward (double) forward power in the range -1.0 to 1.0 (negative is
   *                backwards, positive is forward).
   * @param rotate  (double) rotation power in the range -1.0 to 1.0 (negative is
   *                clockwise, positive is counter-clockwise).
   */
  public void setArcadePower(double forward, double rotate) {
    double max = Math.abs(forward) +
        (Math.abs(forward) * Math.abs(Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_TURN_BIAS)) + Math.abs(rotate);
    double scale = (max <= 1.0) ? 1.0 : (1.0 / max);
    m_rightMaster.set(ControlMode.PercentOutput,
        scale * (forward + (rotate + (forward * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_TURN_BIAS))));
    m_leftMaster.set(ControlMode.PercentOutput,
        scale * (forward - (rotate + (forward * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_TURN_BIAS))));
  }

  /**
   * Set the desired drive speed as a factor of maximum drive speed in the range 0 to 1 (0 to -1) where 0 is
   * stopped and 1 (-1) is the maximum forward (backward) speed that can be achieved by the drive.
   *
   * @param forward              (double) forward speed in the range -1.0 to 1.0 (negative is
   *                             backwards, positive is forward).
   * @param rotate               (double) rotation speed in the range -1.0 to 1.0 (negative is
   *                             clockwise, positive is counter-clockwise).
   * @param trackExpectedHeading (boolean) Set this to {@code true} when moving straight and wanting to track along
   *                             the NavX expected heading; which is normally only for an autonomous forward/backward move
   *                             or a driver move where no rotation is applied.
   * @param setExpectedToCurrent (boolean) Set to {@code true} if the expected heading should be updated to the current heading;
   *                             which is normally true only when there is a driver controlled rotation.
   */
  public void setArcadeSpeed(double forward, double rotate,
                             boolean trackExpectedHeading, boolean setExpectedToCurrent) {
    m_navx.recomputeHeading(setExpectedToCurrent);
    if (trackExpectedHeading) {
      NavX.HeadingInfo headingInfo = m_navx.getHeadingInfo();
      if (null != headingInfo) {
        // We really do have a working NavX, so incorporate it into keeping the robot moving in the correct direction.
        rotate = Math.abs(forward) * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_HEADING_Kp *
            (headingInfo.heading - headingInfo.expectedHeading);
      }
    }
    double max = Math.abs(forward) + Math.abs(rotate);
    double scale = (max <= 1.0) ? 1.0 : (1.0 / max);

    m_targetRightSpeed = scale * (forward + rotate) * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_MAX_RPM;
    m_targetLeftSpeed = scale * (forward - rotate) * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_MAX_RPM;

    m_rightMaster.set(ControlMode.Velocity, m_targetRightSpeed);
    m_leftMaster.set(ControlMode.Velocity, m_targetLeftSpeed);
  }

  /**
   *
   * @param right
   * @param left
   */
  public void setTankSpeed (double right, double left) {
    m_navx.recomputeHeading(false);
    if (Math.abs(right) > 1.0) {
      right /= Math.abs(right);
      left /= Math.abs(right);
    }
    if (Math.abs(left) > 1.0) {
      right /= Math.abs(left);
      left /= Math.abs(left);
    }
    m_targetRightSpeed = right * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_MAX_RPM;
    m_targetLeftSpeed = left * Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_MAX_RPM;

    m_rightMaster.set(ControlMode.Velocity, m_targetRightSpeed);
    m_leftMaster.set(ControlMode.Velocity, m_targetLeftSpeed);
  }
  /**
   * Sets the neutral mode for all drive motor controllers.
   *
   * @param mode The neutral mode to be set.
   */
  public void setNeutralMode(NeutralMode mode) {
    //method to easily set the neutral mode of all of the driveTrain motors
    m_rightMaster.setNeutralMode(mode);
    m_rm2.setNeutralMode(mode);
    m_rm3.setNeutralMode(mode);
    m_leftMaster.setNeutralMode(mode);
    m_lm2.setNeutralMode(mode);
    m_lm3.setNeutralMode(mode);
  }

  /**
   * Call this whenever the robot selection changes.
   */
  public void setRobot() {
    // right drivetrain TalonSRX PID
    setupTalonPID(m_rightMaster,
        (1.0 + Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_TURN_BIAS),
        Constants.ROBOT.DRIVE_ENCODER_PHASE);
    // left drivetrain TalonSRX PID
    setupTalonPID(m_leftMaster,
        (1.0 - Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_TURN_BIAS),
        Constants.ROBOT.DRIVE_ENCODER_PHASE);
  }

  /**
   * Call this to setup a TalonSRX PID for the drive of the robot.
   *
   * @param pidController
   * @param biasMultiplier
   * @param encoderPhase
   */
  private void setupTalonPID(TalonSRX pidController, double biasMultiplier, boolean encoderPhase) {
    pidController.config_kF(0, Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_Kf * biasMultiplier);
    pidController.config_kP(0, Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_Kp);
    pidController.config_kI(0, Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_Ki);
    pidController.config_IntegralZone(0,
        (int) (Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_INTEGRAL_ZONE *
            Constants.ROBOT.GEARS[Constants.GEAR].DRIVE_MAX_RPM));
    pidController.config_kD(0, 0);
    pidController.setSensorPhase(encoderPhase);
  }

  /**
   * @return Returns the right drive encoder position.
   */
  public double getRightPosition() {
    return m_rightMaster.getSelectedSensorPosition();
  }

  /**
   * @return Returns the left drive encoder position.
   */
  public double getLeftPosition() {
    return m_leftMaster.getSelectedSensorPosition();
  }

  /**
   * Reset the right and left encoder positions to 0.0.
   */
  public void resetEncoders() {
    m_rightMaster.setSelectedSensorPosition(0, 0, 10);
    m_leftMaster.setSelectedSensorPosition(0, 0, 10);
  }

  /**
   * @return Returns the actual speed of the right drive.
   */
  public double getRightSpeed() {
    return m_rightMaster.getSelectedSensorVelocity();
  }

  /**
   * @return Returns the actual speed of the left drive.
   */
  public double getLeftSpeed() {
    return m_leftMaster.getSelectedSensorVelocity();
  }

  /**
   * @return Returns the last right drive target speed requested.
   */
  public double getTargetRightSpeed() {
    return m_targetRightSpeed;
  }

  /**
   * @return Returns the last left drive target speed requested.
   */
  public double getTargetLeftSpeed() {
    return m_targetLeftSpeed;
  }

  /**
   *
   */
  public void resetIntegral() {
    m_rightMaster.setIntegralAccumulator(0);
    m_leftMaster.setIntegralAccumulator(0);
  }

  /**
   * Toggle between first and second gear
   */
  public void toggleShift() {
    m_shifter.set(!m_shifter.get());
    Constants.GEAR = getGear().ordinal();
    setRobot();
  }

  /**
   * Get the current gear.
   *
   * @return ({ @ link DriveSubsystem # GEAR }) the current gear.
   */
  public GEAR getGear() {
    return m_shifter.get() ? GEAR.SECOND : GEAR.FIRST;
  }

  /**
   * Set the gear.
   *
   * @param gear ({ @ link DriveSubsystem # GEAR }) the gear to set.
   */
  public void setGear(Constants.DriveGears gear) {
    m_shifter.set(Constants.DriveGears.SECOND == gear);
    Constants.GEAR = gear.ordinal();
    setRobot();
   }

  /**
   * @return Returns the average of the left and right encoder positions.
   */
  public double getTotalPosition() {
    return m_leftMaster.getSelectedSensorPosition() + m_rightMaster.getSelectedSensorPosition();
  }

  /**
   * @return Returns the average of the left and right encoder positions.
   */
  public double getDifferencePosition() {
    return m_leftMaster.getSelectedSensorPosition() - m_rightMaster.getSelectedSensorPosition();
  }

  public enum GEAR {
    FIRST,
    SECOND;
  }
}
