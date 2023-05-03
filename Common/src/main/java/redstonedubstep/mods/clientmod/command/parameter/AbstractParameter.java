package redstonedubstep.mods.clientmod.command.parameter;

import net.minecraft.network.chat.MutableComponent;
import redstonedubstep.mods.clientmod.command.CommandException;

public abstract class AbstractParameter<T> {
	public abstract T getValue();

	public abstract CommandException setValue(String value, int pos);

	public abstract void setToDefault();

	public abstract boolean isRequired();

	public abstract void setRequired(boolean required);

	public abstract MutableComponent toDescription();
}
