package net.redstonedubstep.clientmod.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.redstonedubstep.clientmod.command.parameter.AbstractParameter;

public class CommandException {

	private final ITextComponent title;
	private final List<ITextComponent> description;

	private CommandException(ITextComponent title, List<ITextComponent> description) {
		this.title = title;
		this.description = description;
	}

	public static CommandException empty() {
		List<ITextComponent> description = Lists.newArrayList(new TranslationTextComponent("screen.clientmod:mainScreen.supportedCommands"));

		CommandLibrary.commandList.forEach(c -> description.add(new StringTextComponent(c.prefix)));

		return new CommandException(new TranslationTextComponent("screen.clientmod:mainScreen.exception.missingPrefix"), description);
	}

	public static CommandException prefixNotFound() {
		List<ITextComponent> description = Lists.newArrayList(new TranslationTextComponent("screen.clientmod:mainScreen.supportedCommands"));

		CommandLibrary.commandList.forEach(c -> description.add(new StringTextComponent(c.prefix)));

		return new CommandException(new TranslationTextComponent("screen.clientmod:mainScreen.exception.prefixNotFound"), description);
	}

	public static CommandException noParameter(AbstractParameter<?> parameter, int pos) {
		List<ITextComponent> description = Lists.newArrayList(new TranslationTextComponent("screen.clientmod:mainScreen.exception.position", pos + 1), parameter.toDescription());

		return new CommandException(new TranslationTextComponent("screen.clientmod:mainScreen.exception.missingParameter"), description);
	}

	public static CommandException invalidParameter(AbstractParameter<?> parameter, int pos, String wrongValue) {
		List<ITextComponent> description = Lists.newArrayList(new TranslationTextComponent("screen.clientmod:mainScreen.exception.position", pos + 1), new TranslationTextComponent("screen.clientmod:mainScreen.exception.wrongValue", wrongValue), parameter.toDescription());

		return new CommandException(new TranslationTextComponent("screen.clientmod:mainScreen.exception.invalidParameter"), description);
	}

	public ITextComponent getTitle() {
		return title;
	}

	public List<ITextComponent> getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return getTitle().getString() + getDescription();
	}
}
