package net.redstonedubstep.clientmod.command.parameter;

import net.minecraft.util.text.IFormattableTextComponent;
import net.redstonedubstep.clientmod.command.CommandException;

public abstract class AbstractParameter<T> {
	public abstract T getValue();

	public abstract CommandException setValue(String value, int pos);

	public abstract void setToDefault();

	public abstract boolean isRequired();

	public abstract void setRequired(boolean required);

	public abstract IFormattableTextComponent toDescription();
}
