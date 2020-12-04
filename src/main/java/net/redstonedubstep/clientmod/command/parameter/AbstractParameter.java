package net.redstonedubstep.clientmod.command.parameter;

import net.redstonedubstep.clientmod.command.CommandResult;

public abstract class AbstractParameter<T> {
	public abstract T getValue();

	public abstract CommandResult setValue(String value);

	public abstract void setToDefault();

	public abstract boolean isRequired();

	public abstract void setRequired(boolean required);
}
