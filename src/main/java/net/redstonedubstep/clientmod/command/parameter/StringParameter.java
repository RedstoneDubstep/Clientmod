package net.redstonedubstep.clientmod.command.parameter;

import java.util.ArrayList;
import java.util.List;

import net.redstonedubstep.clientmod.command.CommandException;

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
	public CommandException setValue(String value, int pos) {
		if (!allowedValues.isEmpty() && !allowedValues.contains(value))
			return CommandException.invalidParameter(this, pos);

		this.value = value;
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
		StringBuilder sb = new StringBuilder("Allowed input: String");

		if (!allowedValues.isEmpty()) {
			sb.append(", allowed values: ");
			allowedValues.forEach(s -> sb.append(s + ", "));
		}

		return sb.toString();
	}
}
