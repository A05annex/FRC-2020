/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LowerCylinderSubsystem extends SubsystemBase {

  private final Solenoid m_shifter = new Solenoid(Constants.Pneumatics.LIFT_LOWER_CYLINDERS);
   
  /**
   * Creates a new LowerCylinderSubsystem. Works the 2 lower pheumatic cylinders.
   */
  public LowerCylinderSubsystem() {

  } 

  public void extendCylinders () {
    m_shifter.set(true);
  }

 public void retractCylinders () {
    m_shifter.set(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
