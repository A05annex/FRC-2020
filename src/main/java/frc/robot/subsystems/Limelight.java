/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

  NetworkTable m_table = NetworkTableInstance.getDefault().getTable("limelight");
  double v;
  double x;
  double y;
  double area;
  String mode;
  String streamMode;
  private MODE m_mode;
  private STREAM m_stream;
  /**
   * Creates a new Limelight.
   * This constructor is private since this class is a Singleton. External classes
   * should use the {@link #getInstance()} method to get the instance.
   */
  private Limelight() {
    // when initialized, use driver camera
    setMode(MODE.DRIVE);
    /*
    table.getEntry("ledMode").setNumber(1);  //1 is off, 2 is seizure mode, 3 is on
    table.getEntry("camMode").setNumber(1);  //1 is driver mode (turns off vision processing)'
    */
    setStream(STREAM.LIMELIGHT);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run to
    // update vision variables
    NetworkTableEntry tv = m_table.getEntry("tv"); // whether there are any targets, 0 or 1
    NetworkTableEntry tx = m_table.getEntry("tx"); // horizontal distance from cursor
    NetworkTableEntry ty = m_table.getEntry("ty"); // vertical distance from cursor
    NetworkTableEntry ta = m_table.getEntry("ta"); // area of target

    // read values periodically, not sure why you need getDouble but its in the limelight docs
    v = tv.getDouble(0.0);
    x = tx.getDouble(0.0);
    y = ty.getDouble(0.0);
    area = ta.getDouble(0.0);
  }

  public void toggleMode() {
    setMode(m_mode == MODE.DRIVE ? MODE.VISION : MODE.DRIVE);
  }

  public MODE getMode() {
    return m_mode;
  }

  public void setMode(MODE mode) {
    switch (mode) {
      case VISION:
        m_table.getEntry("pipeline").setNumber(0); // vision pipeline
        m_table.getEntry("ledMode").setNumber(3); //1 is off, 2 is seizure mode, 3 is on
        break;
      case DRIVE:
      default:
        m_table.getEntry("pipeline").setNumber(1); // driver pipeline
        m_table.getEntry("ledMode").setNumber(1); //1 is off, 2 is seizure mode, 3 is on
        mode = MODE.DRIVE;
        break;
    }
    m_mode = mode;


  }

  public void toggleStream() {
    setStream(m_stream == STREAM.SECONDARY ? STREAM.LIMELIGHT :
        (m_stream == STREAM.SIDE_BY_SIDE ? STREAM.SECONDARY : STREAM.SIDE_BY_SIDE));
  }

  public STREAM getStream() {
    return m_stream;
  }

  public void setStream(STREAM stream) {
    switch (stream) {
      case SIDE_BY_SIDE:
        m_table.getEntry("stream").setNumber(0);
        break;
      case SECONDARY:
        m_table.getEntry("stream").setNumber(2);
        break;
      case LIMELIGHT:
      default:
        // default to limelight if something invalid is passed in.
        m_table.getEntry("stream").setNumber(1);
        stream = STREAM.LIMELIGHT;
    }
    m_stream = stream;
  }

  // set modes of the limelight camera
  public void setDriveCamera() {
    m_table.getEntry("pipeline").setNumber(1); // driver pipeline
    /*
    table.getEntry("ledMode").setNumber(1);  //1 is off, 2 is seizure mode, 3 is on
    table.getEntry("camMode").setNumber(1);  //1 is driver mode (turns off vision processing)'
    */
    mode = "drive";
  }

  public void setVisionCamera() {
    m_table.getEntry("pipeline").setNumber(0); // vision pipeline
    /*
    table.getEntry("ledMode").setNumber(3);  //1 is off, 2 is seizure mode, 3 is on
    table.getEntry("camMode").setNumber(0);  //1 is driver mode (turns off vision processing)
    */
    mode = "vision";
  }

  public void setSeizureMode() {
    m_table.getEntry("ledMode").setNumber(2);  //1 is off, 2 is seizure mode, 3 is on
    // table.getEntry("camMode").setNumber(1);  //1 is driver mode (turns off vision processing)
    mode = "seizure";
  }

  public void setLimelightStream() {
    m_table.getEntry("stream").setNumber(1);
    streamMode = "limelight";
  }

  public void setSideBySideStream() {
    m_table.getEntry("stream").setNumber(0);
    streamMode = "side by side";
  }

  public void setSecondaryStream() {
    m_table.getEntry("stream").setNumber(2);
    streamMode = "secondary";
  }

  // return variables, see above
  /*
  public Boolean isTarget() {
    if (v == 0) {return false;}
    else if (v == 1) {return true;}
    else return null;
  }
  */
  public double isTarget() {
    return v;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getArea() {
    return area;
  }

  public String getStreamMode() {
    return streamMode;
  }

  public enum MODE {
    DRIVE,
    VISION;
  }

  //================================================================================================================================
  public enum STREAM {
    LIMELIGHT,
    SIDE_BY_SIDE,
    SECONDARY;
  }
  /**
   * The Singleton instance of this Limelight. External classes should
   * use the {@link #getInstance()} method to get the instance.
   */
  private static final Limelight INSTANCE = new Limelight();

  /**
   * Returns the Singleton instance of this Limelight. This static method
   * should be used -- {@code Limelight.getInstance();} -- by external
   * classes, rather than the constructor to get the instance of this class.
   */
  public static Limelight getInstance() {
    return INSTANCE;
  }
  //================================================================================================================================

}
