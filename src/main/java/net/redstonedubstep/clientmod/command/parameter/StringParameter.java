package net.redstonedubstep.clientmod.command.parameter;

import net.redstonedubstep.clientmod.command.CommandResult;

public class StringParameter extends AbstractParameter<String> {
	private String value;
	private String defaultValue;
	private boolean required;

	public StringParameter() {
		this(true, "");
	}

	public StringParameter(boolean required) {
		this(required, "");
	}

	public StringParameter(boolean required, String defaultValue) {
		this.required = required;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public CommandResult setValue(String value) {
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
}
