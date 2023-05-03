package redstonedubstep.mods.clientmod.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import redstonedubstep.mods.clientmod.command.parameter.AbstractParameter;

public class CommandException {

	private final MutableComponent title;
	private final List<MutableComponent> description;

	private CommandException(MutableComponent title, List<MutableComponent> description) {
		this.title = title;
		this.description = description;
	}

	public static CommandException empty() {
		List<MutableComponent> description = Lists.newArrayList(Component.translatable("screen.clientmod:mainScreen.supportedCommands"));

		CommandLibrary.commandList.forEach(c -> description.add(Component.literal(c.prefix)));

		return new CommandException(Component.translatable("screen.clientmod:mainScreen.exception.missingPrefix"), description);
	}

	public static CommandException prefixNotFound(String wrongPrefix) {
		List<MutableComponent> description = Lists.newArrayList(Component.translatable("screen.clientmod:mainScreen.supportedCommands"));

		CommandLibrary.commandList.forEach(c -> description.add(Component.literal(c.prefix)));

		return new CommandException(Component.translatable("screen.clientmod:mainScreen.exception.prefixNotFound", wrongPrefix), description);
	}

	public static CommandException noParameter(AbstractParameter<?> parameter, int pos) {
		List<MutableComponent> description = Lists.newArrayList(Component.translatable("screen.clientmod:mainScreen.exception.position", pos + 1), parameter.toDescription());

		return new CommandException(Component.translatable("screen.clientmod:mainScreen.exception.missingParameter"), description);
	}

	public static CommandException invalidParameter(AbstractParameter<?> parameter, int pos, String wrongValue) {
		List<MutableComponent> description = Lists.newArrayList(Component.translatable("screen.clientmod:mainScreen.exception.position", pos + 1), Component.translatable("screen.clientmod:mainScreen.exception.wrongValue", wrongValue), parameter.toDescription());

		return new CommandException(Component.translatable("screen.clientmod:mainScreen.exception.invalidParameter"), description);
	}

	public MutableComponent getTitle() {
		return title;
	}

	public List<MutableComponent> getDescription() {
		return description;
	}

	public Component getFullDescription() {
		MutableComponent fullDescription = title.withStyle(ChatFormatting.GRAY);

		description.forEach(c -> {
			fullDescription.append("\n");
			fullDescription.append(c.withStyle(ChatFormatting.WHITE));
		});

		return fullDescription;
	}

	@Override
	public String toString() {
		return getTitle().getString() + getDescription();
	}
}
