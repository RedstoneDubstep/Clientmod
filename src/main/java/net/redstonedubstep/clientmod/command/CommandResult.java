package net.redstonedubstep.clientmod.command;

import net.minecraft.util.text.StringTextComponent;

public enum CommandResult {
	EXECUTED(null),
	EMPTY("Make sure to enter a prefix!"),
	PREFIX_NOT_FOUND("No command with this prefix has been found!"),
	NO_PARAMETER("This command needs a parameter!"),
	INVALID_PARAMETER("This is not a valid parameter!");

	private final String message;

	CommandResult(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
