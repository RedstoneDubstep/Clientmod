package net.redstonedubstep.clientmod.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.redstonedubstep.clientmod.command.parameter.AbstractParameter;

public class CommandException {

	private final IFormattableTextComponent title;
	private final List<IFormattableTextComponent> description;

	private CommandException(IFormattableTextComponent title, List<IFormattableTextComponent> description) {
		this.title = title;
		this.description = description;
	}

	public static CommandException empty() {
		List<IFormattableTextComponent> description = Lists.newArrayList(new TranslationTextComponent("screen.clientmod:mainScreen.supportedCommands"));

		CommandLibrary.commandList.forEach(c -> description.add(new StringTextComponent(c.prefix)));

		return new CommandException(new TranslationTextComponent("screen.clientmod:mainScreen.exception.missingPrefix"), description);
	}

	public static CommandException prefixNotFound() {
		List<IFormattableTextComponent> description = Lists.newArrayList(new TranslationTextComponent("screen.clientmod:mainScreen.supportedCommands"));

		CommandLibrary.commandList.forEach(c -> description.add(new StringTextComponent(c.prefix)));

		return new CommandException(new TranslationTextComponent("screen.clientmod:mainScreen.exception.prefixNotFound"), description);
	}

	public static CommandException noParameter(AbstractParameter<?> parameter, int pos) {
		List<IFormattableTextComponent> description = Lists.newArrayList(new TranslationTextComponent("screen.clientmod:mainScreen.exception.position", pos + 1), parameter.toDescription());

		return new CommandException(new TranslationTextComponent("screen.clientmod:mainScreen.exception.missingParameter"), description);
	}

	public static CommandException invalidParameter(AbstractParameter<?> parameter, int pos, String wrongValue) {
		List<IFormattableTextComponent> description = Lists.newArrayList(new TranslationTextComponent("screen.clientmod:mainScreen.exception.position", pos + 1), new TranslationTextComponent("screen.clientmod:mainScreen.exception.wrongValue", wrongValue), parameter.toDescription());

		return new CommandException(new TranslationTextComponent("screen.clientmod:mainScreen.exception.invalidParameter"), description);
	}

	public IFormattableTextComponent getTitle() {
		return title;
	}

	public List<IFormattableTextComponent> getDescription() {
		return description;
	}

	public ITextComponent getFullDescription() {
		IFormattableTextComponent fullDescription = title.mergeStyle(TextFormatting.GRAY);

		description.forEach(c -> {
			fullDescription.appendString("\n");
			fullDescription.appendSibling(c.mergeStyle(TextFormatting.WHITE));
		});
		
		return fullDescription;
	}

	@Override
	public String toString() {
		return getTitle().getString() + getDescription();
	}
}
