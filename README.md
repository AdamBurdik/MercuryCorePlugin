# Mercury Core
**Mercury Core** is a plugin for Minecraft Paper servers.

The goal of Mercury Core is to provide an API for interacting with the server without having to use Bukkit/Paper directly.
### Mercury Core currently contains these features:
- Player translations
- Item system with blueprints and custom components
- Mob system with blueprint-based mob customizability
- Ability to create items and mobs using TOML files
- Simple placeholder system via mini-message
- Simple configuration system using TOML
- Easy way to store data in database

### Plans for the future
- Easy way to interact with packets
- Custom events
- Integration with Velocity
- Support for different types of databases (e.g., MySQL)
- Improved item and mob systems
- Integration of Lua (or another language) for simple scripts
- Documentation and wiki

## Highlights
### Creating custom mob blueprint
```java
public class DummyMobBlueprint extends MercuryMobBlueprint { 
	public DummyMobBlueprint() {
		super(
				EntityType.ZOMBIE,
				"Dummy Mob! <red>Hello <player:name> <mob:type> <mob:health>/<mob:max_health>!",
				new MobAttributeContainer()
						.set(MercuryAttribute.DAMAGE, 100d)
						.set(MercuryAttribute.MAX_HEALTH, 2000d)
						.set(MercuryAttribute.HEALTH, 2000d),
				null

		);
	}
```
- [DummyMobBlueprint](https://github.com/AdamBurdik/MercuryCorePlugin/blob/master/src/main/java/me/adamix/mercury/core/mob/DummyMobBlueprint.java)

### Interacting with database
```java
// Loads data from database and stores it in cache.
MercuryCore.loadData(playerUuid, PlayerData.class);

// Retrieves data from cache.
var data = MercuryCore.getData(playerUuid, PlayerData.class);
data.increaseScore(100);

// Saves data to database.
MercuryCore.saveData(playerUuid, PlayerData.class);

// Removes data from cache.
MercuryCore.unloadData(playerUuid, PlayerData.class);
```
- **Note:** Data holder needs to be registred for PlayerData

### Getting data from toml configuration file
```java
File tomlFile = ...;

MercuryConfiguration configuration = new MercuryConfiguration(tomlFile);
@NotNull Key key = configuration.getKeySafe("id"); // If key is not present, it will throw an exception
@Nullable String name = configuration.getString("name");
```

# How To Use
ToDO: Add documentation

# How To Build
1. Clone the repository
```bash
   git clone https://github.com/AdamBurdik/MercuryCorePlugin.git
```
2. Navigate to the directory
```bash
   cd MercuryCorePlugin
```
3. Build the project
```bash
   gradlew build
```