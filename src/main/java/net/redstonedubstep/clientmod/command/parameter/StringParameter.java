package net.redstonedubstep.clientmod.command.parameter;

import java.util.ArrayList;
import java.util.List;

import net.redstonedubstep.clientmod.command.CommandResult;

public class StringParameter extends AbstractParameter<String> {
	private String value;
	private final String defaultValue;
	private final List<String> allowedValues;
	private boolean required;

	public StringParameter() {
		this(new ArrayList<>());
	}

	public StringParameter(List<String> allowedValues) {
		this(allowedValues, true, "");
	}

	public StringParameter(List<String> allowedValues, boolean required, String defaultValue) {
		this.required = required;
		this.defaultValue = defaultValue;
		this.allowedValues = allowedValues;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public CommandResult setValue(String value) {
		if (!allowedValues.isEmpty() && allowedValues.contains(value))
			return CommandResult.INVALID_PARAMETER;

		this.value = value;
		return null;
	}

	@Override
	public void setToDefault() {
		this.value = defaultValue;
	}

	public List<String> getAllowedValues() {
		return allowedValues;
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
