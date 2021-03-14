This is a more-or-less small mod that adds some neat client features. There's currently no config for these features (or even release for the mod), because it's just a little private project of my own.

Features of this mod:
- Client Commands: Utility commands that can be entered in a separate screen which opens by pressing "<" by default
  - folder: Opens a minecraft related folder. Parameters: "resources", "mods", "mc"
  - image: Displays an image of some helpful guide inside Minecraft. Parameters: "brewing", "trades"
  - namemc: Opens the namemc page of the playername you entered in your browser.
  - radar: Gives you a list of all the entities found in the radar's search radius, or a list of specific entities (including their positions)
  - settings: Displays the mod's config settings (and automatically updates the config file when a setting is toggled)
  - wiki: Opens the wiki page of the name you entered after the command in your web browser
    
- Config-toggleable features of the mod (can also be toggled by the settings command screen):
  - shouldReloadSounds: Should F3+T also reload sounds? (Turning this off will make resource reloading a bit faster)
  - drawReloadingBackground: Should there be a red background when reloading resources with F3+T?
  - notifyWhenMinceraftScreen: Should Minecraft play a (loud) sound when the Minceraft logo is shown?