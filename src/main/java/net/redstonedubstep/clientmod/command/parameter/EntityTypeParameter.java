package net.redstonedubstep.clientmod.command.parameter;

import java.util.Optional;

import net.minecraft.entity.EntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.redstonedubstep.clientmod.command.CommandException;

public class EntityTypeParameter extends AbstractParameter<EntityType<?>>{
	private EntityType<?> value;
	private EntityType<?> defaultValue;
	private boolean required;

	public EntityTypeParameter() {
		this(true);
	}

	public EntityTypeParameter(boolean required) {
		this(required, null);
	}

	public EntityTypeParameter(boolean required, EntityType<?> defaultValue) {
		this.required = required;
		this.defaultValue = defaultValue;
	}

	@Override
	public EntityType<?> getValue() {
		return value;
	}

	@Override
	public CommandException setValue(String value, int pos) {
		String entityName = value.replace(" ", "_");
		Optional<EntityType<?>> optional = EntityType.byKey(entityName);

		if (!optional.isPresent()) {
			return CommandException.invalidParameter(this, pos);
		} else {
			this.value = optional.orElse(null);
		}

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
	public ITextComponent toDescription() {
		return new TranslationTextComponent("screen.clientmod:mainScreen.exception.allowedInputs", "the registry name of an Entity");
	}
}
