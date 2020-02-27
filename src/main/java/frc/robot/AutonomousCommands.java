package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * The autonomous Command Programs, defined in the robot container
 */
public enum AutonomousCommands {
  POSITION1("Center", null),
  POSITION2("Right", null),
  POSITION3("Left", null),
  POSITION4("Trench", null),
  POSITION5("Full", null),
  POSITION6("Move", null);


  public final String NAME;
  public Command COMMAND;

  AutonomousCommands(String name, Command command) {
    NAME = name;
    COMMAND = command;
  }

  static public String[] asStringArray() {
    AutonomousCommands autos[] = AutonomousCommands.values();
    String[] strings = new String[autos.length];
    for (int i = 0; i < autos.length; i++) {
      strings[i] = autos[i].NAME;
    }
    return strings;
  }

  static public Command getDefaultCommand() {
    return POSITION1.COMMAND;
  }

  static public String getDefaultName() {
    return POSITION1.NAME;
  }
}
