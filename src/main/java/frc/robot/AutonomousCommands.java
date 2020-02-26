package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * The autonomous Command Programs
 */
public enum AutonomousCommands {
  POSITION1("extreme right", null /* Put extreme right start command group here */ ),
  POSITION2("right", null /* Put right start command group here */),
  POSITION3("middle", null /* Put center start command group here */),
  POSITION4("left", null /* Put left start command group here */);


  public final String NAME;
  public final Command COMMAND;

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
    return POSITION2.COMMAND;
  }

  static public String getDefaultName() {
    return POSITION2.NAME;
  }
}
