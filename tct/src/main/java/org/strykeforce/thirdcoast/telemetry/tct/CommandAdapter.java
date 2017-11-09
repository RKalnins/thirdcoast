package org.strykeforce.thirdcoast.telemetry.tct;

import static java.util.Comparator.comparing;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.inject.Inject;

@ParametersAreNonnullByDefault
public class CommandAdapter {

  private final List<Command> commands;

  @Inject
  public CommandAdapter(Set<Command> commands) {
    this.commands = commands.stream()
        .sorted(comparing(Command::weight).thenComparing(Command::name))
        .collect(Collectors.toList());
  }

  String getMenuText(int position) {
    return commands.get(position).name();
  }

  int getCount() {
    return commands.size();
  }

  public void perform(int position) {
    Command command = commands.get(position);
    command.perform();
    if (command.post().isPresent()) {
      command.post().get().perform();
    }
  }

}
