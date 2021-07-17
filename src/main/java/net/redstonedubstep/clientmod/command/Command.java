package net.redstonedubstep.clientmod.command;

import java.util.function.Function;

import net.redstonedubstep.clientmod.command.parameter.AbstractParameter;

public class Command {
	public final String prefix;
	private final AbstractParameter<?>[] params;
	private final Function<AbstractParameter<?>[], CommandException> action;

	public Command(String prefix, Function<AbstractParameter<?>[], CommandException> action, AbstractParameter<?> param) {
		this(prefix, action, new AbstractParameter[]{param});
	}
	public Command(String prefix, Function<AbstractParameter<?>[], CommandException> action, AbstractParameter<?>... params) {
		this.prefix = prefix;
		this.action = action;
		this.params = params;
	}
	
	public CommandException execute(String parameter) {
		CommandException result = formatParameters(parameter);
		if (result != null) //when something went wrong while formatting parameters
			return result;
		else
			return action.apply(params);
	}

	private CommandException formatParameters(String parameterIn) {
		String[] stringParams = parameterIn.split(" ", params.length);
		CommandException result = null;

		for (int i = 0; i < params.length; i++) {
			if (stringParams.length < i + 1 || stringParams[i] == null || stringParams[i].isEmpty()) {
				if (params[i].isRequired())
					return CommandException.noParameter(params[i], i);
				else {
					params[i].setToDefault();
					continue;
				}
			}

			result = params[i].setValue(stringParams[i], i);
			if (result != null)
				return result;
		}

		return result;
	}

}
