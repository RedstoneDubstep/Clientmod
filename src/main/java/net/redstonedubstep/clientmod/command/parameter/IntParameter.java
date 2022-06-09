package net.redstonedubstep.clientmod.command.parameter;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.redstonedubstep.clientmod.command.CommandException;

public class IntParameter extends AbstractParameter<Integer> {
	private Integer value;
	private final Integer defaultValue;
	private final Integer maxValue;
	private final Integer minValue;
	private boolean required;

	public IntParameter() {
		this(true, null);
	}

	public IntParameter(boolean required, Integer defaultValue) {
		this(required, defaultValue, Integer.MAX_VALUE, Integer.MIN_VALUE);
	}

	public IntParameter(boolean required, Integer defaultValue, Integer maxValue, Integer minValue) {
		this.required = required;
		this.defaultValue = defaultValue;
		this.maxValue = maxValue;
		this.minValue = minValue;
	}

	public boolean isValueAllowed(Integer value) {
		return value <= maxValue && value >= minValue;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public CommandException setValue(String value, int pos) {
		try{
			this.value = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return CommandException.invalidParameter(this, pos, value);
		}

		if (!isValueAllowed(this.value))
			return CommandException.invalidParameter(this, pos, value);

		return null;
	}

	@Override
	public void setToDefault() {
		this.value = defaultValue;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public MutableComponent toDescription() {
		return Component.translatable( "Allowed input: Int" + (maxValue < Integer.MAX_VALUE ? (", highest allowed value: " + maxValue) : "") + (minValue > Integer.MIN_VALUE ? (", lowest allowed value: " + minValue) : ""));
	}
}
