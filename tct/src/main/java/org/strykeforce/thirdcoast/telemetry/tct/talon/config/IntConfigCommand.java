package org.strykeforce.thirdcoast.telemetry.tct.talon.config;

import com.ctre.CANTalon;
import java.util.OptionalInt;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.strykeforce.thirdcoast.telemetry.tct.talon.TalonSet;

public abstract class IntConfigCommand extends ConfigCommand {

  public IntConfigCommand(String name, int weight, Terminal terminal, TalonSet talonSet) {
    super(name, weight, terminal, talonSet);
  }

  public IntConfigCommand(String name, Terminal terminal, TalonSet talonSet) {
    super(name, terminal, talonSet);
  }

  protected abstract void config(CANTalon talon, int value);

  @Override
  public void perform() {
    OptionalInt opt = getIntValue();
    if (!opt.isPresent()) {
      return;
    }
    int value = opt.getAsInt();
    for (CANTalon talon : talonSet.selected()) {
      config(talon, value);
      logger.info("set {} for {} to {}", name(), talon.getDescription(), value);
    }
  }

  protected OptionalInt getIntValue() {
    OptionalInt value = OptionalInt.empty();

    while (!value.isPresent()) {
      String line = null;
      try {
        line = reader.readLine(prompt()).trim();
      } catch (EndOfFileException | UserInterruptException e) {
        break;
      }

      if (line.isEmpty()) {
        logger.info("no value entered");
        break;
      }
      int setpoint = 0;
      try {
        setpoint = Integer.valueOf(line);
      } catch (NumberFormatException nfe) {
        terminal.writer().println("please enter an integer");
        continue;
      }
      value = OptionalInt.of(setpoint);
    }
    return value;
  }
}