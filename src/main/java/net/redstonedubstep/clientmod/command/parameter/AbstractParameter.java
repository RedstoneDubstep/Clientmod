package net.redstonedubstep.clientmod.command.parameter;

public abstract class AbstractParameter<T> {
	public abstract T getValue();

	public abstract void setValue(T value);

	public abstract void setToDefault();

	public abstract boolean isRequired();

	public abstract void setRequired(boolean required);
}
