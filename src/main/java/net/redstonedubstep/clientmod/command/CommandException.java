package net.redstonedubstep.clientmod.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.redstonedubstep.clientmod.command.parameter.AbstractParameter;

public class CommandException {

	private String title;
	private List<String> description;

	private CommandException(String title, List<String> description) {
		this.title = title;
		this.description = description;
	}

	public static CommandException empty() {
		List<String> description = Lists.newArrayList("The following commands are currently supported: ");

		CommandLibrary.commandList.forEach(c -> description.add(c.prefix));

		return new CommandException("Make sure to enter a prefix!", description);
	}

	public static CommandException prefixNotFound() {
		List<String> description = Lists.newArrayList("The following commands are currently supported: ");

		CommandLibrary.commandList.forEach(c -> description.add(c.prefix));

		return new CommandException("No command with this prefix has been found!", description);
	}

	public static CommandException noParameter(AbstractParameter<?> parameter, int pos) {
		List<String> description = Lists.newArrayList("Position: "+(pos + 1), parameter.toDescription());

		return new CommandException("This command needs at least one more parameter!", description);
	}
	public static CommandException invalidParameter(AbstractParameter<?> parameter, int pos) {
		List<String> description = Lists.newArrayList("Position: "+(pos + 1), parameter.toDescription());

		return new CommandException("This is not a valid parameter!", description);
	}

	public String getTitle() {
		return title;
	}

	public List<String> getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return getTitle()+getDescription();
	}
}
