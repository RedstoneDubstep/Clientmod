package net.redstonedubstep.clientmod.command;

import java.util.function.Function;

public class Command {
	public final String prefix;
	private final int parameterCount;
	private Function<String, CommandResult> action;

	public Command(String prefix, int parameterCount, Function<String, CommandResult> action) {
		this.prefix = prefix;
		this.parameterCount = parameterCount;
		this.action = action;
	}

	public CommandResult execute(String parameter) {
		return action.apply(parameter);
	}
}
