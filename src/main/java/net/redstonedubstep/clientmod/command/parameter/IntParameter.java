package net.redstonedubstep.clientmod.command.parameter;

import net.redstonedubstep.clientmod.command.CommandResult;

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
	public CommandResult setValue(String value) {
		try{
			this.value = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return CommandResult.INVALID_PARAMETER;
		}

		if (!isValueAllowed(this.value))
			return CommandResult.INVALID_PARAMETER;

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
}
