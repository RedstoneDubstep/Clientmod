package net.redstonedubstep.clientmod.command.parameter;

import net.redstonedubstep.clientmod.command.CommandException;

public class IntParameter extends AbstractParameter<Integer> {
	private int value;
	private final int defaultValue;
	private final int maxValue;
	private boolean required;

	public IntParameter() {
		this(true, 0);
	}

	public IntParameter(boolean required, int defaultValue) {
		this(required, defaultValue, Integer.MAX_VALUE);
	}

	public IntParameter(boolean required, int defaultValue, int maxValue) {
		this.required = required;
		this.defaultValue = defaultValue;
		this.maxValue = maxValue;
	}

	public boolean isValueAllowed(int value) {
		return value <= maxValue && value > 0;
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
			return CommandException.invalidParameter(this, pos);
		}

		if (!isValueAllowed(this.value))
			return CommandException.invalidParameter(this, pos);

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
	public String toDescription() {
		return "Allowed input: Int" + (maxValue < Integer.MAX_VALUE ? (", highest allowed value: "+maxValue) : "");
	}
}
