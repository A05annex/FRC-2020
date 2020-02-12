/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LiftSubsystem extends SubsystemBase {

  private static final DoubleSolenoid.Value EXTENDED = DoubleSolenoid.Value.kForward;
  private static final DoubleSolenoid.Value RETRACTED = DoubleSolenoid.Value.kReverse;

  private final DoubleSolenoid m_lowerCylinder = new DoubleSolenoid(Constants.Pneumatics.LOWER_LIFT_EXTEND,
      Constants.Pneumatics.LOWER_LIFT_RETRACT);
  private final DoubleSolenoid m_upperCylinder = new DoubleSolenoid(Constants.Pneumatics.UPPER_LIFT_EXTEND,
      Constants.Pneumatics.UPPER_LIFT_RETRACT);
  private TalonSRX m_winch = new TalonSRX(Constants.MotorControllers.LIFT_WINCH);

  /**
   * Creates a new LowerCylinderSubsystem. Works the 2 lower pneumatic cylinders.
   */
  public LiftSubsystem() {
    m_upperCylinder.set(RETRACTED);
    m_lowerCylinder.set(RETRACTED);
    m_winch.configFactoryDefault();
    m_winch.setNeutralMode(NeutralMode.Brake);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void extendLower() {
    m_lowerCylinder.set(EXTENDED);
  }

  public void retractLower() {
    if (m_upperCylinder.get() == RETRACTED) {
      m_lowerCylinder.set(RETRACTED);
    }
  }

  public void extendUpper() {
    if (m_lowerCylinder.get() == EXTENDED) {
      m_upperCylinder.set(EXTENDED);
    }
  }

  public void retractUpper() {
    m_upperCylinder.set(RETRACTED);
  }

  public void setWinchPower(double power) {
    m_winch.set(ControlMode.PercentOutput, power);
  }
}
